package top.lshaci.framework.core.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import lombok.extern.slf4j.Slf4j;
import top.lshaci.framework.core.enums.FileType;
import top.lshaci.framework.core.exception.BaseException;

/**
 * POI excel utils
 * 
 * @author lshaci
 * @version 0.0.1
 */
@Slf4j
public abstract class POIExcelUtils {
	
	/**
	 * Change excel file to entity list
	 * 
	 * @param excelFile  the excel file
	 * @param excelTitles the excel title array
	 * @param fieldNames the entity field name array
	 * @param entityClass  the entity class
	 * @return  the entity list
	 */
	public static <E> List<E> excel2Entities(File excelFile, String[] excelTitles, String[] fieldNames, Class<E> entityClass) {
		if (ArrayUtils.isEmpty(excelTitles) || ArrayUtils.isEmpty(fieldNames)) {
			log.error("The excel title array or entity field name array is empty!");
			throw new BaseException("The excel title array or entity field name array must not be empty!");
		}
		
		int titleLength = excelTitles.length;
		int fieldLength = fieldNames.length;
		if (titleLength != fieldLength) {
			log.error("The excel title array length not equal entity field name array length!");
			log.warn("Use the min length!!!");
		}
		
		int length = Math.min(titleLength, fieldLength);
		Map<String, String> relation = new HashMap<>();
		
		for (int i = 0; i < length; i++) {
			relation.put(excelTitles[i], fieldNames[i]);
		}
		
		return excel2Entities(excelFile, relation, entityClass);
	}
		
	
	/**
	 * Change excel file to entity list
	 * 
	 * @param excelFile the excel file
	 * @param relation the key is excel title, the value is entity field name
	 * @param entityClass the entity class
	 * @return the entity list
	 */
	public static <E> List<E> excel2Entities(File excelFile, Map<String, String> relation, Class<E> entityClass) {
		checkParams(excelFile, relation, entityClass);
		
		FileType fileType = getFileType(excelFile);
		
		Workbook workBook = getWorkBook(excelFile, fileType);
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
			List<E> rowDatas = getRowDatas(sheet, lastRowNum, titles.length, titles, relation, entityClass);
			
			if (CollectionUtils.isEmpty(rowDatas)) {
				continue;
			}
			
			result.addAll(rowDatas);
		}
		
		return result;
	}
	
	/**
	 * Get the row data and change to entity list of the sheet
	 *
	 * @param sheet the sheet
	 * @param lastRowNum the all row number
	 * @param rowLength the row length
	 * @param titles the excel title array
	 * @param relation the key is excel title, the value is entity field name
	 * @param entityClass the entity class
	 * @return the entity list
	 */
	private static <E> List<E> getRowDatas(Sheet sheet, int lastRowNum, int rowLength, String[] titles, 
			Map<String, String> relation, Class<E> entityClass) {
		List<E> rowDatas = new ArrayList<>();
		// Loop the sheet get row
		for (int i = 1; i <= lastRowNum; i++) {
			Row row = sheet.getRow(i);
			// Loop the row get cell
			E entity = ReflectionUtils.newInstance(entityClass);
			
			if (entity == null) {
				String msg = "New instance is error!";
				log.error(msg);
				throw new BaseException(msg);
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
				String fieldName = relation.get(title);
				
				setEntityFieldValue(entity, fieldName, cellValue);
			}
			rowDatas.add(entity);
			
		}
		return rowDatas;
	}

	/**
	 * Set the entity field value
	 * 
	 * @param entity the entity
	 * @param fieldName the field name
	 * @param cellValue the cell value
	 */
	private static <E> void setEntityFieldValue(E entity, String fieldName, String cellValue) {
		Class<? extends Object> clazz = entity.getClass();
		try {
			Field field = clazz.getDeclaredField(fieldName);
			Object targetValue = StringConverterUtils.getTargetValue(field.getType(), cellValue);
			ReflectionUtils.setFieldValue(entity, field, targetValue);
		} catch (NoSuchFieldException | SecurityException e) {
			log.warn("The field(" + fieldName + ") not found or is not has security!", e);
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
			throw new BaseException("This excel sheet's title row not has cell!");
		}
		
		String[] titles = new String[lastCellNum];
		for (int i = 0; i < lastCellNum; i++) {
			Cell cell = row.getCell(i);
			if (cell == null) {
				continue;
			}
			titles[i] = cell.getStringCellValue();
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
			if (FileType.XLSX_DOCX.equals(fileType)) {
				workbook = new XSSFWorkbook(is);
			}
			if (FileType.XLS_DOC.equals(fileType)) {
				workbook = new HSSFWorkbook(is);
			}
		} catch (IOException e) {
			log.error("Create excle work book is error!", e);
			throw new BaseException("Create excle work book is error!");
		}
		
		Objects.requireNonNull(workbook, "The excle work book is must not be null!");
		
		return workbook;
	}

	/**
	 * Get the file type and check the excel file
	 * 
	 * @param excelFile the excel file
	 * @return the file type{@FileType}
	 */
	private static FileType getFileType(File excelFile) {
		Objects.requireNonNull(excelFile, "The excel file is must not be null!");
		try {
			FileType fileType = FileTypeUtil.getType(excelFile);
			if (FileType.XLSX_DOCX.equals(fileType) || FileType.XLS_DOC.equals(fileType)) {
				return fileType;
			}
			throw new BaseException("The file is not excel!");
		} catch (IOException e) {
			log.error("Get file header has error!", e);
			throw new BaseException("Get file header has error!");
		}
	}
	
	/**
	 * 
	 * 
	 * @param excelFile the excel file
	 * @param relation the key is excel title, the value is entity field name
	 * @param entityClass the entity class
	 */
	private static <E> void checkParams(File excelFile, Map<String, String> relation, Class<E> entityClass) {
		Objects.requireNonNull(excelFile, "The excel file is must not be null!");
		Objects.requireNonNull(relation, "The excel title relation is must not be null!");
		Objects.requireNonNull(entityClass, "The entity class is must not be null!");
		
	}
}
