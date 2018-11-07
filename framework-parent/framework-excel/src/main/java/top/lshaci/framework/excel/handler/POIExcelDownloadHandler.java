package top.lshaci.framework.excel.handler;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
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
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import lombok.extern.slf4j.Slf4j;
import top.lshaci.framework.excel.annotation.DownloadAllField;
import top.lshaci.framework.excel.annotation.DownloadConvert;
import top.lshaci.framework.excel.annotation.DownloadExcelFirstTitle;
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
 * @version 0.0.4
 */
@Slf4j
public abstract class POIExcelDownloadHandler {
	
	/**
	 * The current row number of write data
	 */
	private static final ThreadLocal<Integer> CURRENT_ROW_NUMBER = new ThreadLocal<>();
	
	/**
	 * The excel font name
	 */
	private static final ThreadLocal<String> FONT_NAME = new ThreadLocal<>();
	
	/**
	 * The default font name
	 */
	private static final String DEFAULT_FONT_NAME = "宋体";

	/**
	 * Change excel file to entity list
	 * 
	 * @param entities the entity list
	 * @return the excel work book output stream
	 */
	public static <E> ByteArrayOutputStream entities2Excel(List<E> entities) {
		checkParams(entities);
		
		Class<?> entityClass = entities.get(0).getClass();
		
		List<DownloadOrder> titleOrder = getTitleOrder(entityClass);
		if (CollectionUtils.isEmpty(titleOrder)) {
			log.error("The excel title is empty!");
			throw new ExcelHandlerException("The excel title must not be empty!");
		}
		
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
			
			setColumnWidth(titleOrder, sheet);
			
			// set current row number
			CURRENT_ROW_NUMBER.set(0);
			
			// set first title
			setFirstTitle(workbook, sheet, entityClass, titleOrder.size());
			
			// set column titles
			setColumnTitles(workbook, sheet, titleOrder);
			
			// set sheet content
			setSheetRows(workbook, sheet, rowDatas);
			
			workbook.write(os);
			return os;
		} catch (Exception e) {
			log.error("Parse entity list error", e);
			throw new ExcelHandlerException("Convert entity list to excel file is error!", e);
		}
	}

    /**
     * Set column width
     * 
     * @param titleOrder the titles
     * @param sheet the excel sheet of the work book
     */
    private static void setColumnWidth(List<DownloadOrder> titleOrder, XSSFSheet sheet) {
        // set default column width
        sheet.setDefaultColumnWidth(12);
        // set first column width
        sheet.setColumnWidth(0, 1500);
        // set column width
        for (int i = 0; i < titleOrder.size(); i++) {
        	DownloadOrder downloadOrder = titleOrder.get(i);
        	int columnWidth = downloadOrder.getColumnWidth();
        	sheet.setColumnWidth(i + 1, columnWidth <= 0 ? 12 * 256 : columnWidth * 256);
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
		int currentRowNumber = CURRENT_ROW_NUMBER.get();
		XSSFCellStyle contentStyle = createContentStyle(workbook);
		for (int i = 0; i < rowDatas.size(); i++) {
			XSSFRow row = sheet.createRow(currentRowNumber++);
			row.setHeight((short) 320);
			String[] rowData = rowDatas.get(i);
			for (int j = 0; j <= rowData.length; j++) {
				XSSFCell cell = row.createCell(j);
				String value = j == 0 ? (i + 1) + "" : rowData[j - 1];
				cell.setCellValue(value);
				cell.setCellStyle(contentStyle);
			}
			CURRENT_ROW_NUMBER.set(currentRowNumber);
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
		font.setFontName(FONT_NAME.get());
		// set font
		style.setFont(font);

		return style;
	}
	
	/**
	 * Set excel first title
	 * 
	 * @param workbook the excel work book
	 * @param sheet the excel sheet of the work book
	 * @param entityClass the entity class
	 * @param size the column title size
	 */
	private static void setFirstTitle(XSSFWorkbook workbook, XSSFSheet sheet, Class<?> entityClass, int size) {
		DownloadExcelFirstTitle firstTitle = entityClass.getAnnotation(DownloadExcelFirstTitle.class);
		if (firstTitle != null) {
			String firstTitleName = firstTitle.name();
			if (StringUtils.isNoneBlank(firstTitleName)) {
				int currentRowNumber = CURRENT_ROW_NUMBER.get();
				XSSFRow row = sheet.createRow(currentRowNumber++);
				row.setHeight(firstTitle.height());
				CellRangeAddress region = new CellRangeAddress(0, 0, 0, size);
				sheet.addMergedRegion(region);
				XSSFCell cell = row.createCell(0);
				cell.setCellValue(firstTitleName);
				cell.setCellStyle(createFirstTitleStyle(workbook));
				
				// set merge cell border
				setMergeCellBorder(region, sheet);
				CURRENT_ROW_NUMBER.set(currentRowNumber );
			}
		}
	}
	
	/**
	 * Set merge cell border
	 * 
	 * @param region the merger cell region
	 * @param sheet the excel sheet of the work book
	 */
	private static void setMergeCellBorder(CellRangeAddress region, XSSFSheet sheet) {
		RegionUtil.setBorderTop(BorderStyle.THIN, region, sheet);
		RegionUtil.setBorderRight(BorderStyle.THIN, region, sheet);
		RegionUtil.setBorderBottom(BorderStyle.THIN, region, sheet);
		RegionUtil.setBorderLeft(BorderStyle.THIN, region, sheet);
	}
	
	/**
	 * Create first title style
	 * 
	 * @param workbook the excel work book
	 * @return the row content style
	 */
	private static XSSFCellStyle createFirstTitleStyle(XSSFWorkbook workbook) {
		XSSFCellStyle style = workbook.createCellStyle();
		
		// 使用 \n 换行设置为true
		style.setWrapText(true);
		
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
		font.setFontHeightInPoints((short) 20);
		font.setBold(true);
		font.setFontName(FONT_NAME.get());
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
		int currentRowNumber = CURRENT_ROW_NUMBER.get();
		XSSFRow row = sheet.createRow(currentRowNumber++);
		row.setHeight((short) 360);
		XSSFCellStyle columnTitleStyle = createColumnTitleStyle(workbook);
		for (int i = 0; i <= titleOrder.size(); i++) {
			XSSFCell cell = row.createCell(i);
			String value = i == 0 ? "序号" : titleOrder.get(i - 1).getTitle();
			cell.setCellValue(value);
			cell.setCellStyle(columnTitleStyle);
		}
		CURRENT_ROW_NUMBER.set(currentRowNumber);
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
		font.setFontName(FONT_NAME.get());
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
	 * Get excel sheet name of the entity class
	 * 
	 * @param entityClass the entity class
	 * @return the excel sheet name
	 */
	private static String getSheetName(Class<?> entityClass) {
		DownloadExcelSheet downloadExcelSheet = entityClass.getAnnotation(DownloadExcelSheet.class);
		String sheetName = entityClass.getSimpleName();
		if (downloadExcelSheet != null) {
			sheetName = downloadExcelSheet.sheetName();
			String fontName = downloadExcelSheet.fontName();
			fontName = StringUtils.isNotBlank(fontName) ? fontName.trim() : DEFAULT_FONT_NAME;
			FONT_NAME.set(fontName);
			
			sheetName = StringUtils.isNotBlank(sheetName) ? sheetName.trim() : entityClass.getSimpleName();
		}
		return sheetName;
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
				
				ExcelRelationModel relationModel = handlerRelations.get(title);
				
				if (relationModel != null) {
					value = getConvertValue(value, relationModel);
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
	 * Get the convert value with cell value
	 * 
	 * @param value the field value
	 * @param relationModel the excel relation model
	 * @return the convert value
	 */
	private static Object getConvertValue(Object value, ExcelRelationModel relationModel) {
		Method method = relationModel.getConvertMethod();
		
		if (method != null) {
			Object instance = relationModel.getConvertInstance();
			Object convertValue = ReflectionUtils.invokeMethod(instance, method, value);
			return convertValue == null ? "" : convertValue.toString();
		}
		
		return value;
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
		boolean downloadAllField = entityClass.getAnnotation(DownloadAllField.class) != null;
		
		return Arrays.stream(fields)
				.filter(f -> f.getAnnotation(DownloadIgnore.class) == null)
				.filter(f -> downloadAllField || f.getAnnotation(DownloadExcelTitle.class) != null)
				.collect(Collectors.toMap(
					f -> getFieldTitleName(f), 
					f -> createExcelRelationModel(f, convertClassCache))
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
			Method convertMethod = getConvertMethod(convert, field.getType());
			model.setConvertInstance(convertInstance);
			model.setConvertMethod(convertMethod);
		}
		
		return model;
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
	 * @param clazz the field type
	 * @return the convert method
	 */
	private static Method getConvertMethod(DownloadConvert convert, Class<?> clazz) {
		String methodName = convert.method();
		Class<?> convertClass = convert.clazz();
		
		if (StringUtils.isBlank(methodName)) {
			throw new ExcelHandlerException("The convert method name must not be empty!");
		}
		
		try {
			return convertClass.getDeclaredMethod(methodName, clazz);
		} catch (NoSuchMethodException | SecurityException e) {
			String msg = "Get the convert method is error!";
			log.error(msg, e);
			throw new ExcelHandlerException(msg);
		}
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

		boolean downloadAllField = entityClass.getAnnotation(DownloadAllField.class) != null;

		return Arrays.stream(fields)
				.filter(f -> f.getAnnotation(DownloadIgnore.class) == null)
				.filter(f -> downloadAllField || f.getAnnotation(DownloadExcelTitle.class) != null)
				.map(f -> createDownloadOrder(f, downloadAllField))
				.sorted()
				.collect(Collectors.toList());
	}
	
	/**
	 * Create download order of field
	 * 
	 * @param field            the field
	 * @param downloadAllField if true download all field
	 * @return the download order
	 */
	private static DownloadOrder createDownloadOrder(Field field, boolean downloadAllField) {
		DownloadExcelTitle downloadExcelTitle = field.getAnnotation(DownloadExcelTitle.class);
		
		DownloadOrder downloadOrder = new DownloadOrder(field);
		
		if (downloadExcelTitle != null) {
			String title = downloadExcelTitle.title();
			if (StringUtils.isBlank(title)) {
				title = field.getName();
			}
			int order = downloadExcelTitle.order();
			int columnWidth = downloadExcelTitle.columnWidth();
			
			downloadOrder.setTitle(title);
			downloadOrder.setOrder(order);
			downloadOrder.setColumnWidth(columnWidth);
		} else {
			downloadOrder.setTitle(field.getName());
			downloadOrder.setOrder(0);
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
