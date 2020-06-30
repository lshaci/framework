package top.lshaci.framework.utils.string.converter;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>Convert the string to string</p><br>
 *
 * <b>1.0.7: </b>使用hutool替换commons lang3<br>
 *
 * @author lshaci
 * @since 0.0.1
 * @version 1.0.7
 */
@Slf4j
public class String2StringConverter implements StringConverter<String> {


	@Override
	public String convert(String source) {
		log.debug("The string is : " + source);

        if (StrUtil.isEmpty(source)) {
            return null;
        }
        return trimSource(source);
	}

}
