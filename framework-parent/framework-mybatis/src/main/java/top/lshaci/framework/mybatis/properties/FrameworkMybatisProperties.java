package top.lshaci.framework.mybatis.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Framework mybatis properties
 *
 * @author lshaci
 * @since 1.0.2
 */
@Data
@ConfigurationProperties(prefix = "framework.mybatis")
public class FrameworkMybatisProperties {

    /**
     * 需要开启的功能
     */
    private Enabled enabled = new Enabled();

    /**
     * framework mybatis中的功能
     */
    @Data
    class Enabled {

        /**
         * 是否开启mybatis plus分页(默认开启)
         */
        private Boolean pagination = true;

        /**
         * 是否开启mybatis plus乐观锁(默认开启)
         */
        private Boolean optimisticLocker = true;

    }
}
