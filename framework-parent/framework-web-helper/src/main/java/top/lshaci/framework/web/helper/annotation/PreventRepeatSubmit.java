package top.lshaci.framework.web.helper.annotation;

import java.lang.annotation.*;

/**
 * <p>The annotation used to prevent repeat submit</p><br><br>
 *
 * <b>1.0.7:</b>Add attribute <code>timeout</code>
 *
 * @author lshaci
 * @since 1.0.5
 * @version 1.0.7
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PreventRepeatSubmit {

    /**
     * Operation timeout, default value 500 millisecond
     *
     * @return the operation timeout
     */
    long timeout() default 500;
}
