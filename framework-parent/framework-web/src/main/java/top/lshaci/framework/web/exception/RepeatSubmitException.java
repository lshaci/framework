package top.lshaci.framework.web.exception;

/**
 * Repeat Submit Exception
 *
 * @author lshaci
 * @since 1.0.1
 */
public class RepeatSubmitException extends RuntimeException {

	private static final long serialVersionUID = -547471788824710478L;

	public RepeatSubmitException() {
	}

	public RepeatSubmitException(String message) {
		super(message);
	}

	public RepeatSubmitException(Throwable cause) {
		super(cause);
	}

	public RepeatSubmitException(String message, Throwable cause) {
		super(message, cause);
	}

	public RepeatSubmitException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
