package top.lshaci.framework.druid.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;

import lombok.extern.slf4j.Slf4j;

/**
 * Druid Web Config
 * 
 * @author lshaci
 * @version 0.0.1
 */
@Configuration
//Load druid web config
@PropertySource("classpath:druidWeb.properties")
@Slf4j
public class DruidWebConfig {
	
	@Value("${druid.statViewServlet.allowIp}")
	private String allowIp;	// IP白名单
	
	@Value("${druid.statViewServlet.denyIp}")
	private String denyIp;	// IP黑名单
	
	@Value("${druid.statViewServlet.username}")
	private String username;	// 管理界面登录账号
	
	@Value("${druid.statViewServlet.password}")
	private String password;	// 管理界面登录密码
	
	@Value("${druid.statViewServlet.resetEnable}")
	private String resetEnable;	// 是否重置数据
	
	/**
	 * 注册StatViewServlet
	 */
	@Bean
	public ServletRegistrationBean druidStatViewServlet() {
		log.info("Init Druid Web Stat View Servlet...");
		
		// org.springframework.boot.context.embedded.ServletRegistrationBean提供类的进行注册.
		ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(new StatViewServlet(), "/druid/*");
		// 添加初始化参数：initParams
		// 白名单：
		servletRegistrationBean.addInitParameter("allow", allowIp);
		// IP黑名单 (存在共同时，deny优先于allow) : 如果满足deny的话提示:Sorry, you are not permitted to
		// view this page.
		servletRegistrationBean.addInitParameter("deny", denyIp);
		// 登录查看信息的账号密码.
		servletRegistrationBean.addInitParameter("loginUsername", username);
		servletRegistrationBean.addInitParameter("loginPassword", password);
		// 是否能够重置数据.
		servletRegistrationBean.addInitParameter("resetEnable", resetEnable);
		
		return servletRegistrationBean;
	}
	
	@Value("${druid.webStatFilter.exclusions}")
	private String exclusions;	// 监控统计排除这些静态资源
	
	@Value("${druid.webStatFilter.sessionStatMaxCount}")
	private String sessionStatMaxCount;	// 记录session的最大统计数
	
	@Value("${druid.webStatFilter.profileEnable}")
	private String profileEnable;	// 监控单个url地址调用的sql列表信息
	
	/**
	 * 注册filterRegistrationBean
	 */
	@Bean
	public FilterRegistrationBean druidWebStatFilter() {
		log.info("Init Druid Web Stat Filter...");
		
		FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(new WebStatFilter());
		// 添加过滤规则.
		filterRegistrationBean.addUrlPatterns("/*");
		// 添加不需要忽略的格式信息.
		filterRegistrationBean.addInitParameter("exclusions", exclusions);
		filterRegistrationBean.addInitParameter("sessionStatMaxCount", sessionStatMaxCount);
		filterRegistrationBean.addInitParameter("profileEnable", profileEnable);
		
		return filterRegistrationBean;
	}

}
