package top.lshaci.framework.excel.exception;

import top.lshaci.framework.common.exception.BaseException;
import top.lshaci.framework.excel.enums.ImportError;

/**
 * 导入操作异常信息
 * 
 * @author lshaci
 * @since 1.0.2
 */
public class ImportHandlerException extends BaseException {

	private static final long serialVersionUID = 1L;

	public ImportHandlerException(String message) {
		super(message);
	}
	
	public ImportHandlerException(ImportError importError) {
		super(importError.getMsg());
	}
	
	public ImportHandlerException(ImportError importError, Throwable cause) {
		super(importError.getMsg(), cause);
	}

}
