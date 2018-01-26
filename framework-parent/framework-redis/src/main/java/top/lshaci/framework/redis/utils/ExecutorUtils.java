package top.lshaci.framework.redis.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Executor thread util
 * 
 * @author lshaci
 * @since 0.0.4
 */
public class ExecutorUtils {

	/**
	 * the executor thread pool
	 */
	private final static ExecutorService executorService = Executors.newCachedThreadPool();
	
	/**
	 * Use thread pool execute command
	 * 
	 * @param command the execute command
	 */
	public static void execute(Runnable command) {
		executorService.execute(command);
	}
	
}
