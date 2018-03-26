package top.lshaci.framework.excel.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The first title name of the excel corresponding to the entity
 * 
 * @author lshaci
 * @since 0.0.4
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DownloadExcelFirstTitle {

	/**
	 * Get the excel first title name
	 * 
	 * @return the excel first title name
	 */
	String name();
	
	/**
	 * Get the excel first title height(Set the height in "twips" or 1/20th of a point.)
	 * 
	 * @return the excel first title height
	 */
	short height() default 50 * 20;
}
