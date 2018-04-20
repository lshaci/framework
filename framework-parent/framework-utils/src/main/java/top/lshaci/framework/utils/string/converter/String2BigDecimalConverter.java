package top.lshaci.framework.utils.string.converter;

import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * Convert the string to big decimal
 * 
 * @author lshaci
 * @since 0.0.1
 */
@Slf4j
public class String2BigDecimalConverter implements StringConverter<BigDecimal> {
	

	@Override
	public BigDecimal convert(String source) {
		log.debug("The string is : " + source);

        if (StringUtils.isBlank(source)) {
            return null;
        }
        source = trimSource(source);
        
        try {
			return new BigDecimal(source);
		} catch (Exception e) {
			log.warn("Parse string to decimal is error!  --> " + source, e);
		}
        return null;
	}

}
