package top.lshaci.framework.redis.lock.model;

/**
 * Distributed task interface
 * 
 * @author lshaci
 * @since 0.0.4
 *
 * @param <R> The result type
 */
@FunctionalInterface
public interface DistributedTask<R> {

	/**
	 * When an object implementing interface, the method is distributed task
	 * 
	 * @return the task result
	 */
	R run();
}
