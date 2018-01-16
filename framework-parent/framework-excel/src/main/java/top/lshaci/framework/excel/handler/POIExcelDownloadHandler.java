package top.lshaci.framework.excel.handler;

import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import lombok.extern.slf4j.Slf4j;
import top.lshaci.framework.excel.annotation.DownloadConvert;
import top.lshaci.framework.excel.annotation.DownloadExcelSheet;
import top.lshaci.framework.excel.annotation.DownloadExcelTitle;
import top.lshaci.framework.excel.annotation.DownloadIgnore;
import top.lshaci.framework.excel.exception.ExcelHandlerException;
import top.lshaci.framework.excel.model.DownloadOrder;
import top.lshaci.framework.excel.model.ExcelRelationModel;
import top.lshaci.framework.utils.ReflectionUtils;

/**
 * POI excel download handler
 * 
 * @author lshaci
 * @since 0.0.3
 */
@Slf4j
public abstract class POIExcelDownloadHandler {

	/**
	 * Change excel file to entity list
	 * 
	 * @param entities the entity list
	 * @return the entity list
	 */
	public static <E> InputStream entities2Excel(List<E> entities) {
		checkParams(entities);
		
		Class<?> entityClass = entities.get(0).getClass();
		
		List<DownloadOrder> titleOrder = getTitleOrder(entityClass);
		List<String[]> rowDatas = entities2StringArrays(entities, titleOrder, entityClass);
		
		if (CollectionUtils.isEmpty(rowDatas)) {
			log.error("The content is empty!");
			throw new ExcelHandlerException("The content must not be empty!");
		}
		
		try (
				ByteArrayOutputStream os = new ByteArrayOutputStream();
				XSSFWorkbook workbook = new XSSFWorkbook();
		) {
			String sheetName = getSheetName(entityClass);
			
			XSSFSheet sheet = workbook.createSheet(sheetName);
			
			// set default column width
			sheet.setDefaultColumnWidth(12);
			// set first column width
			sheet.setColumnWidth(0, 1500);
			
			// set column titles
			setColumnTitles(workbook, sheet, titleOrder);
			
			// set sheet content
			setSheetRows(workbook, sheet, rowDatas);
			
			workbook.write(os);
			return new ByteArrayInputStream(os.toByteArray());
		} catch (Exception e) {
			log.error("Parse entity list error", e);
			throw new ExcelHandlerException("Convert entity list to excel file is error!", e);
		}
	}
	
	/**
	 * Set excel sheet row content
	 * 
	 * @param workbook the excel work book
	 * @param sheet the excel sheet of the work book
	 * @param rowDatas the row datas
	 */
	private static void setSheetRows(XSSFWorkbook workbook, XSSFSheet sheet, List<String[]> rowDatas) {
		XSSFCellStyle contentStyle = createContentStyle(workbook);
		for (int i = 0; i < rowDatas.size(); i++) {
			XSSFRow row = sheet.createRow(1 + i);
			row.setHeight((short) 320);
			String[] rowData = rowDatas.get(i);
			for (int j = 0; j <= rowData.length; j++) {
				XSSFCell cell = row.createCell(j);
				String value = j == 0 ? (i + 1) + "" : rowData[j - 1];
				cell.setCellValue(value);
				cell.setCellStyle(contentStyle);
			}
		}
	}
	
	/**
	 * Create row content style
	 * 
	 * @param workbook the excel work book
	 * @return the row content style
	 */
	private static XSSFCellStyle createContentStyle(XSSFWorkbook workbook) {
		XSSFCellStyle style = workbook.createCellStyle();
		// set fill fore ground color
		style.setFillForegroundColor(new XSSFColor(Color.WHITE));
		// style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		// set cell border
		setBorder(style);
		// set the horizontal and vertical center.
		style.setAlignment(HorizontalAlignment.CENTER);
		style.setVerticalAlignment(VerticalAlignment.CENTER);
		// create a font 
		XSSFFont font = workbook.createFont();
		font.setColor(new XSSFColor(Color.BLACK));
		font.setFontHeightInPoints((short) 10);
		font.setFontName("宋体");
		// set font
		style.setFont(font);

		return style;
	}
	
	/**
	 * Set sheet titles
	 * 
	 * @param workbook the excel work book
	 * @param sheet the excel sheet of the work book
	 * @param titleOrder the titles
	 */
	private static void setColumnTitles(XSSFWorkbook workbook, XSSFSheet sheet, List<DownloadOrder> titleOrder) {
		XSSFRow row = sheet.createRow(0);
		row.setHeight((short) 360);
		XSSFCellStyle columnTitleStyle = createColumnTitleStyle(workbook);
		for (int i = 0; i <= titleOrder.size(); i++) {
			XSSFCell cell = row.createCell(i);
			String value = i == 0 ? "序号" : titleOrder.get(i - 1).getTitle();
			cell.setCellValue(value);
			cell.setCellStyle(columnTitleStyle);
		}
	}
	
	/**
	 * Create column title style
	 * 
	 * @param workbook the excel work book
	 * @return the column title style
	 */
	private static XSSFCellStyle createColumnTitleStyle(XSSFWorkbook workbook) {
		XSSFCellStyle style = workbook.createCellStyle();
		// set fill fore ground color
		style.setFillForegroundColor(new XSSFColor(Color.WHITE));
		// set cell border
		setBorder(style);
		// set the horizontal and vertical center.
		style.setAlignment(HorizontalAlignment.CENTER);
		style.setVerticalAlignment(VerticalAlignment.CENTER);
		// create a font 
		XSSFFont font = workbook.createFont();
		font.setColor(new XSSFColor(Color.BLACK));
		font.setFontHeightInPoints((short) 10);
		font.setBold(true);
		font.setFontName("宋体");
		// set font
		style.setFont(font);
		
		return style;
	}
	
	/**
	 * Set cell style border
	 * 
	 * @param style the cell style
	 */
	private static void setBorder(XSSFCellStyle style) {
		style.setBorderBottom(BorderStyle.THIN);
		style.setBorderLeft(BorderStyle.THIN);
		style.setBorderRight(BorderStyle.THIN);
		style.setBorderTop(BorderStyle.THIN);
	}
	
	/**
	 * Parse entity list to string array list
	 * 
	 * @param entities the entity list
	 * @param titleOrder the title order list
	 * @param entityClass the entity class
	 * @return the string array list
	 */
	private static <E> List<String[]> entities2StringArrays(List<E> entities, List<DownloadOrder> titleOrder, Class<?> entityClass) {
		if (CollectionUtils.isEmpty(titleOrder)) {
			log.error("The title is empty");
			throw new ExcelHandlerException("The download excel title must not be empty!");
		}
		
		Map<String, ExcelRelationModel> handlerRelations = handlerRelations(entityClass);
		
		List<String[]> rows = new ArrayList<>();
		
		for (E entity : entities) {
			int length = titleOrder.size();
			String[] row = new String[length];
			
			for (int i = 0; i < length; i++) {
				DownloadOrder downloadOrder = titleOrder.get(i);
				Field field = downloadOrder.getField();
				String title = downloadOrder.getTitle();
				
				Object value = ReflectionUtils.getFieldValue(entity, field);
				
				ExcelRelationModel excelRelationModel = handlerRelations.get(title);
				
				if (excelRelationModel != null) {
					Method convertMethod = excelRelationModel.getConvertMethod();
					Object convertInstance = excelRelationModel.getConvertInstance();
					
					value = ReflectionUtils.invokeMethod(convertInstance, convertMethod, value);
				}
				
				if (value != null) {
					row[i] = value.toString();
				} else {
					row[i] = "";
				}
			}
			
			rows.add(row);
		}
		return rows;
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
		
		return Arrays.stream(fields)
				.filter(f -> f.getAnnotation(DownloadIgnore.class) == null)
				.collect(Collectors.toMap(
					f -> getFieldTitleName(f), 
					f -> {
						return createExcelRelationModel(f, convertClassCache);
					})
				);
	}
	
	/**
	 * Get title name of the excel corresponding to the field
	 * 
	 * @param field the field
	 * @return the title name of the excel
	 */
	private static String getFieldTitleName(Field field) {
		DownloadExcelTitle excelTitle = field.getAnnotation(DownloadExcelTitle.class);
		if (excelTitle != null) {
			return excelTitle.title();
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
	private static Object getConvertInstance(Map<Class<?>, Object> convertClassCache, DownloadConvert convert) {
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
	private static Method getConvertMethod(DownloadConvert convert) {
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
		
		DownloadConvert convert = field.getAnnotation(DownloadConvert.class);
		
		if (convert != null) {
			Object convertInstance = getConvertInstance(convertClassCache, convert);
			Method convertMethod = getConvertMethod(convert);
			model.setConvertInstance(convertInstance);
			model.setConvertMethod(convertMethod);
		}
		
		return model;
	}

	/**
	 * Get excel order list of the entity class
	 * 
	 * @param entityClass the entity class
	 * @return the excel order list
	 */
	private static List<DownloadOrder> getTitleOrder(Class<?> entityClass) {
		Field[] fields = entityClass.getDeclaredFields();

		if (ArrayUtils.isEmpty(fields)) {
			throw new ExcelHandlerException("The entity not has any field!");
		}

		return Arrays.stream(fields)
				.filter(f -> f.getAnnotation(DownloadIgnore.class) == null)
				.map(f -> createDownloadOrder(f))
				.sorted()
				.collect(Collectors.toList());
	}
	
	/**
	 * Get excel sheet name of the entity class
	 * 
	 * @param entityClass the entity class
	 * @return the excel sheet name
	 */
	private static String getSheetName(Class<?> entityClass) {
		DownloadExcelSheet downloadExcelSheet = entityClass.getAnnotation(DownloadExcelSheet.class);
		if (downloadExcelSheet == null || StringUtils.isEmpty(downloadExcelSheet.value())) {
			return entityClass.getSimpleName();
		} else {
			return downloadExcelSheet.value();
		}
		
	}
	
	/**
	 * Create download order of field
	 * 
	 * @param field the field
	 * @return the download order
	 */
	private static DownloadOrder createDownloadOrder(Field field) {
		DownloadExcelTitle downloadExcelTitle = field.getAnnotation(DownloadExcelTitle.class);
		
		DownloadOrder downloadOrder = new DownloadOrder();
		downloadOrder.setField(field);
		
		if (downloadExcelTitle == null) {
			downloadOrder.setTitle(field.getName());
			downloadOrder.setOrder(0);
		} else {
			String title = downloadExcelTitle.title();
			if (StringUtils.isEmpty(title)) {
				title = field.getName();
			}
			int order = downloadExcelTitle.order();
			
			downloadOrder.setTitle(title);
			downloadOrder.setOrder(order);
		}
		
		
		return downloadOrder;
	}

	/**
	 * Check the parameter
	 * 
	 * @param entities the entity list
	 */
	private static <E> void checkParams(List<E> entities) {
		if (CollectionUtils.isEmpty(entities)) {
			log.error("The entity list is empty!");
			throw new ExcelHandlerException("The entity list must not be empty!");
		}
	}
}
