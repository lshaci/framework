package top.lshaci.framework.fastdfs.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.TrackerServer;
import top.lshaci.framework.fastdfs.enums.ErrorCode;
import top.lshaci.framework.fastdfs.exception.FastDFSException;
import top.lshaci.framework.fastdfs.properties.FrameworkFastDFSProperties;

import java.util.Map;
import java.util.Properties;

/**
 * <p>Tracker server pool</p><br>
 *
 * <b>1.0.6: </b>Modify the initialization method
 *
 * @author lshaci
 * @since 0.0.4
 * @version 1.0.6
 */
@Data
@Slf4j
public class TrackerServerPool {

	/**
	 * The max file size
	 */
	private long maxFileSize;
	/**
	 * Reverse proxy address(Use with nginx)
	 */
	private String reverseProxyAddress;
	/**
	 * The tracker server pool
	 */
	private GenericObjectPool<TrackerServer> trackerServerPool;

    /**
     * Constructor a <code>TrackerServerPool</code> with fastDFS properties
     *
     * @param properties The fastDFS properties
     * @throws Exception if fastDFS client init failure
     */
	public TrackerServerPool(FrameworkFastDFSProperties properties) throws Exception {
		this.maxFileSize = properties.getMaxFileSize().toBytes();
		this.reverseProxyAddress = properties.getReverseProxyAddress();
		// fast dfs client init
		clientInit(properties.getProperties());
		// pool config
		GenericObjectPoolConfig<TrackerServer> poolConfig = new GenericObjectPoolConfig<>();
		poolConfig.setMinIdle(properties.getMinIdle());
        poolConfig.setMaxIdle(properties.getMaxIdle());
		poolConfig.setMaxTotal(properties.getMinIdle());

		trackerServerPool = new GenericObjectPool<>(new TrackerServerFactory(), poolConfig);
	}

    /**
     * Init fastDFS client
     *
     * @param properties The fastDFS properties
     * @throws Exception if fastDFS client init failure
     */
	private void clientInit(Map<String, String> properties) throws Exception {
        Properties fastDFSProperties = new Properties();
        if (MapUtils.isNotEmpty(properties)) {
            properties.forEach((k, v) -> fastDFSProperties.put("fastdfs." + k, v));
        }
        ClientGlobal.initByProperties(fastDFSProperties);
        log.debug("ClientGlobal configInfo: \n{}", ClientGlobal.configInfo());
    }

	/**
	 * Borrow tracker server from pool
	 *
	 * @return Tracker server instance
	 */
	public TrackerServer borrowObject() {
        try {
            return trackerServerPool.borrowObject();
        } catch (Exception e) {
        	log.error(ErrorCode.FETCH_TRACKER_SERVER_FAILED.getCode(), e);
            throw new FastDFSException(ErrorCode.FETCH_TRACKER_SERVER_FAILED);
        }
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
