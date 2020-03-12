package top.lshaci.framework.web.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Error code enum<br><br>
 * <b>0.0.4: </b>Add org.springframework.http.converter.HttpMessageNotReadableException<br>
 * org.springframework.web.HttpRequestMethodNotSupportedException<br>
 * top.lshaci.framework.web.exception.RolePermissionException <br>
 * <b>1.0.1: </b>Add top.lshaci.framework.web.exception.RepeatSubmitException<br>
 * <b>1.0.4: </b>整理异常code<br>
 *
 * @author lshaci
 * @since 0.0.3
 * @version 1.0.4
 */
@Getter
@AllArgsConstructor
public enum ErrorCode {
    /**
     * top.lshaci.framework.web.exception.RepeatSubmitException
     */
    REPEAT_SUBMIT_EXCEPTION(40003, "上次操作未完成，请勿重复操作", "top.lshaci.framework.web.exception.RepeatSubmitException"),

    /**
     * java.lang.ArithmeticException
     */
    ARITHMETIC_EXCEPTION(50001, "异常算术条件", "java.lang.ArithmeticException"),
    /**
     * java.lang.SecurityException
     */
    SECURITY_EXCEPTION(50002, "违反安全规定", "java.lang.SecurityException"),
    /**
     * java.lang.ClassCastException
     */
    CLASS_CAST_EXCEPTION(50003, "对象强制转换错误", "java.lang.ClassCastException"),
    /**
     * java.lang.NullPointerException
     */
    NULL_POINTER_EXCEPTION(50004, "对象未经初始化", "java.lang.NullPointerException"),
    /**
     * java.lang.NoSuchMethodException
     */
    NO_SUCH_METHOD_EXCEPTION(50005, "未找到指定方法", "java.lang.NoSuchMethodException"),
    /**
     * java.lang.ClassNotFoundException
     */
    CLASS_NOT_FOUND_EXCEPTION(50006, "指定的类不存在", "java.lang.ClassNotFoundException"),
    /**
     * java.lang.IllegalArgumentException
     */
    ILLEGAL_ARGUMENT_EXCEPTION(50007, "参数错误或非法", "java.lang.IllegalArgumentException"),
    /**
     * java.lang.IndexOutOfBoundsException
     */
    INDEX_OUT_OF_BOUNDS_EXCEPTION(50008, "索引超出范围", "java.lang.IndexOutOfBoundsException"),

	/**
	 * org.springframework.dao.DataAccessException
	 */
    DATA_ACCESS_EXCEPTION(51101, "数据访问失败", "org.springframework.dao.DataAccessException"),
    /**
     * org.springframework.dao.DataIntegrityViolationException
     */
    DATA_INTEGRITY_VIOLATION_EXCEPTION(51102, "数据异常, 操作失败", "org.springframework.dao.DataIntegrityViolationException"),

    /**
     * org.springframework.http.converter.HttpMessageConversionException
     */
    HTTP_MESSAGE_CONVERSION_EXCEPTION(51201, "参数转换异常", "org.springframework.http.converter.HttpMessageConversionException"),

    /**
     * org.springframework.web.HttpRequestMethodNotSupportedException
     */
    HTTP_REQUEST_METHOD_NOT_SUPPORTED_EXCEPTION(51301, "请求方法不支持", "org.springframework.web.HttpRequestMethodNotSupportedException"),
    /**
     * org.springframework.beans.TypeMismatchException
     */
    TYPE_MISMATCH_EXCEPTION(51302, "参数类型不匹配", "org.springframework.beans.TypeMismatchException"),

    /**
     * com.mysql.cj.jdbc.exceptions.CommunicationsException
     */
    COMMUNICATIONS_EXCEPTION(52001, "数据库通信错误", "com.mysql.cj.jdbc.exceptions.CommunicationsException"),

    /**
     * java.io.IOException
     */
    IO_EXCEPTION(50101, "IO异常", "java.io.IOException"),

    /**
     * java.sql.SQLException
     */
    SQL_EXCEPTION(50201, "操作数据库异常", "java.sql.SQLException"),
    /**
     * java.sql.DataTruncation
     */
    DATA_TRUNCATION_EXCEPTION(50202, "服务器不能接收所有数据", "java.sql.DataTruncation"),

    /**
     * java.text.ParseException
     */
    PARSE_EXCEPTION(50301, "日期格式错误", "java.text.ParseException"),
    /**
     * java.net.ConnectException
     */
    CONNECT_EXCEPTION(50401, "服务器连接异常", "java.net.ConnectException"),
    /**
     * java.util.concurrent.CancellationException
     */
    CANCELLATION_EXCEPTION(50501, "任务已被取消", "java.util.concurrent.CancellationException"),

    /**
     * java.lang.Exception
     */
    INTERNAL_PROGRAM_ERROR(50000, "程序内部错误，操作失败", "java.lang.Exception"),
    ;

    /**
     * 异常编码
     */
	private int code;
    /**
     * 异常消息
     */
	private String msg;
    /**
     * 异常类
     */
	private String exceptionClass;

}
