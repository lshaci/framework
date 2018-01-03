package top.lshaci.framework.service.exception;

import top.lshaci.framework.common.exception.BaseException;

/**
 * Framework base service exception
 * 
 * @author lshaci
 * @since 0.0.1
 */
public class BaseServiceException extends BaseException {

	private static final long serialVersionUID = -6561904721408033517L;

	public BaseServiceException() {
	}

	public BaseServiceException(String message) {
		super(message);
	}

	public BaseServiceException(Throwable cause) {
		super(cause);
	}

	public BaseServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	public BaseServiceException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
