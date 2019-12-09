package top.lshaci.framework.web.helper.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import top.lshaci.framework.web.helper.service.PreventRepeat;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

/**
 * Redis-based prevent repeat submit
 *
 * @author lshaci
 * @since 1.0.5
 */
@AllArgsConstructor
public class RedisPreventRepeat implements PreventRepeat {

    /**
     * The timeout of the submit key
     */
    private final long timeout;

    /**
     * The String Redis Template
     */
    private final StringRedisTemplate redisTemplate;

    @Override
    public String getAndSet(String key) {
        String value = redisTemplate.opsForValue().getAndSet(key, VALUE);
        redisTemplate.expire(key, timeout, MILLISECONDS);
        return value;
    }

    @Override
    public void remove(String key) {
        redisTemplate.delete(key);
    }
}
