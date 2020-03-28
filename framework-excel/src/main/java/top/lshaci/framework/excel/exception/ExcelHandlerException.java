package top.lshaci.framework.excel.exception;

import top.lshaci.framework.common.exception.BaseException;

/**
 * Framework excel handler exception
 *
 * @author lshaci
 * @since 1.0.2
 */
public class ExcelHandlerException extends BaseException {

	private static final long serialVersionUID = 8582645036430072514L;

	public ExcelHandlerException() {
	}

	public ExcelHandlerException(String message) {
		super(message);
	}

	public ExcelHandlerException(Throwable cause) {
		super(cause);
	}

	public ExcelHandlerException(String message, Throwable cause) {
		super(message, cause);
	}

	public ExcelHandlerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
