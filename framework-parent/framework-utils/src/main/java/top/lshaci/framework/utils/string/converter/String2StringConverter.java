package top.lshaci.framework.utils.string.converter;

import org.apache.commons.lang3.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * Convert the string to string
 * 
 * @author lshaci
 * @version 0.0.1
 */
@Slf4j
public class String2StringConverter implements StringConverter<String> {
	

	@Override
	public String convert(String source) {
		log.debug("The string is : " + source);

        if (StringUtils.isEmpty(source)) {
            return null;
        }
        return trimSource(source);
	}

}
