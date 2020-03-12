package top.lshaci.framework.web.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import top.lshaci.framework.web.model.ExceptionMessage;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>Framework web properties</p>
 *
 * <p><b>1.0.4: </b>添加自定义异常信息属性{@code exceptionMessages}</p><br>
 *
 * @author lshaci
 * @since 1.0.2
 * @version 1.0.4
 */
@Data
@ConfigurationProperties(prefix = "framework.web")
public class FrameworkWebProperties {

    /**
     * 全局异常处理配置
     */
    private GlobalExceptionHandle globalExceptionHandle = new GlobalExceptionHandle();

    /**
     * 防重复提交的配置
     */
    @Valid
    private PreventRepeatSubmit preventRepeatSubmit = new PreventRepeatSubmit();

    /**
     * Global Exception Handle
     *
     * @author lshaci
     * @since 1.0.7
     */
    @Data
    public class GlobalExceptionHandle {

        /**
         * 是否开启全局异常处理
         */
        private boolean enabled = true;

        /**
         * 异常信息
         */
        private List<ExceptionMessage> messages = new ArrayList<>();
    }

    /**
     * Prevent Repeat Submit
     *
     * @author lshaci
     * @since 1.0.7
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
        @Min(value = 1000, message = "防重复提交超时时间必须大于1000")
        private long timeout = 2000;
    }
}
