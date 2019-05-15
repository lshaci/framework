package top.lshaci.framework.excel1.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The first title name of the excel1 corresponding to the entity
 *
 * @author lshaci
 * @since 0.0.4
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DownloadExcelFirstTitle {

	/**
	 * Get the excel1 first title name
	 *
	 * @return the excel1 first title name
	 */
	String name();

	/**
	 * Get the excel1 first title height(Set the height in "twips" or 1/20th of a point.)
	 *
	 * @return the excel1 first title height
	 */
	short height() default 50 * 20;
}
