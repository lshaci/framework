package top.lshaci.framework.fastdfs.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.unit.DataSize;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

import static top.lshaci.framework.fastdfs.constant.FastDFSConstant.FAST_DFS_PREFIX;

/**
 * <p>Framework FastDFS Properties</p><br>
 *
 * <b>1.0.6: </b>Additional fastDFS properties
 *
 * @author lshaci
 * @since 0.0.4
 * @version 1.0.6
 */
@Data
@Validated
@ConfigurationProperties(FAST_DFS_PREFIX)
public class FrameworkFastDFSProperties {

	/**
	 * Enable the fastDFS config
	 */
	private boolean enabled = true;
	/**
	 *  Reverse proxy address(Use with nginx)
	 */
	private String reverseProxyAddress;
	/**
	 * The tracker server pool min idle connection number
	 */
	@Min(value = 1, message = "The [framework.fastdfs.min-idle] must greater than 1!")
	private int minIdle = 2;
	/**
	 * The tracker server pool max idle connection number
	 */
	@Min(value = 1, message = "The [framework.fastdfs.max-idle] must greater than 1!")
	private int maxIdle = 8;
	/**
	 * The tracker server pool max total connection number
	 */
	@Min(value = 1, message = "The [framework.fastdfs.max-total] must greater than 1!")
	private int maxTotal = 8;
	/**
	 * Max file size of upload (1MB)
	 */
	@NotNull(message = "The [framework.fastdfs.max-file-size] must not be null!")
	private DataSize maxFileSize = DataSize.ofMegabytes(1);
	/**
	 * Additional fastDFS properties
	 */
	private Map<String, String> properties = new HashMap<>();
}
