package top.lshaci.framework.web.helper.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.validation.Valid;
import javax.validation.constraints.Min;

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
    @Valid
    private PreventRepeatSubmit preventRepeatSubmit = new PreventRepeatSubmit();

    /**
     * Prevent Repeat Submit
     */
    @Data
    public class PreventRepeatSubmit {

        /**
         * 是否开启防重复提交
         */
        private boolean enabled = true;

        /**
         * 提交地址的超时时间(ms)
         */
        @Min(value = 1, message = "The [framework.web.helper.prevent-repeat-submit-timeout] must be gt zero!")
        private long timeout = 500;
    }
}
