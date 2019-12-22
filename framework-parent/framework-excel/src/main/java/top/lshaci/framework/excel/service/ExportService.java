package top.lshaci.framework.excel.service;

import org.apache.poi.ss.usermodel.Workbook;
import top.lshaci.framework.excel.entity.ExportSheetParam;
import top.lshaci.framework.excel.service.impl.DefaultExportService;

import java.util.List;

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
}
