package top.lshaci.framework.web.exception;

/**
 * Framework web base not login exception
 * 
 * @author lshaci
 * @since 0.0.4
 */
public class LoginException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public LoginException() {
	}

	public LoginException(String message) {
		super(message);
	}

	public LoginException(Throwable cause) {
		super(cause);
	}

	public LoginException(String message, Throwable cause) {
		super(message, cause);
	}

	public LoginException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
