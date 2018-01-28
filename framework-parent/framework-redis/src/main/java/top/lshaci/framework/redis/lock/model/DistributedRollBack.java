package top.lshaci.framework.redis.lock.model;

/**
 * Distributed roll back interface
 * 
 * @author lshaci
 *
 * @param <R> The result type
 */
@FunctionalInterface
public interface DistributedRollBack {

	/**
	 * When an object implementing interface, when distributed task execute fail invoke this method
	 */
	void rollBack();
}
