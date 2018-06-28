package ${groupId}.${artifactId};

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import tk.mybatis.spring.annotation.MapperScan;
import top.lshaci.framework.mybatis.MybatisApplication;
import top.lshaci.framework.swagger.SwaggerApplication;
import top.lshaci.framework.web.WebApplication;

@SpringBootApplication
@Import({MybatisApplication.class, SwaggerApplication.class, WebApplication.class})
@MapperScan("${groupId}.${artifactId}.mapper")
public class Application {

	public static void main(String[] args)  {
		SpringApplication.run(Application.class, args);
	}

}
