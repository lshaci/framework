package top.lshaci.framework.web.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import top.lshaci.framework.web.enums.ErrorCode;
import top.lshaci.framework.web.model.ExceptionMessage;

import java.util.Arrays;
import java.util.List;
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
    private static Map<Class<? extends Exception>, ExceptionMessage> exceptionMessageMap;

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
                .filter(em -> Objects.nonNull(em.getExceptionClass()))
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
     * 添加 异常类全限定名对应的异常消息映射关系
     *
     * @param exceptionMessages 自定义的异常消息
     */
    public static void putAll(List<ExceptionMessage> exceptionMessages) {
        if (CollectionUtils.isEmpty(exceptionMessages)) {
            log.info("The exception message list is empty!");
        }

        exceptionMessages.forEach(m -> {
            ExceptionMessage oldMessage = exceptionMessageMap.put(m.getExceptionClass(), m);
            if (Objects.nonNull(oldMessage)) {
                log.info("==>Exception Message Change<== old: {}, new: {}", oldMessage, m);
            }
        });
    }

    /**
     * 根据异常类Class获取异常消息
     *
     * @param exceptionClass 异常类Class
     * @return 异常消息
     */
    public static ExceptionMessage get(Class<? extends Exception> exceptionClass){
        ExceptionMessage exceptionMessage = exceptionMessageMap.get(exceptionClass);
        if (Objects.nonNull(exceptionClass)) {
            return exceptionMessage;
        }

        log.warn("The exception message is undefined. Try to get the super exception message!");
        return exceptionMessageMap.values()
                .stream()
                .filter(c -> c.getExceptionClass().isAssignableFrom(exceptionClass))
                .findFirst()
                .orElse(DEFAULT_MESSAGE);
    }

}
