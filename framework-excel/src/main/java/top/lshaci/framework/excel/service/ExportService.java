package top.lshaci.framework.excel.service;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import top.lshaci.framework.excel.annotation.ExportSheet;
import top.lshaci.framework.excel.entity.ExportSheetParam;
import top.lshaci.framework.excel.entity.ExportTitleParam;
import top.lshaci.framework.excel.enums.ExcelType;
import top.lshaci.framework.excel.service.impl.DefaultExportService;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * Excel 导出业务接口
 *
 * @author lshaci
 * @since 1.0.6
 */
@FunctionalInterface
public interface ExportService {

    /**
     * Excel类型为XLSX最大的数据量
     */
     int XLSX_MAX_SIZE = 10000;

    /**
     * 创建Excel工作簿{@code Workbook}
     *
     * @param cls 对象类型
     * @param datas 数据集合
     * @param sheetParam Sheet的参数
     * @return Excel工作簿
     */
    Workbook create(Class<?> cls, List<?> datas, ExportSheetParam sheetParam);

    /**
     * 获取默认的Excel导出业务类
     *
     * @return <code>DefaultExportService</code>
     */
    static ExportService get() {
        return new DefaultExportService();
    }

    /**
     * 获取列标题过滤器
     *
     * @return 列标题过滤器
     */
    default Predicate<ExportTitleParam> columnFilter() {
        return t -> true;
    }

    /**
     * 创建表格中的sheet
     *
     * @param workbook Excel工作薄
     * @param sheetParam 导出Sheet的参数
     * @param sheetNumber  当前sheet的编号(<b>编号从1开始</b>)
     * @return 根据名称创建的sheet
     */
    default Sheet sheet(Workbook workbook, ExportSheetParam sheetParam, int sheetNumber) {
        Integer number = sheetParam.getNumber();
        String name = sheetParam.getName();
        if (Objects.equals(1, number)) {
            return workbook.createSheet(name);
        } else {
            return workbook.createSheet(name + "_" + sheetNumber);
        }
    }

    /**
     * 根据导出实体类信息和数据条数创建Excel WorkBook
     *
     * @param cls 对象类型
     * @param size 导出数据条数
     * @return Excel WorkBook
     */
    default Workbook workbook(Class<?> cls, int size) {
        ExportSheet exportSheet = cls.getAnnotation(ExportSheet.class);
        if (Objects.isNull(exportSheet)) {
            return new XSSFWorkbook();
        }

        if (ExcelType.XLS.equals(exportSheet.type())) {
            return new HSSFWorkbook();
        } else if (size < ExportService.XLSX_MAX_SIZE) {
            return new XSSFWorkbook();
        } else {
            return new SXSSFWorkbook();
        }
    }

    /**
     * 设置列宽后的操作
     */
    default void afterSetColumnWidth() {
        // 如果有添加行, 请操作crn增加
    }

    /**
     * 设置Sheet标题后的操作
     */
    default void afterSetSheetTitle() {
        // 如果有添加行, 请操作crn增加
    }

    /**
     * 设置列标题后的操作
     */
    default void afterSetColumnTitles() {
        // 如果有添加行, 请操作crn增加
    }

    /**
     * 设置行数据后的操作
     */
    default void afterSetRowContent() {
        // 如果有添加行, 请操作crn增加
    }
}
