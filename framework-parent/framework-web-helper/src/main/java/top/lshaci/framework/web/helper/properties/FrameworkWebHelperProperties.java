package top.lshaci.framework.web.helper.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <p>Framework web helper properties</p>
 *
 * @author lshaci
 * @since 1.0.5
 */
@Data
@ConfigurationProperties(prefix = "framework.web.helper")
public class FrameworkWebHelperProperties {

    /**
     * 防重复提交的配置
     */
    private PreventRepeatSubmit preventRepeatSubmit = new PreventRepeatSubmit();

    /**
     * Prevent Repeat Submit
     */
    @Data
    public class PreventRepeatSubmit {

        /**
         * 是否开启
         */
        private boolean enabled = true;

        /**
         * 防重复key的超时时间(ms)
         */
        private long timeout = 500;
    }
}
