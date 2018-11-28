package top.lshaci.framework.fastdfs;

import java.io.IOException;

import javax.annotation.PostConstruct;

import org.csource.common.MyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;
import top.lshaci.framework.fastdfs.constant.FastDFSConstant;
import top.lshaci.framework.fastdfs.properties.FastDFSProperties;

@Slf4j
@Configuration
@EnableConfigurationProperties(FastDFSProperties.class)
@ConditionalOnProperty(prefix = FastDFSConstant.FAST_DFS_PREFIX, value = "enabled", havingValue = "true")
public class FastDFSClientConfig {
	
    @Autowired
    private FastDFSProperties properties;
    
    @PostConstruct
    public void initPool() throws IOException, MyException {
    	log.debug("Init tracker server pool config.");
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
    }
    
    @PostConstruct
    public void setMaxFileSize() {
    	log.debug("Set fast dfs client max file size.");
    	int maxFileSize = properties.getMaxFileSize();
    	if (maxFileSize < 0) {
			maxFileSize = FastDFSConstant.DEFAULT_MAX_FILE_SIZE;
		}
    	FastDFSClient.maxFileSize = maxFileSize;
    	log.debug("The fast dfs client max file size is:{}", maxFileSize);
    }

}
