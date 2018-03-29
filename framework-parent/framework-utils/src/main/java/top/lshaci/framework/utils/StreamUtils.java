package top.lshaci.framework.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

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
            byte[] buffer = new byte[2048];  
            int len;  
            while ((len = in.read(buffer)) > -1) {  
            	out.write(buffer, 0, len);  
            }  
            out.flush();  
            return out;  
        } catch (IOException e) {  
            log.error("Copy input stream error!", e);  
            throw new UtilException("Copy input stream error!");
        } 
	}
}
