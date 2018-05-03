package top.lshaci.framework.swagger.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.util.StringUtils;

import lombok.extern.slf4j.Slf4j;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Swagger Config<br><br>
 * <b>0.0.4:</b>  Add {@link ConditionalOnProperty}
 * 
 * @author lshaci
 * @since 0.0.1
 * @version 0.0.4
 */
@Configuration
@EnableSwagger2
@PropertySource("classpath:swagger.properties")
@ConditionalOnProperty(value = "swagger.enabled", havingValue = "true", matchIfMissing = false)
@Slf4j
public class SwaggerConfig {
	
	@Value("${swagger.basePackage}")
	private String basePackage;
	
	@Bean
    public Docket createRestApi() throws Exception {
		log.info("Init Swagger UI Config...");
		
		if (StringUtils.isEmpty(basePackage)) {
			throw new Exception("The controller base package must not be null!");
		}
		
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage(basePackage))
                .paths(PathSelectors.any())
                .build();
    }
	
	@Value("${swagger.title}")
	private String title;
	
	@Value("${swagger.description}")
	private String description;
	
	@Value("${swagger.serviceUrl}")
	private String serviceUrl;
	
	@Value("${swagger.version}")
	private String version;
	
	@Value("${swagger.contact.name}")
	private String contactName;
	
	@Value("${swagger.contact.url}")
	private String contactUrl;
	
	@Value("${swagger.contact.email}")
	private String contactEmail;

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(title)
                .description(description)
                .termsOfServiceUrl(serviceUrl)
                .contact(new Contact(contactName, contactUrl, contactEmail))
                .version(version)
                .build();
    }

}
