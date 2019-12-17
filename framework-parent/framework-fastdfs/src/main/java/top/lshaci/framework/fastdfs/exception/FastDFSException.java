package top.lshaci.framework.fastdfs.exception;

import lombok.Getter;
import top.lshaci.framework.common.exception.BaseException;
import top.lshaci.framework.fastdfs.enums.ErrorCode;

/**
 * <p>FastDFS Exception</p><br>
 *
 * <b>1.0.6: </b> Remove unused constructors
 *
 * @author lshaci
 * @since 0.0.4
 * @version 1.0.6
 */
@Getter
public class FastDFSException extends BaseException {

	private static final long serialVersionUID = 1L;

	/**
	 * The upload file error message
	 */
	private ErrorCode errorCode;

	/**
	 * Constructor a <code>FastDFSException</code> with error code
	 *
	 * @param errorCode The upload file error message
	 */
	public FastDFSException(ErrorCode errorCode) {
		super(errorCode.getMessage());
		this.errorCode = errorCode;
	}
}
