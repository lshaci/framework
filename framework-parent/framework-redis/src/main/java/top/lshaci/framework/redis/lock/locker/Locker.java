package top.lshaci.framework.redis.lock.locker;

/**
 * Locker
 * 
 * @author lshaci
 * @since 0.0.4
 */
public interface Locker {

	/**
	 * Try get lock, default lock expire time is 4s
	 * 
	 * @param lockKey the key of lock
	 * @return the exact moment when the lock expires
	 */
    long tryLock(String lockKey);

    /**
     * Try get lock, and set lock expire time(millisecond)
     * 
     * @param lockKey the key of lock
     * @param lockExpireTime the expire time of lock
     * @return the exact moment when the lock expires
     */
    long tryLock(String lockKey, long lockExpireTime);

    /**
     * Release the lock, check the lock has expired(millisecond)
     * 
     * @param lockKey the key of lock
     * @param lockReleaseTime the release time of lock
     */
    void unLock(String lockKey, long lockReleaseTime);
}
