package top.lshaci.framework.utils.string.converter;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import top.lshaci.framework.utils.constants.Constants;

import java.util.Date;

/**
 * <p>Convert the string to date</p><br>
 *
 * <b>0.0.4: </b>Add millisecond conversion<br>
 * <b>1.0.7: </b>使用hutool替换commons lang3<br>
 *
 * @author lshaci
 * @since 0.0.1
 * @version 1.0.7
 */
@Slf4j
public class String2DateConverter implements StringConverter<Date> {

	@Override
	public Date convert(String source) {
		log.debug("The string is : " + source);

        if (StrUtil.isBlank(source)) {
            return null;
        }
        source = trimSource(source);
        try {
            if (source.contains("-")) {
                if (source.contains(":")) {
                	if (source.contains(".")) {
                		return Constants.MSEC_DATE_FORMATTER.parse(source);
					}
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
