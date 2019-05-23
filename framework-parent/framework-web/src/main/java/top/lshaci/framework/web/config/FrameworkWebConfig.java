package top.lshaci.framework.web.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.context.request.RequestContextListener;

import lombok.extern.slf4j.Slf4j;
import top.lshaci.framework.web.aspect.PreventRepeatSubmitAspect;
import top.lshaci.framework.web.aspect.UserRoleAspect;
import top.lshaci.framework.web.aspect.WebLogAspect;
import top.lshaci.framework.web.handler.exception.GlobalExceptionHandler;

/**
 * <p>Framework web config</p>
 *
 * <b>0.0.4: </b>Add setDownloadCacheSize method; Add config GlobalExceptionHandler, WebLogAspect, PreventRepeatSubmitAspect
 * <b>1.0.2: </b>修改配置属性前缀；删除DownloadUtil cacheSize配置
 *
 * @author lshaci
 * @since 0.0.3
 * @version 1.0.2
 */
@Slf4j
@Configuration
@PropertySource("classpath:web.properties")
public class FrameworkWebConfig {

    /**
     * Config request context listener
     *
     * @return the request context listener bean
     */
    @Bean
    public RequestContextListener requestContextListener() {
        log.debug("Config request context listener...");
        return new RequestContextListener();
    }

    /**
     * Config global exception handler
     *
     * @return the global exception handler bean
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(value = "framework.web.enabled.global-exception-handler", havingValue = "true")
    public GlobalExceptionHandler globalExceptionHandler() {
        log.debug("Config global exception handler...");
        return new GlobalExceptionHandler();
    }

    /**
     * Config web log aspect
     *
     * @return the web log aspect bean
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(value = "framework.web.enabled.web-log", havingValue = "true")
    public WebLogAspect webLogAspect() {
        log.debug("Config web log aspect...");
        return new WebLogAspect();
    }

    /**
     * Config prevent repeat submit aspect
     *
     * @return the prevent repeat submit aspect bean
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(value = "framework.web.enabled.prevent-repeat-submit", havingValue = "true")
    public PreventRepeatSubmitAspect preventRepeatSubmitAspect() {
        log.debug("Config prevent repeat submit aspect...");
        return new PreventRepeatSubmitAspect();
    }

    /**
     * Config user role aspect
     *
     * @return the user role aspect bean
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(value = "framework.web.enabled.user-role", havingValue = "true")
    public UserRoleAspect userRoleAspect() {
    	log.debug("Config user role aspect...");
    	return new UserRoleAspect();
    }

}
