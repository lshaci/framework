package top.lshaci.framework.web.helper.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import top.lshaci.framework.web.helper.aspect.PreventRepeatSubmitAspect;
import top.lshaci.framework.web.helper.properties.FrameworkWebHelperProperties;
import top.lshaci.framework.web.helper.service.PreventRepeat;
import top.lshaci.framework.web.helper.service.impl.RedisPreventRepeat;
import top.lshaci.framework.web.helper.service.impl.TimedCachePreventRepeat;

/**
 * <p>Framework web helper config</p>
 *
 * @author lshaci
 * @since 1.0.5
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
    public PreventRepeatSubmitAspect preventRepeatSubmitAspect(PreventRepeat preventRepeat) {
        log.debug("Config prevent repeat submit aspect...");
        return new PreventRepeatSubmitAspect(preventRepeat);
    }

    /**
     * Redis Prevent Repeat
     *
     * @param stringRedisTemplate string Redis Template
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(StringRedisTemplate.class)
    public PreventRepeat redisPreventRepeat(StringRedisTemplate stringRedisTemplate){
        log.debug("Config redis prevent repeat...");
        return new RedisPreventRepeat(properties.getPreventRepeatSubmit().getTimeout(), stringRedisTemplate);
    }

    @Bean
    @ConditionalOnMissingBean(StringRedisTemplate.class)
    public PreventRepeat timedCachePreventRepeat(){
        log.debug("Config timed cache prevent repeat...");
        return new TimedCachePreventRepeat(properties.getPreventRepeatSubmit().getTimeout());
    }


}
