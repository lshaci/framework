package top.lshaci.framework.utils.string.converter;

/**
 * Convert the string to another type
 * 
 * @author lshaci
 * @since 0.0.1
 * @param <T> the target type
 */
public interface StringConverter<T> {
	
	/**
	 * Removed this source space
	 * 
	 * @param source the string source
	 * @return the removed space string
	 */
	default String trimSource(String source) {
		return source.trim();
	}

	/**
	 * Convert the string to another type
	 * 
	 * @param source the string source
	 * @return the target type entity
	 */
	T convert(String source);
}
