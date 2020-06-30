package top.lshaci.framework.excel.exception;

import top.lshaci.framework.common.exception.BaseException;
import top.lshaci.framework.excel.enums.ExportError;

/**
 * 导出操作异常信息
 * 
 * @author lshaci
 * @since 1.0.2
 */
public class ExportHandlerException extends BaseException {

	private static final long serialVersionUID = 1L;

	public ExportHandlerException(String message) {
		super(message);
	}
	
	public ExportHandlerException(ExportError exportError) {
		super(exportError.getMsg());
	}
	
	public ExportHandlerException(ExportError exportError, Throwable cause) {
		super(exportError.getMsg(), cause);
	}

}
