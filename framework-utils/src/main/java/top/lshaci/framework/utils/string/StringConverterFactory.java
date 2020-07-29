package top.lshaci.framework.utils.string;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import top.lshaci.framework.utils.ClassUtils;
import top.lshaci.framework.utils.exception.UtilException;
import top.lshaci.framework.utils.string.converter.StringConverter;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static java.util.stream.Collectors.toMap;

/**
 * <p>String converter factory</p><br>
 *
 * <b>0.0.4: </b>Add string converters type judgment<br>
 * <b>1.0.7: </b>使用hutool替换commons lang3<br>
 *
 * @author lshaci
 * @since 0.0.1
 * @version 1.0.7
 */
@Slf4j
public class StringConverterFactory {

	/**
	 * the string converters(key is converter target type, value is converter)
	 */
	private Map<String, StringConverter<?>> stringConverters = new HashMap<>();

	/**
	 * Constructor a new StringConverterFactory
	 */
	private StringConverterFactory() {

	}

	/**
	 * Build default string converter factory
	 *
	 * @return the default string converter factory
	 */
	public static StringConverterFactory buildDefaultFactory() {
		return buildDefaultFactory(StringConverter.class.getPackage().getName());
	}

	/**
	 * Build string converter factory of package name
	 *
	 * @param packageName the package name
	 * @return the string converter factory
	 */
	public static StringConverterFactory buildDefaultFactory(String packageName) {
		if (StrUtil.isEmpty(packageName)) {
			return buildDefaultFactory();
		}

		String defaultPackage = StringConverter.class.getPackage().getName();
		Set<Class<?>> classSet = ClassUtils.getClassSet(packageName, false);
		if (!defaultPackage.equals(packageName)) {
			classSet.addAll(ClassUtils.getClassSet(defaultPackage, false));
		}

		if (CollectionUtil.isEmpty(classSet)) {
			String msg = "This package is not has any string converter! --> " + packageName;
			log.error(msg);
			throw new UtilException(msg);
		}

		Map<String, StringConverter<?>> stringConverters = classSet.stream()
				.filter(c -> !c.isInterface())
				.filter(c -> StringConverter.class.isAssignableFrom(c))
				.collect(toMap(
					c -> ClassUtils.getInterfaceGenericType(c).getSimpleName().toLowerCase(),
					c -> (StringConverter<?>) ReflectUtil.newInstance(c))
				);

		StringConverterFactory factory = new StringConverterFactory();
		factory.stringConverters = stringConverters;
		return factory;
	}

	/**
	 * Get string converter of target type
	 *
	 * @param <T> the string converter target type
	 * @param targetClass the target type
	 * @return the string converter of target type if exist
	 */
	@SuppressWarnings("unchecked")
	public <T> StringConverter<T> getConverter(Class<T> targetClass) {
		Objects.requireNonNull(targetClass, "The target class must not be null!");

		String key = targetClass.getSimpleName().toLowerCase();

		if ("int".equals(key)) {
			key = Integer.class.getSimpleName().toLowerCase();
		}

		StringConverter<?> stringConverter = stringConverters.get(key);

		if (stringConverter == null) {
			log.warn("The string converter is not exist of type " + key);
		}

		return (StringConverter<T>) stringConverter;
	}
}
