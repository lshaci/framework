package top.lshaci.framework.swagger.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import top.lshaci.framework.swagger.model.DocketInfo;
import top.lshaci.framework.swagger.properties.FrameworkSwaggerProperties;

import java.util.LinkedList;
import java.util.List;

import static com.google.common.base.Predicates.*;
import static springfox.documentation.builders.RequestHandlerSelectors.basePackage;
import static springfox.documentation.spi.DocumentationType.SWAGGER_2;
import static top.lshaci.framework.swagger.properties.FrameworkSwaggerProperties.SWAGGER_PREFIX;

/**
 * <p>Swagger auto configuration</p><br>
 * <b>1.0.1: </b>Add grouping configuration<br>
 * <b>1.0.7: </b>添加全局参数配置<br>
 *
 * @author lshaci
 * @since 0.0.4
 * @version 1.0.7
 */
@Slf4j
@Configuration
@EnableSwagger2
@EnableConfigurationProperties(FrameworkSwaggerProperties.class)
@ConditionalOnProperty(prefix = SWAGGER_PREFIX, value = "enabled", havingValue = "true", matchIfMissing = true)
public class FrameworkSwaggerConfig implements BeanFactoryAware {

	private final FrameworkSwaggerProperties properties;

	private BeanFactory beanFactory;

	public FrameworkSwaggerConfig(FrameworkSwaggerProperties properties) {
		this.properties = properties;
	}

	/**
	 * Config swagger docket bean
	 *
	 * @return swagger docket bean list
	 */
	@Bean
	@ConditionalOnMissingBean
	public List<Docket> docket() {
		log.debug("Init Swagger UI Config...");
		ConfigurableBeanFactory configurableBeanFactory = (ConfigurableBeanFactory) beanFactory;

		// No group
		if (properties.getDocket().isEmpty()) {
			Docket defaultDocket = defaultDocket();
			configurableBeanFactory.registerSingleton("defaultDocket", defaultDocket);
			return null;
		} else {
			// Grouping
	        List<Docket> docketList = new LinkedList<>();
			properties.getDocket().forEach((groupName, docketInfo) -> {
				Docket docket = docket(groupName, docketInfo);
				configurableBeanFactory.registerSingleton(groupName, docket);
				docketList.add(defaultDocket());
			});
			return docketList;
		}

	}

	/**
	 * Create swagger docket(grouping)
	 *
	 * @param groupName group name
	 * @param docketInfo docket information
	 * @return swagger docket
	 */
	private Docket docket(String groupName, DocketInfo docketInfo) {
		return new Docket(SWAGGER_2)
				.apiInfo(apiInfo(docketInfo))
				.groupName(groupName)
				.directModelSubstitute(Byte.class, Integer.class)
				.select()
				.apis(basePackage(docketInfo.getBasePackage()))
				.paths(
					and(
						not(or(docketInfo.excludePath())),
						or(docketInfo.basePath())
					)
                )
				.build().globalOperationParameters(properties.globalParameter());
	}

	/**
	 * Create swagger api information
	 *
	 * @param docketInfo docket information
	 * @return swagger api information
	 */
	private ApiInfo apiInfo(DocketInfo docketInfo) {
		return new ApiInfoBuilder()
				.title(docketInfo.getTitle().isEmpty() ? properties.getTitle() : docketInfo.getTitle())
				.description(docketInfo.getDescription().isEmpty() ? properties.getDescription() : docketInfo.getDescription())
				.termsOfServiceUrl(docketInfo.getTermsOfServiceUrl().isEmpty() ? properties.getTermsOfServiceUrl() : docketInfo.getTermsOfServiceUrl())
				.contact(docketInfo.getContact().get())
				.version(docketInfo.getVersion().isEmpty() ? properties.getVersion() : docketInfo.getVersion())
				.license(docketInfo.getLicense().isEmpty() ? properties.getLicense() : docketInfo.getLicense())
				.licenseUrl(docketInfo.getLicenseUrl().isEmpty() ? properties.getLicenseUrl() : docketInfo.getLicenseUrl())
				.build();
	}

	/**
	 * Create default docket
	 *
	 * @return swagger docket
	 */
	private Docket defaultDocket() {
		return new Docket(SWAGGER_2)
				.apiInfo(defaultApiInfo())
				.directModelSubstitute(Byte.class, Integer.class)
				.select()
				.apis(basePackage(properties.getBasePackage()))
				.paths(
					and(
						not(or(properties.excludePath())),
						or(properties.basePath())
					)
                )
				.build().globalOperationParameters(properties.globalParameter());
	}

	/**
	 * Build default swagger api information
	 *
	 * @return default swagger api information
	 */
	private ApiInfo defaultApiInfo() {
		return new ApiInfoBuilder()
				.title(properties.getTitle())
				.description(properties.getDescription())
				.termsOfServiceUrl(properties.getTermsOfServiceUrl())
				.contact(properties.getContact().get())
				.version(properties.getVersion())
				.license(properties.getLicense())
				.licenseUrl(properties.getLicenseUrl())
				.build();
	}

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}

}
