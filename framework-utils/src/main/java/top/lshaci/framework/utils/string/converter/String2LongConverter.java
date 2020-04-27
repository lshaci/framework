package top.lshaci.framework.utils.string.converter;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>Convert the string to long</p><br>
 *
 * <b>1.0.7: </b>使用hutool替换commons lang3<br>
 *
 * @author lshaci
 * @since 1.0.2
 * @version 1.0.7
 */
@Slf4j
public class String2LongConverter implements StringConverter<Long> {

	@Override
	public Long convert(String source) {
		log.debug("The string is : " + source);

        if (StrUtil.isBlank(source)) {
            return null;
        }
        source = trimSource(source);
        try {
        	return Long.parseLong(source);
        } catch (Exception e) {
        	log.warn("Parse string to long is error!  --> " + source, e);
        }
        return null;
	}

}
