package top.lshaci.framework.excel.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
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

import top.lshaci.framework.excel.annotation.export.ExportEntity;
import top.lshaci.framework.excel.annotation.export.ExportSheet;
import top.lshaci.framework.excel.annotation.export.ExportTitle;
import top.lshaci.framework.excel.entity.ExportSheetParam;
import top.lshaci.framework.excel.entity.ExportTitleEntity;
import top.lshaci.framework.excel.utils.CellValueUtil;

/**
 * 导出Excel业务类
 * 
 * @author lshaci
 * @since 1.0.2
 */
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
	private List<ExportTitleEntity> titleEntities;

	/**
	 * 用于生成内容的参数
	 */
	private List<ExportTitleEntity> contentEntities;

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
	public ExportService(Class<?> cls, List<?> datas, Workbook workbook) {
		this.cls = cls;
		this.datas = datas;
		this.workbook = workbook;
		int total = CollectionUtils.isEmpty(datas) ? 0 : datas.size();
		this.sheetParam = new ExportSheetParam(cls.getAnnotation(ExportSheet.class), total);
		this.sheetTitleStyle = sheetParam.getCellStyleBuilder().sheetTitleStyle(workbook, sheetParam);
		this.columnTitleStyle = sheetParam.getCellStyleBuilder().columnTitleStyle(workbook, sheetParam);
		this.contentStyle = sheetParam.getCellStyleBuilder().contentStyle(workbook, sheetParam);
	}

	/**
	 * 创建Excel 
	 */
	public void create() {
		handleExportTitleEntities();
		titleEntities.forEach(System.err::println);
		System.err.println(this.sheetParam.getSize());
		contentEntities.forEach(System.err::println);
		
		Stream.iterate(0, n -> n + 1).limit(sheetParam.getNumber()).forEach(n -> {
			currentRowNumber = 0;
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
		Row row = sheet.createRow(currentRowNumber++);
		for (int i = 0; i < this.contentEntities.size(); i++) {
			ExportTitleEntity titleEntity = this.contentEntities.get(i);
			row.setHeight((short) (titleEntity.getHeight() * 20));
			String cellValue = CellValueUtil.get(titleEntity, data);
			
			Cell cell = row.createCell(i);
			cell.setCellValue(cellValue);
			cell.setCellStyle(contentStyle);
		}
	}

	/**
	 * 处理需要导出列的参数信息
	 */
	private void handleExportTitleEntities() {
		List<ExportTitleEntity> exportTitleEntities = fetchExportTitleEntities(this.cls);
		exportTitleEntities.addAll(getEntities(this.cls));
		if (this.sheetParam.isAddIndex()) {
			exportTitleEntities.add(new ExportTitleEntity(this.sheetParam));
		}

		List<ExportTitleEntity> groupTitleEntities = new ArrayList<>();
		exportTitleEntities.stream()
				.filter(e -> StringUtils.isNotBlank(e.getGroupName()))
				.sorted()
				.collect(Collectors.groupingBy(ExportTitleEntity::getGroupName))
				.forEach((k, v) -> {
					List<ExportTitleEntity> children = v.stream().sorted().collect(Collectors.toList());
					ExportTitleEntity exportTitleEntity = new ExportTitleEntity()
							.setTitle(k).setChildren(children)
							.setOrder(v.get(0).getOrder());
					groupTitleEntities.add(exportTitleEntity);
				});

		List<ExportTitleEntity> singleTitleEntities = exportTitleEntities.stream()
				.filter(e -> StringUtils.isBlank(e.getGroupName()))
				.collect(Collectors.toList());

		singleTitleEntities.addAll(groupTitleEntities);

		this.titleEntities = singleTitleEntities.stream()
				.sorted()
				.collect(Collectors.toList());

		this.contentEntities = this.titleEntities.stream()
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
	private List<ExportTitleEntity> fetchExportTitleEntities(Class<?> cls) {
		Map<String, ExportTitleEntity> exportTitleEntityMap = new HashMap<>();
		getFields(cls, exportTitleEntityMap);
		getMethods(cls, exportTitleEntityMap);
		return exportTitleEntityMap.values().stream().collect(Collectors.toList());
	}

	/**
	 * 设置列宽
	 */
	private void setColumnWidth() {
		// set default column width
		this.sheet.setDefaultColumnWidth(12);
		// set column width
		for (int i = 0; i < this.contentEntities.size(); i++) {
			ExportTitleEntity exportTitleEntity = this.contentEntities.get(i);
			this.sheet.setColumnWidth(i, exportTitleEntity.getWidth() * 256);
		}
	}

	/**
	 * 设置列标题
	 */
	private void setColumnTitles() {
		Row row1 = sheet.createRow(currentRowNumber);
		Row row2 = sheet.createRow(currentRowNumber + 1);
		row1.setHeight(sheetParam.getColumnTitleHeight());
		row2.setHeight(sheetParam.getColumnTitleHeight());

		int cn = 0;
		for (int i = 0; i < this.titleEntities.size(); i++) {
			ExportTitleEntity titleEntity = this.titleEntities.get(i);
			List<ExportTitleEntity> children = titleEntity.getChildren();

			if (CollectionUtils.isEmpty(children)) {
				CellRangeAddress region = new CellRangeAddress(currentRowNumber, currentRowNumber + 1, cn, cn);
				sheet.addMergedRegion(region);
				Cell cell = row1.createCell(cn++);
				cell.setCellValue(titleEntity.getTitle());
				cell.setCellStyle(columnTitleStyle);
				sheetParam.getCellStyleBuilder().setMergeCellBorder(region, sheet);
				continue;
			}

			CellRangeAddress region = new CellRangeAddress(currentRowNumber, currentRowNumber, cn, cn + children.size() - 1);
			sheet.addMergedRegion(region);
			Cell cell = row1.createCell(cn);
			cell.setCellValue(titleEntity.getTitle());
			cell.setCellStyle(columnTitleStyle);
			sheetParam.getCellStyleBuilder().setMergeCellBorder(region, sheet);

			for (int j = 0; j < children.size(); j++) {
				ExportTitleEntity childrenTitleEntity = children.get(j);
				Cell childrenCell = row2.createCell(cn++);
				childrenCell.setCellValue(childrenTitleEntity.getTitle());
				childrenCell.setCellStyle(columnTitleStyle);
			}

		}
		currentRowNumber += 2;
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
		int size = this.contentEntities.size();
		CellRangeAddress region = new CellRangeAddress(0, 0, 0, size - 1);
		sheet.addMergedRegion(region);
		Cell cell = row.createCell(0);
		cell.setCellValue(sheetParam.getTitle());
		cell.setCellStyle(sheetTitleStyle);
		// 设置合并单元格的边框
		sheetParam.getCellStyleBuilder().setMergeCellBorder(region, sheet);
	}

	/**
	 * 获取字段上定义的需要导出的列信息
	 * 
	 * @param cls 实体类型
	 * @param exportTitleEntityMap 列信息Map
	 */
	private void getFields(Class<?> cls, Map<String, ExportTitleEntity> exportTitleEntityMap) {
		if (cls == Object.class) {
			return;
		}
		Arrays.stream(cls.getDeclaredFields())
				.filter(f -> Objects.nonNull(f.getAnnotation(ExportTitle.class)))
				.forEach(f -> {
					if (exportTitleEntityMap.get(f.getName()) == null) {
						exportTitleEntityMap.put(f.getName(), new ExportTitleEntity(f, cls));
					}
				});
		getFields(cls.getSuperclass(), exportTitleEntityMap);
	}

	/**
	 * 获取公共方法上定义的需要导出的列信息
	 * 
	 * @param cls 实体类型
	 * @param exportTitleEntityMap 列信息Map
	 */
	private void getMethods(Class<?> cls, Map<String, ExportTitleEntity> exportTitleEntityMap) {
		if (cls == Object.class) {
			return;
		}
		
		Arrays.stream(cls.getMethods())
			.filter(m -> Objects.nonNull(m.getAnnotation(ExportTitle.class)))
			.forEach(m -> {
				if (exportTitleEntityMap.get(m.getName()) == null) {
					exportTitleEntityMap.put(m.getName(), new ExportTitleEntity(m));
				}
			});
		getMethods(cls.getSuperclass(), exportTitleEntityMap);
	}
	
	/**
	 * 获取内嵌实体中需要导出的列参数集合
	 * 
	 * @param cls 导出类型
	 * @return 内嵌实体中需要导出的列参数集合
	 */
	private List<ExportTitleEntity> getEntities(Class<?> cls) {
		return Arrays.stream(cls.getDeclaredFields())
		.filter(f -> Objects.nonNull(f.getAnnotation(ExportEntity.class)))
		.flatMap(f -> {
			ExportEntity exportEntity = f.getAnnotation(ExportEntity.class);
			return this.fetchExportTitleEntities(f.getType())
					.stream()
					.map(e -> e.setEntity(true).setEntityField(f)
							.setGroupName(exportEntity.title())
							.setOrder(exportEntity.order() + e.getOrder() / 100.0)
					);
			
		}).collect(Collectors.toList());
	}
}
