package top.lshaci.framework.pdfUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.URL;

import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.itextpdf.text.pdf.BaseFont;

import lombok.extern.slf4j.Slf4j;
import top.lshaci.framework.common.exception.BaseException;

/**
 * Itext pdf utils
 * 
 * @author lshaci
 * @since 0.0.4
 */
@Slf4j
public class ItextPdfUtils {

	/**
	 * Export PDF with html string <br>
	 * Add two font files<b>(arialuni.ttf, simsun.ttc)</b>  to the classpath.<b>(Do not modify the file name.)</b> <br>
	 * The html freemarker template need set body style,<br>
	 * 				For example: <b>&lt;body style = "font-family: SimSun;"&gt;</b> <br>
	 * 				<b>The sample code: </b><br><br>
	 * 				Map&lt;String, Object&gt; data = new HashMap&lt;&gt;();<br>
	 * 				String htmlStr = FreemarkerUtils.build(Test.class, "/pdf").setTemplate("test.ftl").generate(data);<br>
	 * 				ByteArrayOutputStream pdfOs = ItextPdfUtils.export(htmlStr);<br><br>
	 * 				resp.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode("测试", "UTF-8") + ".pdf");<br>
	 * 				resp.setContentType("application/pdf");<br><br>
	 * 				ServletOutputStream outputStream = resp.getOutputStream();<br>
	 * 				outputStream.write(pdfOs.toByteArray());<br>
	 * @param htmlStr html string
	 * @param fontPath the font path(Relative to the classpath)
	 * @return the pdf ByteArrayOutputStream
	 */
	public static ByteArrayOutputStream export(String htmlStr, String fontPath) {
		try (
			ByteArrayOutputStream os = new ByteArrayOutputStream();
		) {
			ITextRenderer renderer = new ITextRenderer();
			renderer.setDocumentFromString(htmlStr);

			// Solve the problem of Chinese support.
			ITextFontResolver fontResolver = renderer.getFontResolver();
			URL resource = Thread.currentThread().getContextClassLoader().getResource(".");
			String path = resource.getPath().replaceAll("%20", " ");
			String pdfPath = path + fontPath;
			
			fontResolver.addFont(pdfPath + File.separator + "arialuni.ttf", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
			fontResolver.addFont(pdfPath + File.separator + "simsun.ttc", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);

			renderer.layout();
			renderer.createPDF(os, true);
			return os;
		} catch (Exception e) {
			log.error("Error exporting PDF!", e);
			throw new BaseException("Error exporting PDF!", e);
		}
	}
}
