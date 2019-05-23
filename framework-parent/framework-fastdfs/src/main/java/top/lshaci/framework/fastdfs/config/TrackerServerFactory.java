package top.lshaci.framework.fastdfs.config;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;

/**
 * Tracker Server Factory
 *
 * @author lshaci
 * @since 0.0.4
 */
class TrackerServerFactory extends BasePooledObjectFactory<TrackerServer>{

	@Override
	public TrackerServer create() throws Exception {
		// TrackerClient
        TrackerClient trackerClient = new TrackerClient();
        // TrackerServer
        TrackerServer trackerServer = trackerClient.getConnection();

        return trackerServer;
	}

	@Override
	public PooledObject<TrackerServer> wrap(TrackerServer trackerServer) {
		return new DefaultPooledObject<>(trackerServer);
	}

}
