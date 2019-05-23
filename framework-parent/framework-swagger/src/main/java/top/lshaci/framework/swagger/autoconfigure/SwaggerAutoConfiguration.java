package top.lshaci.framework.swagger.autoconfigure;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

import lombok.extern.slf4j.Slf4j;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import top.lshaci.framework.swagger.properties.FrameworkSwaggerProperties;
import top.lshaci.framework.swagger.properties.FrameworkSwaggerProperties.DocketInfo;

/**
 * Swagger auto configuration<br><br>
 * <b>1.0.1: </b>Add grouping configuration
 *
 * @author lshaci
 * @since 0.0.4
 * @version 1.0.1
 */
@Slf4j
@Configuration
@EnableSwagger2
@EnableConfigurationProperties(FrameworkSwaggerProperties.class)
@ConditionalOnProperty(prefix = FrameworkSwaggerProperties.SWAGGER_PREFIX, value = "enabled", havingValue = "true", matchIfMissing = true)
public class SwaggerAutoConfiguration implements BeanFactoryAware {

	@Autowired
	private FrameworkSwaggerProperties properties;

	private BeanFactory beanFactory;

	/**
	 * Config swagger docket bean
	 *
	 * @return swagger docket bean list
	 * @throws Exception
	 */
	@Bean
	@ConditionalOnMissingBean
	public List<Docket> docket() throws Exception {
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
		// handle base path
        // If not set, add [/**]
        if (docketInfo.getBasePath().isEmpty()) {
        	docketInfo.getBasePath().add("/**");
        }

        List<Predicate<String>> basePath = new ArrayList<>();
        for (String path : docketInfo.getBasePath()) {
            basePath.add(PathSelectors.ant(path));
        }

        // handle exclude path
        List<Predicate<String>> excludePath = new ArrayList<>();
        for (String path : docketInfo.getExcludePath()) {
            excludePath.add(PathSelectors.ant(path));
        }

		return new Docket(DocumentationType.SWAGGER_2)
				.apiInfo(apiInfo(docketInfo))
				.groupName(groupName)
				.directModelSubstitute(Byte.class, Integer.class)
				.select()
				.apis(RequestHandlerSelectors.basePackage(docketInfo.getBasePackage()))
				.paths(
						Predicates.and(
                                Predicates.not(Predicates.or(excludePath)),
                                Predicates.or(basePath)
                        )
                )
				.build();
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
		// handle base path
        // If not set, add [/**]
        if (properties.getBasePath().isEmpty()) {
        	properties.getBasePath().add("/**");
        }

        List<Predicate<String>> basePath = new ArrayList<>();
        for (String path : properties.getBasePath()) {
            basePath.add(PathSelectors.ant(path));
        }

        // handle exclude path
        List<Predicate<String>> excludePath = new ArrayList<>();
        for (String path : properties.getExcludePath()) {
            excludePath.add(PathSelectors.ant(path));
        }

		return new Docket(DocumentationType.SWAGGER_2)
				.apiInfo(defaultApiInfo())
				.directModelSubstitute(Byte.class, Integer.class)
				.select()
				.apis(RequestHandlerSelectors.basePackage(properties.getBasePackage()))
				.paths(
						Predicates.and(
                                Predicates.not(Predicates.or(excludePath)),
                                Predicates.or(basePath)
                        )
                )
				.build();
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
