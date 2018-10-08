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
 * @since 0.0.3
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DownloadExcelTitle {

	/**
	 * Get the excel title name
	 * 
	 * @return the excel title name
	 */
	String title();
	
	/**
	 * Get the title order(Small on the right, Big on the left)
	 * 
	 * @return the title order
	 */
	int order();
	
	/**
	 * Get the column width
	 * 
	 * @return the column width
	 */
	int columnWidth() default 12;
}
