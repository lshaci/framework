package top.lshaci.framework.excel1.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The sheet name of the excel1 corresponding to the entity
 *
 * @author lshaci
 * @since 0.0.1
 * @version 0.0.4
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DownloadExcelSheet {

	/**
	 * Get the excel1 sheet name
	 *
	 * @return the excel1 sheet name
	 */
	String sheetName();

	/**
	 * Get the excel1 font name
	 *
	 * @return the excel1 font name
	 */
	String fontName() default "宋体";
}
