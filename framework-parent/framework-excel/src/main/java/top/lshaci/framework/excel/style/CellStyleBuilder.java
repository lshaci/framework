package top.lshaci.framework.excel.style;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;

import top.lshaci.framework.excel.entity.ExportSheetParam;

/**
 * 单元格样式构建者
 * 
 * @author lshaci
 * @since 1.0.2
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
	 * @param exportSheetParam Excel Sheet参数
	 * @return 内容单元格样式
	 */
	CellStyle contentStyle(Workbook workbook, ExportSheetParam exportSheetParam);

	/**
	 * 创建Sheet标题单元格样式
	 * 
	 * @param workbook Excel工作簿
	 * @param exportSheetParam Excel Sheet参数
	 * @return Sheet标题单元格样式
	 */
	CellStyle sheetTitleStyle(Workbook workbook, ExportSheetParam exportSheetParam);

	/**
	 * 创建列标题单元格样式
	 * 
	 * @param workbook Excel工作簿
	 * @param exportSheetParam Excel Sheet参数
	 * @return 列标题单元格样式
	 */
	CellStyle columnTitleStyle(Workbook workbook, ExportSheetParam exportSheetParam);

}
