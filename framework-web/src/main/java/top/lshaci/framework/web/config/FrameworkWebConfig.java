package top.lshaci.framework.web.config;

import java.util.List;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.CollectionUtils;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import top.lshaci.framework.web.aspect.PreventRepeatSubmitAspect;
import top.lshaci.framework.web.exception.handler.GlobalExceptionHandler;
import top.lshaci.framework.web.model.ExceptionMessage;
import top.lshaci.framework.web.properties.FrameworkWebProperties;
import top.lshaci.framework.web.service.PreventRepeat;
import top.lshaci.framework.web.service.PreventRepeatKey;
import top.lshaci.framework.web.service.impl.RedisPreventRepeat;
import top.lshaci.framework.web.service.impl.TimedCachePreventRepeat;
import top.lshaci.framework.web.utils.GlobalExceptionUtils;

/**
 * <p>Framework web config</p>
 *
 * <p><b>0.0.4: </b>Add setDownloadCacheSize method; Add config GlobalExceptionHandler, WebLogAspect, PreventRepeatSubmitAspect</p>
 * <p><b>1.0.2: </b>修改配置属性前缀；删除DownloadUtil cacheSize, RequestContextListener配置</p>
 * <p><b>1.0.4: </b>添加配置中的全局异常处理信息</p>
 * <p><b>1.0.7: </b>删除webLog、删除角色控制配置, 添加防重复提交配置</p>
 *
 * @author lshaci
 * @since 0.0.3
 * @version 1.0.7
 */
@Slf4j
@Configuration
@AllArgsConstructor
@EnableConfigurationProperties(FrameworkWebProperties.class)
public class FrameworkWebConfig {

    private final FrameworkWebProperties properties;

    /**
     * Config global exception handler
     *
     * @return the global exception handler bean
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(value = "framework.web.global-exception-handle.enabled", havingValue = "true", matchIfMissing = true)
    public GlobalExceptionHandler globalExceptionHandler() {
        log.debug("Config global exception handler...");
        List<ExceptionMessage> exceptionMessages = properties.getGlobalExceptionHandle().getMessages();
        if (!CollectionUtils.isEmpty(exceptionMessages)) {
            GlobalExceptionUtils.putAll(exceptionMessages);
        }
        return new GlobalExceptionHandler();
    }

    /**
     * Config timed cache prevent repeat, Conditional on missing bean {@code StringRedisTemplate}
     *
     * @return the timed cache prevent repeat bean
     */
    @Bean
    @ConditionalOnMissingBean
    public PreventRepeat preventRepeat() {
        log.debug("Config timed cache prevent repeat service...");
        return new TimedCachePreventRepeat(properties.getPreventRepeatSubmit().getTimeout());
    }

    /**
     * Config prevent repeat submit key service, Conditional on missing bean
     *
     * @return the prevent repeat submit key bean
     */
    @Bean
    @ConditionalOnMissingBean
    public PreventRepeatKey preventRepeatKey() {
        log.debug("Config prevent repeat submit key service...");
        return request -> request.getSession().getId() + ":" + request.getMethod() + ":" + request.getRequestURI();
    }

    /**
     * Config prevent repeat submit aspect
     *
     * @param preventRepeat the prevent repeat submit service
     * @param preventRepeatKey the submit key generate service
     * @return the prevent repeat submit aspect bean
     */
    @Bean
    @ConditionalOnProperty(value = "framework.web.prevent-repeat-submit.enabled", havingValue = "true", matchIfMissing = true)
    public PreventRepeatSubmitAspect preventRepeatSubmitAspect(PreventRepeat preventRepeat, PreventRepeatKey preventRepeatKey) {
        log.debug("Config prevent repeat submit aspect...");
        return new PreventRepeatSubmitAspect(preventRepeat, preventRepeatKey);
    }

    /**
     * Redis Prevent Repeat Config
     *
     * @author lshaci
     * @since 1.0.7
     */
    @Configuration
    @ConditionalOnBean(StringRedisTemplate.class)
    class RedisPreventRepeatConfig {

        /**
         * Config redis prevent repeat, Conditional on bean {@code StringRedisTemplate}
         *
         * @param stringRedisTemplate the string redis template
         * @return the redis prevent repeat bean
         */
        @Bean
        @ConditionalOnMissingBean
        public PreventRepeat preventRepeat(StringRedisTemplate stringRedisTemplate) {
            log.debug("Config redis prevent repeat service...");
            return new RedisPreventRepeat(properties.getPreventRepeatSubmit().getTimeout(), stringRedisTemplate);
        }


    }
}
