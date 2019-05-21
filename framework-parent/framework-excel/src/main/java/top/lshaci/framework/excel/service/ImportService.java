package top.lshaci.framework.excel.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import lombok.extern.slf4j.Slf4j;
import top.lshaci.framework.excel.annotation.ImportTitle;
import top.lshaci.framework.excel.entity.ImportSheetParam;
import top.lshaci.framework.excel.entity.ImportTitleParam;
import top.lshaci.framework.excel.enums.ImportError;
import top.lshaci.framework.excel.exception.ImportHandlerException;
import top.lshaci.framework.excel.utils.ImportValueUtil;
import top.lshaci.framework.utils.ReflectionUtils;

/**
 * 导入Excel业务类
 *
 * @author lshaci
 * @since 1.0.2
 */
@Slf4j
public class ImportService {

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
	public ImportService(Class<?> cls, Workbook workbook, ImportSheetParam sheetParam) {
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
			.map(r -> {
				Object obj = ReflectionUtils.newInstance(this.cls);
				this.titleParams.forEach(p -> {
					Cell cell = r.getCell(p.getColNum());
					Object value = ImportValueUtil.getTargetValue(cell, p);
					verifyValue(r, p, value);
					ReflectionUtils.invokeMethod(obj, p.getMethod(), value);
				});
				return obj;
			}).collect(Collectors.toList());
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
			log.error("{}的值不能为空", titleParam.getTitle());
			throw new ImportHandlerException("第" + (row.getRowNum() + 1) + "行, 第" + (titleParam.getColNum() + 1) + "列的值为空");
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
		if (StringUtils.isNotBlank(this.sheetParam.getName())) {
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
		if (Objects.isNull(row)) {
			log.error("{}行不存在数据", this.sheetParam.getTitleRow());
			throw new ImportHandlerException(ImportError.TITLE_ROW_NOT_EXIST);
		}

		row.forEach(c -> {
			String title = ImportValueUtil.get(c);
			if (Objects.isNull(title)) {
				return;
			}

			ImportTitleParam titleParam = titleParamMap.get(title);
			if (Objects.isNull(titleParam)) {
				log.warn("[{}]未定义为需要导入的列", title);
				if (this.sheetParam.isForceRelevance()) {
					throw new ImportHandlerException(ImportError.INVALID_TEMPLATE);
				}
			} else {
				titleParamMap.remove(title);
				titleParam.setColNum(c.getColumnIndex());
				this.titleParams.add(titleParam);
			}
		});

		if (MapUtils.isNotEmpty(titleParamMap)) {
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

	private void getFields(Class<?> cls, Map<String, ImportTitleParam> titleParamMap) {
		if (cls == Object.class) {
			return;
		}
		Arrays.stream(cls.getDeclaredFields())
				.filter(f -> Objects.nonNull(f.getAnnotation(ImportTitle.class)))
				.forEach(f -> {
					String title = f.getAnnotation(ImportTitle.class).title();
					if (StringUtils.isNotBlank(title)) {
						titleParamMap.put(title, new ImportTitleParam(f, cls));
					}
				});
		getFields(cls.getSuperclass(), titleParamMap);
	}

}
