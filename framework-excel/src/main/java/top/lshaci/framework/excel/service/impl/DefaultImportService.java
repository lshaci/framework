package top.lshaci.framework.excel.service.impl;

import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import top.lshaci.framework.excel.annotation.ImportTitle;
import top.lshaci.framework.excel.entity.ImportSheetParam;
import top.lshaci.framework.excel.entity.ImportTitleParam;
import top.lshaci.framework.excel.enums.ImportError;
import top.lshaci.framework.excel.exception.ImportHandlerException;
import top.lshaci.framework.excel.helper.ImportValueHelper;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 导入Excel业务类
 *
 * @author lshaci
 * @since 1.0.2
 */
@Slf4j
public class DefaultImportService {

	/**
	 * 导入对象类型
	 */
	private Class<?> cls;

	/**
	 * Excel工作表
	 */
	private Sheet sheet;

	/**
	 * 导入工作表参数
	 */
	private ImportSheetParam sheetParam;

	/**
	 * 导入列参数信息集合
	 */
	private List<ImportTitleParam> titleParams;

	/**
	 * 根据导入实体类型、Excel工作簿、Excel工作表参数创建导入业务对象
	 *
	 * @param cls 导入实体类型
	 * @param workbook Excel工作簿
	 * @param sheetParam Excel工作表参数
	 */
	public DefaultImportService(Class<?> cls, Workbook workbook, ImportSheetParam sheetParam) {
		this.cls = cls;
		this.sheetParam = Objects.isNull(sheetParam) ? new ImportSheetParam() : sheetParam;
		this.sheet = fetchSheet(workbook);
		this.titleParams = new ArrayList<>();
	}

	/**
	 * 获取解析后的数据集合
	 *
	 * @return 解析后的集合数据
	 */
	public List<?> fetch() {
		handleTitleParams();

		return Stream.iterate(this.sheetParam.getTitleRow() + 1, n -> n + 1)
			.limit(this.sheet.getLastRowNum())
			.map(this.sheet::getRow)
			.filter(Objects::nonNull)
			.map(this::row2Obj)
			.filter(Objects::nonNull) // 过滤空行值
			.collect(Collectors.toList());
	}

	/**
	 * 将行数据转换为对象
	 *
	 * @param row 行数据
	 * @return 行数据对应的对象
	 */
	private Object row2Obj(Row row) {
		Object obj = ReflectUtil.newInstance(this.cls);
		List<Object> values = new ArrayList<>();
		this.titleParams.forEach(p -> {
			Cell cell = row.getCell(p.getColNum());
			Object value = ImportValueHelper.getTargetValue(cell, p);
			verifyValue(row, p, value);
			ReflectUtil.invoke(obj, p.getMethod(), value);
			values.add(value);
		});
		// 对象中所有的值为空, 则返回空, 在上一层中过滤掉
		return values.stream().filter(Objects::isNull).count() == values.size() ? null : obj;
	}

	/**
	 * 实体类中字段的值是否必须验证
	 *
	 * @param row 行数据
	 * @param titleParam 列参数信息
	 * @param value 字段的值
	 */
	private void verifyValue(Row row, ImportTitleParam titleParam, Object value) {
		if (Objects.isNull(value) && titleParam.isRequired()) {
			int rn = row.getRowNum() + 1;
			int cn = titleParam.getColNum() + 1;
			log.error("[{}]的值不能为空; 第{}行, 第{}列的值解析后为空", titleParam.getTitle(), rn, cn);
			throw new ImportHandlerException("第" + rn + "行, 第" + cn + "列的值解析后为空");
		}
	}

	/**
	 * 获取Excel工作表
	 *
	 * @param workbook Excel工作薄
	 * @return 指定的Excel工作表
	 */
	private Sheet fetchSheet(Workbook workbook) {
		Sheet sheet = null;
		if (StrUtil.isNotBlank(this.sheetParam.getName())) {
			sheet = workbook.getSheet(this.sheetParam.getName());
		} else {
			sheet = workbook.getSheetAt(this.sheetParam.getIndex());
		}
		if (Objects.isNull(sheet)) {
			throw new ImportHandlerException(ImportError.SHEET_NOT_EXIST);
		}
		return sheet;
	}

	/**
	 * 处理列参数信息
	 */
	private void handleTitleParams() {
		Map<String, ImportTitleParam> titleParamMap = fetchTitleParams();
		Row row = this.sheet.getRow(this.sheetParam.getTitleRow());
		if (Objects.isNull(row) || row.getLastCellNum() == -1) {
			log.error("{}行不存在数据", this.sheetParam.getTitleRow());
			throw new ImportHandlerException(ImportError.TITLE_ROW_NOT_EXIST);
		}

		row.forEach(c -> {
			String title = ImportValueHelper.get(c);
			if (Objects.isNull(title)) {
				return;
			}

			ImportTitleParam titleParam = titleParamMap.get(title);
			if (Objects.isNull(titleParam)) {
				log.warn("[{}]未定义为需要导入的列", title);
				if (this.sheetParam.isForceEntity()) {
					throw new ImportHandlerException(ImportError.INVALID_TEMPLATE);
				}
			} else {
				titleParamMap.remove(title);
				titleParam.setColNum(c.getColumnIndex());
				this.titleParams.add(titleParam);
			}
		});

		if (MapUtils.isNotEmpty(titleParamMap) && this.sheetParam.isForceSheet()) {
			log.warn("{}列在Excel文件中不存在", titleParamMap.keySet());
			throw new ImportHandlerException(ImportError.INVALID_TEMPLATE);
		}
	}

	/**
	 * 获取列参数信息
	 *
	 * @return 列参数信息
	 */
	private Map<String, ImportTitleParam> fetchTitleParams() {
		Map<String, ImportTitleParam> titleParamMap = new HashMap<>();
		getFields(this.cls, titleParamMap);
		if (MapUtils.isEmpty(titleParamMap)) {
			throw new ImportHandlerException(ImportError.NOT_DEFINE_IMPORT_COLUMN);
		}
		return titleParamMap;
	}

	/**
	 * 获取字段上的{@code @ImportTitle}注解信息, 生成列参数信息
	 *
	 * @param cls 导入实体类型
	 * @param titleParamMap 列参数信息
	 */
	private void getFields(Class<?> cls, Map<String, ImportTitleParam> titleParamMap) {
		if (cls == Object.class) {
			return;
		}
		Arrays.stream(cls.getDeclaredFields())
				.filter(f -> Objects.nonNull(f.getAnnotation(ImportTitle.class)))
				.forEach(f -> {
					String title = f.getAnnotation(ImportTitle.class).title();
					if (StrUtil.isNotBlank(title)) {
						titleParamMap.put(title, new ImportTitleParam(f, cls));
					}
				});
		getFields(cls.getSuperclass(), titleParamMap);
	}

}
