package ${groupId}.${artifactId}.config;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import top.lshaci.framework.web.converter.FastJsonConverterFactory;

import java.util.List;

/**
 * Web config
 *
 * @author lshaci
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * 配置FastJson消息转换器
     */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        FastJsonHttpMessageConverter fastJsonHttpMessageConverter = FastJsonConverterFactory.build()
                .addSerializerFeature(
                        SerializerFeature.WriteMapNullValue,
                        SerializerFeature.PrettyFormat,
                        SerializerFeature.DisableCircularReferenceDetect)
                .get();
        converters.add(fastJsonHttpMessageConverter);
    }
}