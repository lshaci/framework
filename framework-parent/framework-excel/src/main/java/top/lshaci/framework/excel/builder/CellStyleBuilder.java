package top.lshaci.framework.excel.builder;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;

/**
 * <p>单元格样式构建者</p><br>
 *
 * <b>1.0.6: </b>将样式创建方法的入参<code>ExportSheetParam</code>修改为<code>fontName</code>
 *
 * @author lshaci
 * @since 1.0.2
 * @version 1.0.6
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

}
