package top.lshaci.framework.fastdfs.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.lshaci.framework.fastdfs.properties.FrameworkFastDFSProperties;
import top.lshaci.framework.fastdfs.util.FastDFSClient;

import static top.lshaci.framework.fastdfs.constant.FastDFSConstant.FAST_DFS_PREFIX;

/**
 * <p>Fast dfs client config</p><br>
 *
 * <b>1.0.6: </b>从properties读取配置修改为从yml中获取
 *
 * @author lshaci
 * @since 0.0.4
 * @version 1.0.6
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(FrameworkFastDFSProperties.class)
@ConditionalOnProperty(prefix = FAST_DFS_PREFIX, value = "enabled", havingValue = "true", matchIfMissing = true)
public class FastDFSClientConfig {

    /**
     * 创建FastDfs Tracker Server连接池
	 *
	 * @param properties 配置文件中定义的属性
	 *
	 * @return FastDfs 服务连接池
	 * @throws Exception 创建失败抛出异常
     */
    @Bean
    @ConditionalOnMissingBean
    public TrackerServerPool trackerServerPool(FrameworkFastDFSProperties properties) throws Exception {
		log.debug("Config fast dfs tracker server pool.");
        TrackerServerPool trackerServerPool = new TrackerServerPool(properties);

        log.debug("Set fast dfs client.");
        FastDFSClient.pool = trackerServerPool;
        log.debug("The fast dfs client info: \n{}", FastDFSClient.info());
        return trackerServerPool;
    }

}
