package top.lshaci.framework.utils.string.converter;

import org.apache.commons.lang3.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * Convert the string to long
 * 
 * @author lshaci
 * @since 0.0.1
 */
@Slf4j
public class String2LongConverter implements StringConverter<Long> {

	@Override
	public Long convert(String source) {
		log.debug("The string is : " + source);

        if (StringUtils.isBlank(source)) {
            return null;
        }
        source = trimSource(source);
        try {
        	return Long.parseLong(source);
        } catch (Exception e) {
        	log.warn("Parse string to long is error!  --> " + source, e);
        }
        return null;
	}

}
