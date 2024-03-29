package top.lshaci.framework.fastdfs.util;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.csource.common.MyException;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.FileInfo;
import org.csource.fastdfs.ProtoCommon;
import org.csource.fastdfs.StorageClient1;
import org.csource.fastdfs.TrackerServer;
import org.springframework.web.multipart.MultipartFile;
import top.lshaci.framework.fastdfs.config.TrackerServerPool;
import top.lshaci.framework.fastdfs.constant.FastDFSConstant;
import top.lshaci.framework.fastdfs.enums.ErrorCode;
import top.lshaci.framework.fastdfs.enums.FileSuffixContentType;
import top.lshaci.framework.fastdfs.exception.FastDFSException;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

/**
 * FastDfs客户端工具
 *
 * @author lshaci
 * @since 0.0.4
 */
@Slf4j
public class FastDFSClient {

	/**
	 * FastDfs服务器连接池
	 */
    public static TrackerServerPool pool;

	private FastDFSClient() {
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
		if(StrUtil.isBlank(filepath)){
            throw new FastDFSException(ErrorCode.FILE_PATH_IS_NULL);
        }
        File file = new File(filepath.trim());
        try (
        		InputStream is = new FileInputStream(file);
        ) {
            // 获取文件名
            filepath = toLocal(filepath);
            String filename = filepath.substring(filepath.lastIndexOf("/") + 1);

            return upload(is, filename, descriptions);
        } catch (IOException e) {
            log.error(ErrorCode.FILE_NOT_EXIST.getCode(), e);
            throw new FastDFSException(ErrorCode.FILE_NOT_EXIST);
        }

    }

	/**
	 * Upload with base64 string
	 *
	 * @param base64 the base64 string
	 * @return The file path after uploading successfully<br>
	 * 		<i><b>example：</b><code>group1/M00/00/00/wKgDwFv-MoSAAvKrAAUC5Uh8n8c53.jpeg</code></i>
	 */
	public static String uploadWithBase64(String base64) {
        return uploadWithBase64(base64, null);
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
        if(StrUtil.isBlank(base64)){
            throw new FastDFSException(ErrorCode.FILE_IS_EMPTY);
        }
        return upload(new ByteArrayInputStream(Base64.decodeBase64(base64)), filename, descriptions);
    }

	/**
	 * Upload
	 *
	 * @param is           the file input stream
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
		// the file descriptions
		NameValuePair[] nvps = createFileDescriptions(filename, descriptions);
		// the file name suffix
		String suffix = getFilenameSuffix(filename);

		TrackerServer trackerServer = null;
		try (
				InputStream _is = is
		) {
			trackerServer = pool.borrowObject();
			StorageClient1 storageClient = new StorageClient1(trackerServer, null);
			// read available bytes
			byte[] fileBuff = new byte[_is.available()];
			_is.read(fileBuff, 0, fileBuff.length);

			// upload
			String path = storageClient.upload_file1(fileBuff, suffix, nvps);

			if (StrUtil.isBlank(path)) {
				throw new FastDFSException(ErrorCode.FILE_UPLOAD_FAILED);
			}

			log.debug("upload file success, return path is {}", path);
			return path;
		} catch (IOException | MyException e) {
			log.error(ErrorCode.FILE_UPLOAD_FAILED.getCode());
			throw new FastDFSException(ErrorCode.FILE_UPLOAD_FAILED);
		} finally {
			pool.returnObject(trackerServer);
		}
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
			if (pool.getMaxFileSize() != -1 && is.available() > pool.getMaxFileSize()) {
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
		List<NameValuePair> nvpsList = new ArrayList<>();

		// the file name
		if (StrUtil.isNotBlank(filename)) {
			nvpsList.add(new NameValuePair(FastDFSConstant.FILE_DESCRIPTION_FILE_NAME, filename));
		}
		// the file descriptions
		if (MapUtil.isNotEmpty(descriptions)) {
			descriptions.forEach((key, value) -> nvpsList.add(new NameValuePair(key, value)));
		}

		return nvpsList.toArray(new NameValuePair[0]);
	}

	/**
	 * Download file
	 *
	 * @param filepath the fastdfs file path
	 * @param response the http servlet response
	 */
	public static void download(String filepath, HttpServletResponse response) {
		download(filepath, null, response);
	}

	/**
	 * Download file
	 *
	 * @param filepath the fastdfs file path
	 * @param filename the download file name
	 * @param response the http servlet response
	 */
	public static void download(String filepath, String filename, HttpServletResponse response) {
		if (response == null) {
			throw new FastDFSException(ErrorCode.RESPONSE_IS_NULL);
		}

        filepath = toLocal(filepath.trim());
        // Get the file name
        if (StrUtil.isBlank(filename)) {
            filename = getOriginalFilename(filepath);
        }
        log.debug("Download file, the file path is: {}, filename: {}", filepath, filename);

		setResponseHeader(filename, response);
        try {
			write(filepath, response.getOutputStream());
		} catch (IOException e) {
			log.error(ErrorCode.FETCH_RESPONSE_STREAM_FAILED.getCode(), e);
			throw new FastDFSException(ErrorCode.FETCH_RESPONSE_STREAM_FAILED);
		}
    }

	/**
	 * Set response header
	 *
	 * @param filename the download file name
	 * @param response the http servlet response
	 */
	private static void setResponseHeader(String filename, HttpServletResponse response) {
		if (StrUtil.isBlank(filename)) {
			log.warn("Set response header failed, because the file name is blank.");
			return;
		}

		try {
			String encoderName = URLEncoder.encode(filename, "UTF-8");
			String contentType = FileSuffixContentType.getContentType(getFilenameSuffix(filename));
			if (StrUtil.isNotBlank(contentType)) {
				response.setContentType(contentType + ";charset=UTF-8");
			}
		    response.setHeader("Accept-Ranges", "bytes");
		    response.setHeader("Content-Disposition", "attachment;filename=" + encoderName);
		} catch (UnsupportedEncodingException e) {
			log.warn("Ignore.", e);
		}
	}

	/**
	 * Write file
	 *
	 * @param filepath the fastdfs file path
	 * @param os the output stream
	 */
	public static void write(String filepath, OutputStream os) {
		verifyParameters(filepath, os);

		filepath = toLocal(filepath.trim());
		log.debug("Write file, the file path is: {}", filepath);

		TrackerServer trackerServer = pool.borrowObject();
		StorageClient1 storageClient = new StorageClient1(trackerServer, null);

		byte[] fileByte;
		try {
			// download file from fastdfs
			fileByte = storageClient.download_file1(filepath);
		} catch (IOException | MyException e) {
			log.error(ErrorCode.FILE_DOWNLOAD_FAILED.getCode(), e);
			throw new FastDFSException(ErrorCode.FILE_DOWNLOAD_FAILED);
		} finally {
			pool.returnObject(trackerServer);
		}

		// file not exist, throw exception
		if(fileByte == null){
			log.error(ErrorCode.FILE_NOT_EXIST.getCode() + ": {}", filepath);
			throw new FastDFSException(ErrorCode.FILE_NOT_EXIST);
		}

		try (
				OutputStream _os = os;
				InputStream is = new ByteArrayInputStream(fileByte);
		) {

			byte[] buffer = new byte[1024 * 5];
			int len;
			while ((len = is.read(buffer)) > 0) {
				_os.write(buffer, 0, len);
			}
		} catch (IOException e) {
			log.error(ErrorCode.FILE_WRITE_FAILED.getCode(), e);
			throw new FastDFSException(ErrorCode.FILE_WRITE_FAILED);
		}
	}

	/**
	 * Verify download parameters
	 *
	 * @param filepath the fastdfs file path
	 * @param os the output stream
	 */
	private static void verifyParameters(String filepath, OutputStream os) {
		verifyFilepath(filepath);
		if (os == null) {
			throw new FastDFSException(ErrorCode.OUTPUT_STREAM_IS_NULL);
		}
	}

	/**
	 * Verify fastdfs file path
	 *
	 * @param filepath the fastdfs file path
	 */
	private static void verifyFilepath(String filepath) {
		if(StrUtil.isBlank(filepath)){
			throw new FastDFSException(ErrorCode.FILE_PATH_IS_NULL);
		}
	}

	/**
	 * Delete file from fastdfs by file path
	 *
	 * @param filepath the fastdfs file path
	 * @return 0 for success, none zero for fail
	 */
	public static int deleteFile(String filepath) {
		verifyFilepath(filepath);

		TrackerServer trackerServer = pool.borrowObject();
		StorageClient1 storageClient = new StorageClient1(trackerServer, null);
		try {
			return storageClient.delete_file1(filepath);
		} catch (IOException | MyException e) {
			log.error(ErrorCode.FILE_DELETE_FAILED.getCode(), e);
			throw new FastDFSException(ErrorCode.FILE_DELETE_FAILED);
		} finally {
			pool.returnObject(trackerServer);
		}
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
	public static FileInfo getFileInfo(String filepath) {
		TrackerServer trackerServer = pool.borrowObject();
		StorageClient1 storageClient = new StorageClient1(trackerServer, null);
		try {
			return storageClient.get_file_info1(filepath);
		} catch (IOException | MyException e) {
			log.error(ErrorCode.FETCH_FILE_INFO_FAILED.getCode(), e);
			throw new FastDFSException(ErrorCode.FETCH_FILE_INFO_FAILED);
		} finally {
			pool.returnObject(trackerServer);
		}
	}

    /**
     * Get token to access the server and concatenate it after the address
     *
     * @param filepath the fastdfs file path
     * @param httpSecretKey the http secret key
     * @return token, example： token=078d370098b03e9020b82c829c205e1f&amp;ts=1508141521
     */
    public static String getToken(String filepath, String httpSecretKey){
        // unix seconds
        int ts = (int) Instant.now().getEpochSecond();
        // token
        String token;
        try {
            token = ProtoCommon.getToken(getFilename(filepath), ts, httpSecretKey);
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException | MyException e) {
			log.error(ErrorCode.FETCH_TOKEN_FAILED.getCode(), e);
			throw new FastDFSException(ErrorCode.FETCH_TOKEN_FAILED);
		}

        StringBuilder sb = new StringBuilder();
        sb.append("token=")
          .append(token)
          .append("&ts=")
          .append(ts);

        return sb.toString();
    }

	/**
	 * Get the file descriptions from fastdfs
	 *
	 * @param filepath the fastdfs file path
	 * @return the file descriptions
	 */
	public static Map<String, String> getFileDescriptions(String filepath) {
		TrackerServer trackerServer = pool.borrowObject();
		StorageClient1 storageClient = new StorageClient1(trackerServer, null);
		NameValuePair[] nvps = null;
		try {
			nvps = storageClient.get_metadata1(filepath);
		} catch (IOException | MyException e) {
			log.warn("Error getting file description: {}", filepath);
		} finally {
			pool.returnObject(trackerServer);
		}

		Map<String, String> infoMap = null;

		if (ArrayUtil.isNotEmpty(nvps)) {
			infoMap = Arrays.stream(nvps)
					.collect(toMap(NameValuePair::getName, NameValuePair::getValue, (k1, k2) -> k1));
		}

		return infoMap;
	}

	/**
	 * Get the original file name
	 *
	 * @param filepath the fastdfs file path
	 * @return the original file name
	 */
	private static String getOriginalFilename(String filepath) {
		Map<String, String> descriptions = getFileDescriptions(filepath);
		if (MapUtil.isNotEmpty(descriptions)) {
			return descriptions.get(FastDFSConstant.FILE_DESCRIPTION_FILE_NAME);
		}
		return null;
	}

	/**
	 * Get the file name suffix
	 *
	 * @param filename the file name
	 * @return The file name suffix
	 */
	private static String getFilenameSuffix(String filename) {
		String suffix = null;
		if (StrUtil.isNotBlank(filename)) {
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
	private static String toLocal(String path) {
        if (StrUtil.isNotBlank(path)) {
            path = path.replaceAll("\\\\", FastDFSConstant.SEPARATOR);

            if (path.contains(FastDFSConstant.DOT)) {
                String pre = path.substring(0, path.lastIndexOf(FastDFSConstant.DOT) + 1);
                String suffix = path.substring(path.lastIndexOf(FastDFSConstant.DOT) + 1).toLowerCase();
                path = pre + suffix;
            }
        }
		return path;
	}

	/**
	 * Get fastdfs file name, example: M00/00/00/wKgDwFv-UHqAbX4nAAUC5Uh8n8c03.jpeg
	 *
	 * @param fileId the file id, example: group1/M00/00/00/wKgDwFv-UHqAbX4nAAUC5Uh8n8c03.jpeg
	 * @return the fastdfs file name
	 */
	private static String getFilename(String fileId) {
		String[] results = new String[2];
		StorageClient1.split_file_id(fileId, results);

		return results[1];
	}

	/**
	 * Get the file server address
	 *
	 * @return the file server address
	 */
	public static String getReverseProxyAddress() {
		return pool.getReverseProxyAddress();
	}

	/**
	 * The client information
	 *
	 * @return The client information
	 */
	public static String info() {
		StringBuilder sb = new StringBuilder();
		sb.append("{\n  reverse_proxy_address = ")
		  .append(pool.getReverseProxyAddress())
		  .append("\n  max_file_size(byte) = ")
		  .append(pool.getMaxFileSize())
		  .append("\n}");
		return sb.toString();
	}
}
