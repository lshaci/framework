package top.lshaci.framework.excel.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * If the entity class has this annotation, Means to verify that the title of the excel corresponds to that of the entity class
 * 
 * @author lshaci
 * @since 0.0.4
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface UploadVerifyTitleLength {

}
