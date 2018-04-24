package top.lshaci.framework.web.exception;

import top.lshaci.framework.common.exception.BaseException;

/**
 * Framework web base exception
 * 
 * @author lshaci
 * @since 0.0.4
 */
public class WebBaseException extends BaseException {

	private static final long serialVersionUID = 1L;

	public WebBaseException() {
	}

	public WebBaseException(String message) {
		super(message);
	}

	public WebBaseException(Throwable cause) {
		super(cause);
	}

	public WebBaseException(String message, Throwable cause) {
		super(message, cause);
	}

	public WebBaseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
