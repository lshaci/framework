package top.lshaci.framework.excel.handle;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import lombok.extern.slf4j.Slf4j;
import top.lshaci.framework.excel.entity.ImportSheetParam;
import top.lshaci.framework.excel.enums.ImportError;
import top.lshaci.framework.excel.exception.ImportHandlerException;
import top.lshaci.framework.excel.service.DefaultImportService;
import top.lshaci.framework.utils.FileTypeUtil;
import top.lshaci.framework.utils.StreamUtils;
import top.lshaci.framework.utils.enums.FileType;

/**
 * Excel导入处理器
 *
 * @author lshaci
 * @since 1.0.2
 */
@Slf4j
public class ImportHandler {
	
	/**
	 * 允许的Excel文件类型
	 */
    private final static List<FileType> ALLOW_FILE_TYPES = Arrays.asList(
    		FileType.XLSX_DOCX, FileType.XLS_DOC, 
    		FileType.WPS, FileType.WPSX
    );

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
	@SuppressWarnings("unchecked")
	public static <E> List<E> parse(InputStream is, Class<E> cls, ImportSheetParam sheetParam) {
		verifyParam(is, cls);
		ByteArrayOutputStream buffer = StreamUtils.copyInputStream(is);

        Workbook workbook = getWorkBook(buffer);
        return (List<E>) new DefaultImportService(cls, workbook, sheetParam).fetch();
	}

	/**
	 * 参数验证
	 * 
     * @param is 输入流
     * @param cls 解析对象类型
	 */
	private static void verifyParam(InputStream is, Class<?> cls) {
		if (Objects.isNull(cls)) {
			throw new ImportHandlerException(ImportError.ENTITY_IS_NULL);
		}
		if (Objects.isNull(is)) {
			throw new ImportHandlerException(ImportError.DATA_IS_NULL);
		}
	}
	
	/**
	 * 获取Excel工作簿
	 * 
	 * @param buffer 缓冲流信息
	 * @param fileType Excel文件类型
	 * @return Excel工作簿
	 */
	private static Workbook getWorkBook(ByteArrayOutputStream buffer) {
		FileType fileType = getFileType(buffer);
		
        Workbook workbook = null;
        try (
        		InputStream is = new ByteArrayInputStream(buffer.toByteArray())
        ) {
            if (FileType.XLSX_DOCX.equals(fileType) || FileType.WPSX.equals(fileType)) {
                workbook = new XSSFWorkbook(is);
            }
            if (FileType.XLS_DOC.equals(fileType) || FileType.WPS.equals(fileType)) {
                workbook = new HSSFWorkbook(is);
            }
        } catch (Exception e) {
            log.error("创建Excel工作簿发生错误", e);
        }

        if (workbook == null) {
            throw new ImportHandlerException(ImportError.WORKBOOK_IS_NULL);
        }

        return workbook;
    }

	/**
	 * Excel文件类型
	 * 
	 * @param buffer 缓冲流信息
	 * @return Excel文件类型
	 */
    private static FileType getFileType(ByteArrayOutputStream buffer) {
    	try (
    			InputStream is = new ByteArrayInputStream(buffer.toByteArray())	
    	) {
    		FileType fileType = FileTypeUtil.getType(is);
    		if (ALLOW_FILE_TYPES.contains(fileType)) {
                return fileType;
            }
		} catch (Exception e) {
			log.error(ImportError.FETCH_EXCEL_TYPE_ERROR.getMsg(), e);
			throw new ImportHandlerException(ImportError.FETCH_EXCEL_TYPE_ERROR, e);
		}
    	
        throw new ImportHandlerException(ImportError.NOT_EXCEL);
    }
}
