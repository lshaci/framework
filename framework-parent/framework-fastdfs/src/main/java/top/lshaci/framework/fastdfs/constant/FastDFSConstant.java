package top.lshaci.framework.fastdfs.constant;

/**
 * FastDFS constant
 * 
 * @author lshaci
 * @since 0.0.4
 */
public interface FastDFSConstant {
	
	/**
	 * The dot(.)
	 */
	String DOT = ".";
	
	/**
	 * The file separator
	 */
	String SEPARATOR = "/";
	
	/**
	 * FastDFS config properties prefix
	 */
	String FAST_DFS_PREFIX = "spring.fastdfs";
	
	/**
	 * The file description - file name
	 */
	String FILE_DESCRIPTION_FILE_NAME = "filename";
	
	/**
	 * FastDFS default config name
	 */
	String DEFAULT_CONFIG = "fastdfs.properties";

	/**
	 * The default min storage connection - 2
	 */
	int DEFAULT_MIN_STORAGE_CONNECTION = 2;
	
	/**
	 * The default max storage connection - 8
	 */
	int DEFAULT_MAX_STORAGE_CONNECTION = 8;
	
	/**
	 * The default upload file max size(1MB) 
	 */
	int DEFAULT_MAX_FILE_SIZE = 1024 * 1024;
}
