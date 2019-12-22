package top.lshaci.framework.excel.handle;

import org.apache.poi.ss.usermodel.Workbook;
import top.lshaci.framework.excel.annotation.ExportSheet;
import top.lshaci.framework.excel.entity.ExportSheetParam;
import top.lshaci.framework.excel.enums.ExportError;
import top.lshaci.framework.excel.exception.ExportHandlerException;
import top.lshaci.framework.excel.service.DefaultExportService;
import top.lshaci.framework.excel.service.ExportService;

import java.util.List;
import java.util.Objects;

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
		verifyParam(cls);
		return service(exportService).create(cls, datas, sheetParam(cls, sheetParam));
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
	 * 传入的Sheet导出参数为空, 则通过导出类型上的<code>ExportSheet</code>注解进行创建
	 *
	 * @param cls 导出对象类型
	 * @param sheetParam Sheet导出参数
	 * @return Sheet导出参数
	 */
	private static ExportSheetParam sheetParam(Class<?> cls, ExportSheetParam sheetParam) {
		return Objects.nonNull(sheetParam) ? sheetParam : ExportSheetParam.build(cls.getAnnotation(ExportSheet.class));
	}

	/**
	 * 生成Excel的业务类为空, 则使用<code>DefaultExportService</code>
	 *
	 * @see DefaultExportService
	 * @param exportService  生成Excel的业务类
	 * @return 生成Excel的业务类
	 */
	private static ExportService service(ExportService exportService) {
		return Objects.isNull(exportService) ? new DefaultExportService() : exportService;
	}

}
