package top.lshaci.framework.excel1.handler;

import java.util.Map;

import top.lshaci.framework.excel1.exception.ExcelHandlerException;
import top.lshaci.framework.utils.ReflectionUtils;

/**
 * Poi excel1 handler
 *
 * @author lshaci
 * @since 0.0.4
 */
abstract class POIExcelBaseHandler {

    /**
     * Get the field convert instance
     *
     * @param convertClassCache the convert class cache
     * @param convertClass the field convert class
     * @return the convert instance
     */
    protected static Object getConvertInstance(Map<Class<?>, Object> convertClassCache, Class<?> convertClass) {
        if (convertClass == null) {
            throw new ExcelHandlerException("目标类型转换类为空");
        }

        Object convertInstance = convertClassCache.get(convertClass);
        if (convertInstance == null) {
            convertInstance = ReflectionUtils.newInstance(convertClass);
            convertClassCache.put(convertClass, convertInstance);
        }

        return convertInstance;
    }
}
