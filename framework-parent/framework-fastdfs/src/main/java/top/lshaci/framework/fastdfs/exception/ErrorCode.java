package top.lshaci.framework.fastdfs.exception;

import lombok.Getter;

/**
 * 上传文件错误信息
 * 
 * @author lshaci
 * @since 0.0.4
 */
@Getter
public enum ErrorCode {
	
	/**
	 * This file is empty.
	 */
	FILE_IS_EMPTY("This file is empty.", "上传的文件为空"),
	/**
	 * File uploading failed.
	 */
	FILE_UPLOAD_FAILED("File uploading failed.", "上传文件发生错误"),
	/**
	 * The file size exceeds the limit.
	 */
	FILE_OUT_SIZE("The file size exceeds the limit.", "文件大小超过限制"),
	
	FILE_PATH_ISNULL("error.fastdfs.file_path_isnull", "文件路径为空"),

    FILE_ISNULL("error.fastdfs.file_isnull", "文件为空"),

    FILE_NOT_EXIST("error.fastdfs.file_not_exist", "文件不存在"),

    FILE_DOWNLOAD_FAILED("error.fastdfs.file_download_failed", "文件下载失败"),

    FILE_DELETE_FAILED("error.fastdfs.file_delete_failed", "删除文件失败"),

    FILE_SERVER_CONNECTION_FAILED("error.fastdfs.file_server_connection_failed", "文件服务器连接失败"),

    

    FILE_TYPE_ERROR_IMAGE("error.file.type.image", "图片类型错误"),

    FILE_TYPE_ERROR_DOC("error.file.type.doc", "文档类型错误"),

    FILE_TYPE_ERROR_VIDEO("error.file.type.video", "音频类型错误"),

    FILE_TYPE_ERROR_COMPRESS("error.file.type.compress", "压缩文件类型错误");
	
	public String code;

    public String message;
    
    private ErrorCode(String code, String message){
        this.code = code;
        this.message = message;
    }

}
