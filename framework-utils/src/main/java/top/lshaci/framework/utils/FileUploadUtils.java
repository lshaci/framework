package top.lshaci.framework.utils;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import top.lshaci.framework.utils.enums.FileType;
import top.lshaci.framework.utils.exception.UtilException;

import java.io.*;
import java.util.List;

/**
 * <p>文件上传工具</p><br>
 *
 * <b>1.0.7: </b>使用hutool替换commons lang3<br>
 *
 * @author lshaci
 * @since 1.0.4
 * @version 1.0.7
 */
@Slf4j
public abstract class FileUploadUtils {

	/**
	 * 通过文件后缀名验证上传文件是否允许
	 *
	 * @param filename 上传文件的文件名
	 * @param allowSuffixes 允许上传的后缀名集合(使用大写)
	 */
	public static void verifySuffix(String filename, List<String> allowSuffixes) {
		if (StrUtil.isBlank(filename)) {
			throw new UtilException("文件名不能为空");
		}
		if (CollectionUtil.isEmpty(allowSuffixes)) {
			throw new UtilException("允许的文件类型不能为空");
		}
		String suffix = filename.substring(filename.lastIndexOf(".") + 1);
		if (StrUtil.isBlank(suffix)) {
			throw new UtilException("上传文件类型错误");
		}
		if (!(allowSuffixes.contains(suffix.trim().toUpperCase()) || allowSuffixes.contains(suffix.trim().toLowerCase()))) {
			throw new UtilException("该文件类型不被允许");
		}
	}

	/**
	 * 通过文件头信息验证上传文件是否允许
	 *
	 * @param inputStream 文件输入流
	 * @param allowFileTypes 允许上传的文件类型集合
	 */
	public static void verifySuffix(InputStream inputStream, List<FileType> allowFileTypes) {
		if (inputStream == null) {
			throw new UtilException("文件不能为空");
		}
		if (CollectionUtil.isEmpty(allowFileTypes)) {
			throw new UtilException("允许的文件类型不能为空");
		}
		FileType fileType = null;
		try (
				ByteArrayOutputStream os = StreamUtils.copyInputStream(inputStream);
				ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray())
		) {
			fileType = FileTypeUtil.getType(is);
		} catch (Exception e) {
			log.error("获取文件流错误", e);
			throw new UtilException("上传文件发送错误");
		}
		if (fileType == null || !allowFileTypes.contains(fileType)) {
			throw new UtilException("该文件类型不被允许");
		}
	}

	/**
	 * 保存文件到服务器
	 *
	 * @param dir 保存文件的路径
	 * @param saveFileName 保存文件名
	 * @param inputStream 文件输入流
	 */
	public static void saveFile(String dir, String saveFileName, InputStream inputStream) {
		if (inputStream == null) {
			throw new UtilException("文件不能为空");
		}
		if (StrUtil.isBlank(dir)) {
			throw new UtilException("保存文件的路径不能为空");
		}
		if (StrUtil.isBlank(saveFileName)) {
            throw new UtilException("保存文件名不能为空");
        }

		File saveFile = new File(dir, saveFileName);
		if (!saveFile.getParentFile().exists()) {
			saveFile.getParentFile().mkdirs();
		}

		try (
				FileOutputStream os = new FileOutputStream(saveFile);
		) {
			StreamUtils.write(inputStream, os);
		} catch (IllegalStateException | IOException e) {
			log.error("Failed to save file!", e);
			throw new UtilException("保存文件失败", e);
		}
	}

}
