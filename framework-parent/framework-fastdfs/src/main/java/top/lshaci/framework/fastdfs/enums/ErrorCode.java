package top.lshaci.framework.fastdfs.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * The upload file error message
 * 
 * @author lshaci
 * @since 0.0.4
 */
@Getter
@AllArgsConstructor
public enum ErrorCode {
	
	/**
	 * This file is empty.
	 */
	FILE_IS_EMPTY("This file is empty.", "上传的文件为空"),
	/**
	 * File uploading failed.
	 */
	FILE_UPLOAD_FAILED("File uploading failed.", "上传文件失败"),
	/**
	 * Error uploading file.
	 */
	FILE_UPLOAD_ERROR("Error uploading file.", "上传文件发生错误"),
	/**
	 * The file size exceeds the limit.
	 */
	FILE_OUT_SIZE("The file size exceeds the limit.", "文件大小超过限制"),
	/**
	 * The file path is null.
	 */
	FILE_PATH_IS_NULL("The file path is null.", "文件路径为空"),
	/**
	 * The file not exist.
	 */
	FILE_NOT_EXIST("The file not exist.", "文件不存在"),
	/**
	 * File download failed.
	 */
    FILE_DOWNLOAD_FAILED("File download failed.", "文件下载失败"),
    /**
     * File write failed.
     */
    FILE_WRITE_FAILED("File write failed.", "写出文件失败"),
	/**
	 * The output stream is null.
	 */
	OUTPUT_STREAM_IS_NULL("The output stream is null.", "输出流为空"),
	/**
	 * The http servelt response is null.
	 */
	RESPONSE_IS_NULL("The http servelt response is null.", "HTTP响应为空"),
	/**
	 * Fetch http servelt response stream failed.
	 */
	FETCH_RESPONSE_STREAM_FAILED("Fetch http servelt response stream failed.", "获取HTTP响应流失败"),
	/**
	 * Fetch token failed.
	 */
	FETCH_TOKEN_FAILED("Fetch token failed.", "获取token失败"),
	/**
	 * Fetch file information failed.
	 */
	FETCH_FILE_INFO_FAILED("Fetch file information failed.", "获取文件信息失败"),
	/**
	 * Fetch tracker server failed.
	 */
	FETCH_TRACKER_SERVER_FAILED("Fetch tracker server failed.", "获取tracker server失败"),
	/**
	 * File deletion failed.
	 */
    FILE_DELETE_FAILED("File deletion failed.", "删除文件失败"),
    /**
     * Fastdfs file server connection failed.
     */
    FILE_SERVER_CONNECTION_FAILED("Fastdfs file server connection failed.", "文件服务器连接失败"),
	;
	
	private String code;
	private String message;
    
}
