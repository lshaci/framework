package top.lshaci.framework.fastdfs.exception;

import lombok.Getter;
import top.lshaci.framework.common.exception.BaseException;

@Getter
public class FastDFSException extends BaseException {

	private static final long serialVersionUID = 1L;
	
	private ErrorCode errorCode;

	public FastDFSException() {
	}

	public FastDFSException(ErrorCode errorCode) {
		super(errorCode.getCode());
		this.errorCode = errorCode;
	}
	
	public FastDFSException(String message) {
		super(message);
	}

	public FastDFSException(Throwable cause) {
		super(cause);
	}

	public FastDFSException(String message, Throwable cause) {
		super(message, cause);
	}

	public FastDFSException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
