package top.lshaci.framework.excel.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import top.lshaci.framework.excel.annotation.ExcelEntity;
import top.lshaci.framework.excel.annotation.ExportTitle;
import top.lshaci.framework.excel.entity.ExportSheetParam;
import top.lshaci.framework.excel.entity.ExportTitleParam;
import top.lshaci.framework.excel.enums.ExportError;
import top.lshaci.framework.excel.exception.ExportHandlerException;
import top.lshaci.framework.excel.service.ExportService;
import top.lshaci.framework.utils.ClassUtils;
import top.lshaci.framework.utils.ReflectionUtils;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static top.lshaci.framework.excel.entity.ExportTitleParam.indexTitle;
import static top.lshaci.framework.excel.service.impl.ExportValueUtil.fetch;

/**
 * Excel 导出业务实现
 *
 * @author lshaci
 * @since 1.0.6
 */
@Slf4j
public class DefaultExportService implements ExportService {

    /**
     * 导出对象类
     */
    protected Class<?> cls;

    /**
     * 需要导出的数据
     */
    protected List<?> datas;

    /**
     * Excel工作簿
     */
    protected Workbook workbook;

    /**
     * 导出Sheet的参数
     */
    protected ExportSheetParam sheetParam;

    /**
     * 用于生成列标题的参数
     */
    protected List<ExportTitleParam> titleParams;

    /**
     * 用于生成内容的参数
     */
    protected List<ExportTitleParam> contentParams;

    /**
     * 当前行号<b>current row number</b>
     */
    protected int crn;

    /**
     * 当前的Sheet
     */
    protected Sheet sheet;

    /**
     * Sheet标题样式
     */
    protected CellStyle sheetTitleStyle;

    /**
     * 列标题样式
     */
    protected CellStyle columnTitleStyle;

    /**
     * 内容单元格样式
     */
    protected CellStyle contentStyle;

    /**
     * 集合列信息, 用于获取集合字段数据
     */
    protected ExportTitleParam collectionTitleParam;

    @Override
    public Workbook create(Class<?> cls, List<?> datas, ExportSheetParam sheetParam) {
        init(cls, datas, sheetParam);
        handleTitleParams();
        fillData();
        return workbook;
    }

    /**
     * 填充表格数据
     */
    protected void fillData() {
        Stream.iterate(1, n -> n + 1).limit(sheetParam.getNumber()).forEach(n -> {
            crn = 0;
            sheetParam.getIndexBuilder().reset();
            this.sheet = sheet(this.workbook, this.sheetParam, n);

            setColumnWidth();
            afterSetColumnWidth();

            setSheetTitle();
            afterSetSheetTitle();

            setColumnTitles();
            afterSetColumnTitles();

            if (CollectionUtils.isEmpty(datas)) {
                return;
            }

            if (this.sheetParam.isFreezeTitle()) {
                this.sheet.createFreezePane(0, crn, 0, crn);
            }

            int size = sheetParam.getSize();
            int end = n * size > datas.size() ? datas.size() : n * size;
            datas.subList((n - 1) * size, end).forEach(this::setRowContent);

            afterSetRowContent();
        });
    }

    /**
     * 设置行内容
     *
     * @param data 行数据
     */
    protected void setRowContent(Object data) {
        if (nonNull(this.collectionTitleParam)) {
            handleHasCollection(data);
        } else {
            Row row = sheet.createRow(crn++);
            AtomicInteger i = new AtomicInteger();
            this.contentParams.forEach(t -> setContentCellValue(row, t.getHeight(), i.getAndIncrement(), fetch(t, data)));
        }
    }

    /**
     * 处理实体中有集合时单元格数据
     *
     * @param data 行数据
     */
    protected void handleHasCollection(Object data) {
        Row row = sheet.createRow(crn);
        Collection<?> collectionValue = (Collection<?>) ReflectionUtils.getFieldValue(data, this.collectionTitleParam.getEntityField());
        for (int i = 0; i < this.contentParams.size(); i++) {
            ExportTitleParam titleParam = this.contentParams.get(i);
            if (CollectionUtils.isEmpty(collectionValue)) {
                String cellValue = titleParam.isCollection() ? "" : fetch(titleParam, data);
                setContentCellValue(row, titleParam.getHeight(), i, cellValue);
                continue;
            }
            int crn = this.crn;
            for (Object value : collectionValue) {
                String cellValue;
                // 如果当前列是集合列
                if (titleParam.isCollection()) {
                    cellValue = isNull(value) ? "" : value.toString();
                    // 如果集合列中的泛型未标记为@ExcelEntity, 则直接使用集合中元素的值
                    cellValue = isNull(titleParam.getMethod()) ? cellValue : fetch(titleParam, value);
                } else {
                    cellValue = fetch(titleParam, data);
                }
                Row nextRow = sheet.getRow(crn) == null ? sheet.createRow(crn) : sheet.getRow(crn);
                // 非集合列, 集合数据大于1条, 指定合并行
                if (!titleParam.isCollection() && collectionValue.size() > 1 && titleParam.isMerge()) {
                    cellMerge(row, contentStyle, cellValue, this.crn, this.crn + collectionValue.size() - 1, i, i);
                    break;
                }
                // 非集合列, 不合并行, 不填充相同数据, 单元格设置值为指定的填充数据
                if (!(titleParam.isCollection() || titleParam.isFillSame() || crn == this.crn)) {
                    cellValue = titleParam.getFillValue();
                }
                setContentCellValue(nextRow, titleParam.getHeight(), i, cellValue);
                crn++;
            }
        }
        crn += (CollectionUtils.isEmpty(collectionValue) ? 1 : collectionValue.size());
    }

    /**
     * 设置内容单元格的值
     *
     * @param row 单元格所在行
     * @param rowHeight 行高
     * @param columnNumber 单元格所在列
     * @param cellValue 单元格的值
     */
    protected void setContentCellValue(Row row, int rowHeight, int columnNumber, String cellValue) {
        row.setHeight((short) (rowHeight * 20));
        Cell cell = row.createCell(columnNumber);
        cell.setCellValue(cellValue);
        cell.setCellStyle(contentStyle);
    }

    /**
     * 设置列标题
     */
    protected void setColumnTitles() {
        Row row1 = sheet.createRow(crn);
        row1.setHeight(sheetParam.getColumnTitleHeight());

        if (this.contentParams.size() > this.titleParams.size()) {
            // 存在二级标题
            Row row2 = sheet.createRow(crn + 1);
            row2.setHeight(sheetParam.getColumnTitleHeight());
            setColumnTitles(row1, row2);
            crn += 2;
        } else {
            // 不存在二级标题
            AtomicInteger i = new AtomicInteger();
            this.titleParams.forEach(t -> columnTitleCell(row1, i.getAndIncrement(), t.getTitle()));
            crn++;
        }
    }

    /**
     * 设置第一行和第二行的标题信息
     *
     * @param row1 第一行标题的行信息
     * @param row2 第二行标题的行信息
     */
    protected void setColumnTitles(Row row1, Row row2) {
        final AtomicInteger cn = new AtomicInteger(); // 列号
        for (ExportTitleParam titleParam : this.titleParams) {
            List<ExportTitleParam> children = titleParam.getChildren();

            if (CollectionUtils.isEmpty(children)) {
                int n = cn.getAndIncrement();
                // 合并行
                cellMerge(row1, columnTitleStyle, titleParam.getTitle(), crn, crn + 1, n, n);
                continue;
            }
            // 合并列
            int n = cn.get();
            cellMerge(row1, columnTitleStyle, titleParam.getTitle(), crn, crn, n, n + children.size() - 1);
            // 设置二级标题
            children.forEach(c -> columnTitleCell(row2, cn.getAndIncrement(), c.getTitle()));
        }
    }

    /**
     * 设置列标题单元格信息
     *
     * @param row 标题行信息
     * @param cn 列号
     * @param title 列标题
     */
    protected void columnTitleCell(Row row, int cn, String title) {
        Cell childrenCell = row.createCell(cn);
        childrenCell.setCellValue(title);
        childrenCell.setCellStyle(columnTitleStyle);
    }

    /**
     * 设置Sheet标题
     */
    protected void setSheetTitle() {
        String title = sheetParam.getTitle();
        if (isBlank(title)) {
            return;
        }
        Row row = sheet.createRow(crn++);
        row.setHeight(sheetParam.getTitleHeight());
        cellMerge(row, sheetTitleStyle, title, 0, 0, 0, this.contentParams.size() - 1);
    }

    /**
     * 设置列宽
     */
    protected void setColumnWidth() {
        // set default column width
        this.sheet.setDefaultColumnWidth(12);
        // set column width
        AtomicInteger i = new AtomicInteger();
        this.contentParams.forEach(t -> this.sheet.setColumnWidth(i.getAndIncrement(), t.getWidth() * 256));
    }

    /**
     * 初始化传入的参数
     *
     * @param cls 导出对象类型
     * @param datas 导出数据集合
     * @param sheetParam Sheet的参数
     */
    protected void init(Class<?> cls, List<?> datas, ExportSheetParam sheetParam) {
        this.cls = cls;
        this.datas = datas;
        int total = CollectionUtils.isEmpty(datas) ? 0 : datas.size();
        this.sheetParam = sheetParam.setSizeAndNumber(total);
        this.workbook = workbook(cls, total);
        handleStyle();
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
    protected void cellMerge(Row row, CellStyle cellStyle, String value, int firstRow, int lastRow, int firstCol, int lastCol) {
        CellRangeAddress region = new CellRangeAddress(firstRow, lastRow, firstCol, lastCol);
        sheet.addMergedRegion(region);
        Cell cell = row.createCell(firstCol);
        cell.setCellValue(value);
        cell.setCellStyle(cellStyle);
        // 设置合并单元格的边框
        sheetParam.getCellStyleBuilder().setMergeCellBorder(region, sheet);
    }

    /**
     * 处理需要导出列的参数信息
     */
    protected void handleTitleParams() {
        List<ExportTitleParam> titleParams = fetchTitleParams(this.cls);
        titleParams.addAll(getEntities(this.cls));
        titleParams.addAll(getCollections(this.cls));

        if (this.sheetParam.isAddIndex()) {
            titleParams.add(indexTitle(this.sheetParam));
        }

        List<ExportTitleParam> groupTitleParams = new ArrayList<>();
        titleParams.stream()
                .filter(e -> StringUtils.isNotBlank(e.getGroupName()))
                .sorted()
                .collect(groupingBy(ExportTitleParam::getGroupName))
                .forEach((k, v) -> {
                    List<ExportTitleParam> children = v.stream().sorted().collect(toList());
                    ExportTitleParam titleParam = new ExportTitleParam()
                            .setChildren(children).setOrder(v.get(0).getOrder());
                    titleParam.setTitle(k);
                    groupTitleParams.add(titleParam);
                });

        this.titleParams = Stream.concat(
                        titleParams.stream().filter(e -> isBlank(e.getGroupName())),
                        groupTitleParams.stream()
                ).filter(columnFilter())
                .sorted().collect(toList());

        this.contentParams = this.titleParams.stream()
                .flatMap(e -> {
                    if (CollectionUtils.isEmpty(e.getChildren())) {
                        return Stream.of(e);
                    } else {
                        return e.getChildren().stream();
                    }
                }).collect(toList());

        this.collectionTitleParam = this.contentParams.stream()
                .filter(ExportTitleParam::isCollection)
                .findFirst()
                .orElse(null);
    }

    /**
     * 根据实体类型获取需要导出的列参数集合
     *
     * @param cls 实体类型
     * @return 列参数集合
     */
    protected List<ExportTitleParam> fetchTitleParams(Class<?> cls) {
        Map<String, ExportTitleParam> titleParamMap = new HashMap<>();
        getFields(cls, titleParamMap);
        getMethods(cls, titleParamMap);
        return new ArrayList<>(titleParamMap.values());
    }

    /**
     * 获取字段上定义的需要导出的列信息
     *
     * @param cls 实体类型
     * @param titleParamMap 列信息Map
     */
    protected void getFields(Class<?> cls, Map<String, ExportTitleParam> titleParamMap) {
        if (cls == Object.class) {
            return;
        }
        Arrays.stream(cls.getDeclaredFields())
                .filter(f -> nonNull(f.getAnnotation(ExportTitle.class)))
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
    protected void getMethods(Class<?> cls, Map<String, ExportTitleParam> titleParamMap) {
        if (cls == Object.class) {
            return;
        }

        Arrays.stream(cls.getMethods())
                .filter(m -> nonNull(m.getAnnotation(ExportTitle.class)))
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
    protected List<ExportTitleParam> getEntities(Class<?> cls) {
        return Arrays.stream(cls.getDeclaredFields())
                .filter(f -> nonNull(f.getAnnotation(ExportTitle.class)))
                .filter(f -> f.getAnnotation(ExportTitle.class).isEntity())
                .filter(f -> {
                    ExcelEntity excelEntity = f.getType().getAnnotation(ExcelEntity.class);
                    if (isNull(excelEntity)) {
                        log.error("{}未使用ExcelEntity注解标记", f.getType());
                        throw new ExportHandlerException(ExportError.NOT_EXCEL_ENTITY);
                    }
                    return true;
                }).flatMap(f -> {
                    ExportTitle exportTitle = f.getAnnotation(ExportTitle.class);
                    return this.fetchTitleParams(f.getType())
                            .stream()
                            .map(e -> e.setEntityField(f)
                                    .setGroupName(exportTitle.title())
                                    .setOrder(exportTitle.order() + e.getOrder() / 100.0)
                            );
                }).collect(toList());
    }

    /**
     * 获取集合字段中需要导出的列参数集合
     *
     * @param cls 导出类型
     * @return 集合字段中需要导出的列参数集合
     */
    protected List<ExportTitleParam> getCollections(Class<?> cls) {
        long count = Arrays.stream(cls.getDeclaredFields())
                .filter(f -> nonNull(f.getAnnotation(ExportTitle.class)))
                .filter(f -> f.getAnnotation(ExportTitle.class).isCollection())
                .count();
        if (count > 1) {
            throw new ExportHandlerException(ExportError.ONLY_ONE_COLLECTION);
        }
        return Arrays.stream(cls.getDeclaredFields())
                .filter(f -> nonNull(f.getAnnotation(ExportTitle.class)))
                .filter(f -> f.getAnnotation(ExportTitle.class).isCollection())
                .filter(f -> {
                    if (!Collection.class.isAssignableFrom(f.getType())) {
                        log.error("使用ExportTitle注解标记的字段{}不是集合类型", f.getName());
                        throw new ExportHandlerException(ExportError.NOT_COLLECTION);
                    }
                    return true;
                }).flatMap(f -> {
                    ExportTitle exportTitle = f.getAnnotation(ExportTitle.class);
                    Class<?> fieldGenericType = ClassUtils.getFieldGenericType(f);
                    ExcelEntity excelEntity = fieldGenericType.getAnnotation(ExcelEntity.class);
                    if (nonNull(excelEntity)) {
                        return this.fetchTitleParams(fieldGenericType)
                                .stream()
                                .map(e -> e.setEntityField(f)
                                        .setCollection(true)
                                        .setGroupName(exportTitle.title())
                                        .setOrder(exportTitle.order() + e.getOrder() / 100.0)
                                );
                    }
                    ExportTitleParam titleParam = new ExportTitleParam(f, cls)
                            .setCollection(true)
                            .setEntityField(f); // 用于获取集合字段的值
                    titleParam.setMethod(null); // 设为null, 则填充单元格数据时, 直接使用集合中元素的toString()值
                    return Stream.of(titleParam);
                }).collect(toList());
    }

    /**
     * 处理表格中需要使用到的样式
     */
    protected void handleStyle() {
        String fontName = sheetParam.getFontName();
        this.sheetTitleStyle = sheetParam.getCellStyleBuilder().sheetTitleStyle(workbook, fontName);
        this.columnTitleStyle = sheetParam.getCellStyleBuilder().columnTitleStyle(workbook, fontName);
        this.contentStyle = sheetParam.getCellStyleBuilder().contentStyle(workbook, fontName);
    }

}
