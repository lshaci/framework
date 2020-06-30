package ${groupId}.${artifactId}.config;

import lombok.extern.slf4j.Slf4j;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.lshaci.framework.web.converter.FastJsonConverterFactory;

/**
 * FastJson config
 *
 * @author lshaci
 */
@Slf4j
@Configuration
public class FastJsonConfig {

    /**
     * Fast Json Http Message Converter
     */
    @Bean
    public HttpMessageConverters fastJsonHttpMessageConverters() {
        log.debug("Config fast json http message converter.");
        FastJsonHttpMessageConverter fastJsonHttpMessageConverter = FastJsonConverterFactory.build()
                .addSerializerFeature(
                        SerializerFeature.WriteMapNullValue,
                        SerializerFeature.PrettyFormat,
                        SerializerFeature.DisableCircularReferenceDetect
                 ).get();
        return new HttpMessageConverters(fastJsonHttpMessageConverter);
    }
}
