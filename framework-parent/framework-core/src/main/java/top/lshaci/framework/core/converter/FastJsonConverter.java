package top.lshaci.framework.core.converter;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import top.lshaci.framework.core.constants.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * FastJson HttpMessageConverter
 *
 * @author lshaci
 * @version 0.0.1
 */
@Slf4j
public class FastJsonConverter {

    /**
     * Create fast json http message converter(date format is: <b>yyyy-MM-dd HH:mm:ss</b>)
     *
     * @return the fast json http message converter
     */
    public static FastJsonHttpMessageConverter create() {
        return fastJsonHttpMessageConverter(Constants.LONG_DATE_FORMAT_STR);
    }

    /**
     * Create fast json http message converter with date format
     *
     * @param dateFormat The date format string
     * @return the fast json http message converter
     */
    public static FastJsonHttpMessageConverter create(String dateFormat) {
        if (StringUtils.isEmpty(dateFormat)) {
            return create();
        }
        return fastJsonHttpMessageConverter(dateFormat);
    }

    /**
     * Create fast json http message converter with date format string
     * 
     * @param dateFormat the date format string
     * @return the fast json http message converte
     */
    private static FastJsonHttpMessageConverter fastJsonHttpMessageConverter(String dateFormat) {
        log.info("Init fastJson HttpMessageConverter...");

        FastJsonHttpMessageConverter fastJsonHttpMessageConverter = new FastJsonHttpMessageConverter();

        List<MediaType> supportedMediaTypes = new ArrayList<MediaType>();
        supportedMediaTypes.add(MediaType.parseMediaType("text/html;charset=UTF-8"));
        supportedMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);

        fastJsonHttpMessageConverter.setSupportedMediaTypes(supportedMediaTypes);
        fastJsonHttpMessageConverter.setFastJsonConfig(getFastJsonConfig(dateFormat));

        return fastJsonHttpMessageConverter;
    }

    /**
     * Get fast json config with date format string 
     * 
     * @param dateFormat date format string 
     * @return the fast json config
     */
    private static FastJsonConfig getFastJsonConfig(String dateFormat) {
        log.debug("Init fastJson config...");

        FastJsonConfig fastJsonConfig = new FastJsonConfig();

        List<SerializerFeature> serializerFeatureList = new ArrayList<>();
//		serializerFeatureList.add(SerializerFeature.WriteMapNullValue);
        serializerFeatureList.add(SerializerFeature.WriteNullStringAsEmpty);

        SerializerFeature[] serializerFeatures = serializerFeatureList.toArray(new SerializerFeature[serializerFeatureList.size()]);
        fastJsonConfig.setSerializerFeatures(serializerFeatures);

        fastJsonConfig.setDateFormat(dateFormat);

        return fastJsonConfig;
    }
}
