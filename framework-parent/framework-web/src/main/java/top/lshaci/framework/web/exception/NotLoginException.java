package top.lshaci.framework.web.exception;

/**
 * Framework web base not login exception
 * 
 * @author lshaci
 * @since 0.0.4
 */
public class NotLoginException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public NotLoginException() {
	}

	public NotLoginException(String message) {
		super(message);
	}

	public NotLoginException(Throwable cause) {
		super(cause);
	}

	public NotLoginException(String message, Throwable cause) {
		super(message, cause);
	}

	public NotLoginException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
