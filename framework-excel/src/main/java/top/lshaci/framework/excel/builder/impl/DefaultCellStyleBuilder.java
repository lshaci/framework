package top.lshaci.framework.excel.builder.impl;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import top.lshaci.framework.excel.builder.CellStyleBuilder;

/**
 * <p>默认的单元格样式构建者</p><br>
 *
 * <p>1.0.6: 将样式创建方法的入参<code>ExportSheetParam</code>修改为<code>fontName</code></p>
 * <p>1.0.8: 将createFont, setCenter, setBorder方法提取到接口中
 *
 * @author lshaci
 * @since 1.0.2
 * @version 1.0.8
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
}
