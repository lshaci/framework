package top.lshaci.framework.utils.string.converter;

import org.apache.commons.lang3.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * Convert the string to float
 * 
 * @author lshaci
 * @since 0.0.1
 */
@Slf4j
public class String2FloatConverter implements StringConverter<Float> {

	@Override
	public Float convert(String source) {
		log.debug("The string is : " + source);

        if (StringUtils.isEmpty(source)) {
            return null;
        }
        source = trimSource(source);
        try {
        	return Float.parseFloat(source);
        } catch (Exception e) {
        	log.warn("Parse string to float is error!  --> " + source, e);
        }
        return null;
	}

}
