package top.lshaci.framework.fastdfs.properties;

import org.apache.commons.lang3.StringUtils;
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
	 * 是否启用FastDfs配置(默认开启)
	 */
	private Boolean enabled = true;

	/**
	 *  反向代理地址
	 */
	private String reverseProxyAddress;
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
	 * The upload max file size
	 */
	private long maxFileSize = FastDFSConstant.DEFAULT_MAX_FILE_SIZE;

    /**
     * The upload max file size. Values can use the suffixed "MB" or "KB" to indicate a Megabyte or Kilobyte size.
     *
     * @param maxFileSize the maximum request size
     */
    public void setMaxFileSize(String maxFileSize) {
        this.maxFileSize = parseSize(maxFileSize);
    }

    /**
     * Parse file size
     *
     * @param size the size of the String type
     * @return the size of the Long type
     */
    private long parseSize(String size) {
        if (StringUtils.isBlank(size)) {
            return FastDFSConstant.DEFAULT_MAX_FILE_SIZE;
        }
        size = size.toUpperCase();
        if (size.endsWith("KB")) {
            return Long.valueOf(size.substring(0, size.length() - 2)) * 1024;
        }
        if (size.endsWith("MB")) {
            return Long.valueOf(size.substring(0, size.length() - 2)) * 1024 * 1024;
        }
        return Long.valueOf(size);
    }
}
