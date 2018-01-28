package top.lshaci.framework.redis.lock.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Distributed task execution results.
 * 
 * @author lshaci
 * @since 0.0.4
 *
 * @param <D> the task result data type
 */
@Getter
@Setter
public class DistributedTaskResult<D> {
	
	/**
	 * The success message
	 */
	private static final String SUCCESS_MSG = "Execute the distributed task successfully.";
	
	/*
	 * Task execution result identification.
	 */
	private boolean status;
	/*
	 * Task execution results.
	 */
	private D data;
	/*
	 * Task execution result prompt message.
	 */
	private String message;
	/*
	 * The task performs an exception that occurs.
	 */
	private Exception e;
	
	/**
	 * Constructs a successful distributed task result with the task result data
	 * 
	 * @param data the task result data
	 */
	public DistributedTaskResult(D data) {
		this.status = true;
		this.message = SUCCESS_MSG;
		this.data = data;
	}
	
	/**
	 * Constructs a failure distributed task result with a failure message
	 * 
	 * @param message a failure message
	 */
	public DistributedTaskResult(String message) {
		this.status = false;
		this.message = message;
	}
	
	/**
	 * Constructs a failure distributed task result with a failure message and the exception 
	 * 
	 * @param message a failure message
	 * @param e the exception of the task execute
	 */
	public DistributedTaskResult(String message, Exception e) {
		this.status = false;
		this.message = message;
		this.e = e;
	}
	


}
