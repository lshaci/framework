package top.lshaci.framework.web.helper.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import top.lshaci.framework.web.helper.aspect.PreventRepeatSubmitAspect;
import top.lshaci.framework.web.helper.properties.FrameworkWebHelperProperties;
import top.lshaci.framework.web.helper.service.PreventRepeat;
import top.lshaci.framework.web.helper.service.PreventRepeatKey;
import top.lshaci.framework.web.helper.service.impl.RedisPreventRepeat;
import top.lshaci.framework.web.helper.service.impl.TimedCachePreventRepeat;
import top.lshaci.framework.web.helper.utils.FreemarkerUtil;

/**
 * <p>Framework web helper config</p><br>
 *
 * <b>1.0.7:</b>Add freemarker util bean
 *
 * @author lshaci
 * @since 1.0.5
 * @version 1.0.7
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(FrameworkWebHelperProperties.class)
public class FrameworkWebHelperConfig {

    @Autowired
    private FrameworkWebHelperProperties properties;

    /**
     * Config prevent repeat submit aspect
     *
     * @return the prevent repeat submit aspect bean
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(value = "framework.web.helper.prevent-repeat-submit.enabled", havingValue = "true", matchIfMissing = true)
    public PreventRepeatSubmitAspect preventRepeatSubmitAspect(PreventRepeat preventRepeat, PreventRepeatKey preventRepeatKey) {
        log.debug("Config prevent repeat submit aspect...");
        return new PreventRepeatSubmitAspect(preventRepeat, preventRepeatKey);
    }

    /**
     * Config redis prevent repeat, Conditional on bean {@code StringRedisTemplate}
     *
     * @param stringRedisTemplate the string redis template
     * @return the redis prevent repeat bean
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(StringRedisTemplate.class)
    public PreventRepeat redisPreventRepeat(StringRedisTemplate stringRedisTemplate){
        log.debug("Config redis prevent repeat service...");
        return new RedisPreventRepeat(properties.getPreventRepeatSubmit().getTimeout(), stringRedisTemplate);
    }

    /**
     * Config timed cache prevent repeat, Conditional on missing bean {@code StringRedisTemplate}
     *
     * @return the timed cache prevent repeat bean
     */
    @Bean
    @ConditionalOnMissingBean(StringRedisTemplate.class)
    public PreventRepeat timedCachePreventRepeat(){
        log.debug("Config timed cache prevent repeat service...");
        return new TimedCachePreventRepeat(properties.getPreventRepeatSubmit().getTimeout());
    }

    /**
     * Config prevent repeat submit key service, Conditional on missing bean
     *
     * @return the prevent repeat submit key bean
     */
    @Bean
    @ConditionalOnMissingBean
    public PreventRepeatKey preventRepeatKey() {
        log.debug("Config prevent repeat submit key service...");
        return request -> request.getSession().getId() + ":" + request.getMethod() + ":" + request.getRequestURI();
    }

    /**
     * Config freemarker util
     *
     * @return freemarker util bean
     */
    @Bean
    @ConditionalOnClass(freemarker.template.Configuration.class)
    public FreemarkerUtil freemarkerUtil() {
        log.debug("Config freemarker util...");
        return new FreemarkerUtil();
    }

}
