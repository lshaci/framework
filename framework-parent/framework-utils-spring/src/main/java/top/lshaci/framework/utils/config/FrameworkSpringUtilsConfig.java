package top.lshaci.framework.utils.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.freemarker.FreeMarkerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactoryBean;
import top.lshaci.framework.utils.FreemarkerUtil;
import top.lshaci.framework.utils.SpringUtil;

import java.util.Properties;

/**
 * <p>Framework spring utils config</p><br>
 *
 * @author lshaci
 * @since 1.0.7
 */
@Slf4j
@Configuration
public class FrameworkSpringUtilsConfig {

    /**
     * Config freemarker util
     *
     * @param properties the freemarker properties
     * @return freemarker util bean
     */
    @Bean
    @ConditionalOnBean(FreeMarkerProperties.class)
    public FreemarkerUtil freemarkerUtil(FreeMarkerProperties properties) throws Exception {
        log.debug("Config freemarker util...");

        FreeMarkerConfigurationFactoryBean factory = new FreeMarkerConfigurationFactoryBean();
        factory.setTemplateLoaderPaths(properties.getTemplateLoaderPath());
        factory.setPreferFileSystemAccess(properties.isPreferFileSystemAccess());
        factory.setDefaultEncoding(properties.getCharsetName());
        Properties settings = new Properties();
        settings.putAll(properties.getSettings());
        factory.setFreemarkerSettings(settings);
        factory.afterPropertiesSet();

        FreemarkerUtil freemarkerUtil = new FreemarkerUtil();
        freemarkerUtil.setConfiguration(factory.getObject());
        return freemarkerUtil;
    }

    /**
     * Config spring util
     *
     * @return spring util bean
     */
    @Bean
    public SpringUtil springUtil() {
        log.debug("Config spring util...");
        return new SpringUtil();
    }

}
