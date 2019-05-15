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
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;

import top.lshaci.framework.excel.annotation.export.ExportEntity;
import top.lshaci.framework.excel.annotation.export.ExportSheet;
import top.lshaci.framework.excel.annotation.export.ExportTitle;
import top.lshaci.framework.excel.entity.ExportSheetParam;
import top.lshaci.framework.excel.entity.ExportTitleEntity;
import top.lshaci.framework.excel.utils.CellValueUtil;

public class ExportService {

	private Class<?> cls;

	private List<?> datas;

	private Workbook workbook;

	private ExportSheetParam sheetParam;

	private List<ExportTitleEntity> titleEntities;

	private List<ExportTitleEntity> contentEntities;

	private int currentRowNumber;

	public ExportService(Class<?> cls, List<?> datas, Workbook workbook) {
		this.cls = cls;
		this.datas = datas;
		this.workbook = workbook;
		int total = CollectionUtils.isEmpty(datas) ? 0 : datas.size();
		this.sheetParam = new ExportSheetParam(cls.getAnnotation(ExportSheet.class), total);
	}

	public void createSheet() {
		handleExportTitleEntities();
		titleEntities.forEach(System.err::println);
		System.err.println(this.sheetParam.getSize());
		contentEntities.forEach(System.err::println);
		
		Stream.iterate(0, n -> n + 1).limit(sheetParam.getNumber()).forEach(n -> {
			currentRowNumber = 0;
			Sheet sheet = workbook.createSheet(sheetParam.getName() + "_" + (n + 1));
			setColumnWidth(sheet);
			setSheetTitle(sheet);
			setColumnTitles(sheet);

			if (CollectionUtils.isEmpty(datas)) {
				return;
			}
			
			int size = sheetParam.getSize();
			int end = (n + 1) * size;
			end = end > datas.size() ? datas.size() : end;
			List<?> subDatas = datas.subList(n * size, end);
			
			CellStyle contentStyle = sheetParam.getCellStyleBuilder().contentStyle(workbook, sheetParam);
			subDatas.forEach(d -> {
				setRowContent(sheet, contentStyle, d);
			});
		});
	}
	
	private void setRowContent(Sheet sheet, CellStyle contentStyle, Object data) {
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

	private void handleExportTitleEntities() {
		List<ExportTitleEntity> exportTitleEntities = fetchExportTitleEntities(this.cls);
		exportTitleEntities.addAll(getEntities(this.cls));
		if (sheetParam.isAddIndex()) {
			exportTitleEntities.add(new ExportTitleEntity(sheetParam));
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

	private List<ExportTitleEntity> fetchExportTitleEntities(Class<?> cls) {
		Map<String, ExportTitleEntity> exportTitleEntityMap = new HashMap<>();
		getFields(cls, exportTitleEntityMap);
		getMethods(cls, exportTitleEntityMap);
		return exportTitleEntityMap.values().stream().collect(Collectors.toList());
	}

	private void setColumnWidth(Sheet sheet) {
		// set default column width
		sheet.setDefaultColumnWidth(12);
		// set column width
		for (int i = 0; i < this.contentEntities.size(); i++) {
			ExportTitleEntity exportTitleEntity = this.contentEntities.get(i);
			sheet.setColumnWidth(i, exportTitleEntity.getWidth() * 256);
		}
	}

	private void setColumnTitles(Sheet sheet) {
		Row row1 = sheet.createRow(currentRowNumber);
		Row row2 = sheet.createRow(currentRowNumber + 1);
		row1.setHeight(sheetParam.getColumnTitleHeight());
		row2.setHeight(sheetParam.getColumnTitleHeight());

		CellStyle columnTitleStyle = sheetParam.getCellStyleBuilder().columnTitleStyle(workbook, sheetParam);
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
				// 设置合并单元格的边框
				setMergeCellBorder(region, sheet);
				continue;
			}

			CellRangeAddress region = new CellRangeAddress(currentRowNumber, currentRowNumber, cn, cn + children.size() - 1);
			sheet.addMergedRegion(region);
			Cell cell = row1.createCell(cn);
			cell.setCellValue(titleEntity.getTitle());
			cell.setCellStyle(columnTitleStyle);
			// 设置合并单元格的边框
			setMergeCellBorder(region, sheet);

			for (int j = 0; j < children.size(); j++) {
				ExportTitleEntity childrenTitleEntity = children.get(j);
				Cell childrenCell = row2.createCell(cn++);
				childrenCell.setCellValue(childrenTitleEntity.getTitle());
				childrenCell.setCellStyle(columnTitleStyle);
			}

		}
		currentRowNumber += 2;
	}

	private void setSheetTitle(Sheet sheet) {
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
		cell.setCellStyle(sheetParam.getCellStyleBuilder().sheetTitleStyle(workbook, sheetParam));
		// 设置合并单元格的边框
		setMergeCellBorder(region, sheet);
	}

	private void setMergeCellBorder(CellRangeAddress region, Sheet sheet) {
		RegionUtil.setBorderTop(BorderStyle.THIN, region, sheet);
		RegionUtil.setBorderRight(BorderStyle.THIN, region, sheet);
		RegionUtil.setBorderBottom(BorderStyle.THIN, region, sheet);
		RegionUtil.setBorderLeft(BorderStyle.THIN, region, sheet);
	}


	private void getFields(Class<?> cls, Map<String, ExportTitleEntity> exportTitleEntityMap) {
		if (cls == Object.class) {
			return;
		}
		Arrays.stream(cls.getDeclaredFields())
				.filter(f -> Objects.nonNull(f.getAnnotation(ExportTitle.class)))
				.forEach(f -> {
					if (exportTitleEntityMap.get(f.getName()) == null) {
						exportTitleEntityMap.put(f.getName(), new ExportTitleEntity(f));
					}
				});
		getFields(cls.getSuperclass(), exportTitleEntityMap);
	}

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
