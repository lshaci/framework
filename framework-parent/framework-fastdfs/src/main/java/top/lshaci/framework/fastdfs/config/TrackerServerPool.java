package top.lshaci.framework.fastdfs.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.csource.common.MyException;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.TrackerServer;
import top.lshaci.framework.fastdfs.enums.ErrorCode;
import top.lshaci.framework.fastdfs.exception.FastDFSException;

import java.io.IOException;

/**
 * Tracker server pool
 *
 * @author lshaci
 * @since 0.0.4
 */
@Slf4j
public class TrackerServerPool {

    /**
     * Tracker server config
     */
	protected static String config;

    /**
     * The min storage connection
     */
	protected static int minStorageConnection;

    /**
     * The max storage connection
     */
	protected static int maxStorageConnection;

    /**
     * The tracker server pool
     */
	protected static GenericObjectPool<TrackerServer> trackerServerPool;

    /**
     * Privatization construction method
     */
    private TrackerServerPool(){

    }

    /**
     * Init tracker server pool
     *
     * @throws IOException
     * @throws MyException
     */
	public static void initPool() throws IOException, MyException {
		log.debug("Init tracker server pool...");
		// load config properties
		ClientGlobal.initByProperties(config);
		log.debug("ClientGlobal configInfo: {}", ClientGlobal.configInfo());

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
	 * @throws FastDFSException
	 */
	public static TrackerServer borrowObject() throws FastDFSException {
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
    public static void returnObject(TrackerServer trackerServer){
    	if (trackerServer != null) {
    		trackerServerPool.returnObject(trackerServer);
		}
    }

}
