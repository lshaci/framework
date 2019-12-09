package top.lshaci.framework.web.helper.annotation;

import java.lang.annotation.*;

/**
 * The annotation used to prevent repeat submit<br><br>
 *
 * @author lshaci
 * @since 1.0.5
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PreventRepeatSubmit {

}
