package top.lshaci.framework.excel.builder.impl;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import top.lshaci.framework.excel.builder.CellStyleBuilder;

/**
 * <p>默认的单元格样式构建者</p><br>
 *
 * <b>1.0.6: </b>将样式创建方法的入参<code>ExportSheetParam</code>修改为<code>fontName</code>
 *
 * @author lshaci
 * @since 1.0.2
 * @version 1.0.6
 */
public class DefaultCellStyleBuilder implements CellStyleBuilder {

	@Override
	public void setMergeCellBorder(CellRangeAddress region, Sheet sheet) {
		RegionUtil.setBorderTop(BorderStyle.THIN, region, sheet);
		RegionUtil.setBorderRight(BorderStyle.THIN, region, sheet);
		RegionUtil.setBorderBottom(BorderStyle.THIN, region, sheet);
		RegionUtil.setBorderLeft(BorderStyle.THIN, region, sheet);
	}

	@Override
	public CellStyle contentStyle(Workbook workbook, String fontName) {
		CellStyle style = workbook.createCellStyle();
		style.setWrapText(true);
		setBorder(style);
		setCenter(style);

		Font font = createFont(workbook, IndexedColors.BLACK, (short) 10, false, fontName);
		style.setFont(font);

		return style;
	}

	@Override
	public CellStyle sheetTitleStyle(Workbook workbook, String fontName) {
		CellStyle style = workbook.createCellStyle();
		style.setWrapText(true);
		setBorder(style);
		setCenter(style);

		Font font = createFont(workbook, IndexedColors.BLACK, (short) 20, true, fontName);
		style.setFont(font);

		return style;
	}

	@Override
	public CellStyle columnTitleStyle(Workbook workbook, String fontName) {
		CellStyle style = workbook.createCellStyle();
		setBorder(style);
		setCenter(style);

		Font font = createFont(workbook, IndexedColors.BLACK, (short) 12, true, fontName);
		style.setFont(font);

		return style;
	}

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
	private Font createFont(Workbook workbook, IndexedColors color, short height, Boolean bold, String fontName) {
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
	private void setCenter(CellStyle style) {
		style.setAlignment(HorizontalAlignment.CENTER);
		style.setVerticalAlignment(VerticalAlignment.CENTER);
	}

	/**
	 * 设置单元格边框
	 *
	 * @param style 单元格样式
	 */
	private void setBorder(CellStyle style) {
		style.setBorderBottom(BorderStyle.THIN);
		style.setBorderLeft(BorderStyle.THIN);
		style.setBorderRight(BorderStyle.THIN);
		style.setBorderTop(BorderStyle.THIN);
	}

}
