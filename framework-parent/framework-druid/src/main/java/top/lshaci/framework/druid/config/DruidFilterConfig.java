package top.lshaci.framework.druid.config;

import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.alibaba.druid.filter.Filter;
import com.alibaba.druid.filter.logging.Slf4jLogFilter;
import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.wall.WallConfig;
import com.alibaba.druid.wall.WallFilter;

import lombok.extern.slf4j.Slf4j;

/**
 * Druid Filter Config
 * 
 * @author lshaci
 * @version 0.0.1
 */
@Configuration
//Load druid filter config
@PropertySource("classpath:druidFilter.properties")
@Slf4j
public class DruidFilterConfig {
	
	@Value("${druid.wallFilter.logViolation}")
	private boolean logViolation;	// 对被认为是攻击的SQL进行LOG.error输出
	
	/**
	 * Define wall filter config
	 * 
	 * @return the wall filter config
	 */
	@Bean(name = "wallConfig")
	@ConfigurationProperties("druid.wallFilter.config")
	public WallConfig wallConfig() {
		log.info("Init Druid Wall Filter Config...");

		return new WallConfig();
	}
	
	/**
	 * Define wall filter
	 * 
	 * @param wallConfig the wall filter config
	 * @return the wall filter
	 */
	@Bean
	public WallFilter wallFilter(WallConfig wallConfig) {
		log.info("Init Druid Wall Filter...");
		
		WallFilter wallFilter = new WallFilter();
		wallFilter.setLogViolation(logViolation);
		wallFilter.setConfig(wallConfig);
		
		return wallFilter;
	}
	
	/**
	 * Define stat filter
	 * 
	 * @return the stat filter
	 */
	@Bean
	@ConfigurationProperties("druid.statFilter")
	public StatFilter statFilter() {
		log.info("Init Druid Stat Filter...");

		return new StatFilter();
	}
	
	/**
	 * Define log filter
	 * 
	 * @return the log filter
	 */
	@Bean
	@ConfigurationProperties("druid.logFilter")
	public Slf4jLogFilter logFilter() {
		log.info("Init Druid Log Filter...");
		
	    return new Slf4jLogFilter();
	}
	
	@Autowired(required = false)
	@Qualifier("dataSource")
	private DruidDataSource dataSource;
	
	@Autowired(required = false)
	@Qualifier("firstDataSource")
	private DruidDataSource firstDataSource;
	
	@Autowired(required = false)
	@Qualifier("secondDataSource")
	private DruidDataSource secondDataSource;
	
	/**
	 * Init druid data source filter
	 */
	@PostConstruct
	private void initDataSourceFilter() {
		List<Filter> filters = Arrays.asList(statFilter(), wallFilter(wallConfig()), logFilter());
		log.info("Start set Druid Filters...");
		for (Filter filter : filters) {
			log.info("\t --> Filter class name is : " + filter.getClass().getName());
		}
		log.info("End set Druid Filters...");
		if (dataSource != null) {
			dataSource.clearFilters();
			dataSource.setProxyFilters(filters);
		}
		if (firstDataSource != null) {
			firstDataSource.clearFilters();
			firstDataSource.setProxyFilters(filters);
		}
		if (secondDataSource != null) {
			secondDataSource.clearFilters();
			secondDataSource.setProxyFilters(filters);
		}
	}

}
