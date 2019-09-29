package top.lshaci.framework.web.utils;

import lombok.extern.slf4j.Slf4j;
import top.lshaci.framework.web.enums.ErrorCode;
import top.lshaci.framework.web.model.ExceptionMessage;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

import static java.util.stream.Collectors.toMap;

/**
 * Global Exception Utils
 *
 * @author lshaci
 * @since 1.0.4
 */
@Slf4j
public class GlobalExceptionUtils {

    /**
     * 异常类全限定名对应的异常消息映射关系
     */
    private static Map<String, ExceptionMessage> exceptionMessageMap;

    /**
     * 默认的异常消息
     */
    private static final ExceptionMessage DEFAULT_MESSAGE = new ExceptionMessage(ErrorCode.INTERNAL_PROGRAM_ERROR);

    static {
        /**
         * 根据系统中定义的错误码, 初始化异常类全限定名对应的异常消息映射关系
         */
        exceptionMessageMap = Arrays.stream(ErrorCode.values())
                .map(ExceptionMessage::new)
                .collect(toMap(ExceptionMessage::getExceptionClass, Function.identity(), (k1, k2) -> k2));
    }

    /**
     * 添加 异常类全限定名对应的异常消息映射关系
     *
     * @param exceptionMessage 自定义的异常消息
     */
    public static void put(ExceptionMessage exceptionMessage) {
        ExceptionMessage oldMessage = exceptionMessageMap.put(exceptionMessage.getExceptionClass(), exceptionMessage);
        if (Objects.nonNull(oldMessage)) {
            log.info("==>Exception Message Change<== old: {}, new: {}", oldMessage, exceptionMessage);
        }
    }

    /**
     * 根据异常类Class获取异常消息
     *
     * @param exceptionClass 异常类Class
     * @return 异常消息
     */
    public static ExceptionMessage get(Class<? extends Exception> exceptionClass) {
        String className = exceptionClass.getName();
        ExceptionMessage exceptionMessage = exceptionMessageMap.get(className);
        return Objects.nonNull(exceptionMessage) ? exceptionMessage: DEFAULT_MESSAGE;
    }
}
