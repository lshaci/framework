package top.lshaci.framework.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextListener;

/**
 * Spring web mvc config
 * 
 * @author lshaci
 * @since 0.0.3
 */
@Configuration
public class WebMvcConfig {

	/**
	 * Config request context listener
	 * 
	 * @return the request context listener bean
	 */
	@Bean
	public RequestContextListener requestContextListener() {
		return new RequestContextListener();
	}
}
