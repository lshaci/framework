package top.lshaci.framework.fastdfs.config;

import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.TrackerServer;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import top.lshaci.framework.fastdfs.enums.ErrorCode;
import top.lshaci.framework.fastdfs.exception.FastDFSException;

/**
 * Tracker server pool
 *
 * @author lshaci
 * @since 0.0.4
 */
@Data
@Slf4j
public class TrackerServerPool {

	/**
	 * The max file size
	 */
	private long maxFileSize;
	/**
	 * 反向代理地址
	 */
	private String reverseProxyAddress;
    /**
     * Tracker server config
     */
	private String config;

    /**
     * The min storage connection
     */
	private int minStorageConnection;

    /**
     * The max storage connection
     */
	private int maxStorageConnection;

    /**
     * The tracker server pool
     */
	private GenericObjectPool<TrackerServer> trackerServerPool;

    /**
     * Init tracker server pool
     *
     * @throws Exception 初始化FastDfs Tracker Server连接池失败抛出异常
     */
	public void init() throws Exception {
		log.debug("Init tracker server pool...");
		// load config properties
		ClientGlobal.initByProperties(config);
		log.debug("ClientGlobal configInfo: \n{}", ClientGlobal.configInfo());

		// pool config
		GenericObjectPoolConfig<TrackerServer> poolConfig = new GenericObjectPoolConfig<>();
		poolConfig.setMinIdle(minStorageConnection);
		poolConfig.setMaxTotal(maxStorageConnection);

		trackerServerPool = new GenericObjectPool<>(new TrackerServerFactory(), poolConfig);
	}

	/**
	 * Borrow tracker server from pool
	 *
	 * @return Tracker server instance
	 */
	public TrackerServer borrowObject() {
        TrackerServer trackerServer = null;
        try {
            trackerServer = trackerServerPool.borrowObject();
        } catch (Exception e) {
        	log.error(ErrorCode.FETCH_TRACKER_SERVER_FAILED.getCode(), e);
            throw new FastDFSException(ErrorCode.FETCH_TRACKER_SERVER_FAILED);
        }
        if (trackerServer == null) {
        	throw new FastDFSException(ErrorCode.FETCH_TRACKER_SERVER_FAILED);
		}
        return trackerServer;
    }

    /**
     * Return tracker server to pool
     *
     * @param trackerServer the tracker server instance
     */
    public void returnObject(TrackerServer trackerServer){
    	if (trackerServer != null) {
    		trackerServerPool.returnObject(trackerServer);
		}
    }

}