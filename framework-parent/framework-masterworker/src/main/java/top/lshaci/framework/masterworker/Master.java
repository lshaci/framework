package top.lshaci.framework.masterworker;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Task master
 * 
 * @author lshaci
 *
 * @param <R> The task result type
 */
public class Master<R> {
	
	/**
	 * The task queue to be executed
	 */
	private ConcurrentLinkedQueue<Task<R>> taskQueue = new ConcurrentLinkedQueue<>();

	/**
	 * All task workers
	 */
	private HashMap<String, Thread> workers = new HashMap<>();
	
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
		this(10);
	}
	
	/**
	 * Construct a task master with get result delay time
	 * 
	 * @param delayMillis the get result delay time
	 */
	public Master(long delayMillis) {
		this.delayMillis = delayMillis;
		int processors = Runtime.getRuntime().availableProcessors();
		Worker<R> worker = new Worker<>(taskQueue, result);
		for (int i = 0; i < processors * 2; i++) {
			workers.put("worker:" + i, new Thread(() -> worker.run()));
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
			worker.start();
		});
	}
	
	/**
	 * Determine if all tasks are completed
	 * 
	 * @return if all tasks ar completed while return true
	 */
	public boolean isComplete() {
		for (Entry<String, Thread> entry : workers.entrySet()) {
			System.err.println(entry.getValue().getState());
			if (!Thread.State.TERMINATED.equals(entry.getValue().getState())) {
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
