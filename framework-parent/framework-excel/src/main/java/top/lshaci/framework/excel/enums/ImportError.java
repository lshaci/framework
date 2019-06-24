package top.lshaci.framework.excel.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 导入时的错误信息
 * 
 * @author lshaci
 * @since 1.0.2
 */
@Getter
@AllArgsConstructor
public enum ImportError {
	/**
	 * 导入的数据为空
	 */
	DATA_IS_NULL("导入的数据为空"),
	/**
	 * 导入对象类型为空
	 */
	ENTITY_IS_NULL("导入对象类型为空"),
	/**
	 * 上传的文件不是Excel格式
	 */
	NOT_EXCEL("上传的文件不是Excel格式"),
	/**
	 * 标题行不存在
	 */
	TITLE_ROW_NOT_EXIST("标题行不存在"),
	/**
	 * Excel工作表不存在
	 */
	SHEET_NOT_EXIST("Excel工作表不存在"),
	/**
	 * 未使用正确的Excel模板
	 */
	INVALID_TEMPLATE("未使用正确的Excel模板"),
	/**
	 * 未从导入数据中获取到Excel工作簿
	 */
	WORKBOOK_IS_NULL("未从导入数据中获取到Excel工作簿"),
	/**
	 * 导入对象类型中未定义需要导入的列
	 */
	NOT_DEFINE_IMPORT_COLUMN("导入对象类型中未定义需要导入的列"),
	/**
	 * 从导入数据中获取Excel类型发生错误
	 */
	FETCH_EXCEL_TYPE_ERROR("从导入数据中获取Excel类型发生错误"),
	;
	
	private String msg;

}
