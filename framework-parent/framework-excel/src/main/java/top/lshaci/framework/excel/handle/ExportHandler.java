package top.lshaci.framework.excel.handle;

import java.util.List;
import java.util.Objects;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import top.lshaci.framework.excel.annotation.ExportSheet;
import top.lshaci.framework.excel.enums.ExcelType;
import top.lshaci.framework.excel.enums.ExportError;
import top.lshaci.framework.excel.exception.ExportHandlerException;
import top.lshaci.framework.excel.service.ExportService;

/**
 * Excel导出处理器
 *
 * @author lshaci
 * @since 1.0.2
 */
public class ExportHandler {

	/**
	 * Excel类型为XLSX最大的数据量
	 */
	private static final int XLSX_MAX_SIZE = 10000;

	/**
	 * 根据导出实体类信息和数据条数导出Excel WorkBook
	 *
	 * @param cls 导出实体类信息
	 * @param datas 需要导出的数据
	 * @return Excel WorkBook
	 */
	public static <E> Workbook export(Class<E> cls, List<E> datas) {
		return export(cls, datas, null);
	}

	/**
	 * 根据导出实体类信息和数据条数导出Excel WorkBook
	 *
	 * @param cls 导出实体类信息
	 * @param datas 需要导出的数据
	 * @param sheetTitle sheet中的标题, 会覆盖注解{@code @ExportSheet}中的title属性
	 * @return Excel WorkBook
	 */
	public static <E> Workbook export(Class<E> cls, List<E> datas, String sheetTitle) {
		verifyParam(cls);
		Workbook workbook = getWorkbook(cls, CollectionUtils.isEmpty(datas) ? 0 : datas.size());
		new ExportService(cls, datas, workbook, sheetTitle).create();
		return workbook;
	}

	/**
	 * 验证参数
	 *
	 * @param cls 导出对象类型
	 */
	private static void verifyParam(Class<?> cls) {
		if (Objects.isNull(cls)) {
			throw new ExportHandlerException(ExportError.ENTITY_IS_NULL);
		}
	}

	/**
	 * 根据导出实体类信息和数据条数创建Excel WorkBook
	 *
	 * @param cls 导出实体类信息
	 * @param size 导出数据条数
	 * @return Excel WorkBook
	 */
	private static Workbook getWorkbook(Class<?> cls, int size) {
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

}
