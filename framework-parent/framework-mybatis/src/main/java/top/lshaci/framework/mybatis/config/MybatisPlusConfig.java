package top.lshaci.framework.mybatis.config;

import com.baomidou.mybatisplus.extension.plugins.OptimisticLockerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Mybatis plus config
 *
 * @author lshaci
 * @since 1.0.2
 */
@Slf4j
@Configuration
public class MybatisPlusConfig {

    /**
     * 分页插件
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(value = "framework.mybatis.enabled.pagination", havingValue = "true", matchIfMissing = true)
    public PaginationInterceptor paginationInterceptor() {
        log.debug("Config mybatis plus pagination interceptor.");
        return new PaginationInterceptor();
    }

    /**
     * 乐观锁插件
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(value = "framework.mybatis.enabled.optimistic-locker", havingValue = "true")
    public OptimisticLockerInterceptor optimisticLockerInterceptor() {
        log.debug("Config mybatis plus optimistic locker interceptor.");
        return new OptimisticLockerInterceptor();
    }
}
