package top.lshaci.framework.core.converter;

import java.util.Date;

import org.springframework.core.convert.converter.Converter;
import org.springframework.util.StringUtils;

import lombok.extern.slf4j.Slf4j;
import top.lshaci.framework.common.constants.Constants;

/**
 * String to date converter
 *
 * @author lshaci
 * @since 0.0.1
 */
@Slf4j
public class String2DateConverter implements Converter<String, Date> {

    @Override
    public Date convert(String source) {
        log.debug("The string is : " + source);

        if (StringUtils.isEmpty(source)) {
            return null;
        }
        source = source.trim();
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
            throw new RuntimeException(String.format("Parser %s to Date fail", source));
        }
        throw new RuntimeException(String.format("Parser %s to Date fail", source));
    }
}
