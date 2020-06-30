package top.lshaci.framework.utils.exception;

import top.lshaci.framework.common.exception.BaseException;

/**
 * Framework utils exception
 * 
 * @author lshaci
 * @since 0.0.1
 */
public class UtilException extends BaseException {

	private static final long serialVersionUID = 8582645036430072514L;

	public UtilException() {
	}

	public UtilException(String message) {
		super(message);
	}

	public UtilException(Throwable cause) {
		super(cause);
	}

	public UtilException(String message, Throwable cause) {
		super(message, cause);
	}

	public UtilException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
