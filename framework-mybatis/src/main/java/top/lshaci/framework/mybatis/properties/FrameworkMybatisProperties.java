package top.lshaci.framework.mybatis.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <p>Framework mybatis properties</p><br>
 *
 * <b>1.0.3: </b>添加SQL执行效率插件配置<br>
 * <b>1.0.7: </b>升级mybatis plus3.3.1版本, 官方删除SQL执行效率插件Bean配置; 推荐使用第三方插件https://mybatis.plus/guide/p6spy.html<br>
 *
 * @author lshaci
 * @since 1.0.2
 * @version 1.0.7
 */
@Data
@ConfigurationProperties(prefix = "framework.mybatis")
public class FrameworkMybatisProperties {

    /**
     * Mybatis Plus分页
     */
    private Pagination pagination = new Pagination();
    /**
     * Mybatis Plus乐观锁
     */
    private OptimisticLocker optimisticLocker = new OptimisticLocker();
    /**
     * Mybatis Plus乐观锁配置
     */
    @Data
    public class OptimisticLocker {

        /**
         * 是否开启mybatis plus乐观锁(默认开启)
         */
        private boolean enabled = true;
    }

    /**
     * Mybatis Plus分页配置
     */
    @Data
    public class Pagination {
        /**
         * 是否开启mybatis plus分页(默认开启)
         */
        private boolean enabled = true;
        /**
         * 溢出总页数，设置第一页
         */
        private boolean overflow = true;
        /**
         * 单页限制 500 条，小于 0 如 -1 不受限制
         */
        private long limit = 500;
    }
}
