package top.lshaci.framework.excel.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import top.lshaci.framework.excel.entity.ExportTitleEntity;
import top.lshaci.framework.utils.ReflectionUtils;

/**
 * 获取单元格值的工具类
 * 
 * @author lshaci
 * @since 1.0.2
 */
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
			return titleEntity.getIndex().getAndIncrement() + "";
		}
		// 是否需要转换
		if (titleEntity.isNeedConvert()) {
			Object obj = data;
			if (titleEntity.isEntity()) {
				obj = ReflectionUtils.getFieldValue(data, titleEntity.getEntityField());
			}
			if (Objects.isNull(obj)) {
				return "";
			}
			Object value = ReflectionUtils.invokeMethod(obj, titleEntity.getMethod());
			
			return getConvertValue(titleEntity, value);
		}
		
		Object value = ReflectionUtils.invokeMethod(data, titleEntity.getMethod());
		
		return Objects.isNull(value) ? "" : value.toString();
	}
	
	/**
	 * 获取需要转换的单元格的值
	 * 
	 * @param titleEntity 列参数信息
	 * @param value 字段的值
	 * @return 转换后单元格的值
	 */
	private static String getConvertValue(ExportTitleEntity titleEntity, Object value) {
		if (Objects.isNull(value)) {
			return "";
		}
		Class<?> convertClass = titleEntity.getConvertClass();
		Object convertInstance = convertCacheMap.get(convertClass);
		if (Objects.isNull(convertInstance)) {
			convertInstance = ReflectionUtils.newInstance(convertClass);
			convertCacheMap.put(convertClass, convertInstance);
		}
		
		return (String) ReflectionUtils.invokeMethod(convertInstance, titleEntity.getConvertMethod(), value);
	}

}
