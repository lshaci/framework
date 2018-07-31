package top.lshaci.framework.swagger.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/**
 * Swagger 2 config properties
 * 
 * @author lshaci
 * @since 0.0.4
 */
@Data
@ConfigurationProperties(prefix = SwaggerProperties.SWAGGER_PREFIX)
public class SwaggerProperties {

    /**
     * The swagger properties prefix
     */
    public final static String SWAGGER_PREFIX = "spring.swagger";

    /**
     * The controller base package
     */
    private String basePackage;
    /**
     * The swagger api title
     */
    private String title = "Swagger2 RESTful APIs";
    /**
     * The swagger api description
     */
    private String description = "Spring boot Project Use Swaggers UI Build RESTful APIs";
    /**
     * The swagger api service url
     */
    private String serviceUrl = "http://www.lshaci.top";
    /**
     * The swagger api version
     */
    private String version = "1.0";
    /**
     * The swagger api maintenance personnel
     */
    private Contact contact = new Contact();

    @Data
    public class Contact {
        private String name = "lshaci";
        private String url = "http://www.lshaci.top";
        private String email = "lshaci@qq.com";
        
        public springfox.documentation.service.Contact get() {
            return new springfox.documentation.service.Contact(name, url, email);
        }
    }
    
}
