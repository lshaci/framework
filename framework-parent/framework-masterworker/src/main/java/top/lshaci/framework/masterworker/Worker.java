package top.lshaci.framework.masterworker;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Task worker
 * 
 * @author lshaci
 *
 * @param <R> The task result type
 * @since 0.0.4
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
	 * Task execution completion identification
	 */
	private boolean done;

	/**
	 * Construct a task worker with task queue and tasks result container
	 * 
	 * @param taskQueue the task queue to be executed
	 * @param result the tasks execution result containers
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
			    this.done = Boolean.TRUE;
				break;
			}
			R r = task.execute();
			result.put(task.getUniqueName(), r);
		}
	}

	/**
	 * Whether the execution is completed
	 * 
	 * @return if completed return true
	 */
    public boolean isDone() {
        return done;
    }

}
