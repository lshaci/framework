package top.lshaci.framework.web.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Access the roles required for this method
 * 
 * @author lshaci
 * @since 0.0.4
 */
@Documented
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface NeedRole {

	/**
	 * Access the roles required by the method
	 * 
	 * @return the need role array
	 */
	String[] value();
}
