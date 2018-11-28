package top.lshaci.framework.fastdfs.constant;

/**
 * FastDFS constant
 * 
 * @author lshaci
 * @since 0.0.4
 */
public interface FastDFSConstant {
	
	String POINT = ".";
	
	String SEPARATOR = "/";
	
	String FAST_DFS_PREFIX = "spring.fastdfs";
	
	String FILE_DESCRIPTION_FILE_NAME = "filename";
	
	String DEFAULT_CONFIG = "fastdfs.properties";

	int DEFAULT_MIN_STORAGE_CONNECTION = 2;
	
	int DEFAULT_MAX_STORAGE_CONNECTION = 8;
	
	int DEFAULT_MAX_FILE_SIZE = 1024 * 1024;
}
