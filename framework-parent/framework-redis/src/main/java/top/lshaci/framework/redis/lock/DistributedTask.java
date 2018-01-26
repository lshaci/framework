package top.lshaci.framework.redis.lock;

/**
 * Distributed task interface
 * 
 * @author lshaci
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
