package top.lshaci.framework.mybatis.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Config datasource with druid<br><br>
 * 
 * @author lshaci
 * @since 0.0.4
 */
@Configuration
@PropertySource("classpath:datasource_single.properties")
public class SingleDataSourceConfig {
	
}
