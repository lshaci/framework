package top.lshaci.framework.excel.helper;

import cn.hutool.core.util.ReflectUtil;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 获取单元格值的工具类
 *
 * @author lshaci
 * @since 1.0.2
 */
@Slf4j
class BaseValueUtil {

	/**
	 * 转换对象缓存Map
	 */
	private static Map<Class<?>, Object> convertCacheMap = new HashMap<>();

	/**
	 * 获取需要转换的值
	 *
	 * @param convertClass 转换类
	 * @param convertMethod 转换方法
	 * @param value 需要转换的原始值
	 * @return 转换后的值
	 */
	protected static Object getConvertValue(Class<?> convertClass, Method convertMethod, Object value) {
		Object convertInstance = convertCacheMap.get(convertClass);
		if (Objects.isNull(convertInstance)) {
			convertInstance = ReflectUtil.newInstance(convertClass);
			convertCacheMap.put(convertClass, convertInstance);
		}
		value = ReflectUtil.invoke(convertInstance, convertMethod, value);
		if (Objects.isNull(value)) {
			log.warn("{}.{}转换后的值为空", convertClass, convertMethod);
			return null;
		}

		return value;
	}
}
