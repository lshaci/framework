package top.lshaci.framework.utils.string.converter;

import org.apache.commons.lang3.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * Convert the string to integer
 * 
 * @author lshaci
 * @since 0.0.1
 */
@Slf4j
public class String2IntegerConverter implements StringConverter<Integer> {

	@Override
	public Integer convert(String source) {
		log.debug("The string is : " + source);

        if (StringUtils.isBlank(source)) {
            return null;
        }
        source = trimSource(source);
        try {
        	return Integer.parseInt(source);
        } catch (Exception e) {
        	log.warn("Parse string to integer is error!  --> " + source, e);
        }
        return null;
	}

}
