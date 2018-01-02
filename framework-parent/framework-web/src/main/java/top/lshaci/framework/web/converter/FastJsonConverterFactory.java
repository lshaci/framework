package top.lshaci.framework.web.converter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;

import lombok.extern.slf4j.Slf4j;
import top.lshaci.framework.common.constants.Constants;

/**
 * FastJson HttpMessageConverter
 *
 * @author lshaci
 * @version 0.0.3
 */
@Slf4j
public class FastJsonConverterFactory {

	/**
	 * FastJsonHttpMessageConverter
	 */
	private FastJsonHttpMessageConverter fastJsonHttpMessageConverter = null;
	
	/**
	 * FastJsonConfig
	 */
	private FastJsonConfig fastJsonConfig = null;
	
	/**
	 * FastJsonHttpMessageConverter supportedMediaTypes
	 */
	private Set<MediaType> supportedMediaTypes = new HashSet<>();

	/**
	 * Constructor privatization
	 */
	private FastJsonConverterFactory() {
		log.debug("Init fast json http message converter...");
		
		fastJsonHttpMessageConverter = new FastJsonHttpMessageConverter();
		
		supportedMediaTypes.add(MediaType.parseMediaType("text/html;charset=UTF-8"));
        supportedMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
        
		fastJsonHttpMessageConverter.setSupportedMediaTypes(new ArrayList<>(supportedMediaTypes));
		
		fastJsonConfig = fastJsonHttpMessageConverter.getFastJsonConfig();
		
		fastJsonConfig.setDateFormat(Constants.LONG_DATE_FORMAT_STR);
	}

	/**
	 * Init fast json converter factory
	 * 
	 * @return fast json converter factory instance
	 */
	public static FastJsonConverterFactory build() {
		log.debug("Init fast json converter factory...");
		
		return new FastJsonConverterFactory();
	}

	/**
     * Create fast json http message converter with date format
     *
     * @param dateFormat The date format string
     * @return this
     */
	public FastJsonConverterFactory setDateFormat(String dateFormat) {
		if (StringUtils.isNotEmpty(dateFormat)) {
			this.fastJsonConfig.setDateFormat(dateFormat);
			
			log.debug("Set fastJson config date format...");
		}

		return this;
	}
	
	/**
	 * Add fast json config serializer features with array
	 * 
	 * @param serializerFeatures serializer feature array
	 * @return this
	 */
	public FastJsonConverterFactory addSerializerFeature(SerializerFeature...serializerFeatures) {
		if (ArrayUtils.isNotEmpty(serializerFeatures)) {
			SerializerFeature[] oldSerializerFeatures = this.fastJsonConfig.getSerializerFeatures();
			
			SerializerFeature[] newSerializerFeatures = ArrayUtils.addAll(oldSerializerFeatures, serializerFeatures);
			this.fastJsonConfig.setSerializerFeatures(newSerializerFeatures);
			
			log.debug("Add fastJson config serializerFeatures...");
		}
		
		return this;
	}
	
	/**
	 * Add fast json config serializer features with list
	 * 
	 * @param serializerFeatures serializer feature list
	 * @return this
	 */
	public FastJsonConverterFactory addSerializerFeature(List<SerializerFeature> serializerFeatures) {
		if (CollectionUtils.isNotEmpty(serializerFeatures)) {
			this.addSerializerFeature(serializerFeatures.toArray(new SerializerFeature[serializerFeatures.size()]));
		}
		
		return this;
	}
	
	/**
	 * Add fast json http message converter supported media type
	 * 
	 * @param mediaType the media type string
	 * @return this
	 */
	public FastJsonConverterFactory addSupportedMediaType(String mediaType) {
		if (StringUtils.isNotEmpty(mediaType)) {
			try {
				this.addSupportedMediaType(MediaType.parseMediaType(mediaType));
			} catch (Exception e) {
				log.warn("This string{} can not parse to a MediaType", mediaType);
			}
		}
		
		return this;
	}
	
	/**
	 * Add fast json http message converter supported media type
	 * 
	 * @param mediaType the media type
	 * @return this
	 */
	public FastJsonConverterFactory addSupportedMediaType(MediaType mediaType) {
		if (mediaType != null) {
			this.supportedMediaTypes.add(mediaType);
			refreshSupportedMediaTypes();
			
			log.debug("Add fast json http message converter supported media type...");
		}
		
		return this;
	}
	
	/**
	 * Add fast json http message converter supported media type
	 * 
	 * @param mediaType the media type list
	 * @return this
	 */
	public FastJsonConverterFactory addSupportedMediaType(List<MediaType> mediaTypes) {
		if (CollectionUtils.isNotEmpty(mediaTypes)) {
			this.supportedMediaTypes.addAll(supportedMediaTypes);
			refreshSupportedMediaTypes();
		}
		
		return this;
	}
	
	/**
	 * Refresh fast json http message converter supported media types
	 */
	private void refreshSupportedMediaTypes() {
		this.fastJsonHttpMessageConverter.setSupportedMediaTypes(new ArrayList<>(supportedMediaTypes));
	}
	
	/**
	 * Get the fast json http message converter
	 * 
	 * @return fast json http message converter instance
	 */
	public FastJsonHttpMessageConverter get() {
		return this.fastJsonHttpMessageConverter;
	}
    
}
