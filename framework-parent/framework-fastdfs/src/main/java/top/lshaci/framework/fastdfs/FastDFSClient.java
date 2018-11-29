package top.lshaci.framework.fastdfs;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.csource.common.MyException;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.FileInfo;
import org.csource.fastdfs.ProtoCommon;
import org.csource.fastdfs.StorageClient1;
import org.csource.fastdfs.TrackerServer;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;
import top.lshaci.framework.fastdfs.constant.FastDFSConstant;
import top.lshaci.framework.fastdfs.exception.ErrorCode;
import top.lshaci.framework.fastdfs.exception.FastDFSException;

@Slf4j
public class FastDFSClient {
	/**
	 * ContentType
	 */
	protected static final Map<String, String> EXT_MAPS = new HashMap<>();
	/**
	 * The max file size
	 */
	protected static int maxFileSize;
	/**
	 * The file server address
	 */
	protected static String fileServerAddr;

	public FastDFSClient() {
		initExt();
	}

	private void initExt() {
		// image
		EXT_MAPS.put("png", "image/png");
		EXT_MAPS.put("gif", "image/gif");
		EXT_MAPS.put("bmp", "image/bmp");
		EXT_MAPS.put("ico", "image/x-ico");
		EXT_MAPS.put("jpeg", "image/jpeg");
		EXT_MAPS.put("jpg", "image/jpeg");
		// 压缩文件
		EXT_MAPS.put("zip", "application/zip");
		EXT_MAPS.put("rar", "application/x-rar");
		// doc
		EXT_MAPS.put("pdf", "application/pdf");
		EXT_MAPS.put("ppt", "application/vnd.ms-powerpoint");
		EXT_MAPS.put("xls", "application/vnd.ms-excel");
		EXT_MAPS.put("xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		EXT_MAPS.put("pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation");
		EXT_MAPS.put("doc", "application/msword");
		EXT_MAPS.put("doc", "application/wps-office.doc");
		EXT_MAPS.put("docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
		EXT_MAPS.put("txt", "text/plain");
		// 音频
		EXT_MAPS.put("mp4", "video/mp4");
		EXT_MAPS.put("flv", "video/x-flv");
	}

	/**
	 * Upload with MultipartFile
	 * 
	 * @param file the multipart file
	 * @return The file path after uploading successfully<br>
	 * 		<i><b>example：</b><code>group1/M00/00/00/wKgDwFv-MoSAAvKrAAUC5Uh8n8c53.jpeg</code></i>
	 */
	public static String uploadWithMultipart(MultipartFile file) {
		return uploadWithMultipart(file, null);
	}

	/**
	 * Upload with MultipartFile
	 * 
	 * @param file         the multipart file
	 * @param descriptions the file descriptions
	 * @return The file path after uploading successfully<br>
	 * 		<i><b>example：</b><code>group1/M00/00/00/wKgDwFv-MoSAAvKrAAUC5Uh8n8c53.jpeg</code></i>
	 */
	public static String uploadWithMultipart(MultipartFile file, Map<String, String> descriptions) {
		if (file == null || file.isEmpty()) {
			throw new FastDFSException(ErrorCode.FILE_IS_EMPTY);
		}
		try {
			return upload(file.getInputStream(), file.getOriginalFilename(), descriptions);
		} catch (IOException e) {
			log.error(ErrorCode.FILE_UPLOAD_FAILED.getCode(), e);
			throw new FastDFSException(ErrorCode.FILE_UPLOAD_FAILED);
		}
	}
	
	/**
	 * Upload with file path
	 * 
	 * @param filepath the file path
	 * @return The file path after uploading successfully<br>
	 * 		<i><b>example：</b><code>group1/M00/00/00/wKgDwFv-MoSAAvKrAAUC5Uh8n8c53.jpeg</code></i>
	 */
	public static String uploadWithFilepath(String filepath) {
        return uploadWithFilepath(filepath, null);
    }
	
	/**
	 * Upload with file path
	 * 
	 * @param filepath the file path
	 * @param descriptions the file descriptions
	 * @return The file path after uploading successfully<br>
	 * 		<i><b>example：</b><code>group1/M00/00/00/wKgDwFv-MoSAAvKrAAUC5Uh8n8c53.jpeg</code></i>
	 */
	public static String uploadWithFilepath(String filepath, Map<String, String> descriptions) {
		if(StringUtils.isBlank(filepath)){
            throw new FastDFSException(ErrorCode.FILE_PATH_IS_NULL);
        }
        File file = new File(filepath.trim());
        String path = null;
        try (
        		InputStream is = new FileInputStream(file);
        ) {
            // 获取文件名
            filepath = toLocal(filepath);
            String filename = filepath.substring(filepath.lastIndexOf("/") + 1);

            path = upload(is, filename, descriptions);
        } catch (IOException e) {
            log.error(ErrorCode.FILE_NOT_EXIST.getCode(), e);
            throw new FastDFSException(ErrorCode.FILE_NOT_EXIST);
        } 

        return path;
    }
	
	/**
	 * Upload with base64 string
	 * 
	 * @param base64 the base64 string
	 * @return The file path after uploading successfully<br>
	 * 		<i><b>example：</b><code>group1/M00/00/00/wKgDwFv-MoSAAvKrAAUC5Uh8n8c53.jpeg</code></i>
	 */
	public static String uploadWithBase64(String base64) {
        return uploadWithBase64(base64, null, null);
    }
	
	/**
	 * Upload with base64 string
	 * 
	 * @param base64 the base64 string
	 * @param filename the file name
	 * @return The file path after uploading successfully<br>
	 * 		<i><b>example：</b><code>group1/M00/00/00/wKgDwFv-MoSAAvKrAAUC5Uh8n8c53.jpeg</code></i>
	 */
	public static String uploadWithBase64(String base64, String filename) {
        return uploadWithBase64(base64, filename, null);
    }
	
	/**
	 * Upload with base64 string
	 * 
	 * @param base64 the base64 string
	 * @param filename the file name
	 * @param descriptions the file descriptions
	 * @return The file path after uploading successfully<br>
	 * 		<i><b>example：</b><code>group1/M00/00/00/wKgDwFv-MoSAAvKrAAUC5Uh8n8c53.jpeg</code></i>
	 */
	public static String uploadWithBase64(String base64, String filename, Map<String, String> descriptions) {
        if(StringUtils.isBlank(base64)){
            throw new FastDFSException(ErrorCode.FILE_IS_EMPTY);
        }
        return upload(new ByteArrayInputStream(Base64.decodeBase64(base64)), filename, descriptions);
    }
	
	/**
	 * Upload
	 * 
	 * @param is           the file input stream
	 * @param filename     the file name
	 * @param descriptions the file descriptions
	 * @return The file path after uploading successfully<br>
	 * 		<i><b>example：</b><code>group1/M00/00/00/wKgDwFv-MoSAAvKrAAUC5Uh8n8c53.jpeg</code></i>
	 */
	public static String upload(InputStream is) {
		return upload(is, null);
	}
	
	/**
	 * Upload
	 * 
	 * @param is           the file input stream
	 * @param filename     the file name
	 * @return The file path after uploading successfully<br>
	 * 		<i><b>example：</b><code>group1/M00/00/00/wKgDwFv-MoSAAvKrAAUC5Uh8n8c53.jpeg</code></i>
	 */
	public static String upload(InputStream is, String filename) {
		return upload(is, filename, null);
	}
	
	/**
	 * Upload
	 * 
	 * @param is           the file input stream
	 * @param filename     the file name
	 * @param descriptions the file descriptions
	 * @return The file path after uploading successfully<br>
	 * 		<i><b>example：</b><code>group1/M00/00/00/wKgDwFv-MoSAAvKrAAUC5Uh8n8c53.jpeg</code></i>
	 */
	public static String upload(InputStream is, String filename, Map<String, String> descriptions) {
		verifyInputStream(is);
		// the converted file name
		filename = toLocal(filename);
		// the return path
		String path = null;
		// the file descriptions
		NameValuePair[] nvps = createFileDescriptions(filename, descriptions);
		// the file name suffix
		String suffix = getFilenameSuffix(filename);

		TrackerServer trackerServer = null;
		try {
			trackerServer = TrackerServerPool.borrowObject();
			StorageClient1 storageClient = new StorageClient1(trackerServer, null);
			// read available bytes
			byte[] fileBuff = new byte[is.available()];
			is.read(fileBuff, 0, fileBuff.length);

			// upload
			path = storageClient.upload_file1(fileBuff, suffix, nvps);

			if (StringUtils.isBlank(path)) {
				throw new FastDFSException(ErrorCode.FILE_UPLOAD_FAILED);
			}

			log.debug("upload file success, return path is {}", path);
		} catch (IOException | MyException e) {
			log.error(ErrorCode.FILE_UPLOAD_FAILED.getCode());
			throw new FastDFSException(ErrorCode.FILE_UPLOAD_FAILED);
		} finally {
			// close input stream
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					log.warn("Close input streat exception.", e);
				}
			}
		}
		TrackerServerPool.returnObject(trackerServer);

		return path;
	}

	/**
	 * Verify that the input stream is valid
	 * @param is the input stream
	 */
	private static void verifyInputStream(InputStream is) {
		if (is == null) {
			throw new FastDFSException(ErrorCode.FILE_IS_EMPTY);
		}

		try {
			if (is.available() > maxFileSize) {
				throw new FastDFSException(ErrorCode.FILE_OUT_SIZE);
			}
		} catch (IOException e) {
			log.warn("Invoke available method error.", e);
		}
	}
	
	/**
	 * Create file descriptions
	 * 
	 * @param filename the file name
	 * @param descriptions the file descriptions
	 * @return the file descriptions with {@link NameValuePair} array
	 */
	private static NameValuePair[] createFileDescriptions(String filename, Map<String, String> descriptions) {
		NameValuePair[] nvps = null;
		List<NameValuePair> nvpsList = new ArrayList<>();

		// the file name
		if (StringUtils.isNotBlank(filename)) {
			nvpsList.add(new NameValuePair(FastDFSConstant.FILE_DESCRIPTION_FILE_NAME, filename));
		}
		// the file descriptions
		if (descriptions != null && descriptions.size() > 0) {
			descriptions.forEach((key, value) -> {
				nvpsList.add(new NameValuePair(key, value));
			});
		}
		if (nvpsList.size() > 0) {
			nvps = new NameValuePair[nvpsList.size()];
			nvpsList.toArray(nvps);
		}
		
		return nvps;
	}

	/**
	 * 删除文件
	 *
	 * @param filepath 文件路径
	 * @return 删除成功返回 0, 失败返回其它
	 */
	public static int deleteFile(String filepath) throws FastDFSException {
		if (StringUtils.isBlank(filepath)) {
			throw new FastDFSException("");
		}

		TrackerServer trackerServer = TrackerServerPool.borrowObject();
		StorageClient1 storageClient = new StorageClient1(trackerServer, null);
		int success = 0;
		try {
			success = storageClient.delete_file1(filepath);
			if (success != 0) {
				throw new FastDFSException("");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (MyException e) {
			e.printStackTrace();
			throw new FastDFSException("");
		}
		// 返还对象
		TrackerServerPool.returnObject(trackerServer);

		return success;
	}

	/**
	 * 获取文件信息
	 * 
	 * @param filepath 文件路径
	 * @return 文件信息
	 * 
	 *         <pre>
	 *  {<br>
	 *      "SourceIpAddr": 源IP <br>
	 *      "FileSize": 文件大小 <br>
	 *      "CreateTime": 创建时间 <br>
	 *      "CRC32": 签名 <br>
	 *  }  <br>
	 *         </pre>
	 */
	public Map<String, Object> getFileInfo(String filepath) throws FastDFSException {
		TrackerServer trackerServer = TrackerServerPool.borrowObject();
		StorageClient1 storageClient = new StorageClient1(trackerServer, null);
		FileInfo fileInfo = null;
		try {
			fileInfo = storageClient.get_file_info1(filepath);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (MyException e) {
			e.printStackTrace();
		}
		// 返还对象
		TrackerServerPool.returnObject(trackerServer);

		Map<String, Object> infoMap = new HashMap<>(4);

		infoMap.put("SourceIpAddr", fileInfo.getSourceIpAddr());
		infoMap.put("FileSize", fileInfo.getFileSize());
		infoMap.put("CreateTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(fileInfo.getCreateTimestamp()));
		infoMap.put("CRC32", fileInfo.getCrc32());

		return infoMap;
	}

	/**
	 * 获取文件描述信息
	 * 
	 * @param filepath 文件路径
	 * @return 文件描述信息
	 */
	public Map<String, Object> getFileDescriptions(String filepath) throws FastDFSException {
		TrackerServer trackerServer = TrackerServerPool.borrowObject();
		StorageClient1 storageClient = new StorageClient1(trackerServer, null);
		NameValuePair[] nvps = null;
		try {
			nvps = storageClient.get_metadata1(filepath);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (MyException e) {
			e.printStackTrace();
		}
		// 返还对象
		TrackerServerPool.returnObject(trackerServer);

		Map<String, Object> infoMap = null;

		if (nvps != null && nvps.length > 0) {
			infoMap = new HashMap<>(nvps.length);

			for (NameValuePair nvp : nvps) {
				infoMap.put(nvp.getName(), nvp.getValue());
			}
		}

		return infoMap;
	}

	/**
	 * 获取源文件的文件名称
	 * 
	 * @param filepath 文件路径
	 * @return 文件名称
	 */
	public String getOriginalFilename(String filepath) throws FastDFSException {
		Map<String, Object> descriptions = getFileDescriptions(filepath);
		if (descriptions.get(FastDFSConstant.FILE_DESCRIPTION_FILE_NAME) != null) {
			return (String) descriptions.get(FastDFSConstant.FILE_DESCRIPTION_FILE_NAME);
		}
		return null;
	}

	/**
	 * Get the file name suffix
	 * 
	 * @param filename the file name
	 * @return The file name suffix
	 */
	public static String getFilenameSuffix(String filename) {
		String suffix = null;
		if (StringUtils.isNotBlank(filename)) {
			if (filename.contains(FastDFSConstant.DOT)) {
				suffix = filename.substring(filename.lastIndexOf(FastDFSConstant.DOT) + 1);
			} else {
				log.error("The filename error without suffix : {}", filename);
			}
		}
		return suffix;
	}

	/**
	 * Convert '\\\\' to '/' in the path, and convert the suffix name to lowercase
	 * 
	 * @param path the file path
	 * @return The converted path
	 */
	public static String toLocal(String path) {
        if (StringUtils.isNotBlank(path)) {
            path = path.replaceAll("\\\\", FastDFSConstant.SEPARATOR);

            if (path.contains(FastDFSConstant.DOT)) {
                String pre = path.substring(0, path.lastIndexOf(FastDFSConstant.DOT) + 1);
                String suffix = path.substring(path.lastIndexOf(FastDFSConstant.DOT) + 1).toLowerCase();
                path = pre + suffix;
            }
        }
		return path;
	}
	
	public static void main(String[] args) {
		toLocal("D:/abc.png");
	}

	/**
	 * 获取FastDFS文件的名称，如：M00/00/00/wKgzgFnkTPyAIAUGAAEoRmXZPp876.jpeg
	 *
	 * @param fileId 包含组名和文件名，如：group1/M00/00/00/wKgzgFnkTPyAIAUGAAEoRmXZPp876.jpeg
	 * @return FastDFS 返回的文件名：M00/00/00/wKgzgFnkTPyAIAUGAAEoRmXZPp876.jpeg
	 */
	public static String getFilename(String fileId) {
		String[] results = new String[2];
		StorageClient1.split_file_id(fileId, results);

		return results[1];
	}

	/**
	 * 获取访问服务器的token，拼接到地址后面
	 *
	 * @param filepath      文件路径 group1/M00/00/00/wKgzgFnkTPyAIAUGAAEoRmXZPp876.jpeg
	 * @param httpSecretKey 秘钥
	 * @return 返回token，如： token=078d370098b03e9020b82c829c205e1f&ts=1508141521
	 */
	public static String getToken(String filepath, String httpSecretKey) {
		// unix seconds
		int ts = (int) Instant.now().getEpochSecond();
		// token
		String token = "null";
		try {
			token = ProtoCommon.getToken(getFilename(filepath), ts, httpSecretKey);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (MyException e) {
			e.printStackTrace();
		}

		StringBuilder sb = new StringBuilder();
		sb.append("token=").append(token);
		sb.append("&ts=").append(ts);

		return sb.toString();
	}
	
	/**
	 * Get the file server address
	 * 
	 * @return the file server address
	 */
	public static String getFileServerAddr() {
		return fileServerAddr;
	}

	/**
	 * The client information
	 * 
	 * @return The client information
	 */
	public static String info() {
		StringBuilder sb = new StringBuilder();
		sb.append("The file server address is: ")
		  .append(fileServerAddr)
		  .append("; ")
		  .append("The max file size is: ")
		  .append(maxFileSize)
		  .append(".");
		return sb.toString();
	}
}
