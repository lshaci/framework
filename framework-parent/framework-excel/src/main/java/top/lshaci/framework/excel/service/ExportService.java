package top.lshaci.framework.excel.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;

import lombok.extern.slf4j.Slf4j;
import top.lshaci.framework.excel.annotation.ExcelEntity;
import top.lshaci.framework.excel.annotation.export.ExportSheet;
import top.lshaci.framework.excel.annotation.export.ExportTitle;
import top.lshaci.framework.excel.entity.ExportSheetParam;
import top.lshaci.framework.excel.entity.ExportTitleParam;
import top.lshaci.framework.excel.exception.ExcelHandlerException;
import top.lshaci.framework.excel.utils.CellValueUtil;
import top.lshaci.framework.utils.ClassUtils;
import top.lshaci.framework.utils.ReflectionUtils;

/**
 * 导出Excel业务类
 *
 * @author lshaci
 * @since 1.0.2
 */
@Slf4j
public class ExportService {

	/**
	 * 导出对象类
	 */
	private Class<?> cls;

	/**
	 * 需要导出的数据
	 */
	private List<?> datas;

	/**
	 * Excel工作簿
	 */
	private Workbook workbook;

	/**
	 * 导出Sheet的参数
	 */
	private ExportSheetParam sheetParam;

	/**
	 * 用于生成列标题的参数
	 */
	private List<ExportTitleParam> titleParams;

	/**
	 * 用于生成内容的参数
	 */
	private List<ExportTitleParam> contentParams;

	/**
	 * 当前行号
	 */
	private int currentRowNumber;

	/**
	 * 当前的Sheet
	 */
	private Sheet sheet;

	/**
	 * Sheet标题样式
	 */
	private CellStyle sheetTitleStyle;

	/**
	 * 列标题样式
	 */
	private CellStyle columnTitleStyle;

	/**
	 * 内容单元格样式
	 */
	private CellStyle contentStyle;

	/**
	 * 根据导出对象类型、数据和Excel工作簿创建一个导出业务类
	 *
	 * @param cls 导出对象类型
	 * @param datas 导出数据集合
	 * @param workbook Excel工作簿
	 */
	public ExportService(Class<?> cls, List<?> datas, Workbook workbook, String sheetTitle) {
		this.cls = cls;
		this.datas = datas;
		this.workbook = workbook;
		int total = CollectionUtils.isEmpty(datas) ? 0 : datas.size();
		this.sheetParam = new ExportSheetParam(cls.getAnnotation(ExportSheet.class), total);
		this.sheetTitleStyle = sheetParam.getCellStyleBuilder().sheetTitleStyle(workbook, sheetParam);
		this.columnTitleStyle = sheetParam.getCellStyleBuilder().columnTitleStyle(workbook, sheetParam);
		this.contentStyle = sheetParam.getCellStyleBuilder().contentStyle(workbook, sheetParam);
		if (StringUtils.isNotBlank(sheetTitle)) {
			this.sheetParam.setTitle(sheetTitle);
		}
	}

	/**
	 * 创建Excel
	 */
	public void create() {
		handleTitleParams();

		Stream.iterate(0, n -> n + 1).limit(sheetParam.getNumber()).forEach(n -> {
			currentRowNumber = 0;
			sheetParam.getIndexBuilder().reset();
			this.sheet = workbook.createSheet(sheetParam.getName() + "_" + (n + 1));
			setColumnWidth();
			setSheetTitle();
			setColumnTitles();

			if (CollectionUtils.isEmpty(datas)) {
				return;
			}

			int size = sheetParam.getSize();
			int end = (n + 1) * size;
			end = end > datas.size() ? datas.size() : end;
			datas.subList(n * size, end).forEach(this::setRowContent);
		});
	}

	/**
	 * 设置行内容
	 *
	 * @param data 行数据
	 */
	private void setRowContent(Object data) {
		Row row = sheet.createRow(currentRowNumber);
		int addRow = 1;
		for (int i = 0; i < this.contentParams.size(); i++) {
			ExportTitleParam titleParam = this.contentParams.get(i);
			if (titleParam.isCollection()) {
				Collection<?> collectionValue = (Collection<?>) ReflectionUtils.getFieldValue(data, titleParam.getEntityField());
				if (CollectionUtils.isEmpty(collectionValue)) {
					setContentCellValue(row, titleParam.getHeight(), i, "");
				} else {
					int cn = currentRowNumber;
					Iterator<?> iterator = collectionValue.iterator();
					while (iterator.hasNext()) {
						Object value = iterator.next();
						Row nextRow = sheet.getRow(cn) == null ? sheet.createRow(cn) : sheet.getRow(cn);
						String tempValue = Objects.isNull(value) ? "" : value.toString();
						String cellValue = Objects.isNull(titleParam.getMethod()) ? tempValue : CellValueUtil.get(titleParam, value);
						setContentCellValue(nextRow, titleParam.getHeight(), i, cellValue);
						cn++;
					}
					addRow = cn - currentRowNumber;
				}
				continue;
			}

			String cellValue = CellValueUtil.get(titleParam, data);
			setContentCellValue(row, titleParam.getHeight(), i, cellValue);
		}
		mergeContentCell(addRow);
	}

	/**
	 * 设置合并单元格
	 * 
	 * @param addRow 当前行所需要增加的行数
	 */
	private void mergeContentCell(int addRow) {
		if (addRow == 1) {
			return;
		}
		for (int i = 0; i < this.contentParams.size(); i++) {
			ExportTitleParam titleParam = this.contentParams.get(i);
			if (titleParam.isCollection()) {
				log.debug("第{}列为集合数据列", i);
				continue;
			}
			
			if (titleParam.isMerge()) {
				Row row = sheet.getRow(currentRowNumber);
				Cell cell = row.getCell(i);
				String value = cell.getStringCellValue();
				cellMerge(row, contentStyle, value, currentRowNumber, currentRowNumber + addRow - 1, i, i);
			} else {
				for (int j = 0; j < addRow; j++) {
					Row row = sheet.getRow(currentRowNumber + j);
					Cell cell = row.getCell(i);
					if (cell == null) {
						String value = titleParam.isIndex() ? CellValueUtil.get(titleParam, null) : "";
						setContentCellValue(row, titleParam.getHeight(), i, value);
					}
				}
			}
		}
		currentRowNumber += addRow;
	}

	/**
	 * 设置内容单元格的值
	 *
	 * @param row 单元格所在行
	 * @param rowHeight 行高
	 * @param columnNumber 单元格所在列
	 * @param cellValue 单元格的值
	 */
	private void setContentCellValue(Row row, int rowHeight, int columnNumber, String cellValue) {
		row.setHeight((short) (rowHeight * 20));
		Cell cell = row.createCell(columnNumber);
		cell.setCellValue(cellValue);
		cell.setCellStyle(contentStyle);
	}

	/**
	 * 处理需要导出列的参数信息
	 */
	private void handleTitleParams() {
		List<ExportTitleParam> titleParams = fetchExportTitleParams(this.cls);
		titleParams.addAll(getEntities(this.cls));
		titleParams.addAll(getCollections(this.cls));

		if (this.sheetParam.isAddIndex()) {
			titleParams.add(new ExportTitleParam(this.sheetParam));
		}

		List<ExportTitleParam> groupTitleParams = new ArrayList<>();
		titleParams.stream()
				.filter(e -> StringUtils.isNotBlank(e.getGroupName()))
				.sorted()
				.collect(Collectors.groupingBy(ExportTitleParam::getGroupName))
				.forEach((k, v) -> {
					List<ExportTitleParam> children = v.stream().sorted().collect(Collectors.toList());
					ExportTitleParam titleParam = new ExportTitleParam()
							.setTitle(k).setChildren(children)
							.setOrder(v.get(0).getOrder());
					groupTitleParams.add(titleParam);
				});

		List<ExportTitleParam> singleTitleParams = titleParams.stream()
				.filter(e -> StringUtils.isBlank(e.getGroupName()))
				.collect(Collectors.toList());

		singleTitleParams.addAll(groupTitleParams);

		this.titleParams = singleTitleParams.stream()
				.sorted()
				.collect(Collectors.toList());

		this.contentParams = this.titleParams.stream()
				.flatMap(e -> {
					if (CollectionUtils.isEmpty(e.getChildren())) {
						return Arrays.asList(e).stream();
					} else {
						return e.getChildren().stream();
					}
				}).collect(Collectors.toList());
	}

	/**
	 * 根据实体类型获取需要导出的列参数集合
	 *
	 * @param cls 实体类型
	 * @return 列参数集合
	 */
	private List<ExportTitleParam> fetchExportTitleParams(Class<?> cls) {
		Map<String, ExportTitleParam> titleParamHashMap = new HashMap<>();
		getFields(cls, titleParamHashMap);
		getMethods(cls, titleParamHashMap);
		return titleParamHashMap.values().stream().collect(Collectors.toList());
	}

	/**
	 * 设置列宽
	 */
	private void setColumnWidth() {
		// set default column width
		this.sheet.setDefaultColumnWidth(12);
		// set column width
		for (int i = 0; i < this.contentParams.size(); i++) {
			ExportTitleParam titleParam = this.contentParams.get(i);
			this.sheet.setColumnWidth(i, titleParam.getWidth() * 256);
		}
	}

	/**
	 * 设置列标题
	 */
	private void setColumnTitles() {
		Row row1 = sheet.createRow(currentRowNumber);
		row1.setHeight(sheetParam.getColumnTitleHeight());

		if (this.contentParams.size() > this.titleParams.size()) {
			// 存在二级标题
			Row row2 = sheet.createRow(currentRowNumber + 1);
			row2.setHeight(sheetParam.getColumnTitleHeight());
			setColumnTitles(row1, row2);
			currentRowNumber += 2;
		} else {
			// 不存在二级标题
			for (int i = 0; i < this.titleParams.size(); i++) {
				columnTitleCell(row1, i, this.titleParams.get(i).getTitle());
			}
			currentRowNumber++;
		}
	}

	/**
	 * 设置第一行和第二行的标题信息
	 *
	 * @param row1 第一行标题的行信息
	 * @param row2 第二行标题的行信息
	 */
	private void setColumnTitles(Row row1, Row row2) {
		int cn = 0; // 列号
		for (int i = 0; i < this.titleParams.size(); i++) {
			ExportTitleParam titleParam = this.titleParams.get(i);
			List<ExportTitleParam> children = titleParam.getChildren();

			if (CollectionUtils.isEmpty(children)) {
				// 合并行
				cellMerge(row1, columnTitleStyle, titleParam.getTitle(), currentRowNumber, currentRowNumber + 1, cn, cn);
				cn++;
				continue;
			}
			// 合并列
			cellMerge(row1, columnTitleStyle, titleParam.getTitle(), currentRowNumber, currentRowNumber, cn, cn + children.size() - 1);
			// 设置二级标题
			for (int j = 0; j < children.size(); j++) {
				columnTitleCell(row2, cn++, children.get(j).getTitle());
			}
		}
	}

	/**
	 * 设置列标题单元格信息
	 *
	 * @param row 标题行信息
	 * @param cn 列号
	 * @param title 列标题
	 */
	private void columnTitleCell(Row row, int cn, String title) {
		Cell childrenCell = row.createCell(cn);
		childrenCell.setCellValue(title);
		childrenCell.setCellStyle(columnTitleStyle);
	}

	/**
	 * 设置单元格合并
	 *
	 * @param row 行信息
	 * @param cellStyle 单元格样式
	 * @param value 单元格的值
	 * @param firstRow 起始行号
	 * @param lastRow 终止行号
	 * @param firstCol 起始列号
	 * @param lastCol 终止列号
	 */
	private void cellMerge(Row row, CellStyle cellStyle, String value, int firstRow, int lastRow, int firstCol, int lastCol) {
		CellRangeAddress region = new CellRangeAddress(firstRow, lastRow, firstCol, lastCol);
		sheet.addMergedRegion(region);
		Cell cell = row.createCell(firstCol);
		cell.setCellValue(value);
		cell.setCellStyle(cellStyle);
		// 设置合并单元格的边框
		sheetParam.getCellStyleBuilder().setMergeCellBorder(region, sheet);
	}

	/**
	 * 设置Sheet标题
	 */
	private void setSheetTitle() {
		String title = sheetParam.getTitle();
		if (StringUtils.isBlank(title)) {
			return;
		}
		Row row = sheet.createRow(currentRowNumber++);
		row.setHeight(sheetParam.getTitleHeight());
		cellMerge(row, sheetTitleStyle, sheetParam.getTitle(), 0, 0, 0, this.contentParams.size() - 1);
	}

	/**
	 * 获取字段上定义的需要导出的列信息
	 *
	 * @param cls 实体类型
	 * @param titleParamMap 列信息Map
	 */
	private void getFields(Class<?> cls, Map<String, ExportTitleParam> titleParamMap) {
		if (cls == Object.class) {
			return;
		}
		Arrays.stream(cls.getDeclaredFields())
				.filter(f -> Objects.nonNull(f.getAnnotation(ExportTitle.class)))
				.filter(f -> {
					ExportTitle exportTitle = f.getAnnotation(ExportTitle.class);
					return !(exportTitle.isEntity() || exportTitle.isCollection());
				}).forEach(f -> {
					if (titleParamMap.get(f.getName()) == null) {
						titleParamMap.put(f.getName(), new ExportTitleParam(f, cls));
					}
				});
		getFields(cls.getSuperclass(), titleParamMap);
	}

	/**
	 * 获取公共方法上定义的需要导出的列信息
	 *
	 * @param cls 实体类型
	 * @param titleParamMap 列信息Map
	 */
	private void getMethods(Class<?> cls, Map<String, ExportTitleParam> titleParamMap) {
		if (cls == Object.class) {
			return;
		}

		Arrays.stream(cls.getMethods())
			.filter(m -> Objects.nonNull(m.getAnnotation(ExportTitle.class)))
			.filter(f -> {
				ExportTitle exportTitle = f.getAnnotation(ExportTitle.class);
				return !(exportTitle.isEntity() || exportTitle.isCollection());
			}).forEach(m -> {
				if (titleParamMap.get(m.getName()) == null) {
					titleParamMap.put(m.getName(), new ExportTitleParam(m));
				}
			});
		getMethods(cls.getSuperclass(), titleParamMap);
	}

	/**
	 * 获取内嵌实体中需要导出的列参数集合
	 *
	 * @param cls 导出类型
	 * @return 内嵌实体中需要导出的列参数集合
	 */
	private List<ExportTitleParam> getEntities(Class<?> cls) {
		return Arrays.stream(cls.getDeclaredFields())
				.filter(f -> Objects.nonNull(f.getAnnotation(ExportTitle.class)))
				.filter(f -> f.getAnnotation(ExportTitle.class).isEntity())
				.filter(f -> {
					ExcelEntity excelEntity = f.getType().getAnnotation(ExcelEntity.class);
					if (Objects.isNull(excelEntity)) {
						log.error("{}未使用ExcelEntity注解标记", f.getType());
						throw new ExcelHandlerException("导出实体未使用ExcelEntity注解标记");
					}
					return true;
				}).flatMap(f -> {
					ExportTitle exportTitle = f.getAnnotation(ExportTitle.class);
					return this.fetchExportTitleParams(f.getType())
							.stream()
							.map(e -> e.setEntityField(f)
									.setGroupName(exportTitle.title())
									.setOrder(exportTitle.order() + e.getOrder() / 100.0)
							);
				}).collect(Collectors.toList());
	}

	/**
	 * 获取集合字段中需要导出的列参数集合
	 *
	 * @param cls 导出类型
	 * @return 集合字段中需要导出的列参数集合
	 */
	private List<ExportTitleParam> getCollections(Class<?> cls) {
		long count = Arrays.stream(cls.getDeclaredFields())
				.filter(f -> Objects.nonNull(f.getAnnotation(ExportTitle.class)))
				.filter(f -> f.getAnnotation(ExportTitle.class).isCollection())
				.count();
		if (count > 1) {
			throw new ExcelHandlerException("导出实体类仅允许标记一个集合类型字段");
		}
		return Arrays.stream(cls.getDeclaredFields())
				.filter(f -> Objects.nonNull(f.getAnnotation(ExportTitle.class)))
				.filter(f -> f.getAnnotation(ExportTitle.class).isCollection())
				.filter(f -> {
					if (!Collection.class.isAssignableFrom(f.getType())) {
						log.error("使用ExportTitle注解标记的字段{}不是集合类型", f.getName());
						throw new ExcelHandlerException("使用ExportTitle注解标记的字段不是集合类型");
					}
					return true;
				}).flatMap(f -> {
					ExportTitle exportTitle = f.getAnnotation(ExportTitle.class);
					Class<?> fieldGenericType = ClassUtils.getFieldGenericType(f);
					ExcelEntity excelEntity = fieldGenericType.getAnnotation(ExcelEntity.class);
					if (Objects.nonNull(excelEntity)) {
						return this.fetchExportTitleParams(fieldGenericType)
								.stream()
								.map(e -> e.setEntityField(f)
										.setCollection(true)
										.setGroupName(exportTitle.title())
										.setOrder(exportTitle.order() + e.getOrder() / 100.0)
								);
					}
					return Arrays.asList(new ExportTitleParam(f, cls)
							.setCollection(true)
							.setEntityField(f)
							.setMethod(null)
						).stream();
				}).collect(Collectors.toList());

	}
}
