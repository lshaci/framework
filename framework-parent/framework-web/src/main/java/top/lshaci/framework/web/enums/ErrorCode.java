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
    RepeatSubmitException(40003, "上次操作未完成，请勿重复操作", "top.lshaci.framework.web.exception.RepeatSubmitException"),

    /**
     * java.lang.ArithmeticException
     */
    ArithmeticException(50001, "异常算术条件", "java.lang.ArithmeticException"),
    /**
     * java.lang.SecurityException
     */
    SecurityException(50002, "违反安全规定", "java.lang.SecurityException"),
    /**
     * java.lang.ClassCastException
     */
    ClassCastException(50003, "类型转换错误", "java.lang.ClassCastException"),
    /**
     * java.lang.NullPointerException
     */
    NullPointerException(50004, "对象未经初始化", "java.lang.NullPointerException"),
    /**
     * java.lang.NoSuchMethodException
     */
    NoSuchMethodException(50005, "未找到指定方法", "java.lang.NoSuchMethodException"),
    /**
     * java.lang.ClassNotFoundException
     */
    ClassNotFoundException(50006, "指定的类不存在", "java.lang.ClassNotFoundException"),
    /**
     * java.lang.IllegalArgumentException
     */
    IllegalArgumentException(50007, "参数错误或非法", "java.lang.IllegalArgumentException"),
    /**
     * java.lang.IndexOutOfBoundsException
     */
    IndexOutOfBoundsException(50008, "索引超出范围", "java.lang.IndexOutOfBoundsException"),

	/**
	 * org.springframework.dao.DataAccessException
	 */
    DataAccessException(51101, "数据访问失败", "org.springframework.dao.DataAccessException"),
    /**
     * org.springframework.dao.DataIntegrityViolationException
     */
    DataIntegrityViolationException(51102, "数据异常, 操作失败", "org.springframework.dao.DataIntegrityViolationException"),

    /**
     * org.springframework.http.converter.HttpMessageConversionException
     */
    HttpMessageConversionException(51201, "参数转换异常", "org.springframework.http.converter.HttpMessageConversionException"),

    /**
     * org.springframework.web.HttpRequestMethodNotSupportedException
     */
    HttpRequestMethodNotSupportedException(51301, "请求方法不支持", "org.springframework.web.HttpRequestMethodNotSupportedException"),

    /**
     * MissingRequestHeaderException
     */
    MissingRequestHeaderException(51401, "请求参数错误", "org.springframework.web.bind.MissingRequestHeaderException"),

    /**
     * org.springframework.beans.TypeMismatchException
     */
    TypeMismatchException(51302, "参数类型不匹配", "org.springframework.beans.TypeMismatchException"),

    /**
     * com.mysql.cj.jdbc.exceptions.CommunicationsException
     */
    CommunicationsException(52001, "数据库通信错误", "com.mysql.cj.jdbc.exceptions.CommunicationsException"),

    /**
     * java.io.IOException
     */
    IOException(50101, "IO异常", "java.io.IOException"),

    /**
     * java.sql.SQLException
     */
    SQLException(50201, "操作数据库异常", "java.sql.SQLException"),
    /**
     * java.sql.DataTruncation
     */
    DataTruncation(50202, "服务器不能接收所有数据", "java.sql.DataTruncation"),

    /**
     * java.text.ParseException
     */
    ParseException(50301, "日期格式错误", "java.text.ParseException"),
    /**
     * java.net.ConnectException
     */
    ConnectException(50401, "服务器连接异常", "java.net.ConnectException"),
    /**
     * java.util.concurrent.CancellationException
     */
    CancellationException(50501, "任务已被取消", "java.util.concurrent.CancellationException"),

    /**
     * java.lang.Exception
     */
    Exception(50000, "程序内部错误，操作失败", "java.lang.Exception"),
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
