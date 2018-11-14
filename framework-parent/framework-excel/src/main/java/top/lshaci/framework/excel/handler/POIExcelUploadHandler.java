package top.lshaci.framework.excel.handler;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
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
import top.lshaci.framework.common.exception.BaseException;
import top.lshaci.framework.excel.annotation.UploadConvert;
import top.lshaci.framework.excel.annotation.UploadExcelTitle;
import top.lshaci.framework.excel.annotation.UploadVerify;
import top.lshaci.framework.excel.exception.ExcelHandlerException;
import top.lshaci.framework.excel.model.ExcelRelationModel;
import top.lshaci.framework.utils.DateUtils;
import top.lshaci.framework.utils.FileTypeUtil;
import top.lshaci.framework.utils.ReflectionUtils;
import top.lshaci.framework.utils.StreamUtils;
import top.lshaci.framework.utils.StringConverterUtils;
import top.lshaci.framework.utils.enums.FileType;

/**
 * POI excel upload handler
 * 
 * @author lshaci
 * @since 0.0.1
 * @version 0.0.4
 */
@Slf4j
public abstract class POIExcelUploadHandler {
    
    /**
     * The allow excel file type list
     */
    private static final List<FileType> ALLOW_FILE_TYPES = Arrays.asList(FileType.XLSX_DOCX, FileType.XLS_DOC, FileType.WPS, FileType.WPSX);
    
    /**
     * The allow excel file suffix list
     */
    private static final List<String> ALLOW_FILE_SUFFIX = Arrays.asList("xls", "xlsx");
    
    /**
     * Change excel file to entity list
     * 
     * @param excelFile the excel file
     * @param entityClass the entity class
     * @return the entity list
     */
    public static <E> List<E> excel2Entities(File excelFile, Class<E> entityClass) throws ExcelHandlerException {
        return excel2Entities(excelFile, 0, entityClass);
    }
    
    /**
     * Change excel file to entity list
     * 
     * @param excelFile the excel file
     * @param titleRow the title row number
     * @param entityClass the entity class
     * @return the entity list
     */
    public static <E> List<E> excel2Entities(File excelFile, int titleRow, Class<E> entityClass) throws ExcelHandlerException {
        checkParams(titleRow, excelFile, entityClass);
        
        FileType fileType = getFileType(excelFile);
        Workbook workBook = getWorkBook(excelFile, fileType);
        
        return workBook2Entities(workBook, titleRow, entityClass);
    }
    
    /**
     * Change excel file to entity list
     * 
     * @param is the excel file input stream
     * @param entityClass the entity class
     * @return the entity list
     */
    public static <E> List<E> excel2Entities(InputStream is, Class<E> entityClass) throws ExcelHandlerException {
        return excel2Entities(is, 0, entityClass);
    }
    
    /**
     * Change excel file to entity list
     * 
     * @param is the excel file input stream
     * @param titleRow the title row number
     * @param entityClass the entity class
     * @return the entity list
     */
    public static <E> List<E> excel2Entities(InputStream is, int titleRow, Class<E> entityClass) throws ExcelHandlerException {
        checkParams(titleRow, is, entityClass);
        ByteArrayOutputStream buffer = StreamUtils.copyInputStream(is);
        
        FileType fileType = getFileType(new ByteArrayInputStream(buffer.toByteArray()));
        Workbook workBook = getWorkBook(new ByteArrayInputStream(buffer.toByteArray()), fileType);
        
        return workBook2Entities(workBook, titleRow, entityClass);
    }
    
    /**
     * Change excel work book to entity list
     * 
     * @param is the excel file input stream
     * @param titleRow the title row number
     * @param entityClass the entity class
     * @return the entity list
     */
    private static <E> List<E> workBook2Entities(Workbook workBook, int titleRow, Class<E> entityClass) {
        Map<String[], ExcelRelationModel> relations = handlerRelations(entityClass);
        
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
            if (lastRowNum <= titleRow) {
            	log.warn("The sheet last row number is {}.", lastRowNum);
                continue;
            }
            
            String[] titles = getTitles(sheet, titleRow);
            verifyTitle(relations.keySet(), titles, entityClass);
            
            List<E> rowDatas = getRowDatas(sheet, titleRow, lastRowNum, titles, entityClass, relations);
            
            if (CollectionUtils.isEmpty(rowDatas)) {
                continue;
            }
            
            result.addAll(rowDatas);
        }
        
        return result;
    }
    
    /**
     * Verify the excel title
     * 
     * @param fieldTitles the set of title defined by annotations on the field
     * @param excelTitles the excel title array
     * @param entityClass the entity class
     */
    private static <E> void verifyTitle(Set<String[]> fieldTitles, String[] excelTitles, Class<E> entityClass) {
    	UploadVerify uploadVerify = entityClass.getAnnotation(UploadVerify.class);
    	if (uploadVerify != null && uploadVerify.verifyTitleLength()) {
    		Set<String> fieldTitleSet = fieldTitles.stream().flatMap(Arrays::stream).collect(Collectors.toSet());
    		Set<String> excelTitleSet = Arrays.stream(excelTitles).collect(Collectors.toSet());
    		fieldTitleSet.retainAll(excelTitleSet);
    		
    		if (CollectionUtils.isEmpty(fieldTitleSet) || fieldTitleSet.size() != excelTitleSet.size()) {
    			throw new ExcelHandlerException("Please use the correct excel file!");
    		}
		}
	}

	/**
     * Get the row data and change to entity list of the sheet
     *
     * @param sheet the sheet
     * @param titleRow the title row number
     * @param lastRowNum the all row number
     * @param titles the excel title array
     * @param entityClass the entity class
     * @param relations the excel relation model map, key is excel title, value is relation model
     * @return the entity list
     */
    private static <E> List<E> getRowDatas(Sheet sheet, int titleRow, int lastRowNum, String[] titles, 
    		Class<E> entityClass, Map<String[], ExcelRelationModel> relations) {
        List<E> rowDatas = new ArrayList<>();
        
        UploadVerify uploadVerify = entityClass.getAnnotation(UploadVerify.class);
        boolean requireCellValue = uploadVerify != null && uploadVerify.requireCellValue();
        boolean requireRowValue = uploadVerify != null && uploadVerify.requireRowValue();
        
        // Loop the sheet get row
        for (int i = titleRow + 1; i <= lastRowNum; i++) {
            Row row = sheet.getRow(i);
            
            // If the row is null, continue loop
            if (row == null) {
            	log.warn("The row[{}] is null!", i + 1);
		    	if (requireRowValue) {
					throw new ExcelHandlerException("The row[" + (i + 1) + "] is null!");
				}
                continue;
            }
            
            // Loop the row get cell
            
            final Set<Object> targetValues = new HashSet<>();
            Map<String, String> cellValueMap = getCellValues(titles, row, requireCellValue);
            
            if (cellValueMap.isEmpty()) {
                continue;
            }
            
            E entity = createEntity(entityClass, relations, targetValues, cellValueMap);

            // all field value is not null, add the new entity to row datas
            if (CollectionUtils.isNotEmpty(targetValues)) {
                rowDatas.add(entity);
            }
        }
        return rowDatas;
    }

    /**
     * Create entity and set the field value
     * 
     * @param entityClass the entity class
     * @param relations the excel relation model map, key is excel title, value is relation model
     * @param targetValues the cell target values
     * @param cellValueMap the cell value map
     * @return the entity
     */
	private static <E> E createEntity(Class<E> entityClass, Map<String[], ExcelRelationModel> relations,
			final Set<Object> targetValues, Map<String, String> cellValueMap) {
		E entity = ReflectionUtils.newInstance(entityClass);
		
		if (entity == null) {
			String msg = "New instance is error!";
			log.error(msg);
			throw new ExcelHandlerException(msg);
		}
		
		relations.forEach((titleArray, relationModel) -> {
		    Object[] cellValues = Arrays.stream(titleArray)
		            .map(t -> cellValueMap.get(t))
		            .collect(Collectors.toList())
		            .toArray(new Object[titleArray.length]);
		    
		    Object targetValue = verifyTargetValue(relationModel, cellValues);
		    
		    if (targetValue != null) {
		        targetValues.add(targetValue);
		        // set field value and get the target value
		        ReflectionUtils.setFieldValue(entity, relationModel.getTargetField(), targetValue);
		    }
		});
		return entity;
	}

    /**
     * Get cell values
     * 
     * @param titles the excel title array
     * @param row the excel row
     * @param requireCellValue verify the cell value
     * @return the cell value map
     */
	private static Map<String, String> getCellValues(String[] titles, Row row, boolean requireCellValue) {
		Map<String, String> cellValueMap = new HashMap<>();
		
		for (int j = 0; j < titles.length; j++) {
			Cell cell = row.getCell(j);
		    if (cell == null) {
		    	log.warn("The cell is null! row:{} column:{}", row.getRowNum() + 1, j + 1);
		    	if (requireCellValue) {
					throw new ExcelHandlerException("The cell is null! row:" + (row.getRowNum() + 1) + " column:" + (j + 1));
				}
		        continue;
		    }
		    
		    int columnIndex = cell.getColumnIndex();
		    String cellValue = getCellValue(cell);
		    if (StringUtils.isEmpty(cellValue)) {
		        continue;
		    }
		    
		    String title = titles[columnIndex];
		    cellValueMap.put(title, cellValue);
		}
		return cellValueMap;
	}
    
	/**
     * Verify and get the target value with cell value
     * 
     * @param relationModel the excel relation model
     * @param cellValues the cell value array
     * @return the convert value
     */
    private static Object verifyTargetValue(ExcelRelationModel relationModel, Object[] cellValues) {
    	Object targetValue = getTargetValue(relationModel, cellValues);
    	if (relationModel.isRequire() && targetValue == null) {
    		String fieldName = relationModel.getTargetField().getName();
			throw new ExcelHandlerException("The filed[" + fieldName + "] must not be null!");
		}
		return targetValue;
	}

	/**
     * Get the target value with cell value
     * 
     * @param relationModel the excel relation model
     * @param cellValues the cell value array
     * @return the convert value
     */
    private static Object getTargetValue(ExcelRelationModel relationModel, Object...cellValues) {
        if (ArrayUtils.isEmpty(cellValues)) {
            return null;
        }
        
        Method method = relationModel.getConvertMethod();
        if (method != null) {
            return getConvertValue(relationModel, cellValues);
        }
        
        Object value = cellValues[0];
        if (value == null) {
            return null;
        }
        
        try {
            return StringConverterUtils.getTargetValue(relationModel.getTargetField().getType(), value.toString());
        } catch (Exception e) {
            log.warn("StringConverterUtils converter value is error!", e);
            return null;
        }
    }
    
    /**
     * Get the convert value with cell value
     * 
     * @param relationModel the excel relation model
     * @param cellValues the cell value array
     * @return the convert value
     */
    private static Object getConvertValue(ExcelRelationModel relationModel, Object...cellValues) {
        try {
            return ReflectionUtils.invokeMethod(relationModel.getConvertInstance(), relationModel.getConvertMethod(), cellValues);
        } catch (Exception e) {
            log.error("Get convert value error! The field is: " + relationModel.getTargetField().getName(), e);
            return null;
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
     * @param titleRow the title row number
     * @return this excel sheet title array
     */
    private static String[] getTitles(Sheet sheet, int titleRow) {
        Row row = sheet.getRow(titleRow < 0 ? 0 : titleRow);
        
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
        try (
            InputStream is = new FileInputStream(excelFile);
        ) {
            return getWorkBook(is, fileType);
        } catch (Exception e) {
            log.error("Create excle work book is error!", e);
            throw new ExcelHandlerException("Create excle work book is error!");
        }
    }
    
    /**
     * Get the excel work book on the basis of file type
     * 
     * @param is the excel file input stream
     * @param fileType the excel file type
     * @return the excel work book
     */
    private static  Workbook getWorkBook(InputStream is, FileType fileType) {
        Workbook workbook = null;
        try {
            /*
             *  Constructs a work book on the basis of file type
             */
            if (FileType.XLSX_DOCX.equals(fileType) || FileType.WPSX.equals(fileType)) {
                workbook = new XSSFWorkbook(is);
            }
            if (FileType.XLS_DOC.equals(fileType) || FileType.WPS.equals(fileType)) {
                workbook = new HSSFWorkbook(is);
            }
        } catch (Exception e) {
            log.error("Create excle work book is error!", e);
            throw new ExcelHandlerException("Create excle work book is error!");
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                log.warn("Close excel file input stream error, ignore this exception!");
            }
        }
        
        Objects.requireNonNull(workbook, "The excle work book is must not be null!");
        
        return workbook;
    }

    /**
     * Get the file type and check the excel file
     * 
     * @param is the excel file input stream
     * @return the file type{@link FileType}
     */
    private static FileType getFileType(InputStream is) {
        Objects.requireNonNull(is, "The excel file input stream must not be null!");
        
        
        FileType fileType = FileTypeUtil.getType(is);
        if (ALLOW_FILE_TYPES.contains(fileType)) {
            return fileType;
        }
        throw new ExcelHandlerException("The file type is not excel!");
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
        
        FileType fileType = FileTypeUtil.getType(excelFile);
        if (ALLOW_FILE_TYPES.contains(fileType)) {
            return fileType;
        }
        throw new ExcelHandlerException("The file type is not excel!");
    }
    
    /**
     * Handler the entity field relations
     * 
     * @param entityClass the entity class
     * @return the excel relation model map, key is excel title, value is relation model
     */
    private static <E> Map<String[], ExcelRelationModel> handlerRelations(Class<E> entityClass) {
        Field[] fields = entityClass.getDeclaredFields();
        
        if (ArrayUtils.isEmpty(fields)) {
            throw new ExcelHandlerException("The entity not has any field!");
        }
        
        Map<Class<?>, Object> convertClassCache = new HashMap<>();
        
        Map<String[], ExcelRelationModel> relations = Arrays.stream(fields)
                .filter(f -> f.getAnnotation(UploadExcelTitle.class) != null)
                .collect(Collectors.toMap(
                    f -> getFieldTitleName(f), 
                    f -> createExcelRelationModel(f, convertClassCache)
                ));
        if (MapUtils.isEmpty(relations)) {
        	throw new ExcelHandlerException("The all fields does not contain @UploadExcelTitle annotation!");
		}
        
        return relations;
    }
    
    /**
     * Get title name of the excel corresponding to the field
     * 
     * @param field the field
     * @return the title name of the excel
     */
    private static String[] getFieldTitleName(Field field) {
        UploadExcelTitle excelTitle = field.getAnnotation(UploadExcelTitle.class);
        if (excelTitle != null) {
            return excelTitle.value();
        }
        throw new ExcelHandlerException("This field" + field.getName() + " does not contain @UploadExcelTitle annotation!");
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
     * @param argSize the argument size
     * @return the convert method
     */
    private static Method getConvertMethod(UploadConvert convert, int argSize) {
        String methodName = convert.method();
        Class<?> convertClass = convert.clazz();
        
        if (StringUtils.isBlank(methodName)) {
            throw new ExcelHandlerException("The convert method name must not be empty!");
        }
        
        try {
            Class<?>[] args = new Class[argSize];
            for (int i = 0; i < argSize; i++) {
                args[i] = String.class;
            }
            return convertClass.getDeclaredMethod(methodName, args);
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
        UploadExcelTitle uploadExcelTitle = field.getAnnotation(UploadExcelTitle.class);
        model.setRequire(uploadExcelTitle.require());
        
        if (convert != null) {
            int argSize = 1;
            if (uploadExcelTitle != null) {
                argSize = uploadExcelTitle.value().length;
            }
            Object convertInstance = getConvertInstance(convertClassCache, convert);
            Method convertMethod = getConvertMethod(convert, argSize);
            model.setConvertInstance(convertInstance);
            model.setConvertMethod(convertMethod);
        }
        
        return model;
    }
    
    /**
     * Check the parameter
     * 
     * @param titleRow the title row number
     * @param excelFile the excel file
     * @param entityClass the entity class
     */
    private static <E> void checkParams(int titleRow, File excelFile, Class<E> entityClass) {
        checkParams(titleRow, entityClass);
        Objects.requireNonNull(excelFile, "The excel file must not be null!");
    }
    
    /**
     * Check the parameter
     * 
     * @param titleRow the title row number
     * @param is the excel file input stream
     * @param entityClass the entity class
     */
    private static <E> void checkParams(int titleRow, InputStream is, Class<E> entityClass) {
        checkParams(titleRow, entityClass);
        Objects.requireNonNull(is, "The excel file input stream must not be null!");
    }
    
    /**
     * Check the parameter
     * @param <E>
     * 
     * @param titleRow the title row number
     * @param is the excel file input stream
     * @param entityClass the entity class
     */
    private static <E> void checkParams(int titleRow, Class<E> entityClass) {
        if (titleRow < 0) {
            throw new BaseException("The number of rows must be greater than or equal to 0!");
        }
        Objects.requireNonNull(entityClass, "The entity class must not be null!");
    }
}