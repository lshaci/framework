package top.lshaci.framework.fastdfs;

import java.io.IOException;

import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.csource.common.MyException;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.TrackerServer;

import lombok.extern.slf4j.Slf4j;
import top.lshaci.framework.fastdfs.exception.FastDFSException;

/**
 * Tracker server pool
 * 
 * @author lshaci
 * @since 0.0.4
 */
@Slf4j
class TrackerServerPool {

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
     * TrackerServer 对象池.
     * GenericObjectPool 没有无参构造
     */
	protected static GenericObjectPool<TrackerServer> trackerServerPool;

    /**
     * Privatization construction method
     */
    private TrackerServerPool(){};

    /**
     * Init tracker server pool
     * 
     * @throws IOException
     * @throws MyException
     */
    protected static void initPool() throws IOException, MyException {
		log.debug("Init tracker server pool...");
		// load config properties
		ClientGlobal.initByProperties(config);
		log.debug("ClientGlobal configInfo: {}", ClientGlobal.configInfo());

		// pool config
		GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
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
    protected static TrackerServer borrowObject() throws FastDFSException {
        TrackerServer trackerServer = null;
        try {
            trackerServer = trackerServerPool.borrowObject();
        } catch (Exception e) {
        	log.error("Error fetching tracker server from pool.", e);
            throw new FastDFSException("Error fetching tracker server from pool.", e);
        }
        if (trackerServer == null) {
        	throw new FastDFSException("The fetching tracker server is null.");
		}
        return trackerServer;
    }

    /**
     * Return tracker server to pool
     * 
     * @param trackerServer the tracker server instance
     */
    protected static void returnObject(TrackerServer trackerServer){
    	trackerServerPool.returnObject(trackerServer);
    }

}
