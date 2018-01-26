package top.lshaci.framework.redis.lock.locker;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * Redis Locker
 * 
 * @author lshaci
 * @since 0.0.4
 */
@Slf4j
@Getter
public class RedisLocker implements Locker {

	/**
	 * The default expire time of this locker(5000ms)
	 */
	private final static long DEFAULT_EXPIRE_TIME = 5000;
	
	/**
	 * The expire time of this locker
	 */
	private long expireTime;

	private RedisTemplate<Object, Object> redisTemplate;
	
	/**
	 * Constructs a new redis locker with default expire time and redis template
	 * 
	 * @param redisTemplate the redis template
	 */
	public RedisLocker(RedisTemplate<Object, Object> redisTemplate) {
		Objects.requireNonNull(redisTemplate, "Redis template must not be null!");
		this.redisTemplate = redisTemplate;
	}

	@Override
	public long tryLock(String lockKey) {
		return tryLock(lockKey, DEFAULT_EXPIRE_TIME);
	}

	@Override
	public synchronized long tryLock(String lockKey, long lockExpireTime) {
		final long expireTime = redisCurrentTime() + lockExpireTime + 1;
		if (redisTemplate.execute(new RedisCallback<Boolean>() {

			@Override
			public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
				JdkSerializationRedisSerializer jdkSerializer = new JdkSerializationRedisSerializer();
				byte[] value = jdkSerializer.serialize(expireTime);
				return connection.setNX(lockKey.getBytes(), value);
			}

		})) {
			log.info("Get lock, the lock key is: " + lockKey);
			// Set expire time, release this lock 
			redisTemplate.expire(lockKey, lockExpireTime, TimeUnit.MILLISECONDS);
			return expireTime;
		} else {
			Long currentLockTime = (Long) redisTemplate.opsForValue().get(lockKey);
			// Lock is expire?
			if (currentLockTime == null || redisCurrentTime() > currentLockTime) {
				long newExpireTime = redisCurrentTime() + lockExpireTime + 1;

				Long oldLockTimeout = (Long) redisTemplate.opsForValue().getAndSet(lockKey, newExpireTime);
				// Lock is expire?
				if (oldLockTimeout == null || redisCurrentTime() > oldLockTimeout) {
					log.info("Get lock, the lock key is: " + lockKey);
					// Set expire time, release this lock 
					redisTemplate.expire(lockKey, lockExpireTime, TimeUnit.MILLISECONDS);
					
					return newExpireTime;
				} 
			}
		}
		
		log.warn("Not get lock, the lock key is: " + lockKey);
		return 0;
	}

	@Override
	public void unLock(String lockKey, long lockReleaseTime) {
		if (redisCurrentTime() > lockReleaseTime) {
			log.info("Unlock success, the lock has been released automatically.");
            return;
        }
		
    	Long currentLockTime = (Long) redisTemplate.opsForValue().get(lockKey);
    	
        if (currentLockTime != null && currentLockTime > redisCurrentTime()) {
        	redisTemplate.delete(lockKey);
        	log.info("Unlock success, the lock has been released.");
        }

	}

	/**
	 * Get redis current time
	 * 
	 * @return redis current time
	 */
	private long redisCurrentTime() {
		return redisTemplate.execute(new RedisCallback<Long>() {

			@Override
			public Long doInRedis(RedisConnection connection) throws DataAccessException {
				return connection.time();
			}

		});
	}

    /**
     * Set default expire time of the lock(millisecond)
     * 
     * @param expireTime the expire time of lock
     */
	public void setDefaultExpireTime(long expireTime) {
		this.expireTime = expireTime;
	}
}
