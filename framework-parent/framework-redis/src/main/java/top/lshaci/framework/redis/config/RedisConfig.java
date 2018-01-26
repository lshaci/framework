package top.lshaci.framework.redis.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.core.RedisTemplate;

import top.lshaci.framework.redis.lock.RedisDistributedLock;
import top.lshaci.framework.redis.lock.locker.Locker;
import top.lshaci.framework.redis.lock.locker.RedisLocker;

/**
 * Redis config
 * 
 * @author lshaci
 * @since 0.0.4
 */
@Configuration
@PropertySource("classpath:redis.properties")
public class RedisConfig {
	
	@Bean
	@ConfigurationProperties("locker.redis")
	public Locker redisLocker(RedisTemplate<Object, Object> redisTemplate) {
		RedisLocker redisLocker = new RedisLocker(redisTemplate);
		return redisLocker;
	}
	
	@Bean
	@ConfigurationProperties("distributed.lock.redis")
	public RedisDistributedLock redisDistributedLock(RedisTemplate<Object, Object> redisTemplate) {
		RedisDistributedLock redisDistributedLock = new RedisDistributedLock(redisLocker(redisTemplate));
		return redisDistributedLock;
	}

}
