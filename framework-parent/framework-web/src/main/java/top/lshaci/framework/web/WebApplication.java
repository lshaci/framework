package top.lshaci.framework.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource("classpath:web.properties")
public class WebApplication {

	public static void main(String[] args) {
        SpringApplication.run(WebApplication.class, args);
    }
}
