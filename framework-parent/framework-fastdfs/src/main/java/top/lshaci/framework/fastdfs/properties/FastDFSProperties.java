package top.lshaci.framework.fastdfs.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;
import top.lshaci.framework.fastdfs.constant.FastDFSConstant;

/**
 * FastDFSProperties
 * 
 * @author lshaci
 * @since 0.0.4
 */
@Data
@ConfigurationProperties(FastDFSConstant.FAST_DFS_PREFIX)
public class FastDFSProperties {
	
	/**
	 * The file server address
	 */
	private String fileServerAddr;
	/**
	 * The fastdfs config
	 */
	private String config = FastDFSConstant.DEFAULT_CONFIG;
	/**
	 * The tracker server pool min connection number
	 */
	private int minStorageConnection = FastDFSConstant.DEFAULT_MIN_STORAGE_CONNECTION;
	/**
	 * The tracker server pool max connection number
	 */
	private int maxStorageConnection = FastDFSConstant.DEFAULT_MAX_STORAGE_CONNECTION;
	/**
	 * The upload max file size(byte)
	 */
	private long maxFileSize = FastDFSConstant.DEFAULT_MAX_FILE_SIZE;

}
