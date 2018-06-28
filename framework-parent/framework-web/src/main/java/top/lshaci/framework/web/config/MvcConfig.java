package top.lshaci.framework.web.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.context.request.RequestContextListener;

import top.lshaci.framework.web.aspect.WebLogAspect;

/**
 * Spring web mvc config<br>
 * <br>
 * <b>0.0.4:</b> Add {@link PropertySource}
 * 
 * @author lshaci
 * @since 0.0.3
 * @version 0.0.4
 */
@PropertySource("classpath:web.properties")
@Configuration
public class MvcConfig {

    /**
     * Config request context listener
     * 
     * @return the request context listener bean
     */
    @Bean
    public RequestContextListener requestContextListener() {
        return new RequestContextListener();
    }

    /**
     * Config web log aspect
     * 
     * @return this web log aspect bean
     */
    @Bean
    @ConditionalOnProperty(value = "webLogAspect.enabled", havingValue = "true", matchIfMissing = false)
    public WebLogAspect webLogAspect() {
        return new WebLogAspect();
    }
}
