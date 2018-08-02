package top.lshaci.framework.web.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import lombok.extern.slf4j.Slf4j;
import top.lshaci.framework.common.exception.BaseException;
import top.lshaci.framework.web.enums.ResponseContentType;
import top.lshaci.framework.web.exception.WebBaseException;

/**
 * Download utils
 * 
 * @author lshaci
 * @since 0.0.4
 * @version 0.0.4
 */
@Slf4j
public class DownloadUtils {
	
	/**
	 * The data cache size
	 */
	public static int cacheSize;

	/**
     * Download file
     * 
     * @param downloadName the download file name<b>(Must include suffixes)</b>
     * @param contentType the http servlet response content type
     * @param outputStream the byte array output stream of the file
     * @param response the http servlet response
     */
    public static void download(String downloadName, ResponseContentType contentType, 
            ByteArrayOutputStream outputStream, HttpServletResponse response) {
        try (
                InputStream fileInputStream = new ByteArrayInputStream(outputStream.toByteArray());
                ServletOutputStream os = response.getOutputStream();
                ) {
            writeFile(downloadName, contentType, response, fileInputStream, os);
        } catch (Exception e) {
            log.error("导出[" + downloadName + "]失败", e);
            throw new WebBaseException("导出[" + downloadName + "]失败", e);
        }
    }

    /**
     * Download file
     * 
     * @param downloadName the download file name<b>(Must include suffixes)</b>
     * @param contentType the http servlet response content type
     * @param fileInputStream the file input stream
     * @param response the http servlet response
     */
    public static void download(String downloadName, ResponseContentType contentType, 
            FileInputStream fileInputStream, HttpServletResponse response) {
        try (
                ServletOutputStream os = response.getOutputStream();
        ) {
            writeFile(downloadName, contentType, response, fileInputStream, os);
        } catch (Exception e) {
            log.error("导出[" + downloadName + "]失败", e);
            throw new WebBaseException("导出[" + downloadName + "]失败", e);
        } finally {
            try {
                fileInputStream.close();
            } catch (IOException e) {
                log.warn("Ignore the resource close exception", e);
            }
        }
    }
    
    /**
     * Download file
     * 
     * @param downloadName the download file name<b>(Must include suffixes)</b>
     * @param contentType the http servlet response content type
     * @param filePath the path of the file
     * @param response the http servlet response
     */
    public static void download(String downloadName, ResponseContentType contentType, 
            String filePath, HttpServletResponse response) {
        try (
                InputStream fileInputStream = new FileInputStream(filePath);
                ServletOutputStream os = response.getOutputStream();
        ) {
            writeFile(downloadName, contentType, response, fileInputStream, os);
        } catch (Exception e) {
            log.error("导出[" + downloadName + "]失败", e);
            throw new WebBaseException("导出[" + downloadName + "]失败", e);
        }
    }
    
    /**
     * Write out file
     * 
     * @param downloadName the download file name
     * @param contentType the http servlet response content type
     * @param response the http servlet response
     * @param fileInputStream the input stream of the file
     * @param os the servlet output stream
     * @throws IOException
     */
    private static void writeFile(String downloadName, ResponseContentType contentType, 
            HttpServletResponse response, InputStream fileInputStream, ServletOutputStream os) throws IOException {
        response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(downloadName, "UTF-8"));
        if (StringUtils.isBlank(downloadName)) {
            throw new BaseException("The download file name must not be empty!");
        }
        if (contentType == null) {
            throw new WebBaseException("The http servlet response content type must not be null!");
        }
        response.setContentType(contentType.getName());

        byte[] b = new byte[cacheSize];
        int length;
        while ((length = fileInputStream.read(b)) > 0) {
            os.write(b, 0, length);
        }
    }
}
