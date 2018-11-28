package top.lshaci.framework.web.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;
import top.lshaci.framework.web.exception.WebBaseException;

/**
 * Upload file utils
 * 
 * @author lshaci
 * @since 0.0.4
 */
@Slf4j
public class UploadUtils {

	/**
	 * Save file
	 * 
	 * @param filePath the save file path
	 * @param saveFileName the save file name
	 * @param file the upload file
	 */
	public static void save(String filePath, String saveFileName, MultipartFile file) {
	    Objects.requireNonNull(file, "The file must not be null!");
        if (StringUtils.isBlank(filePath) || StringUtils.isBlank(saveFileName)) {
            throw new WebBaseException("The file path or save file name is empty!");
        }
        
        File saveFile = new File(filePath, saveFileName);
        if (!saveFile.getParentFile().exists()) {
            saveFile.getParentFile().mkdirs();
        }

        try (
                InputStream is = file.getInputStream(); 
                FileOutputStream os = new FileOutputStream(saveFile);
        ) {
            byte[] b = new byte[2048];
            int length;
            while ((length = is.read(b)) > 0) {
                os.write(b, 0, length);
            }
        } catch (IllegalStateException | IOException e) {
            log.error("Failed to save file!", e);
            throw new WebBaseException("Failed to save file!", e);
        }
	}
}
