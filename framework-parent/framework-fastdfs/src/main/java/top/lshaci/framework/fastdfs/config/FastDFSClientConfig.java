package top.lshaci.framework.fastdfs.config;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;
import top.lshaci.framework.fastdfs.constant.FastDFSConstant;
import top.lshaci.framework.fastdfs.properties.FastDFSProperties;
import top.lshaci.framework.fastdfs.util.FastDFSClient;

/**
 * Fast dfs client config
 *
 * @author lshaci
 * @since 0.0.4
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(FastDFSProperties.class)
@ConditionalOnProperty(prefix = FastDFSConstant.FAST_DFS_PREFIX, value = "enabled", havingValue = "true", matchIfMissing = true)
public class FastDFSClientConfig {

    @Autowired
    private FastDFSProperties properties;
    
    /**
     * 创建Fast dfs服务连接池
     */
    @Bean
    @ConditionalOnMissingBean
    public TrackerServerPool trackerServerPool() throws Exception {
    	TrackerServerPool pool = new TrackerServerPool();
    	initPool(pool);
    	return pool;
    }

    /**
     * Init tracker server pool config
     *
     * @throws Exception if tracker server pool config error throw this
     */
    private void initPool(TrackerServerPool pool) throws Exception {
    	log.debug("Start tracker server pool config.");
    	pool.setConfig(properties.getConfig());
    	long maxFileSize = properties.getMaxFileSize();
    	int minStorageConnection = properties.getMinStorageConnection();
    	int maxStorageConnection = properties.getMaxStorageConnection();
    	if (maxStorageConnection < 0) {
    		maxStorageConnection = FastDFSConstant.DEFAULT_MAX_STORAGE_CONNECTION;
		}
    	if (minStorageConnection < 0 || minStorageConnection > maxStorageConnection) {
    		minStorageConnection = FastDFSConstant.DEFAULT_MIN_STORAGE_CONNECTION;
    	}
    	if (maxFileSize < 0) {
			maxFileSize = FastDFSConstant.DEFAULT_MAX_FILE_SIZE;
		}
    	pool.setMaxFileSize(maxFileSize);
    	pool.setMinStorageConnection(minStorageConnection);
    	pool.setMaxStorageConnection(maxStorageConnection);
    	String fileServerAddr = properties.getFileServerAddr();
    	if (StringUtils.isNotBlank(fileServerAddr)) {
    		pool.setFileServerAddr(fileServerAddr.trim());
		}

    	pool.init();
    	log.debug("Tracker server pool config successfully.");
    }

    /**
     * Set fast dfs client information
     *
     * @throws Exception if server address is empty throw this
     */
    @Autowired
    public void setTrackerServerPool(TrackerServerPool trackerServerPool) {
    	log.debug("Set fast dfs client.");
    	FastDFSClient.pool = trackerServerPool;
    	log.debug("The fast dfs client info: \n\t{}", FastDFSClient.info());
    }

}
