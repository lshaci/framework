package top.lshaci.framework.mybatis.config;

import com.baomidou.mybatisplus.extension.plugins.OptimisticLockerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Mybatis plus config
 *
 * @author lshaci
 * @since 1.0.2
 */
@Slf4j
@Configuration
@PropertySource("classpath:mybatisPlus.properties")
public class MybatisPlusConfig {

    /**
     * 分页插件
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(value = "framework.mybatis.enabled.pagination", havingValue = "true")
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
        return new OptimisticLockerInterceptor();
    }
}
