package top.lshaci.framework.fastdfs.config;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;

/**
 * <p>Tracker Server Factory</p><br>
 *
 * <b>1.0.6: </b>Modify create method
 *
 * @author lshaci
 * @since 0.0.4
 * @version 1.0.6
 */
class TrackerServerFactory extends BasePooledObjectFactory<TrackerServer>{

	@Override
	public TrackerServer create() throws Exception {
        return new TrackerClient().getConnection();
	}

	@Override
	public PooledObject<TrackerServer> wrap(TrackerServer trackerServer) {
		return new DefaultPooledObject<>(trackerServer);
	}

}
