package top.lshaci.framework.fastdfs.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;
import top.lshaci.framework.fastdfs.constant.FastDFSConstant;

@Data
@ConfigurationProperties(FastDFSConstant.FAST_DFS_PREFIX)
public class FastDFSProperties {
	
	private String fileServer;
	
	private String config = FastDFSConstant.DEFAULT_CONFIG;
	
	private int minStorageConnection = FastDFSConstant.DEFAULT_MIN_STORAGE_CONNECTION;
	
	private int maxStorageConnection = FastDFSConstant.DEFAULT_MAX_STORAGE_CONNECTION;
	
	private int maxFileSize = FastDFSConstant.DEFAULT_MAX_FILE_SIZE;

}
