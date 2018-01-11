package top.lshaci.framework.excel.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The title name of the excel corresponding to the entity field
 * 
 * @author lshaci
 * @since 0.0.1
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Convert {

	/**
	 * Get the value(Convert class)
	 * 
	 * @return the convert class
	 */
	Class<?> clazz();
	
	/**
	 * Get the convert method name
	 * 
	 * @return the convert method name
	 */
	String method();
}
