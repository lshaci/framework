package top.lshaci.framework.core.string.converter;

import java.util.Date;

import org.springframework.util.StringUtils;

import lombok.extern.slf4j.Slf4j;
import top.lshaci.framework.core.constants.Constants;

/**
 * Convert the string to date
 * 
 * @author lshaci
 * @version 0.0.1
 */
@Slf4j
public class String2DateConverter implements StringConverter<Date> {

	@Override
	public Date convert(String source) {
		log.debug("The string is : " + source);

        if (StringUtils.isEmpty(source)) {
            return null;
        }
        source = trimSource(source);
        try {
            if (source.contains("-")) {
                if (source.contains(":")) {
                    return Constants.LONG_DATE_FORMATTER.parse(source);
                } else {
                    return Constants.SHORT_DATE_FORMATTER.parse(source);
                }
            } else if (source.matches("^\\d+$")) {
                Long date = new Long(source);
                return new Date(date);
            }
        } catch (Exception e) {
        	log.warn("Parse string to date is error!  --> " + source, e);
        }
        return null;
	}

}
