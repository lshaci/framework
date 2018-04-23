package top.lshaci.framework.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.context.request.RequestContextListener;

/**
 * Spring web mvc config<br><br>
 * version 0.0.4: Add {@PropertySource}
 * 
 * @author lshaci
 * @since 0.0.3
 * @version 0.0.4
 */
@PropertySource("classpath:web.properties")
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
