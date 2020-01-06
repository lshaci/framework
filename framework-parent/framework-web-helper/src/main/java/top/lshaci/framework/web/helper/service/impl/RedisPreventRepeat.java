package top.lshaci.framework.web.helper.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import top.lshaci.framework.web.helper.service.PreventRepeat;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

/**
 * <p>Redis-based prevent repeat submit</p><br>
 *
 * <b>1.0.7:</b>This method <code>getAndSet</code> add parameter <code>timeout</code>
 *
 * @author lshaci
 * @since 1.0.5
 * @version 1.0.7
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
    public String getAndSet(String key, long timeout) {
        String value = redisTemplate.opsForValue().getAndSet(key, VALUE);
        redisTemplate.expire(key, timeout > 0 ? timeout : this.timeout, MILLISECONDS);
        return value;
    }

    @Override
    public void remove(String key) {
        redisTemplate.delete(key);
    }
}
