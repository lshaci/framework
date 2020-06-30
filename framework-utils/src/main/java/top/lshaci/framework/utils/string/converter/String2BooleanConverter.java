package top.lshaci.framework.utils.string.converter;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;

/**
 * <p>Convert the string to boolean</p><br>
 *
 * <b>1.0.7: </b>使用hutool替换commons lang3
 *
 * @author lshaci
 * @since 0.0.1
 * @version 1.0.7
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

        if (StrUtil.isBlank(source)) {
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
