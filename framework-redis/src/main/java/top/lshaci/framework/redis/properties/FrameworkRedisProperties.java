package top.lshaci.framework.redis.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Min;

/**
 * FrameworkRedisProperties
 *
 * @author lshaci
 * @since 1.0.7
 */
@Data
@Validated
@ConfigurationProperties(prefix = "framework.redis")
public class FrameworkRedisProperties {

    /**
     * 生成流水号的长度
     */
    @Min(3)
    private int serialLength = 6;
}
