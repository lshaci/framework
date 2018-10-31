package top.lshaci.framework.masterworker;

import lombok.Data;

/**
 * 
 * 
 * @author lshaci
 *
 * @param <R> The task result type
 */
@Data
public abstract class Task<R> {
	
	/**
	 * Task name, must be unique
	 */
	private String name;
	
	/**
	 * Construct a task with unique task name
	 * 
	 * @param name the task name, must be unique
	 */
	protected Task(String name) {
		this.name = name;
	}

	/**
	 * The concrete method of task execution
	 * 
	 * @return the task result
	 */
	protected abstract R execute();
}
