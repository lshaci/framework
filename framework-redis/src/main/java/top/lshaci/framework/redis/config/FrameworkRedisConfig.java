package top.lshaci.framework.redis.config;

import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import static org.springframework.data.redis.serializer.StringRedisSerializer.UTF_8;

/**
 * FrameworkRedisConfig
 *
 * @author lshaci
 * @since 1.0.7
 */
@Slf4j
@Configuration
public class FrameworkRedisConfig {

    /**
     * Redis中值的序列化方式
     */
    @Bean
    public GenericFastJsonRedisSerializer valueSerializer() {
        log.debug("Config generic fast json redis serializer...");
        return new GenericFastJsonRedisSerializer();
    }

    /**
     * 设置redisTemplate的序列化方式
     *
     * @param redisConnectionFactory the redis connection factory
     * @return RedisTemplate Bean
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        log.debug("Config RedisTemplate<String, Object>...");
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setKeySerializer(UTF_8);
        template.setDefaultSerializer(valueSerializer());
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }
}
