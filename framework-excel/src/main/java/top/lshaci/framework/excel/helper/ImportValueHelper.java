package top.lshaci.framework.excel.helper;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import top.lshaci.framework.excel.entity.ImportTitleParam;
import top.lshaci.framework.utils.DateUtils;
import top.lshaci.framework.utils.StringConverterUtils;

import java.util.Objects;

/**
 * 获取单元格值的工具类
 *
 * @author lshaci
 * @since 1.0.2
 */
@Slf4j
public class ImportValueHelper extends BaseValueUtil {

	/**
	 * 获取单元格的值
	 *
	 * @param cell 单元格
	 * @return 单元格中的值
	 */
	public static String get(Cell cell) {
		if (Objects.isNull(cell)) {
			return null;
		}

		String value;
		switch (cell.getCellTypeEnum()) {
			case STRING: value = cell.getStringCellValue(); break;
			case BOOLEAN: value = String.valueOf(cell.getBooleanCellValue()); break;
			case FORMULA: value = cell.getCellFormula(); break;
			case NUMERIC:
				if (DateUtil.isCellDateFormatted(cell)) {
					value = DateUtils.formatMsecDate(cell.getDateCellValue());
					break;
				}
			default:
				cell.setCellType(CellType.STRING);
				value = cell.getStringCellValue();
			break;
		}

		return Objects.isNull(value) ? null : value.trim();
	}

	/**
	 * 根据单元格和列参数信息获取字段的值
	 *
	 * @param cell 单元格
	 * @param titleParam 列参数信息
	 * @return 字段的值
	 */
	public static Object getTargetValue(Cell cell, ImportTitleParam titleParam) {
		String cellValue = getCellValue(cell, titleParam);
		if (Objects.isNull(cellValue)) {
			return null;
		}

		// 转换方法存在, 则使用转换方法对原始值进行处理
		if (Objects.nonNull(titleParam.getConvertMethod())) {
			return getConvertValue(titleParam.getConvertClass(), titleParam.getConvertMethod(), cellValue);
		} else {
			return StringConverterUtils.getTargetValue(titleParam.getField().getType(), cellValue);
		}
	}

	/**
	 * 获取单元格的值
	 *
	 * @param cell 单元格
	 * @return 单元格中的值
	 */
	private static String getCellValue(Cell cell, ImportTitleParam titleParam) {
		String value = get(cell);
		if (Objects.isNull(value)) {
			return null;
		}

		// 去掉前缀和后缀
		value = replace(value, titleParam);

		// 替换信息存在, 则对原始值进行替换处理
		if (MapUtils.isNotEmpty(titleParam.getReplaceMap())) {
			value = titleParam.getReplaceMap().get(value);
			if (Objects.isNull(value)) {
				log.warn("替换后单元格的值为空, 不作其它处理");
				return null;
			}
		}

		return value;
	}

	/**
	 * 去掉前缀和后缀
	 *
	 * @param value 单元格的值
	 * @param titleParam 列参数信息
	 * @return 去掉前缀和后缀后的值
	 */
	private static String replace(String value, ImportTitleParam titleParam) {
		String regex = "^" + titleParam.getPrefix() + "(.*)" + titleParam.getSuffix() + "$";
		return value.replaceAll(regex, "$1");
	}


}
