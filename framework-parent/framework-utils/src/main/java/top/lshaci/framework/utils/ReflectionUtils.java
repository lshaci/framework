package top.lshaci.framework.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Objects;

import lombok.extern.slf4j.Slf4j;
import top.lshaci.framework.utils.exception.UtilException;

/**
 * Reflection utils
 * 
 * @author lshaci
 * @since 0.0.1
 */
@Slf4j
public abstract class ReflectionUtils {

	/**
	 * Creates a new instance of the class represented by this Class object 
	 * 
	 * @param <E> the class type
	 * @param clazz the class
	 * @return this Class object or null
	 */
	public static <E> E newInstance(Class<E> clazz) {
		Objects.requireNonNull(clazz, "The class is must not be null!");
		try {
			return clazz.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			log.error("Use reflection new instance is error!", e);
			throw new UtilException("Create instance is error of class is : " + clazz.getName());
		}
	}
	
	/**
	 * Get the class implements interfaces generic type(first interface and first generic type)
	 * 
	 * @param clazz the class
	 * @return the generic type
	 */
	public static Class<?> getInterfaceGenericType(Class<?> clazz) {
		return getInterfaceGenericType(clazz, 0, 0);
	}
	
	/**
	 * Get the class generic type
	 * 
	 * @param clazz the class
	 * @param interfaceIndex the index of implements the interface
	 * @param genericTypeIndex the index of the generic type
	 * @return the generic type
	 */
	public static Class<?> getInterfaceGenericType(Class<?> clazz, int interfaceIndex, int genericTypeIndex) {
		Objects.requireNonNull(clazz, "The class is must not be null!");
		
		Type[] interfaces = clazz.getGenericInterfaces();
		
		ParameterizedType type = (ParameterizedType) interfaces[interfaceIndex];
		
		Type genericType = type.getActualTypeArguments()[genericTypeIndex];
		
		return (Class<?>) genericType;
	}
	
	/**
	 * Invoke method
	 * 
	 * @param obj the object
	 * @param method the method
	 * @param args the method arguments
	 * @return the method result
	 */
	public static Object invokeMethod(Object obj, Method method, Object...args) {
		Objects.requireNonNull(obj, "The object is must not be null!");
		Objects.requireNonNull(method, "The method is must not be null!");
		
		try {
			method.setAccessible(true);
			return method.invoke(obj, args);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			log.error("Invoke method is error!", e);
			throw new UtilException("Invoke method is error of method is : " + method.getName());
		}
	}
	
	
	/**
	 * Get field value
	 * 
	 * @param obj the object
	 * @param field the field
	 * @return the field value
	 */
	public static Object getFieldValue(Object obj, Field field) {
		checkFieldOperationParams(obj, field);
		
		try {
			field.setAccessible(true);
			return field.get(obj);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			log.error("Get field value is error!", e);
			throw new UtilException("Get field value is error of field is : " + field.getName());
		}
	}
	
	/**
	 * Set field value
	 * 
	 * @param obj the object
	 * @param field the field
	 * @param value the need set value
	 */
	public static void setFieldValue(Object obj, Field field, Object value) {
		checkFieldOperationParams(obj, field);
		
		try {
			field.setAccessible(true);
			field.set(obj, value);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			log.error("Set field value is error!", e);
			throw new UtilException("Set field value is error of field is : " + field.getName());
		}
	}
	
	/**
	 * Check the field operation params
	 * 
	 * @param obj the object
	 * @param field the field
	 */
	private static void checkFieldOperationParams(Object obj, Field field) {
		Objects.requireNonNull(obj, "The object is must not be null!");
		Objects.requireNonNull(field, "The field is must not be null!");
	}
	
	
}
