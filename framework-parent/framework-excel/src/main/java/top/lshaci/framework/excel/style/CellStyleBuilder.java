package top.lshaci.framework.excel.style;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;

import top.lshaci.framework.excel.entity.ExportSheetParam;

public interface CellStyleBuilder {

	CellStyle contentStyle(Workbook workbook, ExportSheetParam exportSheetParam);

	CellStyle sheetTitleStyle(Workbook workbook, ExportSheetParam exportSheetParam);

	CellStyle columnTitleStyle(Workbook workbook, ExportSheetParam exportSheetParam);

}
