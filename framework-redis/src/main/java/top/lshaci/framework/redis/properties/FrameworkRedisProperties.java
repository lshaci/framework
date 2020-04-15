package top.lshaci.framework.redis.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * FrameworkRedisProperties
 *
 * @author lshaci
 * @since 1.0.0
 */
@Data
@ConfigurationProperties(prefix = "framework.redis")
public class FrameworkRedisProperties {

    /**
     * 生成流水号的长度
     */
    private int serialLength = 6;
}
