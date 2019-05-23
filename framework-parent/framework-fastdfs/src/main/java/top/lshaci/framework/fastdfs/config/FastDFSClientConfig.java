package top.lshaci.framework.fastdfs.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import top.lshaci.framework.fastdfs.constant.FastDFSConstant;
import top.lshaci.framework.fastdfs.properties.FastDFSProperties;
import top.lshaci.framework.fastdfs.util.FastDFSClient;

import javax.annotation.PostConstruct;

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
     * Init tracker server pool config
     *
     * @throws Exception if tracker server pool config error throw this
     */
    @PostConstruct
    public void initPool() throws Exception {
    	log.debug("Start tracker server pool config.");
    	TrackerServerPool.config = properties.getConfig();
    	int minStorageConnection = properties.getMinStorageConnection();
    	int maxStorageConnection = properties.getMaxStorageConnection();
    	if (maxStorageConnection < 0) {
    		maxStorageConnection = FastDFSConstant.DEFAULT_MAX_STORAGE_CONNECTION;
		}
    	if (minStorageConnection < 0 || minStorageConnection > maxStorageConnection) {
    		minStorageConnection = FastDFSConstant.DEFAULT_MIN_STORAGE_CONNECTION;
    	}
    	TrackerServerPool.minStorageConnection = minStorageConnection;
    	TrackerServerPool.maxStorageConnection = maxStorageConnection;

    	TrackerServerPool.initPool();
    	log.debug("Tracker server pool config successfully.");
    }

    /**
     * Set fast dfs client information
     *
     * @throws Exception if server address is empty throw this
     */
    @PostConstruct
    public void setMaxFileSize() {
    	log.debug("Set fast dfs client.");
    	long maxFileSize = properties.getMaxFileSize();
    	if (maxFileSize < 0) {
			maxFileSize = FastDFSConstant.DEFAULT_MAX_FILE_SIZE;
		}
    	String fileServerAddr = properties.getFileServerAddr();
    	if (StringUtils.isNotBlank(fileServerAddr)) {
    		FastDFSClient.fileServerAddr = fileServerAddr.trim();
		}
    	FastDFSClient.maxFileSize = maxFileSize;
    	log.debug("The fast dfs client info: \n\t{}", FastDFSClient.info());
    }

}
