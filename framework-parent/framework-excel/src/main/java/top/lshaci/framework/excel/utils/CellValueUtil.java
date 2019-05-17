package top.lshaci.framework.excel.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import top.lshaci.framework.excel.entity.ExportTitleParam;
import top.lshaci.framework.utils.ReflectionUtils;

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
public class CellValueUtil {

	/**
	 * 转换对象缓存Map
	 */
	private static Map<Class<?>, Object> convertCacheMap = new HashMap<>();

	/**
	 * 根据列参数信息和行数据获取对应单元格的值
	 *
	 * @param titleParam 列参数信息
	 * @param data 行数据
	 * @return 对应单元格的值
	 */
	public static String get(ExportTitleParam titleParam, Object data) {
		// 是否是序号列
		if (titleParam.isIndex()) {
			return titleParam.getIndexNumber();
		}

		if (Objects.isNull(data)) {
			log.warn("当前行数据为空");
			return "";
		}

		Object value = fetchOriginalValue(titleParam, data);
		if (Objects.isNull(value)) {
			log.warn("{}的原始值为空", titleParam.getTitle());
			return "";
		}

		// 转换方法存在, 则使用转换方法对原始只进行处理
		if (Objects.nonNull(titleParam.getConvertMethod())) {
			return getConvertValue(titleParam, value);
		}
		// 枚举方法存在, 则使用枚举方法对原始只进行处理
		if (Objects.nonNull(titleParam.getEnumMethod())) {
			return getEnumValue(titleParam, value);
		}

		String result = value.toString();
		if (StringUtils.isBlank(result)) {
			log.warn("单元格的值为空字符，不作其它处理");
			return "";
		}

		// 替换信息存在, 则对原始值进行替换处理
		if (MapUtils.isNotEmpty(titleParam.getReplaceMap())) {
			result = titleParam.getReplaceMap().get(result);
			if (StringUtils.isBlank(result)) {
				log.warn("替换后单元格的值为空字符，不作其它处理");
				return "";
			}
		}

		return concat(titleParam, result);
	}

	/**
	 * 根据列参数信息和单元格的原始值获取枚举的值
	 *
	 * @param titleParam 列参数信息
	 * @param value 行数据
	 * @return 枚举的值
	 */
	private static String getEnumValue(ExportTitleParam titleParam, Object value) {
		Object enumValue = ReflectionUtils.invokeMethod(value, titleParam.getEnumMethod());
		if (Objects.isNull(enumValue)) {
			log.warn("执行枚举方法后获取到的值为空");
			return "";
		}
		return enumValue.toString();
	}

	/**
	 * 根据列参数信息和行数据获取单元格的原始值
	 *
	 * @param titleParam 列参数信息
	 * @param data 行数据
	 * @return 对应单元格的原始值
	 */
	private static Object fetchOriginalValue(ExportTitleParam titleParam, Object data) {
		Object value = null;
		if (Objects.nonNull(titleParam.getEntityField()) && !titleParam.isCollection()) {
			log.debug("当前字段为内嵌对象的字段");
			Object obj = ReflectionUtils.getFieldValue(data, titleParam.getEntityField());
			if (Objects.nonNull(obj)) {
				value = ReflectionUtils.invokeMethod(obj, titleParam.getMethod());
			}
		} else {
			value = ReflectionUtils.invokeMethod(data, titleParam.getMethod());
		}
		return value;
	}

	/**
	 * 获取需要转换的单元格的值
	 *
	 * @param titleParam 列参数信息
	 * @param value 单元格原始值
	 * @return 转换后单元格的值
	 */
	private static String getConvertValue(ExportTitleParam titleParam, Object value) {
		Class<?> convertClass = titleParam.getConvertClass();
		Object convertInstance = convertCacheMap.get(convertClass);
		if (Objects.isNull(convertInstance)) {
			convertInstance = ReflectionUtils.newInstance(convertClass);
			convertCacheMap.put(convertClass, convertInstance);
		}

		value = ReflectionUtils.invokeMethod(convertInstance, titleParam.getConvertMethod(), value);
		if (Objects.isNull(value)) {
			log.warn("{}转换后的值为空", titleParam.getTitle());
			return "";
		}

		return value.toString();
	}

	/**
	 * 拼接结果值的前后缀
	 *
	 * @param titleParam 列参数信息
	 * @param value 单元格的值
	 * @return 拼接后的值
	 */
	private static String concat(ExportTitleParam titleParam, String value) {
		return titleParam.getPrefix() + value + titleParam.getSuffix();
	}

}
