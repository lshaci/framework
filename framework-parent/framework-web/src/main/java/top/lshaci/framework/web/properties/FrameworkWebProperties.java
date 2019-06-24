package top.lshaci.framework.web.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Framework web properties
 *
 * @author lshaci
 * @since 1.0.2
 */
@Data
@ConfigurationProperties(prefix = "framework.web")
public class FrameworkWebProperties {

    /**
     * 需要开启的功能
     */
    private Enabled enabled = new Enabled();

    /**
     * framework web中的功能
     */
    @Data
    class Enabled {

        /**
         * 是否开启角色名称控制(默认开启)
         */
        private Boolean userRole = true;

        /**
         * 是否开启全局异常处理(默认开启)
         */
        private Boolean globalExceptionHandler = true;

        /**
         * 是否开启防重复提交(默认开启)
         */
        private Boolean preventRepeatSubmit = true;

        /**
         * 是否开启web日志(默认关闭)
         */
        private Boolean webLog = false;

    }
}
