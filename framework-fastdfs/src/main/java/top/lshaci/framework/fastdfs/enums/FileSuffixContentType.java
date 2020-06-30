package top.lshaci.framework.fastdfs.enums;

import cn.hutool.core.util.StrUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * The correspondence between filename suffix and content type
 *
 * @author lshaci
 * @since 0.0.4
 */
@Getter
@AllArgsConstructor
public enum FileSuffixContentType {

	/**
	 * PNG;image/png
	 */
	PNG("image/png"),
	/**
	 * GIF;image/gif
	 */
	GIF("image/gif"),
	/**
	 * BMP;image/bmp
	 */
	BMP("image/bmp"),
	/**
	 * ICO;image/x-ico
	 */
	ICO("image/x-ico"),
	/**
	 * JPG;image/jpeg
	 */
	JPG("image/jpeg"),
	/**
	 * JPEG;image/jpeg
	 */
	JPEG("image/jpeg"),
	/**
	 * ZIP;application/zip
	 */
	ZIP("application/zip"),
	/**
	 * RAR;application/x-rar
	 */
	RAR("application/x-rar"),
	/**
	 * PDF;application/pdf
	 */
	PDF("application/pdf"),
	/**
	 * PPT;application/vnd.ms-powerpoint
	 */
	PPT("application/vnd.ms-powerpoint"),
	/**
	 * PPTX;application/vnd.openxmlformats-officedocument.presentationml.presentation
	 */
	PPTX("application/vnd.openxmlformats-officedocument.presentationml.presentation"),
	/**
	 * XLS;application/vnd.ms-excel
	 */
	XLS("application/vnd.ms-excel"),
	/**
	 * XLSX;application/vnd.openxmlformats-officedocument.spreadsheetml.sheet
	 */
	XLSX("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"),
	/**
	 * DOC;application/msword
	 */
	DOC("application/msword"),
	/**
	 * DOCX;application/vnd.openxmlformats-officedocument.wordprocessingml.document
	 */
	DOCX("application/vnd.openxmlformats-officedocument.wordprocessingml.document"),
	/**
	 * TXT;text/plain
	 */
	TXT("text/plain"),
	/**
	 * MP4;video/mp4
	 */
	MP4("video/mp4"),
	/**
	 * FLV;video/x-flv
	 */
	FLV("video/x-flv"),
	;

	private String contentType;

	/**
	 * Get content type by file suffix
	 *
	 * @param suffix the file suffix
	 * @return the content type corresponding to the suffix name
	 */
	public static String getContentType(String suffix) {
		if (StrUtil.isBlank(suffix)) {
			return null;
		}

		FileSuffixContentType fileSuffixContentType = FileSuffixContentType.valueOf(suffix.trim().toUpperCase());
		if (fileSuffixContentType != null) {
			return fileSuffixContentType.contentType;
		}

		return null;
	}

}
