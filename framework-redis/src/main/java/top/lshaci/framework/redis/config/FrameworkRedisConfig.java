package top.lshaci.framework.redis.config;

import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import top.lshaci.framework.redis.properties.FrameworkRedisProperties;
import top.lshaci.framework.redis.utils.RedisId;

import static org.springframework.data.redis.serializer.StringRedisSerializer.UTF_8;

/**
 * FrameworkRedisConfig
 *
 * @author lshaci
 * @since 1.0.7
 */
@Slf4j
@Configuration
@AllArgsConstructor
@EnableConfigurationProperties(FrameworkRedisProperties.class)
public class FrameworkRedisConfig {

    private final FrameworkRedisProperties properties;

    /**
     * 定义redis中值的序列化方式
     *
     * @return GenericFastJsonRedisSerializer
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

    /**
     * 定义redis id生成工具
     *
     * @return RedisId Bean
     */
    @Bean
    public RedisId redisId() {
        log.debug("Config RedisId...");
        RedisId redisId = new RedisId();
        redisId.setSerialLength(properties.getSerialLength());
        return redisId;
    }
}
