package top.lshaci.framework.excel.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;
import top.lshaci.framework.excel.annotation.ExportSheet;
import top.lshaci.framework.excel.builder.CellStyleBuilder;
import top.lshaci.framework.excel.builder.IndexBuilder;
import top.lshaci.framework.excel.builder.impl.DefaultCellStyleBuilder;
import top.lshaci.framework.excel.builder.impl.DefaultIndexBuilder;
import top.lshaci.framework.utils.ReflectionUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 导出Excel中Sheet的参数
 *
 * @author lshaci
 * @since 1.0.2
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class ExportSheetParam {

	/**
	 * Sheet标题
	 */
	private String title;

	/**
	 * Sheet标题行高
	 */
	private short titleHeight = 36 * 20;

	/**
	 * 列标题行高
	 */
	private short columnTitleHeight = 24 * 20;

	/**
	 * Sheet名称
	 */
	private String name;

	/**
	 * 需要导出的Sheet数量
	 */
	private Integer number = 1;

	/**
	 * 是否添加序号列
	 */
	private boolean addIndex = true;

	/**
	 * 是否合并序号列
	 */
	private boolean mergeIndex = true;
	
	/**
	 * 是否冻结标题
	 */
	private boolean freezeTitle = false;

	/**
	 * 序号列标题
	 */
	private String indexName = "序号";

	/**
	 * 序号列宽度
	 */
	private int indexWidth = 8;

	/**
	 * Sheet中字体名称
	 */
	private String fontName = "宋体";

	/**
	 * 单个Sheet中需要导出的数据条数
	 */
	private int size;

	/**
	 * 单元格样式构造对象
	 */
	private CellStyleBuilder cellStyleBuilder = new DefaultCellStyleBuilder();

	/**
	 * 序号列数据构造对象
	 */
	private IndexBuilder indexBuilder = new DefaultIndexBuilder();

	/**
	 * 其它自定义的参数信息
	 */
	private Map<String, Object> otherParamMap = new HashMap<>();

	/**
	 * 根据实体类上的{@code @ExportSheet}注解和需要导出的数据总条数创建Sheet的参数
	 *
	 * @see ExportSheet
	 *
	 * @param exportSheet 实体类上的ExportSheet注解信息
	 */
	public static ExportSheetParam build(ExportSheet exportSheet) {
		ExportSheetParam param = new ExportSheetParam();
		if (Objects.isNull(exportSheet)) {
			return param;
		}
		param.addIndex = exportSheet.addIndex();
		param.mergeIndex = exportSheet.mergeIndex();
		param.freezeTitle = exportSheet.freezeTitle();
		param.cellStyleBuilder = ReflectionUtils.newInstance(exportSheet.cellStyleBuilder());
		param.indexBuilder = ReflectionUtils.newInstance(exportSheet.indexBuilder());

		if (StringUtils.isNotBlank(exportSheet.fontName())) {
			param.fontName = exportSheet.fontName();
		}
		if (StringUtils.isNotBlank(exportSheet.indexName())) {
			param.indexName = exportSheet.indexName();
		}
		if (StringUtils.isNotBlank(exportSheet.title())) {
			param.title = exportSheet.title();
		}
		if (StringUtils.isNotBlank(exportSheet.name())) {
			param.name = exportSheet.name();
		}
		if (exportSheet.indexWidth() > 0) {
			param.indexWidth = exportSheet.indexWidth();
		}
		if (exportSheet.number() > 0) {
			param.number = exportSheet.number();
		}
		if (exportSheet.titleHeight() > 0) {
			param.titleHeight = (short) (exportSheet.titleHeight() * 20);
		}
		if (exportSheet.columnTitleHeight() > 0) {
			param.columnTitleHeight = (short) (exportSheet.columnTitleHeight() * 20);
		}

		return param;
	}

	public ExportSheetParam setSizeAndNumber(int total) {
		if (total > 0 && total / this.number > 0) {
			Double size = Math.ceil(total / (this.number * 1.0));
			this.size = size.intValue();
		} else {
			this.size = total;
			this.number = 1;
		}
		return this;
	}

	public ExportSheetParam putOtherParam(String key, Object value) {
		this.otherParamMap.put(key, value);
		return this;
	}

	public Object getOtherParam(String key) {
		return this.otherParamMap.get(key);
	}

}
