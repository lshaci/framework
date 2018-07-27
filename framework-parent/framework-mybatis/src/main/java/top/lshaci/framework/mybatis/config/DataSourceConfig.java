package top.lshaci.framework.mybatis.config;

import static top.lshaci.framework.mybatis.datasource.DynamicDataSourceType.FIRST;
import static top.lshaci.framework.mybatis.datasource.DynamicDataSourceType.SECOND;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import com.alibaba.druid.pool.DruidDataSource;

import lombok.extern.slf4j.Slf4j;
import top.lshaci.framework.mybatis.datasource.DynamicDataSource;

/**
 * Config datasource with druid<br><br>
 * 
 * <b>0.0.4: </b>Change to auto configuration
 * 
 * @author lshaci
 * @since 0.0.1
 * @version 0.0.4
 */
@Slf4j
@Configuration
// Load datasource config
@PropertySources({
	@PropertySource("classpath:datasource_first.properties"),
	@PropertySource("classpath:datasource_second.properties")
})
@ConditionalOnProperty(value = "datasource.dynamic", havingValue = "true")
public class DataSourceConfig {
	
	/**
	 * Define dynamic data source(first)
	 * 
	 * @return dynamic data source first
	 */
    @Bean(name = "firstDataSource")
    @ConfigurationProperties("spring.datasource.druid.first")
	@ConditionalOnProperty(value = "spring.datasource.druid.first.url")
	public DataSource firstDataSource() {
		log.debug("Init First Druid DataSource...");
		
		return new DruidDataSource();
	}
	
	/**
	 * Define dynamic data source(second)
	 * 
	 * @return dynamic data source second
	 */
    @Bean(name = "secondDataSource")
    @ConfigurationProperties("spring.datasource.druid.second")
	@ConditionalOnProperty("spring.datasource.druid.second.url")
	public DataSource secondDataSource() {
		log.debug("Init Second Druid DataSource...");
		
		return new DruidDataSource();
	}
	
	/**
	 * Define dynamic primary data source
	 * 
	 * @return dynamic primary data source
	 */
	@Primary
	@Bean(name = "dataSource")
	@DependsOn({"firstDataSource", "secondDataSource"})
    public DynamicDataSource dynamicDataSource() {
		log.debug("Init Dynamic Druid DataSource...");
		
		DynamicDataSource dataSource = new DynamicDataSource();
		
        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put(FIRST, firstDataSource());
        targetDataSources.put(SECOND, secondDataSource());
        
        dataSource.setTargetDataSources(targetDataSources);
        dataSource.setDefaultTargetDataSource(firstDataSource());
        
        return dataSource;
    }
	
	/**
	 * Define transaction manager
	 * 
	 * @return transaction manager
	 */
	@Bean
    public DataSourceTransactionManager transactionManager() {
		log.debug("Init Data Source Transaction Manager...");
		
        return new DataSourceTransactionManager(dynamicDataSource());
    }
	
}
