package top.lshaci.framework.excel.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The sheet name of the excel corresponding to the entity
 * 
 * @author lshaci
 * @since 0.0.1
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DownloadExcelSheet {

	/**
	 * Get the value(excel sheet name)
	 * 
	 * @return the excel sheet name
	 */
	String value();
}
