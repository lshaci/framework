package top.lshaci.framework.web.exception;

/**
 * Permission Exception
 *
 * @author lshaci
 * @since 1.0.0
 */
public class RolePermissionException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public RolePermissionException() {
	}

	public RolePermissionException(String message) {
		super(message);
	}

	public RolePermissionException(Throwable cause) {
		super(cause);
	}

	public RolePermissionException(String message, Throwable cause) {
		super(message, cause);
	}

	public RolePermissionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
