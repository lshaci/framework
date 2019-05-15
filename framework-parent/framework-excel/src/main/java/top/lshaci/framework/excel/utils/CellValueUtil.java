package top.lshaci.framework.excel.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import top.lshaci.framework.excel.entity.ExportTitleEntity;
import top.lshaci.framework.utils.ReflectionUtils;

public class CellValueUtil {
	
	private static Map<Class<?>, Object> convertCacheMap = new HashMap<>();
	
	public static String get(ExportTitleEntity exportTitleEntity, Object data) {
		if (exportTitleEntity.isIndex()) {
			return exportTitleEntity.getIndex().getAndIncrement() + "";
		}
		
		if (exportTitleEntity.isNeedConvert()) {
			Object obj = data;
			if (exportTitleEntity.isEntity()) {
				obj = ReflectionUtils.getFieldValue(data, exportTitleEntity.getEntityField());
			}
			if (Objects.isNull(obj)) {
				return "";
			}
			Object value = ReflectionUtils.getFieldValue(obj, exportTitleEntity.getField());
			
			return getConvertValue(exportTitleEntity, value);
		}
		
		return null;
	}
	
	private static String getConvertValue(ExportTitleEntity exportTitleEntity, Object value) {
		if (Objects.isNull(value)) {
			return "";
		}
		Class<?> convertClass = exportTitleEntity.getConvertClass();
		Object convertInstance = convertCacheMap.get(convertClass);
		if (Objects.isNull(convertInstance)) {
			convertInstance = ReflectionUtils.newInstance(convertClass);
			convertCacheMap.put(convertClass, convertInstance);
		}
		
		return (String) ReflectionUtils.invokeMethod(convertInstance, exportTitleEntity.getConvertMethod(), value);
	}

}
