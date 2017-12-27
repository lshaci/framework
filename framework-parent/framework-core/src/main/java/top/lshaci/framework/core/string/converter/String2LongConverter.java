package top.lshaci.framework.core.string.converter;

import org.springframework.util.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * Convert the string to long
 * 
 * @author lshaci
 * @version 0.0.1
 */
@Slf4j
public class String2LongConverter implements StringConverter<Long> {

	@Override
	public Long convert(String source) {
		log.debug("The string is : " + source);

        if (StringUtils.isEmpty(source)) {
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
