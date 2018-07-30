package top.lshaci.framework.web.enums;

import lombok.Getter;

/**
 * Error code enum<br><br>
 * <b>0.0.4: </b>Add org.springframework.http.converter.HttpMessageNotReadableException<br>
 * org.springframework.web.HttpRequestMethodNotSupportedException<br>
 * top.lshaci.framework.web.exception.NoPermissionException
 * 
 * @author lshaci
 * @since 0.0.3
 * @version 0.0.4
 */
@Getter
public enum ErrorCode {
	
	/**
	 * top.lshaci.framework.web.exception.NotLoginException
	 */
	NOT_LOGIN_EXCEPTION(40001, "登录失效，请重新登录", "top.lshaci.framework.web.exception.NotLoginException"),
	/**
     * top.lshaci.framework.web.exception.NoPermissionException
     */
    NO_PERMISSION_EXCEPTION(40002, "无权限进行此操作", "top.lshaci.framework.web.exception.NoPermissionException"),
	/**
	 * com.xdbigdata.framework.common.exception.BaseException
	 */
	INTERNAL_PROGRAM_ERROR(50000, "程序内部错误，操作失败", "top.lshaci.framework.common.exception.BaseException"),
	/**
	 * org.springframework.dao.DataAccessException
	 */
    DATA_ACCESS_EXCEPTION(50001, "数据库操作失败", "org.springframework.dao.DataAccessException"),
    /**
     * com.mysql.jdbc.CommunicationsException
     */
    COMMUNICATIONS_EXCEPTION(50002, "数据库连接中断", "com.mysql.jdbc.CommunicationsException"),
    /**
     * org.springframework.dao.DataIntegrityViolationException
     */
    DATA_INTEGRITY_VIOLATION_EXCEPTION(50003, "数据异常, 操作失败", "org.springframework.dao.DataIntegrityViolationException"),
    /**
     * com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException
     */
    MYSQL_INTEGRITY_CONSTRAINT_VIOLATION_EXCEPTION(50004, "数据异常, 操作失败", "com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException"),
    /**
     * java.lang.NullPointerException
     */
    NULL_POINTER_EXCEPTION(50005, "对象未经初始化或不存在", "java.lang.NullPointerException"),
    /**
     * java.io.IOException
     */
    IO_EXCEPTION(50006, "IO异常", "java.io.IOException"),
    /**
     * java.lang.ClassNotFoundException
     */
    CLASS_NOT_FOUND_EXCEPTION(50007, "指定的类不存在", "java.lang.ClassNotFoundException"),
    /**
     * java.lang.ArithmeticException
     */
    ARITHMETIC_EXCEPTION(50008, "数学运算异常", "java.lang.ArithmeticException"),
    /**
     * java.lang.ArrayIndexOutOfBoundsException
     */
    ARRAY_INDEX_OUT_OF_BOUNDS_EXCEPTION(50009, "数组下标越界", "java.lang.ArrayIndexOutOfBoundsException"),
    /**
     * java.lang.IllegalArgumentException
     */
    ILLEGAL_ARGUMENT_EXCEPTION(50010, "参数错误或非法", "java.lang.IllegalArgumentException"),
    /**
     * java.lang.ClassCastException
     */
    CLASS_CAST_EXCEPTION(50011, "类型强制转换错误", "java.lang.ClassCastException"),
    /**
     * java.sql.SQLException
     */
    SQL_EXCEPTION(50013, "操作数据库异常", "java.sql.SQLException"),
    /**
     * java.lang.SecurityException
     */
    SECURITY_EXCEPTION(50012, "违背安全原则异常", "java.lang.SecurityException"),
    /**
     * java.lang.NoSuchMethodException
     */
    NOSUCH_METHOD_EXCEPTION(50014, "方法未找到异常", "java.lang.NoSuchMethodException"),
    /**
     * java.net.ConnectException
     */
    CONNECT_EXCEPTION(50016, "服务器连接异常", "java.net.ConnectException"),
    /**
     * java.util.concurrent.CancellationException
     */
    CANCELLATION_EXCEPTION(50017, "任务已被取消的异常", "java.util.concurrent.CancellationException"),
    /**
     * java.util.concurrent.CancellationException
     */
    PARSE_EXCEPTION(50019, "日期格式错误", "java.text.ParseException"),
    /**
     * com.mysql.jdbc.MysqlDataTruncation
     */
    MYSQL_DATA_TRUNCATION_EXCEPTION(50020, "服务器不能接收所有数据", "com.mysql.jdbc.MysqlDataTruncation"),
    /**
     * org.springframework.http.converter.HttpMessageNotReadableException
     */
    HTTP_MESSAGE_NOT_READABLE_EXCEPTION(50021, "参数转换异常", "org.springframework.http.converter.HttpMessageNotReadableException"),
    /**
     * org.springframework.web.HttpRequestMethodNotSupportedException
     */
    HTTP_REQUEST_METHOD_NOT_SUPPORTED_EXCEPTION(50022, "请求方法不支持", "org.springframework.web.HttpRequestMethodNotSupportedException"),
    ;
	
	private int code;
	private String msg;
	private String exceptionClass;
	
	private ErrorCode(int code, String msg, String exceptionClass) {
		this.code = code;
		this.msg = msg;
		this.exceptionClass = exceptionClass;
	}
	
	/**
	 * Get error code by exception
	 * 
	 * @param exception the exception
	 * @return the error code
	 */
	public static ErrorCode getByException(Exception exception) {
		if (exception != null) {
			ErrorCode[] errorCodes = ErrorCode.values();
			for (ErrorCode errorCode : errorCodes) {
				if (exception.getClass().getName().equals(errorCode.exceptionClass)) {
					return errorCode;
				}
			}
		}
		return INTERNAL_PROGRAM_ERROR;
	}
}
