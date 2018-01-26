package top.lshaci.framework.redis.lock;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import top.lshaci.framework.redis.lock.locker.Locker;
import top.lshaci.framework.redis.utils.ExecutorUtils;

/**
 * Redis distributed lock
 * 
 * @author lshaci
 * @since 0.0.4
 */
@Slf4j
@Getter
public class RedisDistributedLock {

	/**
	 * Default retry count of get lock
	 */
	private static final int DEFAULT_RETRY_COUNT = 1;
	/**
	 *  Default expire time of lock(5000ms)
	 */
	private static final long DEFAULT_EXPIRE_TIME = 5000;
	/**
	 * Default delay time of get lock(100ms)
	 */
	private static final long DEFAULT_DELAY_TIME = 100;

	/**
	 * retry count of get lock
	 */
	private int retryCount;
	/**
	 * expire time of lock
	 */
	private long expireTime;
	/**
	 * delay time of get lock
	 */
	private long delayTime;
	/**
	 * the locker
	 */
	private Locker locker;

	/**
	 * Constructs a new redis distributed lock with a locker
	 * 
	 * @param locker the locker
	 */
	public RedisDistributedLock(Locker locker) {
		this.locker = locker;
	}

	/**
	 *  Execute the distributed task
	 * 
	 * @param task the distributed task
	 * @param key the resource key
	 * @return the distributed task result
	 */
	public <R> R execute(DistributedTask<R> task, String key) {
		return execute(task, key, DEFAULT_RETRY_COUNT);
	}
	
	/**
	 *  Execute the distributed task
	 * 
	 * @param task the distributed task
	 * @param key the resource key
	 * @param retryCount the retry count of get lock
	 * @return the distributed task result
	 */
	public <R> R execute(DistributedTask<R> task, String key, int retryCount) {
		return execute(task, key, retryCount, DEFAULT_EXPIRE_TIME);
	}
	
	/**
	 *  Execute the distributed task, and asynchronous execution the roll back
	 * 
	 * @param task the distributed task
	 * @param key the resource key
	 * @param rollBack the distributed task roll back
	 * @return the distributed task result
	 */
	public <R> R execute(DistributedTask<R> task, String key, DistributedRollBack rollBack) {
		return execute(task, key, DEFAULT_RETRY_COUNT, rollBack, true);
	}
	
	/**
	 *  Execute the distributed task, and asynchronous execution the roll back
	 * 
	 * @param task the distributed task
	 * @param key the resource key
	 * @param rollBack the distributed task roll back
	 * @param isAsync whether or not asynchronous execution the roll back
	 * @return the distributed task result
	 */
	public <R> R execute(DistributedTask<R> task, String key, DistributedRollBack rollBack, boolean isAsync) {
		return execute(task, key, DEFAULT_RETRY_COUNT, rollBack, isAsync);
	}
	
	/**
	 *  Execute the distributed task
	 * 
	 * @param task the distributed task
	 * @param key the resource key
	 * @param retryCount the retry count of get lock
	 * @param expireTime the expired time of get lock
	 * @return the distributed task result
	 */
	public <R> R execute(DistributedTask<R> task, String key, int retryCount, long expireTime) {
		return execute(task, key, retryCount, expireTime, null);
	}
	
	/**
	 *  Execute the distributed task, and asynchronous execution the roll back
	 * 
	 * @param task the distributed task
	 * @param key the resource key
	 * @param retryCount the retry count of get lock
	 * @param rollBack the distributed task roll back
	 * @return the distributed task result
	 */
	public <R> R execute(DistributedTask<R> task, String key, int retryCount, DistributedRollBack rollBack) {
		return execute(task, key, retryCount, rollBack, true);
	}
	
	/**
	 *  Execute the distributed task
	 * 
	 * @param task the distributed task
	 * @param key the resource key
	 * @param retryCount the retry count of get lock
	 * @param rollBack the distributed task roll back
	 * @param isAsync whether or not asynchronous execution the roll back
	 * @return the distributed task result
	 */
	public <R> R execute(DistributedTask<R> task, String key, int retryCount, DistributedRollBack rollBack, boolean isAsync) {
		return execute(task, key, retryCount, DEFAULT_DELAY_TIME, DEFAULT_EXPIRE_TIME, rollBack, isAsync);
	}
	
	/**
	 *  Execute the distributed task, and asynchronous execution the roll back
	 * 
	 * @param task the distributed task
	 * @param key the resource key
	 * @param retryCount the retry count of get lock
	 * @param expireTime the expired time of get lock
	 * @param rollBack the distributed task roll back
	 * @return the distributed task result
	 */
	public <R> R execute(DistributedTask<R> task, String key, int retryCount, long expireTime, DistributedRollBack rollBack) {
		return execute(task, key, retryCount, expireTime, rollBack, true);
	}
	
	/**
	 *  Execute the distributed task
	 * 
	 * @param task the distributed task
	 * @param key the resource key
	 * @param retryCount the retry count of get lock
	 * @param expireTime the expired time of get lock
	 * @param rollBack the distributed task roll back
	 * @param isAsync whether or not asynchronous execution the roll back
	 * @return the distributed task result
	 */
	public <R> R execute(DistributedTask<R> task, String key, int retryCount, long expireTime, 
			DistributedRollBack rollBack, boolean isAsync) {
		return execute(task, key, retryCount, DEFAULT_DELAY_TIME, expireTime, rollBack, isAsync);
	}
	
	/**
	 *  Execute the distributed task, and asynchronous execution the roll back
	 * 
	 * @param task the distributed task
	 * @param key the resource key
	 * @param retryCount the retry count of get lock
	 * @param delayTime the delay time of get lock
	 * @param expireTime the expired time of get lock
	 * @param rollBack the distributed task roll back
	 * @return the distributed task result
	 */
	public <R> R execute(DistributedTask<R> task, String key, int retryCount, long delayTime, long expireTime,
			DistributedRollBack rollBack) {
		return execute(task, key, retryCount, delayTime, expireTime, rollBack, true);
	}
	
	/**
	 *  Execute the distributed task
	 * 
	 * @param task the distributed task
	 * @param key the resource key
	 * @param retryCount the retry count of get lock
	 * @param delayTime the delay time of get lock
	 * @param expireTime the expired time of get lock
	 * @param rollBack the distributed task roll back
	 * @param isAsync whether or not asynchronous execution the roll back
	 * @return the distributed task result
	 */
	public <R> R execute(DistributedTask<R> task, String key, int retryCount, long delayTime, long expireTime,
			DistributedRollBack rollBack, boolean isAsync) {
		checkParameter(task, key, retryCount, delayTime, expireTime);
		
		try {
			final String lockKey = "lock:" + key;
			long getLock = 0;
			
			while (getLock == 0 && (retryCount == -1 || retryCount-- > 0)) {
				getLock = locker.tryLock(lockKey, expireTime);
				if (getLock > 0) {
					try {
						return task.run();
					} catch (Exception ex) {
						log.error("Failed to perform distributed task!", ex);
						rollBack(rollBack, isAsync);
					} finally {
						locker.unLock(lockKey, getLock);
					}
				}
				Thread.sleep(delayTime);
			}

			log.warn("Not get lock for key={} retryCount={} delayTime(ms)={}", key, retryCount, delayTime);
		} catch (Exception e) {
			log.error("Performing a distributed task is an exception!", e);
			rollBack(rollBack, isAsync);
		} finally {
			log.warn("Not get lock for key={} retryCount={} delayTime(ms)={}", key, retryCount, delayTime);
		}
		
		return null;
	}

	/**
	 * Check the parameter
	 * 
	 * @param task the distributed task
	 * @param key the resource key
	 * @param retryCount the retry count of get lock
	 * @param delayTime the delay time of get lock
	 * @param expireTime the expired time of get lock
	 */
	private <R> void checkParameter(DistributedTask<R> task, String key, int retryCount, long delayTime, long expireTime) {
		Assert.notNull(task, "The distributed task must not be null!");
		Assert.isTrue(StringUtils.isNotBlank(key), "The resource key must not be blank!");
		Assert.isTrue(retryCount > 0 || retryCount == -1, "The retry count must be greater than zero or equals -1!");
		Assert.isTrue(delayTime > 0, "The delay time must be greater than zero!");
		Assert.isTrue(expireTime > 0, "The expired time must be greater than zero!");
	}

	/**
	 * Invoke distributed roll back method
	 * 
	 * @param rollBack the distributed roll back instance
	 * @param isAsync whether or not asynchronous execution the roll back
	 */
	private void rollBack(DistributedRollBack rollBack, boolean isAsync) {
		if (rollBack != null) {
			log.info("Start rolling back.");
			if (isAsync) {
				ExecutorUtils.execute(() -> { rollBack.rollBack(); });
			} else {
				rollBack.rollBack();
				log.info("Perform synchronous rollback completion.");
			}
		}
	}
	
	/**
	 * Set default retry count of the get lock(millisecond)
	 * 
	 * @param retryCount the retry count
	 */
	public void setDefaultRetryCount(int retryCount) {
		this.retryCount = retryCount;
	}

	/**
	 * Set default expire time of the lock(millisecond)
	 * 
	 * @param expireTime the expire time
	 */
	public void setDefaultExpireTime(long expireTime) {
		this.expireTime = expireTime;
	}

	/**
	 * Set default delay time of the get lock(millisecond)
	 * 
	 * @param delayTime the delay time
	 */
	public void setDefaultDelayTime(long delayTime) {
		this.delayTime = delayTime;
	}

}
