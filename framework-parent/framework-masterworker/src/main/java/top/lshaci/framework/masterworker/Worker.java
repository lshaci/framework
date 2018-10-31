package top.lshaci.framework.masterworker;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Task worker
 * 
 * @author lshaci
 *
 * @param <R> The task result type
 */
public class Worker<R> {
	
	/**
	 * The task queue to be executed
	 */
	private ConcurrentLinkedQueue<Task<R>> taskQueue;

	/**
	 * Tasks execution result containers
	 */
	private ConcurrentHashMap<String, R> result;

	/**
	 * Construct a task worker with task queue and tasks result container
	 */
	protected Worker(ConcurrentLinkedQueue<Task<R>> taskQueue, ConcurrentHashMap<String, R> result) {
		this.taskQueue = taskQueue;
		this.result = result;
	}

	/**
	 * Execution task
	 */
	public void run() {
		while (true) {
			Task<R> task = taskQueue.poll();
			if (task == null) {
				break;
			}
			R r = task.execute();
			result.put(task.getName(), r);
		}
	}

}
