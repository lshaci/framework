package top.lshaci.framework.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import lombok.extern.slf4j.Slf4j;
import top.lshaci.framework.utils.exception.UtilException;

/**
 * Simple stream utils
 * 
 * @author lshaci
 * @since 0.0.4
 */
@Slf4j
public abstract class StreamUtils {

	/**
	 * Copy input stream
	 * 
	 * @param in the need copy input stream
	 * @return byte array output stream
	 */
	public static ByteArrayOutputStream copyInputStream(InputStream in) {
		try (
			ByteArrayOutputStream out = new ByteArrayOutputStream()
		) {
			write(in, out);
			return out;
        } catch (IOException e) {  
            log.error("Copy input stream error!", e);  
            throw new UtilException("Copy input stream error!");
        } 
	}

	/**
	 * 从输入流写文件到输出流
	 *
	 * @param is 输入流
	 * @param os 输出流
	 * @throws IOException
	 */
	public static void write(InputStream is, OutputStream os) throws IOException {
		byte[] b = new byte[2048];
		int length;
		while ((length = is.read(b)) > 0) {
			os.write(b, 0, length);
		}
		os.flush();
	}
}
