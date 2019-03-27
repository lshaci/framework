package top.lshaci.framework.web.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import top.lshaci.framework.web.utils.DownloadUtils;

/**
 * Spring web mvc config<br><br>
 * 
 * <b>0.0.4:</b> Add setDownloadCacheSize method; Add config GlobalExceptionHandler, WebLogAspect, PreventRepeatSubmitAspect
 * 
 * @author lshaci
 * @since 0.0.3
 * @version 0.0.4
 */
@Slf4j
@Configuration
@PropertySource("classpath:web.properties")
public class WebMvcConfig {
    
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
    @ConditionalOnProperty(value = "web.enabled.globalExceptionHandler", havingValue = "true")
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
    @ConditionalOnProperty(value = "web.enabled.webLogAspect", havingValue = "true")
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
    @ConditionalOnProperty(value = "web.enabled.preventRepeatSubmitAspect", havingValue = "true")
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
    @ConditionalOnProperty(value = "web.enabled.userRole", havingValue = "true")
    public UserRoleAspect userRoleAspect() {
    	log.debug("Config user role aspect...");
    	return new UserRoleAspect();
    }
    
    /**
     * Set download utils cache size
     */
    @Autowired
    public void setDownloadCacheSize(@Value("${web.download.cacheSize}") int downloadCacheSize) {
        if (downloadCacheSize <= 0) {
            downloadCacheSize = 2048;
        }
        log.debug("Set DownloadUtils cacheSize: {} bytes", downloadCacheSize);
        DownloadUtils.cacheSize = downloadCacheSize;
    }
}
