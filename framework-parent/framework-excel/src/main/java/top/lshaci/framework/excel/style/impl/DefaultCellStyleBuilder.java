package top.lshaci.framework.excel.style.impl;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;

import top.lshaci.framework.excel.entity.ExportSheetParam;
import top.lshaci.framework.excel.style.CellStyleBuilder;

public class DefaultCellStyleBuilder implements CellStyleBuilder {

	@Override
	public CellStyle contentStyle(Workbook workbook, ExportSheetParam exportSheetParam) {
		CellStyle style = workbook.createCellStyle();
		style.setWrapText(true);
		// set cell border
		setBorder(style);
		setCenter(style);
		// 创建字体
		Font font = createFont(workbook, IndexedColors.BLACK.index, (short) 10, false, exportSheetParam.getFontName());
		// 设置字体
		style.setFont(font);

		return style;
	}

	@Override
	public CellStyle sheetTitleStyle(Workbook workbook, ExportSheetParam exportSheetParam) {
		CellStyle style = workbook.createCellStyle();
		style.setWrapText(true);
		// set cell border
		setBorder(style);
		setCenter(style);
		// create a font
		Font font = createFont(workbook, IndexedColors.BLACK.index, (short) 20, true, exportSheetParam.getFontName());
		// set font
		style.setFont(font);

		return style;
	}

	@Override
	public CellStyle columnTitleStyle(Workbook workbook, ExportSheetParam exportSheetParam) {
		CellStyle style = workbook.createCellStyle();
		// set cell border
		setBorder(style);
		setCenter(style);
		
		// create a font
		Font font = createFont(workbook, IndexedColors.BLACK.index, (short) 12, true, exportSheetParam.getFontName());
		// set font
		style.setFont(font);

		return style;
	}
	
	private Font createFont(Workbook workbook, short color, short height, Boolean bold, String fontName) {
		Font font = workbook.createFont();
		font.setColor(color);
		font.setFontHeightInPoints(height);
		font.setBold(bold);
		font.setFontName(fontName);
		return font;
	}

	private void setCenter(CellStyle style) {
		style.setAlignment(HorizontalAlignment.CENTER);
		style.setVerticalAlignment(VerticalAlignment.CENTER);
	}
	
	private void setBorder(CellStyle style) {
		style.setBorderBottom(BorderStyle.THIN);
		style.setBorderLeft(BorderStyle.THIN);
		style.setBorderRight(BorderStyle.THIN);
		style.setBorderTop(BorderStyle.THIN);
	}

}
