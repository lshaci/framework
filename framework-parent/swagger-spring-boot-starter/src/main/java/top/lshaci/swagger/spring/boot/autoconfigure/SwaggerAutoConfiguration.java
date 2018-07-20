package top.lshaci.swagger.spring.boot.autoconfigure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import lombok.extern.slf4j.Slf4j;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import top.lshaci.swagger.spring.boot.properties.SwaggerProperties;

/**
 * Swagger auto configuration
 * 
 * @author lshaci
 * @since 0.0.4
 */
@Slf4j
@Configuration
@EnableSwagger2
@EnableConfigurationProperties(SwaggerProperties.class)
@ConditionalOnProperty(prefix = SwaggerProperties.SWAGGER_PREFIX,value = "enabled", havingValue = "true")
public class SwaggerAutoConfiguration {
    
    @Autowired
    private SwaggerProperties properties;
    
    @Bean
    @ConditionalOnMissingBean
    public Docket docket() throws Exception {
        log.debug("Init Swagger UI Config...");
        
        if (StringUtils.isEmpty(properties.getBasePackage())) {
            throw new Exception("The controller base package must not be null!");
        }
        
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage(properties.getBasePackage()))
                .paths(PathSelectors.any())
                .build();
    }
    
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(properties.getTitle())
                .description(properties.getDescription())
                .termsOfServiceUrl(properties.getServiceUrl())
                .contact(properties.getContact().get())
                .version(properties.getVersion())
                .build();
    }

}
