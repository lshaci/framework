package top.lshaci.framework.web.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.util.Assert;
import top.lshaci.framework.web.enums.ErrorCode;

/**
 * Global Exception Message
 *
 * @author lshaci
 * @since 1.0.4
 */
@Data
@AllArgsConstructor
public class ExceptionMessage {

    /**
     * 异常码
     */
    private int code;
    /**
     * 自定义的异常信息
     */
    private String message;
    /**
     * 异常类的全限定名
     */
    private String exceptionClass;

    /**
     * 构造全局异常消息对象
     *
     * @param code 异常码
     * @param message 自定义的异常信息
     * @param exceptionClass 异常类的全限定名
     */
    public ExceptionMessage(int code, String message, Class<? extends Exception> exceptionClass) {
        Assert.hasText(message, "The exception message must has text!");
        Assert.notNull(exceptionClass, "The exception class must not be null!");
        this.code = code;
        this.message = message;
        this.exceptionClass = exceptionClass.getName();
    }

    /**
     * 构造全局异常消息对象
     *
     * @param errorCode 错误码
     */
    public ExceptionMessage(ErrorCode errorCode) {
        Assert.notNull(errorCode, "The error code must not be null!");
        this.code = errorCode.getCode();
        this.message = errorCode.getMsg();
        this.exceptionClass = errorCode.getExceptionClass();
    }

}
