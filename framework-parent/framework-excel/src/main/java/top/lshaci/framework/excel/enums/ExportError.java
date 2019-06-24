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
public enum ExportError {
	/**
	 * 导出对象类型不能为空
	 */
	ENTITY_IS_NULL("导出对象类型为空"),
	/**
	 * 导出实体未使用ExcelEntity注解标记
	 */
	NOT_EXCEL_ENTITY("导出实体未使用ExcelEntity注解标记"),
	/**
	 * 导出实体类仅允许标记一个集合类型字段
	 */
	ANY_ONE_COLLECTION("导出实体类仅允许标记一个集合类型字段"),
	/**
	 * 使用ExportTitle注解标记的字段不是集合类型
	 */
	NOT_COLLECTION("使用ExportTitle注解标记的字段不是集合类型"),
	;
	
	private String msg;

}
