package top.lshaci.framework.excel.entity;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 导出Excel中Sheet的参数
 *
 * @author lshaci
 * @since 1.0.2
 */
@Data
@Accessors(chain = true)
public class ImportSheetParam {

	private String name;

	private int index = 0;
	
	private int titleRow = 0;
	
	/**
	 * 是否强制关联所有的列
	 */
	private boolean forceRelevance = true;

	public ImportSheetParam() {
	}


}
