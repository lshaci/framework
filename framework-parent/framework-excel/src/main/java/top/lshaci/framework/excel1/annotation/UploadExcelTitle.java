package top.lshaci.framework.excel1.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The title name of the excel corresponding to the entity field
 *
 * @author lshaci
 * @since 0.0.3
 * @version 0.0.4
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface UploadExcelTitle {

	/**
	 * Get the value(excel title name)
	 *
	 * @return the excel title name
	 */
	String[] value();

	/**
	 * Whether the field value corresponding to the title is required
	 *
	 * @return the require
	 */
	boolean require() default false;
}
