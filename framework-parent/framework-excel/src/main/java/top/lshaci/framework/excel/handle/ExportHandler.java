package top.lshaci.framework.excel.handle;

import org.apache.poi.ss.usermodel.Workbook;
import top.lshaci.framework.excel.annotation.ExportSheet;
import top.lshaci.framework.excel.entity.ExportSheetParam;
import top.lshaci.framework.excel.enums.ExportError;
import top.lshaci.framework.excel.exception.ExportHandlerException;
import top.lshaci.framework.excel.service.DefaultExportService;
import top.lshaci.framework.excel.service.ExportService;
import top.lshaci.framework.utils.ReflectionUtils;

import java.util.List;
import java.util.Objects;

/**
 * Excel导出处理器
 *
 * @author lshaci
 * @since 1.0.2
 */
public class ExportHandler {

	/**
	 * 根据导出实体类信息和数据条数导出Excel WorkBook
	 *
	 * @param cls 导出实体类信息
	 * @param datas 需要导出的数据
	 * @return Excel WorkBook
	 */
	public static <E> Workbook export(Class<E> cls, List<E> datas) {
		return export(cls, datas, null, null);
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
		ExportSheetParam sheetParam = ExportSheetParam.build(cls.getAnnotation(ExportSheet.class)).setTitle(sheetTitle);
		return export(cls, datas, sheetParam, null);
	}

	/**
	 * 根据导出实体类信息和数据条数导出Excel WorkBook
	 *
	 * @param cls 导出实体类信息
	 * @param datas 需要导出的数据
	 * @param serviceClass 生成Excel的业务类
	 * @return Excel WorkBook
	 */
	public static <E> Workbook export(Class<E> cls, List<E> datas, Class<? extends ExportService> serviceClass) {
		verifyParam(cls);
		return service(serviceClass).create(cls, datas, sheetParam(cls, null));
	}

	/**
	 * 根据导出实体类信息和数据条数导出Excel WorkBook
	 *
	 * @param cls 导出实体类信息
	 * @param datas 需要导出的数据
	 * @param sheetParam sheet中的参数
	 * @param serviceClass 生成Excel的业务类
	 * @return Excel WorkBook
	 */
	public static <E> Workbook export(Class<E> cls, List<E> datas, ExportSheetParam sheetParam, Class<? extends ExportService> serviceClass) {
		verifyParam(cls);
		return service(serviceClass).create(cls, datas, sheetParam(cls, sheetParam));
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

	private static ExportSheetParam sheetParam(Class<?> cls, ExportSheetParam sheetParam) {
		return Objects.nonNull(sheetParam) ? sheetParam : ExportSheetParam.build(cls.getAnnotation(ExportSheet.class));
	}

	private static ExportService service(Class<? extends ExportService> serviceClass) {
		return Objects.isNull(serviceClass) ? new DefaultExportService() : ReflectionUtils.newInstance(serviceClass);
	}

}
