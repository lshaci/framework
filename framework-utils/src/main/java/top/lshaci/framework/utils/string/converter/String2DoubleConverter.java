package top.lshaci.framework.utils.string.converter;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>Convert the string to double</p><br>
 *
 * <b>1.0.7: </b>使用hutool替换commons lang3<br>
 *
 * @author lshaci
 * @since 0.0.1
 * @version 1.0.7
 */
@Slf4j
public class String2DoubleConverter implements StringConverter<Double> {

	@Override
	public Double convert(String source) {
		log.debug("The string is : " + source);

        if (StrUtil.isBlank(source)) {
            return null;
        }
        source = trimSource(source);
        try {
        	return Double.parseDouble(source);
        } catch (Exception e) {
        	log.warn("Parse string to double is error!  --> " + source, e);
        }
        return null;
	}

}
