package top.lshaci.framework.excel.utils;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import org.apache.poi.ss.usermodel.Workbook;

import lombok.extern.slf4j.Slf4j;
import top.lshaci.framework.excel.exception.ExcelHandlerException;
import top.lshaci.framework.excel.handle.ExportHandler;

/**
 * Excel 导出工具
 * 
 * @author lshaci
 * @since 1.0.2
 */
@Slf4j
public class ExcelExportUtils {
	
	/**
	 * 根据导出实体类信息和数据条数导出Excel WorkBook
	 *
	 * @param cls 导出实体类信息
	 * @param datas 需要导出的数据
	 * @param os 需要将Excel工作簿写到到输出流
	 */
	public static <E> void export(Class<E> cls, List<E> datas, OutputStream os) {
		export(cls, datas, null, os);
	}
	
	/**
	 * 根据导出实体类信息和数据条数导出Excel WorkBook
	 *
	 * @param cls 导出实体类信息
	 * @param datas 需要导出的数据
	 * @param sheetTitle sheet中的标题, 会覆盖注解{@code @ExportSheet}中的title属性
	 * @param os 需要将Excel工作簿写到到输出流
	 */
	public static <E> void export(Class<E> cls, List<E> datas, String sheetTitle, OutputStream os) {
		try (
				Workbook workbook = ExportHandler.export(cls, datas, sheetTitle)
		) {
			workbook.write(os);
		} catch (IOException e) {
			log.error("将Excel工作簿导出时发生错误", e);
			throw new ExcelHandlerException("将Excel工作簿导出时发生错误", e);
		}
	}

}
