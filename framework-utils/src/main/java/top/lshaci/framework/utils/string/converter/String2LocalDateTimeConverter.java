package top.lshaci.framework.utils.string.converter;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import top.lshaci.framework.utils.constants.Constants;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * <p>将字符串类型的时间转换为{@code LocalDateTime}</p><br>
 *
 * <b>1.0.7: </b>使用hutool替换commons lang3<br>
 *
 * @author lshaci
 * @since 1.0.2
 * @version 1.0.7
 */
@Slf4j
public class String2LocalDateTimeConverter implements StringConverter<LocalDateTime> {

	@Override
	public LocalDateTime convert(String source) {
		log.debug("The string is : " + source);

        if (StrUtil.isBlank(source)) {
            return null;
        }
        source = trimSource(source);
        try {
        	if (source.contains("-")) {
                if (source.contains(":")) {
                	if (source.contains(".")) {
                		return LocalDateTime.parse(source, Constants.MSEC_DATE_TIME_FORMATTER);
					}
                    return LocalDateTime.parse(source, Constants.LONG_DATE_TIME_FORMATTER);
                } else {
                    return LocalDateTime.parse(source, Constants.SHORT_DATE_TIME_FORMATTER);
                }
            } else if (source.matches("^\\d+$")) {
            	Long timeMillis = Long.valueOf(source);
            	return LocalDateTime.ofInstant(Instant.ofEpochMilli(timeMillis), ZoneId.systemDefault());
            }
        } catch (Exception e) {
        	log.warn("Parse string to local date time is error!  --> " + source, e);
        }
        return null;
	}

}
