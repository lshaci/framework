package top.lshaci.framework.excel.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;

import lombok.extern.slf4j.Slf4j;
import top.lshaci.framework.excel.entity.ExportTitleEntity;
import top.lshaci.framework.utils.ReflectionUtils;

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
	 * @param titleEntity 列参数信息
	 * @param data 行数据
	 * @return 对应单元格的值
	 */
	public static String get(ExportTitleEntity titleEntity, Object data) {
		// 是否是序号列
		if (titleEntity.isIndex()) {
			return titleEntity.getIndexNumber();
		}
		
		if (Objects.isNull(data)) {
			log.warn("当前行数据为空");
			return "";
		}
		
		Object value = fetchOriginalValue(titleEntity, data);
		if (Objects.isNull(value)) {
			log.warn("{}字段的原始值为空", titleEntity.getTitle());
			return "";
		}
		
		// 是否需要转换
		if (titleEntity.isNeedConvert()) {
			return getConvertValue(titleEntity, value);
		}
		
		String result = value.toString();
		if (StringUtils.isBlank(result)) {
			log.warn("单元格的值为空字符，不作其它处理");
			return "";
		}
		
		return result;
	}

	/**
	 * 根据列参数信息和行数据获取单元格的原始值
	 * 
	 * @param titleEntity 列参数信息
	 * @param data 行数据
	 * @return 对应单元格的原始值
	 */
	private static Object fetchOriginalValue(ExportTitleEntity titleEntity, Object data) {
		Object value = null;
		if (titleEntity.isEntity()) {
			log.debug("当前字段为内嵌对象的字段");
			Object obj = ReflectionUtils.getFieldValue(data, titleEntity.getEntityField());
			if (Objects.nonNull(obj)) {
				value = ReflectionUtils.invokeMethod(obj, titleEntity.getMethod());
			}
		} else {
			value = ReflectionUtils.invokeMethod(data, titleEntity.getMethod());
		}
		return value;
	}
	
	/**
	 * 获取需要转换的单元格的值
	 * 
	 * @param titleEntity 列参数信息
	 * @param value 单元格原始值
	 * @return 转换后单元格的值
	 */
	private static String getConvertValue(ExportTitleEntity titleEntity, Object value) {
		Class<?> convertClass = titleEntity.getConvertClass();
		Object convertInstance = convertCacheMap.get(convertClass);
		if (Objects.isNull(convertInstance)) {
			convertInstance = ReflectionUtils.newInstance(convertClass);
			convertCacheMap.put(convertClass, convertInstance);
		}
		
		value = ReflectionUtils.invokeMethod(convertInstance, titleEntity.getConvertMethod(), value);
		if (Objects.isNull(value)) {
			log.warn("{}转换后的值为空", titleEntity.getTitle());
			return "";
		}
		
		return value.toString();
	}

}
