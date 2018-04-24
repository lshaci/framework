package top.lshaci.framework.redis.lock.model;

/**
 * Distributed roll back interface
 * 
 * @author lshaci
 * @since 0.0.4
 */
@FunctionalInterface
public interface DistributedRollBack {

	/**
	 * When an object implementing interface, when distributed task execute fail invoke this method
	 */
	void rollBack();
}
