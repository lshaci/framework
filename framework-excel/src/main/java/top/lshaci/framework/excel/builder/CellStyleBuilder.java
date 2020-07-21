package top.lshaci.framework.excel.builder;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

/**
 * <p>单元格样式构建者</p><br>
 *
 * <p>1.0.6: 将样式创建方法的入参<code>ExportSheetParam</code>修改为<code>fontName</code></p>
 * <p>1.0.8: 将createFont, setCenter, setBorder方法提取到接口中
 *
 * @author lshaci
 * @since 1.0.2
 * @version 1.0.8
 */
public interface CellStyleBuilder {

	/**
	 * 设置合并单元格边框
	 *
	 * @param region 合并单元格的区域
	 * @param sheet Excel Sheet
	 */
	void setMergeCellBorder(CellRangeAddress region, Sheet sheet);

	/**
	 * 创建内容单元格样式
	 *
	 * @param workbook Excel工作簿
	 * @param fontName 字体名称
	 * @return 内容单元格样式
	 */
	CellStyle contentStyle(Workbook workbook, String fontName);

	/**
	 * 创建Sheet标题单元格样式
	 *
	 * @param workbook Excel工作簿
	 * @param fontName 字体名称
	 * @return Sheet标题单元格样式
	 */
	CellStyle sheetTitleStyle(Workbook workbook, String fontName);

	/**
	 * 创建列标题单元格样式
	 *
	 * @param workbook Excel工作簿
	 * @param fontName 字体名称
	 * @return 列标题单元格样式
	 */
	CellStyle columnTitleStyle(Workbook workbook, String fontName);

	/**
	 * 创建字体
	 *
	 * @param workbook Excel工作簿
	 * @param color 字体颜色
	 * @param height 字体高度
	 * @param bold 是否加粗
	 * @param fontName 字体名称
	 * @return 创建的字体
	 */
	default Font createFont(Workbook workbook, IndexedColors color, short height, Boolean bold, String fontName) {
		Font font = workbook.createFont();
		font.setColor(color.index);
		font.setFontHeightInPoints(height);
		font.setBold(bold);
		font.setFontName(fontName);
		return font;
	}

	/**
	 * 设置水平和垂直居中
	 *
	 * @param style 单元格样式
	 */
	default void setCenter(CellStyle style) {
		style.setAlignment(HorizontalAlignment.CENTER);
		style.setVerticalAlignment(VerticalAlignment.CENTER);
	}

	/**
	 * 设置单元格边框
	 *
	 * @param style 单元格样式
	 */
	default void setBorder(CellStyle style) {
		style.setBorderBottom(BorderStyle.THIN);
		style.setBorderLeft(BorderStyle.THIN);
		style.setBorderRight(BorderStyle.THIN);
		style.setBorderTop(BorderStyle.THIN);
	}

}
