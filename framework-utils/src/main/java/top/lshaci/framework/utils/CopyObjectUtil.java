package top.lshaci.framework.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import lombok.extern.slf4j.Slf4j;

/**
 * 简单对象属性拷贝工具
 */
/**
 * Simple Object Properties Copy Utils
 * 
 * @author lshaci
 * @since 0.0.4
 */
@Slf4j
public abstract class CopyObjectUtil {
	
	/**
	 * simple properties copy
	 * 
	 * @param source	the source object
	 * @param target	the target class
	 * @return the target object
	 */
	public static <T> T copy(Object source, Class<T> target) {
		return copy(source, target, new HashMap<>());
	}
	
	/**
	 * simple properties copy
	 * 
	 * @param target	the target class
	 * @param extra		the other properties(key: the target object field name, value: the value)
	 * @return the target object
	 */
	public static <T> T copy(Class<T> target, Map<String, Object> extra) {
		return copy(ReflectionUtils.newInstance(target), target, null, extra, null);
	}
	
	/**
	 * simple properties copy
	 * 
	 * @param source	the source object
	 * @param target	the target class
	 * @param rename	the need rename properties(key:the source object field name, value: the target object field name)
	 * @return the target object
	 */
	public static <T> T copy(Object source, Class<T> target, Map<String, String> rename) {
		return copy(source, target, rename, null, null);
	}
	
	/**
	 * simple properties copy
	 * 
	 * @param source	the source object
	 * @param target	the target class
	 * @param skips		the not need copy properties list
	 * @return the target object
	 */
	public static <T> T copy(Object source, Class<T> target, List<String> skips) {
		return copy(source, target, null, null, skips);
	}
	
	/**
	 * simple properties copy
	 * 
	 * @param source	the source object
	 * @param target	the target class
	 * @param rename	the need rename properties(key:the source object field name, value: the target object field name)
	 * @param extra		the other properties(key: the target object field name, value: the value)
	 * @param skips		the not need copy properties list
	 * @return the target object
	 */
	public static <T> T copy(Object source, Class<T> target, Map<String, String> rename, Map<String, Object> extra, List<String> skips) {
		validateParameter(source, target);
		
		if (extra == null) extra = new HashMap<>();
		if (rename == null) rename = new HashMap<>();
		if (skips == null) skips = new ArrayList<>();
		try {
			obj2Map(source, extra, rename, skips);
			return setValue(target, extra);
		} catch (Exception e) {
			log.error("Error copying property!", e);
		}
		return ReflectionUtils.newInstance(target);
	}
	
	/**
	 * create target object and set field value
	 * 
	 * @param target	the target class
	 * @param extra		the properties(key: the target object field name, value: the value)
	 * @return the target object
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	private static <T> T setValue(Class<T> target, Map<String, Object> extra) 
			throws InstantiationException, IllegalAccessException {
		T result = ReflectionUtils.newInstance(target);
		Field[] fields = target.getDeclaredFields();
		
		if (fields == null || fields.length == 0) {
			throw new RuntimeException("Target NO Fields!");
		}
		
		for (Field field : fields) {
			String fieldName = field.getName();
			Object value = extra.get(fieldName);
			if (value != null) {
				try {
					field.setAccessible(true);
					field.set(result, value);
				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
					continue;
				}
			}
		}
		return result;
	}

	/**
	 * change the source object to map, and put to the extra map
	 * 
	 * @param source	the source object
	 * @param extra		the other properties(key: the target object field name, value: the value)
	 * @param rename	the need rename properties(key:the source object field name, value: the target object field name)
	 * @param skips		the not need copy properties list
	 */
	private static void obj2Map(Object source, Map<String, Object> extra, Map<String, String> rename, List<String> skips) {
		Class<? extends Object> clazz = source.getClass();
		Field[] fields = clazz.getDeclaredFields();
		
		if (fields == null || fields.length == 0) {
			throw new RuntimeException("Source NO Fields!");
		}
		
		for (Field field : fields) {
			String fieldName = field.getName();
			if ("serialVersionUID".equals(fieldName) || skips.contains(fieldName)) continue;
			try {
				String temp = rename.get(fieldName);
				if (temp != null && !temp.trim().isEmpty()) {
					fieldName = temp;
				}
				field.setAccessible(true);
				Object value = field.get(source);
				if (value != null) {
					extra.put(fieldName, value);
				}
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
				continue;
			}
		}
	}

	/**
	 * validate  parameter
	 * 
	 * @param source	the source objcet
	 * @param target	the target class
	 */
	private static <T> void validateParameter(Object source, Class<T> target) {
		Objects.requireNonNull(source, "The source object must not be null");
		Objects.requireNonNull(target, "The target class must not be null");
	}
}
