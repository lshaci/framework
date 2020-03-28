package top.lshaci.framework.utils.string.converter;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

import org.apache.commons.lang3.StringUtils;

import lombok.extern.slf4j.Slf4j;
import top.lshaci.framework.utils.constants.Constants;

/**
 * 将字符串类型的时间转换为{@code @LocalDate}
 * 
 * @author lshaci
 * @since 1.0.2
 */
@Slf4j
public class String2LocalDateConverter implements StringConverter<LocalDate> {
	
	@Override
	public LocalDate convert(String source) {
		log.debug("The string is : " + source);

        if (StringUtils.isBlank(source)) {
            return null;
        }
        source = trimSource(source);
        try {
        	if (source.contains("-")) {
                if (source.contains(":")) {
                	if (source.contains(".")) {
                		return LocalDate.parse(source, Constants.MSEC_DATE_TIME_FORMATTER);
					}
                    return LocalDate.parse(source, Constants.LONG_DATE_TIME_FORMATTER);
                } else {
                    return LocalDate.parse(source, Constants.SHORT_DATE_TIME_FORMATTER);
                }
            } else if (source.matches("^\\d+$")) {
            	Long timeMillis = Long.valueOf(source);
            	return LocalDateTime.ofInstant(Instant.ofEpochMilli(timeMillis), ZoneId.systemDefault()).toLocalDate();
            }
        } catch (Exception e) {
        	log.warn("Parse string to local date is error!  --> " + source, e);
        }
        return null;
	}

}
