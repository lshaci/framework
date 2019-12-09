package top.lshaci.framework.web.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;
import top.lshaci.framework.web.aspect.UserRoleAspect;
import top.lshaci.framework.web.aspect.WebLogAspect;
import top.lshaci.framework.web.exception.handler.GlobalExceptionHandler;
import top.lshaci.framework.web.model.ExceptionMessage;
import top.lshaci.framework.web.properties.FrameworkWebProperties;
import top.lshaci.framework.web.utils.GlobalExceptionUtils;

import java.util.List;

/**
 * <p>Framework web config</p>
 *
 * <p><b>0.0.4: </b>Add setDownloadCacheSize method; Add config GlobalExceptionHandler, WebLogAspect, PreventRepeatSubmitAspect</p><br>
 * <p><b>1.0.2: </b>修改配置属性前缀；删除DownloadUtil cacheSize, RequestContextListener配置</p><br>
 * <p><b>1.0.4: </b>添加配置中的全局异常处理信息</p><br>
 *
 * @author lshaci
 * @since 0.0.3
 * @version 1.0.4
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(FrameworkWebProperties.class)
public class FrameworkWebConfig {

    @Autowired
    private FrameworkWebProperties properties;

    /**
     * Config global exception handler
     *
     * @return the global exception handler bean
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(value = "framework.web.enabled.global-exception-handler", havingValue = "true", matchIfMissing = true)
    public GlobalExceptionHandler globalExceptionHandler() {
        log.debug("Config global exception handler...");
        List<ExceptionMessage> exceptionMessages = properties.getExceptionMessages();
        if (!CollectionUtils.isEmpty(exceptionMessages)) {
            GlobalExceptionUtils.putAll(exceptionMessages);
        }
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
     * Config user role aspect
     *
     * @return the user role aspect bean
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(value = "framework.web.enabled.user-role", havingValue = "true", matchIfMissing = true)
    public UserRoleAspect userRoleAspect() {
    	log.debug("Config user role aspect...");
    	return new UserRoleAspect();
    }

}
