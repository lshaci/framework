package top.lshaci.framework.web.config;

import org.apache.coyote.http11.AbstractHttp11Protocol;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.context.request.RequestContextListener;

import lombok.extern.slf4j.Slf4j;
import top.lshaci.framework.web.aspect.PreventRepeatSubmitAspect;
import top.lshaci.framework.web.aspect.WebLogAspect;
import top.lshaci.framework.web.handler.exception.FileUploadExceptionHandler;
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
    
    @Value("${web.dowanload.cacheSize}")
    private int downloadCacheSize;

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
    @ConditionalOnProperty(value = "web.globalExceptionHandler.enabled", havingValue = "true")
    public GlobalExceptionHandler globalExceptionHandler() {
        log.debug("Config global exception handler...");
        return new GlobalExceptionHandler();
    }
    
    /**
	 * Config file upload exception handler
	 *
	 * @return the file upload exception handler bean
	 */
	@Bean
	@ConditionalOnProperty(value = "web.fileUploadExceptionHandler.enabled", havingValue = "true")
	public FileUploadExceptionHandler fileUploadExceptionHandler() {
		log.debug("Config global exception handler...");
		return new FileUploadExceptionHandler();
	}

	/**
	 * Config tomcat servlet web server factory(Use it with {@code fileUploadExceptionHandler})
	 *
	 * @return the tomcat servlet web server factory bean
	 */
	@Bean
	@ConditionalOnProperty(value = "web.fileUploadExceptionHandler.enabled", havingValue = "true")
	public TomcatServletWebServerFactory containerFactory() {
		log.debug("Config tomcat servlet web server factory.");
		TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();
		tomcat.addConnectorCustomizers((TomcatConnectorCustomizer) connector -> {
			if ((connector.getProtocolHandler() instanceof AbstractHttp11Protocol<?>)) {
				((AbstractHttp11Protocol<?>) connector.getProtocolHandler()).setMaxSwallowSize(-1);
			}
		});
		return tomcat;
	}
	
    /**
     * Config web log aspect
     * 
     * @return the web log aspect bean
     */
    @Bean
    @ConditionalOnProperty(value = "web.webLogAspect.enabled", havingValue = "true")
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
    @ConditionalOnProperty(value = "web.preventRepeatSubmitAspect.enabled", havingValue = "true")
    public PreventRepeatSubmitAspect preventRepeatSubmitAspect() {
        log.debug("Config prevent repeat submit aspect...");
        return new PreventRepeatSubmitAspect();
    }
    
    /**
     * Set download utils cache size
     */
    @Autowired
    public void setDownloadCacheSize() {
        if (this.downloadCacheSize <= 0) {
            this.downloadCacheSize = 2048;
        }
        log.debug("Set DownloadUtils cacheSize: {} bytes", this.downloadCacheSize);
        DownloadUtils.cacheSize = this.downloadCacheSize;
    }
}
