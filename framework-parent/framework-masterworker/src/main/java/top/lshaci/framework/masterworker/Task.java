package top.lshaci.framework.masterworker;

import lombok.Data;

/**
 * The abstract task
 * 
 * @author lshaci
 *
 * @param <R> The task result type
 * @since 0.0.4
 */
@Data
public abstract class Task<R> {
	
	/**
	 * Task name, must be unique
	 */
	private String uniqueName;
	
	/**
	 * Construct a task with unique task name
	 * 
	 * @param name the task name, must be unique
	 */
	protected Task(String uniqueName) {
		this.uniqueName = uniqueName;
	}

	/**
	 * The concrete method of task execution
	 * 
	 * @return the task result
	 */
	protected abstract R execute();
}
