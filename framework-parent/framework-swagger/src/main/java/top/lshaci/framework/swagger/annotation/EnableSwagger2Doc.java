package top.lshaci.framework.swagger.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;

import top.lshaci.framework.swagger.autoconfigure.SwaggerAutoConfiguration;

/**
 * This annotation is used to enable swagger
 * 
 * @author lshaci
 * @since 1.0.1
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(SwaggerAutoConfiguration.class)
public @interface EnableSwagger2Doc {

}
