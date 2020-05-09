package top.lshaci.framework.redis.utils;

import cn.hutool.core.util.StrUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import top.lshaci.framework.common.exception.BaseException;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;

import static java.time.LocalDate.now;

/**
 * RedisId
 *
 * @author lshaci
 * @since 1.0.7
 */
public class RedisId {

    /**
     * 流水号长度
     */
    private static int serialLength;
    /**
     * RedisTemplate
     */
    private static RedisTemplate<String, Object> redisTemplate;
    /**
     * 默认的前缀
     */
    private static final String defaultPrefix = "REDIS_ID:";
    /**
     * 系统默认时区
     */
    private static final ZoneId ZONE = ZoneId.systemDefault();

    /**
     * 根据前缀生成唯一标识(prefix + yyyyMMdd + 指定位数流水号)
     *
     * @param prefix 前缀
     * @return 唯一标识
     */
    public static String next(String prefix) {
        return next(prefix, serialLength);
    }

    /**
     * 根据前缀生成唯一标识(prefix + yyyyMMdd + 指定位数流水号)
     *
     * @param prefix 前缀
     * @param serialLength 补齐长度
     * @return 唯一标识
     */
    public static String next(String prefix, int serialLength) {
        String now = now().toString().replace("-", "");
        String key = prefix + now;
        String id = redisNext(key, serialLength);
        return new StringBuilder(key).append(id).toString();
    }

    /**
     * 获取自增的唯一标识(yyyyMMdd + 指定位数流水号)
     *
     * @return 自增的唯一标识
     */
    public static String next() {
        return next(serialLength);
    }

    /**
     * 获取自增的唯一标识(yyyyMMdd + 指定位数流水号)
     *
     * @param serialLength 补齐长度
     * @return 自增的唯一标识
     */
    public static String next(int serialLength) {
        String now = now().toString().replace("-", "");
        String key = defaultPrefix + now;
        String id = redisNext(key, serialLength);
        return new StringBuilder(now).append(id).toString();
    }

    /**
     * 在redis中获取指定key的自增数值, 并使用0补齐到serialLength位
     *
     * @param key redis中的key
     * @param serialLength 补齐长度
     * @return 补齐后的数值
     */
    private static String redisNext(String key, int serialLength) {
        Long id = redisTemplate.opsForValue().increment(key);
        if (Objects.isNull(id)) {
            throw new BaseException("流水号生成失败");
        }
        redisTemplate.expireAt(key, nextDate());
        return StrUtil.padPre(id.toString(), serialLength, '0');
    }

    /**
     * 获取当前时间的下一天的00:00:05
     *
     * @return 下一天的00:00:05
     */
    private static Date nextDate() {
        LocalDateTime localDateTime = LocalDateTime.now().plusDays(1).withHour(0).withMinute(0).withSecond(5);
        Instant instant = localDateTime.atZone(ZONE).toInstant();
        return Date.from(instant);
    }

    public void setSerialLength(int serialLength) {
        RedisId.serialLength = serialLength;
    }

    @Autowired
    public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
        RedisId.redisTemplate = redisTemplate;
    }

}
