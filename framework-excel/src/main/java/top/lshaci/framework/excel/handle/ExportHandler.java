package top.lshaci.framework.excel.handle;

import org.apache.poi.ss.usermodel.Workbook;
import top.lshaci.framework.excel.annotation.ExportSheet;
import top.lshaci.framework.excel.entity.ExportSheetParam;
import top.lshaci.framework.excel.enums.ExportError;
import top.lshaci.framework.excel.exception.ExportHandlerException;
import top.lshaci.framework.excel.service.ExportService;

import java.util.List;
import java.util.Objects;

import static java.util.Objects.nonNull;
import static top.lshaci.framework.excel.entity.ExportSheetParam.build;
import static top.lshaci.framework.excel.service.ExportService.get;

/**
 * <p>Excel导出处理器</p><br>
 *
 * <b>1.0.6:</b>添加参数<code>exportService</code>
 *
 * @author lshaci
 * @since 1.0.2
 * @version 1.0.6
 */
public class ExportHandler {

	/**
	 * 根据导出实体类信息和数据条数导出Excel WorkBook
	 *
	 * @param cls 导出实体类信息
	 * @param datas 需要导出的数据
	 * @param sheetParam sheet中的参数
	 * @param exportService 生成Excel的业务类
	 * @return Excel WorkBook
	 */
	public static <E> Workbook export(Class<E> cls, List<E> datas, ExportSheetParam sheetParam, ExportService exportService) {
		if (Objects.isNull(cls)) {
			throw new ExportHandlerException(ExportError.ENTITY_IS_NULL);
		}

		sheetParam = nonNull(sheetParam) ? sheetParam : build(cls.getAnnotation(ExportSheet.class));
		exportService = nonNull(exportService) ? exportService : get();

		return exportService.create(cls, datas, sheetParam);
	}

}
