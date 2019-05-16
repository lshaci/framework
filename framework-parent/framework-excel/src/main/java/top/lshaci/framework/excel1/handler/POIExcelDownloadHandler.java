package top.lshaci.framework.excel1.handler;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.*;
import top.lshaci.framework.excel1.annotation.*;
import top.lshaci.framework.excel1.exception.ExcelHandlerException;
import top.lshaci.framework.excel1.model.DownloadOrder;
import top.lshaci.framework.excel1.model.ExcelRelationModel;
import top.lshaci.framework.utils.ReflectionUtils;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * POI excel1 download handler<br><br>
 *
 * <b>0.0.4: </b>Add method setColumnWidth; Change exception message to chinese; Add incoming output stream
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
	private final static ThreadLocal<Integer> CURRENT_ROW_NUMBER = new ThreadLocal<>();

	/**
	 * The excel1 font name
	 */
	private final static ThreadLocal<String> FONT_NAME = new ThreadLocal<>();

	/**
	 * The default font name
	 */
	private final static String DEFAULT_FONT_NAME = "宋体";

    /**
     * Change excel1 file to entity list
     *
     * @param entities the entity list
     * @return the excel1 work book output stream
     */
    public static <E> ByteArrayOutputStream entities2Excel(List<E> entities) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        entities2Excel(entities, os);
        return os;
    }

	/**
	 * Change excel1 file to entity list
	 *
	 * @param entities the entity list
     * @param os the output stream
	 */
	public static <E> void entities2Excel(List<E> entities, OutputStream os) {
		checkParams(entities);

		Class<?> entityClass = entities.get(0).getClass();

		List<DownloadOrder> titleOrders = getTitleOrder(entityClass);

		List<String[]> rowDatas = entities2StringArrays(entities, titleOrders, entityClass);

		try (
				OutputStream os_ = os;
				XSSFWorkbook workbook = new XSSFWorkbook();
		) {
			String sheetName = getSheetName(entityClass);

			XSSFSheet sheet = workbook.createSheet(sheetName);

			setColumnWidth(titleOrders, sheet);

			// set current row number
			CURRENT_ROW_NUMBER.set(0);

			// set first title
			setFirstTitle(workbook, sheet, entityClass, titleOrders.size());

			// set column titles
			setColumnTitles(workbook, sheet, titleOrders);

			// set sheet content
			setSheetRows(workbook, sheet, rowDatas);

			workbook.write(os_);
		} catch (Exception e) {
			log.error("Parse entity list error", e);
			throw new ExcelHandlerException("Convert entity list to excel1 file is error!", e);
		}
	}

	/**
	 * Set column width
	 *
	 * @param titleOrder the titles
	 * @param sheet the excel1 sheet of the work book
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
	 * Set excel1 sheet row content
	 *
	 * @param workbook the excel1 work book
	 * @param sheet the excel1 sheet of the work book
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
	 * Create row content builder
	 *
	 * @param workbook the excel1 work book
	 * @return the row content builder
	 */
	private static XSSFCellStyle createContentStyle(XSSFWorkbook workbook) {
		XSSFCellStyle style = workbook.createCellStyle();
		// set fill fore ground color
		style.setFillForegroundColor(new XSSFColor(Color.WHITE));
		// builder.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
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
	 * Set excel1 first title
	 *
	 * @param workbook the excel1 work book
	 * @param sheet the excel1 sheet of the work book
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
	 * @param sheet the excel1 sheet of the work book
	 */
	private static void setMergeCellBorder(CellRangeAddress region, XSSFSheet sheet) {
		RegionUtil.setBorderTop(BorderStyle.THIN, region, sheet);
		RegionUtil.setBorderRight(BorderStyle.THIN, region, sheet);
		RegionUtil.setBorderBottom(BorderStyle.THIN, region, sheet);
		RegionUtil.setBorderLeft(BorderStyle.THIN, region, sheet);
	}

	/**
	 * Create first title builder
	 *
	 * @param workbook the excel1 work book
	 * @return the row content builder
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
	 * @param workbook the excel1 work book
	 * @param sheet the excel1 sheet of the work book
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
	 * Create column title builder
	 *
	 * @param workbook the excel1 work book
	 * @return the column title builder
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
	 * Set cell builder border
	 *
	 * @param style the cell builder
	 */
	private static void setBorder(XSSFCellStyle style) {
		style.setBorderBottom(BorderStyle.THIN);
		style.setBorderLeft(BorderStyle.THIN);
		style.setBorderRight(BorderStyle.THIN);
		style.setBorderTop(BorderStyle.THIN);
	}

	/**
	 * Get excel1 sheet name of the entity class
	 *
	 * @param entityClass the entity class
	 * @return the excel1 sheet name
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

		if (CollectionUtils.isEmpty(rows)) {
			log.error("The content is empty!");
			throw new ExcelHandlerException("导出对象解析内容为空");
		}

		return rows;
	}

	/**
	 * Get the convert value with cell value
	 *
	 * @param value the field value
	 * @param relationModel the excel1 relation model
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
	 * @return the excel1 relation model map, key is excel1 title, value is relation model
	 */
	private static <E> Map<String, ExcelRelationModel> handlerRelations(Class<E> entityClass) {
		Field[] fields = entityClass.getDeclaredFields();

		if (ArrayUtils.isEmpty(fields)) {
			throw new ExcelHandlerException("导出对象中未定义字段");
		}

		Map<Class<?>, Object> convertClassCache = new HashMap<>();
		boolean downloadAllField = entityClass.getAnnotation(DownloadAllField.class) != null;

		return Arrays.stream(fields)
				.filter(f -> f.getAnnotation(DownloadIgnore.class) == null)
				.filter(f -> downloadAllField || f.getAnnotation(DownloadExcelTitle.class) != null)
				.collect(Collectors.toMap(
					POIExcelDownloadHandler::getFieldTitleName,
					f -> createExcelRelationModel(f, convertClassCache))
				);
	}

	/**
	 * Get title name of the excel1 corresponding to the field
	 *
	 * @param field the field
	 * @return the title name of the excel1
	 */
	private static String getFieldTitleName(Field field) {
		DownloadExcelTitle excelTitle = field.getAnnotation(DownloadExcelTitle.class);
		if (excelTitle != null) {
			return excelTitle.title();
		}
		return field.getName();
	}

	/**
	 * Create excel1 relation model by target field
	 *
	 * @param field the target field
	 * @param convertClassCache the convert class cache
	 * @return the excel1 relation model
	 */
	private static ExcelRelationModel createExcelRelationModel(Field field, Map<Class<?>, Object> convertClassCache) {
		ExcelRelationModel model = new ExcelRelationModel(field);

		DownloadConvert convert = field.getAnnotation(DownloadConvert.class);

		if (convert != null) {
			Object convertInstance = POIExcelBaseHandler.getConvertInstance(convertClassCache, convert.clazz());
			Method convertMethod = getConvertMethod(convert, field.getType());
			model.setConvertInstance(convertInstance);
			model.setConvertMethod(convertMethod);
		}

		return model;
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
			throw new ExcelHandlerException("目标类型转换方法名为空");
		}

		try {
			return convertClass.getDeclaredMethod(methodName, clazz);
		} catch (NoSuchMethodException | SecurityException e) {
			String msg = "Get the convert method is error!";
			log.error(msg, e);
			throw new ExcelHandlerException("获取目标类型转换方法错误");
		}
	}

	/**
	 * Get excel1 order list of the entity class
	 *
	 * @param entityClass the entity class
	 * @return the excel1 order list
	 */
	private static List<DownloadOrder> getTitleOrder(Class<?> entityClass) {
		Field[] fields = entityClass.getDeclaredFields();

		if (ArrayUtils.isEmpty(fields)) {
			throw new ExcelHandlerException("导出对象中未定义字段");
		}

		boolean downloadAllField = entityClass.getAnnotation(DownloadAllField.class) != null;

		List<DownloadOrder> titleOrders = Arrays.stream(fields)
				.filter(f -> f.getAnnotation(DownloadIgnore.class) == null)
				.filter(f -> downloadAllField || f.getAnnotation(DownloadExcelTitle.class) != null)
				.map(f -> createDownloadOrder(f))
				.sorted()
				.collect(Collectors.toList());

		if (CollectionUtils.isEmpty(titleOrders)) {
			log.error("The excel1 title is empty!");
			throw new ExcelHandlerException("导出对象中未标记需要导出的字段");
		}

		return titleOrders;
	}

	/**
	 * Create download order of field
	 *
	 * @param field the field
	 * @return the download order
	 */
	private static DownloadOrder createDownloadOrder(Field field) {
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
			throw new ExcelHandlerException("无数据可导出");
		}
	}
}

