package top.lshaci.framework.excel1.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * If the entity class has this annotation, Means to verify upload excel1 file
 *
 * @author lshaci
 * @since 0.0.4
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface UploadVerify {

	/**
	 * Verify that the length of the header is consistent with what is required for uploading
	 *
	 * @return need to verify the title length
	 */
	boolean verifyTitleLength() default false;

	/**
	 * Whether the cell must have a value
	 *
	 * @return the require cell value
	 */
	boolean requireCellValue() default false;

	/**
	 * Whether the row must have a value
	 *
	 * @return the require row value
	 */
	boolean requireRowValue() default false;

}
