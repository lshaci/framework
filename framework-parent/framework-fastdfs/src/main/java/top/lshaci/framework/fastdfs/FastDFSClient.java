package top.lshaci.framework.fastdfs;

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

import org.apache.commons.lang3.StringUtils;
import org.csource.common.MyException;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.FileInfo;
import org.csource.fastdfs.ProtoCommon;
import org.csource.fastdfs.StorageClient1;
import org.csource.fastdfs.TrackerServer;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;
import top.lshaci.framework.fastdfs.exception.ErrorCode;
import top.lshaci.framework.fastdfs.exception.FastDFSException;

@Slf4j
public class FastDFSClient {
	/**
	 * 路径分隔符
	 */
	protected static final String SEPARATOR = "/";
	/**
	 * Point
	 */
	protected static final String POINT = ".";
	/**
	 * ContentType
	 */
	protected static final Map<String, String> EXT_MAPS = new HashMap<>();

	/**
	 * 文件名称Key
	 */
	protected static final String FILENAME = "filename";

	/**
	 * The max file size
	 */
	protected static int maxFileSize;

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
	 * Upload MultipartFile
	 * 
	 * @param file the multipart file
	 * @return The file path after uploading successfully
	 */
	public static String uploadMultipartFile(MultipartFile file) {
		return uploadMultipartFile(file, null);
	}

	/**
	 * Upload MultipartFile
	 * 
	 * @param file         the multipart file
	 * @param descriptions the file descriptions
	 * @return The file path after uploading successfully
	 */
	public static String uploadMultipartFile(MultipartFile file, Map<String, String> descriptions) {
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
	 * Upload
	 * 
	 * @param is           the file input stream
	 * @param filename     the file name
	 * @param descriptions the file descriptions
	 * @return The file path after uploading successfully<br>
	 * 		<i><b>example：</b><code>group1/M00/00/00/wKgDwFv-MoSAAvKrAAUC5Uh8n8c53.jpeg</code></i>
	 */
	private static String upload(InputStream is, String filename, Map<String, String> descriptions) {
		verifyInputStream(is);

		filename = toLocal(filename);
		// 返回路径
		String path = null;
		// 文件描述
		NameValuePair[] nvps = null;
		List<NameValuePair> nvpsList = new ArrayList<>();
		// 文件名后缀
		String suffix = getFilenameSuffix(filename);

		// 文件名
		if (StringUtils.isNotBlank(filename)) {
			nvpsList.add(new NameValuePair(FILENAME, filename));
		}
		// 描述信息
		if (descriptions != null && descriptions.size() > 0) {
			descriptions.forEach((key, value) -> {
				nvpsList.add(new NameValuePair(key, value));
			});
		}
		if (nvpsList.size() > 0) {
			nvps = new NameValuePair[nvpsList.size()];
			nvpsList.toArray(nvps);
		}

		TrackerServer trackerServer = null;
		try {
			trackerServer = TrackerServerPool.borrowObject();
			StorageClient1 storageClient = new StorageClient1(trackerServer, null);
			// 读取流
			byte[] fileBuff = new byte[is.available()];
			is.read(fileBuff, 0, fileBuff.length);

			// 上传
			path = storageClient.upload_file1(fileBuff, suffix, nvps);

			if (StringUtils.isBlank(path)) {
				throw new FastDFSException("上传失败");
			}

			log.debug("upload file success, return path is {}", path);
		} catch (IOException e) {
			e.printStackTrace();
			throw new FastDFSException("");
		} catch (MyException e) {
			e.printStackTrace();
			throw new FastDFSException("");
		} finally {
			// 关闭流
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		// 返还对象
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
	 * 删除文件
	 *
	 * @param filepath 文件路径
	 * @return 删除成功返回 0, 失败返回其它
	 */
	public int deleteFile(String filepath) throws FastDFSException {
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
		if (descriptions.get(FILENAME) != null) {
			return (String) descriptions.get(FILENAME);
		}
		return null;
	}

	/**
	 * 获取文件名称的后缀
	 *
	 * @param filename 文件名 或 文件路径
	 * @return 文件后缀
	 */
	public static String getFilenameSuffix(String filename) {
		String suffix = null;
		String originalFilename = filename;
		if (StringUtils.isNotBlank(filename)) {
			if (filename.contains(SEPARATOR)) {
				filename = filename.substring(filename.lastIndexOf(SEPARATOR) + 1);
			}
			if (filename.contains(POINT)) {
				suffix = filename.substring(filename.lastIndexOf(POINT) + 1);
			} else {
				log.error("filename error without suffix : {}", originalFilename);
			}
		}
		return suffix;
	}

	/**
	 * 转换路径中的 '\' 为 '/' <br>
	 * 并把文件后缀转为小写
	 *
	 * @param path 路径
	 * @return
	 */
	public static String toLocal(String path) {
//        if (StringUtils.isNotBlank(path)) {
//            path = path.replaceAll("\\", SEPARATOR);
//
//            if (path.contains(POINT)) {
//                String pre = path.substring(0, path.lastIndexOf(POINT) + 1);
//                String suffix = path.substring(path.lastIndexOf(POINT) + 1).toLowerCase();
//                path = pre + suffix;
//            }
//        }
		return path;
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
}
