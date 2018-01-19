package top.lshaci.framework.excel.handler;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import lombok.extern.slf4j.Slf4j;
import top.lshaci.framework.excel.annotation.UploadConvert;
import top.lshaci.framework.excel.annotation.UploadExcelTitle;
import top.lshaci.framework.excel.exception.ExcelHandlerException;
import top.lshaci.framework.excel.model.ExcelRelationModel;
import top.lshaci.framework.utils.DateUtils;
import top.lshaci.framework.utils.FileTypeUtil;
import top.lshaci.framework.utils.ReflectionUtils;
import top.lshaci.framework.utils.StringConverterUtils;
import top.lshaci.framework.utils.enums.FileType;

/**
 * POI excel upload handler
 * 
 * @author lshaci
 * @since 0.0.1
 */
@Slf4j
public abstract class POIExcelUploadHandler {
	
	/**
	 * The allow excel file type list
	 */
	private final static List<FileType> ALLOW_FILE_TYPES = Arrays.asList(FileType.XLSX_DOCX, FileType.XLS_DOC, FileType.WPS, FileType.WPSX);
	
	/**
	 * The allow excel file suffix list
	 */
	private final static List<String> ALLOW_FILE_SUFFIX = Arrays.asList("xls", "xlsx");
	
	/**
	 * Change excel file to entity list
	 * 
	 * @param excelFile the excel file
	 * @param entityClass the entity class
	 * @return the entity list
	 */
	public static <E> List<E> excel2Entities(File excelFile, Class<E> entityClass) {
		checkParams(excelFile, entityClass);
		
		FileType fileType = getFileType(excelFile);
		
		Map<String, ExcelRelationModel> relations = handlerRelations(entityClass);
		try (Workbook workBook = getWorkBook(excelFile, fileType);) {
			// The number of sheet
			int sheets = workBook.getNumberOfSheets();
			
			List<E> result = new ArrayList<>();
			for (int i = 0; i < sheets; i++) {
				Sheet sheet = workBook.getSheetAt(i);
				
				// If the sheet is null, continue loop
				if (sheet == null) {
					continue;
				}
				
				int lastRowNum = sheet.getLastRowNum();
				
				// if the sheet not has any data, continue loop
				if (lastRowNum < 1) {
					continue;
				}
				
				String[] titles = getTitles(sheet);
				List<E> rowDatas = getRowDatas(sheet, lastRowNum, titles.length, titles, entityClass, relations);
				
				if (CollectionUtils.isEmpty(rowDatas)) {
					continue;
				}
				
				result.addAll(rowDatas);
			}
			
			return result;
		} catch (Exception e) {
			log.error("Parse excel error", e);
			throw new ExcelHandlerException("Convert excel file to entity list is error!", e);
		}
	}
	
	/**
	 * Get the row data and change to entity list of the sheet
	 *
	 * @param sheet the sheet
	 * @param lastRowNum the all row number
	 * @param rowLength the row length
	 * @param titles the excel title array
	 * @param entityClass the entity class
	 * @param relations the excel relation model map, key is excel title, value is relation model
	 * @return the entity list
	 */
	private static <E> List<E> getRowDatas(Sheet sheet, int lastRowNum, int rowLength, 
			String[] titles, Class<E> entityClass, Map<String, ExcelRelationModel> relations) {
		List<E> rowDatas = new ArrayList<>();
		// Loop the sheet get row
		for (int i = 1; i <= lastRowNum; i++) {
			Row row = sheet.getRow(i);
			// Loop the row get cell
			E entity = ReflectionUtils.newInstance(entityClass);
			
			if (entity == null) {
				String msg = "New instance is error!";
				log.error(msg);
				throw new ExcelHandlerException(msg);
			}
			
			for (Cell cell : row) {
				if (cell == null) {
					continue;
				}
				
				int columnIndex = cell.getColumnIndex();
				String cellValue = getCellValue(cell);
				if (StringUtils.isEmpty(cellValue)) {
					continue;
				}
				
				String title = titles[columnIndex];
				ExcelRelationModel relationModel = relations.get(title);
				if (relationModel == null ) {
					continue;
				}
				
				Field field = relationModel.getTargetField();
				
				if (field == null) {
					continue;
				}
				
				cellValue = getConvertValue(cellValue, relationModel);
				
				setEntityFieldValue(entity, field, cellValue);
			}
			rowDatas.add(entity);
			
		}
		return rowDatas;
	}
	
	/**
	 * Get the convert value with cell value
	 * 
	 * @param cellValue the cell value
	 * @param relationModel the excel relation model
	 * @return the convert value
	 */
	private static String getConvertValue(String cellValue, ExcelRelationModel relationModel) {
		Method method = relationModel.getConvertMethod();
		
		if (method != null) {
			Object instance = relationModel.getConvertInstance();
			Object value = ReflectionUtils.invokeMethod(instance, method, cellValue);
			return value == null ? null : value.toString();
		}
		
		return cellValue;
	}

	/**
	 * Set the entity field value
	 * 
	 * @param entity the entity
	 * @param field the field
	 * @param cellValue the cell value
	 */
	private static <E> void setEntityFieldValue(E entity, Field field, String cellValue) {
		try {
			Object targetValue = StringConverterUtils.getTargetValue(field.getType(), cellValue);
			ReflectionUtils.setFieldValue(entity, field, targetValue);
		} catch (SecurityException e) {
			log.warn("The field(" + field.getName() + ") is not has security!", e);
		}
	}

	/**
	 * Get the cell value and add to row data array
	 * 
	 * @param cell the cell
	 * @return the cell value, type is string
	 */
	private static String getCellValue(Cell cell) {
		if (CellType.NUMERIC.equals(cell.getCellTypeEnum())) {
			if (HSSFDateUtil.isCellDateFormatted(cell)) {
				return DateUtils.formatLongDate(cell.getDateCellValue());
			}
			cell.setCellType(CellType.STRING);
		}
		if (CellType.STRING.equals(cell.getCellTypeEnum())) {
			return cell.getStringCellValue();
		}
		return null;
	}

	/**
	 * Get the excel sheet titles 
	 * 
	 * @param sheet the excel sheet
	 * @return this excel sheet title array
	 */
	private static String[] getTitles(Sheet sheet) {
		Row row = sheet.getRow(0);
		
		short lastCellNum = row.getLastCellNum();
		
		if (lastCellNum < 0) {
			throw new ExcelHandlerException("This excel sheet's title row not has cell!");
		}
		
		String[] titles = new String[lastCellNum];
		for (int i = 0; i < lastCellNum; i++) {
			Cell cell = row.getCell(i);
			if (cell == null) {
				continue;
			}
			titles[i] = cell.getStringCellValue().trim();
		}
		
		return titles;
	}

	/**
	 * Get the excel work book on the basis of file type
	 * 
	 * @param excelFile the excel file
	 * @param fileType the excel file type
	 * @return the excel work book
	 */
	private static Workbook getWorkBook(File excelFile, FileType fileType) {
		Workbook workbook = null;
		try {
			InputStream is = new FileInputStream(excelFile);
			/*
			 *  Constructs a work book on the basis of file type
			 */
			if (FileType.XLSX_DOCX.equals(fileType) || FileType.WPSX.equals(fileType)) {
				workbook = new XSSFWorkbook(is);
			}
			if (FileType.XLS_DOC.equals(fileType) || FileType.WPS.equals(fileType)) {
				workbook = new HSSFWorkbook(is);
			}
		} catch (IOException e) {
			log.error("Create excle work book is error!", e);
			throw new ExcelHandlerException("Create excle work book is error!");
		}
		
		Objects.requireNonNull(workbook, "The excle work book is must not be null!");
		
		return workbook;
	}
	
	/**
	 * Get the file type and check the excel file
	 * 
	 * @param excelFile the excel file
	 * @return the file type{@link FileType}
	 */
	private static FileType getFileType(File excelFile) {
		Objects.requireNonNull(excelFile, "The excel file is must not be null!");
		
		String fileName = excelFile.getName();
		String fileSuffix = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
		
		if (!ALLOW_FILE_SUFFIX.contains(fileSuffix)) {
			throw new ExcelHandlerException("The file suffix name is not excel!");
		}
		
		try {
			FileType fileType = FileTypeUtil.getType(excelFile);
			if (ALLOW_FILE_TYPES.contains(fileType)) {
				return fileType;
			}
			throw new ExcelHandlerException("The file type is not excel!");
		} catch (IOException e) {
			log.error("Get file header has error!", e);
			throw new ExcelHandlerException("Get file header has error!");
		}
	}
	
	/**
	 * Handler the entity field relations
	 * 
	 * @param entityClass the entity class
	 * @return the excel relation model map, key is excel title, value is relation model
	 */
	private static <E> Map<String, ExcelRelationModel> handlerRelations(Class<E> entityClass) {
		Field[] fields = entityClass.getDeclaredFields();
		
		if (ArrayUtils.isEmpty(fields)) {
			throw new ExcelHandlerException("The entity not has any field!");
		}
		
		Map<Class<?>, Object> convertClassCache = new HashMap<>();
		
		return Arrays.stream(fields).collect(Collectors.toMap(
				f -> getFieldTitleName(f), 
				f -> {
					return createExcelRelationModel(f, convertClassCache);
				}));
	}
	
	/**
	 * Get title name of the excel corresponding to the field
	 * 
	 * @param field the field
	 * @return the title name of the excel
	 */
	private static String getFieldTitleName(Field field) {
		UploadExcelTitle excelTitle = field.getAnnotation(UploadExcelTitle.class);
		if (excelTitle != null) {
			return excelTitle.value();
		}
		return field.getName();
	}
	
	/**
	 * Get the field convert instance
	 * 
	 * @param convertClassCache the convert class cache
	 * @param convert the field convert annotation
	 * @return the convert instance
	 */
	private static Object getConvertInstance(Map<Class<?>, Object> convertClassCache, UploadConvert convert) {
		Class<?> convertClass = convert.clazz();
		
		if (convertClass == null) {
			throw new ExcelHandlerException("The convert class must not be null!");
		}
		
		Object convertInstance = convertClassCache.get(convertClass);
		if (convertInstance == null) {
			convertInstance = ReflectionUtils.newInstance(convertClass);
			convertClassCache.put(convertClass, convertInstance);
		}
		
		return convertInstance;
	}
	
	/**
	 * Get the field convert method
	 * 
	 * @param convert the field convert annotation
	 * @return the convert method
	 */
	private static Method getConvertMethod(UploadConvert convert) {
		String methodName = convert.method();
		Class<?> convertClass = convert.clazz();
		
		if (StringUtils.isBlank(methodName)) {
			throw new ExcelHandlerException("The convert method name must not be empty!");
		}
		
		try {
			return convertClass.getDeclaredMethod(methodName, String.class);
		} catch (NoSuchMethodException | SecurityException e) {
			String msg = "Get the convert method is error!";
			log.error(msg, e);
			throw new ExcelHandlerException(msg);
		}
	}
	
	/**
	 * Create excel relation model by target field
	 * 
	 * @param field the target field
	 * @param convertClassCache the convert class cache
	 * @return the excel relation model
	 */
	private static ExcelRelationModel createExcelRelationModel(Field field, Map<Class<?>, Object> convertClassCache) {
		ExcelRelationModel model = new ExcelRelationModel(field);
		
		UploadConvert convert = field.getAnnotation(UploadConvert.class);
		
		if (convert != null) {
			Object convertInstance = getConvertInstance(convertClassCache, convert);
			Method convertMethod = getConvertMethod(convert);
			model.setConvertInstance(convertInstance);
			model.setConvertMethod(convertMethod);
		}
		
		return model;
	}
	
	/**
	 * Check the parameter
	 * 
	 * @param excelFile the excel file
	 * @param entityClass the entity class
	 */
	private static <E> void checkParams(File excelFile, Class<E> entityClass) {
		Objects.requireNonNull(excelFile, "The excel file must not be null!");
		Objects.requireNonNull(entityClass, "The entity class must not be null!");
	}
}
