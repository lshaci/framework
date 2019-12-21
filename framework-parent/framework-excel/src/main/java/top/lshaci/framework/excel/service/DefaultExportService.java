package top.lshaci.framework.excel.service;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import top.lshaci.framework.excel.annotation.ExportSheet;
import top.lshaci.framework.excel.entity.ExportTitleParam;
import top.lshaci.framework.excel.enums.ExcelType;

import java.util.Objects;
import java.util.function.Predicate;

/**
 * <p>默认导出Excel业务类</p><br>
 *
 * <b>1.0.6:</b>抽取抽象基类<code>AbstractExportService</code>
 *
 * @author lshaci
 * @since 1.0.2
 * @version 1.0.6
 */
public class DefaultExportService extends AbstractExportService {

	@Override
	protected void createSheet(int sheetNumber) {
		Integer number = sheetParam.getNumber();
		String name = sheetParam.getName();
		if (Objects.equals(1, number)) {
			this.sheet = workbook.createSheet(name);
		} else {
			this.sheet = workbook.createSheet(name + "_" + sheetNumber);
		}
	}

	@Override
	protected Predicate<ExportTitleParam> columnTitleFilter() {
		return t -> true;
	}

	@Override
	protected void handleStyle() {
		String fontName = sheetParam.getFontName();
		this.sheetTitleStyle = sheetParam.getCellStyleBuilder().sheetTitleStyle(workbook, fontName);
		this.columnTitleStyle = sheetParam.getCellStyleBuilder().columnTitleStyle(workbook, fontName);
		this.contentStyle = sheetParam.getCellStyleBuilder().contentStyle(workbook, fontName);
	}

	@Override
	protected Workbook getWorkbook(int size) {
		ExportSheet exportSheet = cls.getAnnotation(ExportSheet.class);
		if (Objects.isNull(exportSheet)) {
			return new XSSFWorkbook();
		}

		if (ExcelType.XLS.equals(exportSheet.type())) {
			return new HSSFWorkbook();
		} else if (size < XLSX_MAX_SIZE) {
			return new XSSFWorkbook();
		} else {
			return new SXSSFWorkbook();
		}
	}

	@Override
	protected void afterSetColumnWidth() {
		// 如果有添加行, 请操作crn增加
	}

	@Override
	protected void afterSetSheetTitle() {
		// 如果有添加行, 请操作crn增加
	}

	@Override
	protected void afterSetColumnTitles() {
		// 如果有添加行, 请操作crn增加
	}

	@Override
	protected void afterSetRowContent() {
		// 如果有添加行, 请操作crn增加
	}
}
