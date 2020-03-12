package top.lshaci.framework.common.exception;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * <p>Framework common base exception</p> <br>
 *
 * <b>1.0.4: </b>Add field code <br>
 * @author lshaci
 * @since 0.0.1
 * @version 1.0.4
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class BaseException extends RuntimeException {

	private static final long serialVersionUID = -8170023045088441807L;

	/**
	 * Base exception code
	 */
	private int code;

	/** Constructs a base exception with the specified detail message.
	 *
	 * @param message the detail message.
	 */
	public BaseException(String message) {
		super(message);
	}

	/** Constructs a base exception with the specified detail message and code.
	 *
	 * @param code    the exception code.
	 * @param message the detail message.
	 * @since 1.0.4
	 */
	public BaseException(int code, String message) {
		super(message);
		this.code = code;
	}

	public BaseException(Throwable cause) {
		super(cause);
	}

	public BaseException(String message, Throwable cause) {
		super(message, cause);
	}

	public BaseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
