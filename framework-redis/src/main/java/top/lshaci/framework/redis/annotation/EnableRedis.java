package top.lshaci.framework.redis.annotation;

import org.springframework.context.annotation.Import;
import top.lshaci.framework.redis.config.FrameworkRedisConfig;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * EnableRedis
 *
 * @author lshaci
 * @since 1.0.7
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(FrameworkRedisConfig.class)
public @interface EnableRedis {
}
