package top.lshaci.framework.excel.style.impl;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;

import top.lshaci.framework.excel.entity.ExportSheetParam;
import top.lshaci.framework.excel.style.CellStyleBuilder;

/**
 * 默认的单元格样式构建者
 * 
 * @author lshaci
 * @since 1.0.2
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
	public CellStyle contentStyle(Workbook workbook, ExportSheetParam exportSheetParam) {
		CellStyle style = workbook.createCellStyle();
		style.setWrapText(true);
		setBorder(style);
		setCenter(style);
		
		Font font = createFont(workbook, IndexedColors.BLACK.index, (short) 10, false, exportSheetParam.getFontName());
		style.setFont(font);

		return style;
	}

	@Override
	public CellStyle sheetTitleStyle(Workbook workbook, ExportSheetParam exportSheetParam) {
		CellStyle style = workbook.createCellStyle();
		style.setWrapText(true);
		setBorder(style);
		setCenter(style);
		
		Font font = createFont(workbook, IndexedColors.BLACK.index, (short) 20, true, exportSheetParam.getFontName());
		style.setFont(font);

		return style;
	}

	@Override
	public CellStyle columnTitleStyle(Workbook workbook, ExportSheetParam exportSheetParam) {
		CellStyle style = workbook.createCellStyle();
		setBorder(style);
		setCenter(style);
		
		Font font = createFont(workbook, IndexedColors.BLACK.index, (short) 12, true, exportSheetParam.getFontName());
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
	private Font createFont(Workbook workbook, short color, short height, Boolean bold, String fontName) {
		Font font = workbook.createFont();
		font.setColor(color);
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
