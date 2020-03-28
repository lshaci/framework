package top.lshaci.framework.utils.string.converter;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * Convert the string to boolean
 * 
 * @author lshaci
 * @since 0.0.1
 */
@Slf4j
public class String2BooleanConverter implements StringConverter<Boolean> {
	
	/**
	 * true string list
	 */
	private static final List<String> TRUE_STRS = Arrays.asList("O", "T", "Y", "OK", "YES", "TRUE", "1");
	/**
	 * false string list
	 */
	private static final List<String> FALSE_STRS = Arrays.asList("N", "F", "NO", "FALSE", "0");

	@Override
	public Boolean convert(String source) {
		log.debug("The string is : " + source);

        if (StringUtils.isBlank(source)) {
            return null;
        }
        source = trimSource(source);
        
		String upperCaseSource = source.toUpperCase();
		if (TRUE_STRS.contains(upperCaseSource)) {
			return true;
		}
		if (FALSE_STRS.contains(upperCaseSource)) {
			return false;
		}
        return null;
	}

}
