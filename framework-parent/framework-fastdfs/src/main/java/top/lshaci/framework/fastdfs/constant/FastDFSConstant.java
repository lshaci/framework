package top.lshaci.framework.fastdfs.constant;

public interface FastDFSConstant {
	
	String FAST_DFS_PREFIX = "spring.fastdfs";
	
	String DEFAULT_CONFIG = "fastdfs.properties";

	int DEFAULT_MIN_STORAGE_CONNECTION = 2;
	
	int DEFAULT_MAX_STORAGE_CONNECTION = 8;
	
	int DEFAULT_MAX_FILE_SIZE = 1024 * 1024;
}
