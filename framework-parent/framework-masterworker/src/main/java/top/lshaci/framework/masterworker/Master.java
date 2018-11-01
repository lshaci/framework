package top.lshaci.framework.masterworker;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import top.lshaci.framework.masterworker.utils.ExecutorUtils;

/**
 * Task master
 * 
 * @author lshaci
 *
 * @param <R> The task result type
 * @since 0.0.4
 */
public class Master<R> {
    
    /**
     * The default worker number
     */
    private final static int DEFAULT_WORKER_NUMBER = ExecutorUtils.PROCESSORS * 2;
	
	/**
	 * The task queue to be executed
	 */
	private ConcurrentLinkedQueue<Task<R>> taskQueue = new ConcurrentLinkedQueue<>();

	/**
	 * All task workers
	 */
	private HashMap<String, Worker<R>> workers = new HashMap<>();
	
	/**
	 * Tasks execution result containers
	 */
	private ConcurrentHashMap<String, R> result = new ConcurrentHashMap<>();
	
	/**
	 * Get the result delay time
	 */
	private long delayMillis;
	
	/**
	 * Construct a task master
	 */
	public Master() {
		this(10, DEFAULT_WORKER_NUMBER);
	}
	
	/**
	 * Construct a task master with get result delay time
	 * 
	 * @param delayMillis the get result delay time
	 * @param workerNumber the worker number
	 * 
	 * @throws IllegalArgumentException if {@code delayMillis <= 0 or @code workerNumber <= 0}
	 */
	public Master(long delayMillis, int workerNumber) {
	    if (delayMillis < 0 || workerNumber <= 0) {
	        throw new IllegalArgumentException();
	    }
		this.delayMillis = delayMillis;
		for (int i = 0; i < workerNumber; i++) {
			workers.put("worker:" + i, new Worker<>(taskQueue, result));
		}
	}
	
	/**
	 * Submit a task
	 * 
	 * @param task the task
	 */
	public void submit(Task<R> task) {
		taskQueue.add(task);
	}
	
	/**
	 * Execute all tasks
	 */
	public void execute() {
		workers.forEach((key, worker) -> {
		    ExecutorUtils.execute(() -> worker.run());
		});
	}
	
	/**
	 * Determine if all tasks are completed
	 * 
	 * @return if all tasks are completed while return true
	 */
	public boolean isComplete() {
		for (Entry<String, Worker<R>> entry : workers.entrySet()) {
			if (!entry.getValue().isDone()) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Obtain task results
	 * 
	 * @return all tasks result
	 */
	public ConcurrentHashMap<String, R> getResult() {
		while (true) {
			if (isComplete()) {
				return result;
			}
			try {
				Thread.sleep(delayMillis);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
}
