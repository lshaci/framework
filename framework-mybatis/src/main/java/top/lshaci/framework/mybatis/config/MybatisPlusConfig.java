package top.lshaci.framework.mybatis.config;

import com.baomidou.mybatisplus.extension.plugins.OptimisticLockerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.lshaci.framework.mybatis.properties.FrameworkMybatisProperties;

/**
 * <p>Mybatis plus config</p><br>
 *
 * <b>1.0.3: </b>添加SQL执行效率插件Bean配置<br>
 * <b>1.0.7: </b>升级mybatis plus3.3.1版本, 官方删除SQL执行效率插件Bean配置; 推荐使用第三方插件https://mybatis.plus/guide/p6spy.html<br>
 * <b>1.0.8: </b>字段注入修改为构造器注入<br>
 *
 * @author lshaci
 * @since 1.0.2
 * @version 1.0.8
 */
@Slf4j
@Configuration
@AllArgsConstructor
@EnableConfigurationProperties(FrameworkMybatisProperties.class)
public class MybatisPlusConfig {

    private final FrameworkMybatisProperties properties;

    /**
     * 分页插件
     *
     * @return 分页插件
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(value = "framework.mybatis.pagination.enabled", havingValue = "true", matchIfMissing = true)
    public PaginationInterceptor paginationInterceptor() {
        log.debug("Config mybatis plus pagination interceptor.");
        PaginationInterceptor interceptor = new PaginationInterceptor();
        interceptor.setLimit(properties.getPagination().getLimit());
        interceptor.setOverflow(properties.getPagination().isOverflow());
        return interceptor;
    }

    /**
     * 乐观锁插件
     *
     * @return 乐观锁插件
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(value = "framework.mybatis.optimistic-locker.enabled", havingValue = "true", matchIfMissing = true)
    public OptimisticLockerInterceptor optimisticLockerInterceptor() {
        log.debug("Config mybatis plus optimistic locker interceptor.");
        return new OptimisticLockerInterceptor();
    }

}
