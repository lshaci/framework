package top.lshaci.framework.utils;

import top.lshaci.framework.utils.string.StringConverterFactory;
import top.lshaci.framework.utils.string.converter.StringConverter;

/**
 * String converter util
 * 
 * @author lshaci
 * @since 0.0.1
 */
public abstract class StringConverterUtils {

	/**
	 * Get target value
	 * 
	 * @param <T> the target value type
	 * @param targetClass the target class type
	 * @param source the string source
	 * @return the target value
	 */
	public static <T> T getTargetValue(Class<T> targetClass, String source) {
		StringConverter<T> converter = Inner.factory.getConverter(targetClass);
		
		if (converter != null) {
			return converter.convert(source);
		}
		
		return null;
	}
	
	/**
	 * 内部类用于延迟加载
	 * 
	 * @author lshaci
	 * @since 1.0.2
	 */
	static class Inner {
		
		/**
		 * 默认的字符串转换器工厂
		 */
		static StringConverterFactory factory = StringConverterFactory.buildDefaultFactory();
	}
}
