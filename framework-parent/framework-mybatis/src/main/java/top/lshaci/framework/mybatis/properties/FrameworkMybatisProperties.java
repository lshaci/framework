package top.lshaci.framework.mybatis.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <p>Framework mybatis properties</p><br>
 *
 * <b>1.0.3: </b>添加SQL执行效率插件配置
 *
 * @author lshaci
 * @since 1.0.2
 * @version 1.0.3
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
     * Mybatis Plus性能分析
     */
    private Performance performance = new Performance();

    /**
     * Mybatis Plus乐观锁配置
     */
    @Data
    public class OptimisticLocker {

        /**
         * 是否开启mybatis plus乐观锁(默认关闭)
         */
        private boolean enabled = false;
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
        private boolean overflow = false;
        /**
         * 单页限制 500 条，小于 0 如 -1 不受限制
         */
        private long limit = 500;
    }
    /**
     * Mybatis Plus 性能分析配置
     */
    @Data
    public class Performance {
        /**
         * 是否开启mybatis plus性能分析(默认关闭)
         */
        private boolean enabled = false;
        /**
         * SQL 执行最大时长, 超过自动停止运行, 有助于发现问题 <b>(单位ms)</b>
         */
        private long maxTime = 0;
        /**
         * SQL 是否格式化
         */
        private boolean format = false;
        /**
         * 是否写入日志文件
         * <p>true 写入日志文件，不阻断程序执行！</p>
         * <p>false 超过设定的最大执行时长异常提示！</p>
         */
        private boolean writeInLog = false;
    }
}
