package top.lshaci.framework.excel.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import top.lshaci.framework.excel.entity.ImportSheetParam;
import top.lshaci.framework.excel.exception.ExcelHandlerException;
import top.lshaci.framework.excel.handle.ImportHandler;

/**
 * Excel 导入工具
 * 
 * @author lshaci
 * @since 1.0.2
 */
@Slf4j
public class ExcelImportUtils {
	
	/**
	 * 将Excel文件解析为指定对象类型的集合
	 * 
	 * @param file Excel文件
	 * @param cls 解析对象类型
	 * @return 解析后的对象集合
	 */
	public static <E> List<E> parse(File file, Class<E> cls) {
		return parse(file, cls, null);
	}
	
	/**
	 * 将Excel文件解析为指定对象类型的集合
	 * 
	 * @param file Excel文件
	 * @param cls 解析对象类型
	 * @param sheetParam Excel工作表参数
	 * @return 解析后的对象集合
	 */
	public static <E> List<E> parse(File file, Class<E> cls, ImportSheetParam sheetParam) {
		try (
				InputStream is = new FileInputStream(file)	
		) {
			return parse(is, cls, sheetParam);
		} catch (IOException e) {
			log.error("获取Excel文件输入流时发生错误", e);
			throw new ExcelHandlerException("获取Excel文件输入流时发生错误", e);
		} 
	}
	
    /**
     * 将输入流解析为指定对象类型的集合
     * 
     * @param is 输入流
     * @param cls 解析对象类型
     * @return 解析后的对象集合
     */
    public static <E> List<E> parse(InputStream is, Class<E> cls) {
    	return parse(is, cls, null);
    }
    
    /**
     * 将输入流解析为指定对象类型的集合
     * 
     * @param is 输入流
     * @param cls 解析对象类型
     * @param sheetParam Excel工作表参数
     * @return 解析后的对象集合
     */
	public static <E> List<E> parse(InputStream is, Class<E> cls, ImportSheetParam sheetParam) {
        return ImportHandler.parse(is, cls, sheetParam);
	}

}
