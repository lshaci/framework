package top.lshaci.framework.utils.string.converter;

import org.apache.commons.lang3.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * Convert the string to double
 * 
 * @author lshaci
 * @version 0.0.1
 */
@Slf4j
public class String2DoubleConverter implements StringConverter<Double> {

	@Override
	public Double convert(String source) {
		log.debug("The string is : " + source);

        if (StringUtils.isEmpty(source)) {
            return null;
        }
        source = trimSource(source);
        try {
        	return Double.parseDouble(source);
        } catch (Exception e) {
        	log.warn("Parse string to double is error!  --> " + source, e);
        }
        return null;
	}

}
