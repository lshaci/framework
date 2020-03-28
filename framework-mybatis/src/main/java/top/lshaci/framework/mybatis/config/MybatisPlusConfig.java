package top.lshaci.framework.mybatis.config;

import com.baomidou.mybatisplus.extension.plugins.OptimisticLockerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PerformanceInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.lshaci.framework.mybatis.properties.FrameworkMybatisProperties;

/**
 * <p>Mybatis plus config</p><br>
 *
 * <b>1.0.3: </b>添加SQL执行效率插件Bean配置
 *
 * @author lshaci
 * @since 1.0.2
 * @version 1.0.3
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(FrameworkMybatisProperties.class)
public class MybatisPlusConfig {

    @Autowired
    private FrameworkMybatisProperties properties;

    /**
     * 分页插件
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
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(value = "framework.mybatis.optimistic-locker.enabled", havingValue = "true")
    public OptimisticLockerInterceptor optimisticLockerInterceptor() {
        log.debug("Config mybatis plus optimistic locker interceptor.");
        return new OptimisticLockerInterceptor();
    }

    /**
     * SQL执行效率插件
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(value = "framework.mybatis.performance.enabled", havingValue = "true")
    public PerformanceInterceptor performanceInterceptor() {
        log.debug("Config mybatis plus performance interceptor.");
        PerformanceInterceptor interceptor = new PerformanceInterceptor();
        interceptor.setFormat(properties.getPerformance().isFormat());
        interceptor.setMaxTime(properties.getPerformance().getMaxTime());
        return interceptor;
    }
}
