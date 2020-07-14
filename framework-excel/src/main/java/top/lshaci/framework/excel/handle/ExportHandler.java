package top.lshaci.framework.excel.handle;

import org.apache.poi.ss.usermodel.Workbook;
import top.lshaci.framework.excel.annotation.ExportSheet;
import top.lshaci.framework.excel.entity.ExportSheetParam;
import top.lshaci.framework.excel.enums.ExportError;
import top.lshaci.framework.excel.exception.ExportHandlerException;
import top.lshaci.framework.excel.service.ExportService;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static top.lshaci.framework.excel.entity.ExportSheetParam.build;

/**
 * <p>Excel导出处理器</p><br>
 *
 * <b>1.0.6:</b>添加参数<code>exportService</code>
 * <b>1.0.8:</b>使用Optional替换if else
 *
 * @author lshaci
 * @since 1.0.2
 * @version 1.0.8
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

		sheetParam = Optional.ofNullable(sheetParam).orElse(build(cls.getAnnotation(ExportSheet.class)));
		exportService = Optional.ofNullable(exportService).orElseGet(ExportService::get);

		return exportService.create(cls, datas, sheetParam);
	}

}
