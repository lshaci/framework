package top.lshaci.framework.excel.entity;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 导人Excel中Sheet的参数
 *
 * @author lshaci
 * @since 1.0.2
 */
@Data
@Accessors(chain = true)
public class ImportSheetParam {

	/**
	 * 需要导入的Sheet名称
	 */
	private String name;

	/**
	 * 需要导入的Sheet序号(优先级低于name)
	 */
	private int index = 0;
	
	/**
	 * 标题所在的行号
	 */
	private int titleRow = 0;
	
	/**
	 * 实体类型中是使用{@code @ImportTitle}标记的字段是否都为Sheet的标题
	 */
	private boolean forceEntity = true;
	
	/**
	 * Sheet中的标题是否都为实体中使用{@code @ImportTitle}标记的字段
	 */
	private boolean forceSheet= true;

}
