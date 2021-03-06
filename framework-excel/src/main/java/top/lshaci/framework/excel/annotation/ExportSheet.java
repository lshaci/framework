package top.lshaci.framework.excel.annotation;

import top.lshaci.framework.excel.builder.CellStyleBuilder;
import top.lshaci.framework.excel.builder.IndexBuilder;
import top.lshaci.framework.excel.builder.impl.DefaultCellStyleBuilder;
import top.lshaci.framework.excel.builder.impl.DefaultIndexBuilder;
import top.lshaci.framework.excel.enums.ExcelType;

import java.lang.annotation.*;

/**
 * 定义导出Excel单个Sheet的信息
 *
 * @author lshaci
 * @since 1.0.2
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExportSheet {

	/**
	 * Sheet标题
	 *
	 * @return Excel中Sheet的标题
	 */
	String title() default "";

	/**
	 * Sheet标题的高度
	 *
	 * @return Excel中Sheet的标题行高
	 */
	short titleHeight() default 36;

	/**
	 * Sheet列标题的高度
	 *
	 * @return Excel中Sheet的列标题行高
	 */
	short columnTitleHeight() default 20;

	/**
	 * Sheet名称
	 *
	 * @return Excel中Sheet的名称
	 */
	String name() default "";

	/**
	 * 序号列标题
	 *
	 * @return Excel中Sheet的序号列标题
	 */
	String indexName() default "序号";

	/**
	 * 序号列的宽度
	 *
	 * @return 序号列的宽度
	 */
	int indexWidth() default 8;

	/**
	 * Sheet数量
	 *
	 * @return 生成Excel中Sheet的数量
	 */
	int number() default 1;

	/**
	 * Sheet中使用的字体名称
	 *
	 * @return Excel中Sheet使用的字体名称
	 */
	String fontName() default "宋体";

	/**
	 * 是否添加序号列
	 *
	 * @return Excel中Sheet是否添加序号列
	 */
	boolean addIndex() default true;

	/**
	 * 是否合并序号列
	 *
	 * @return 行数据中有Collection是否合并序号列
	 */
	boolean mergeIndex() default true;

	/**
	 * 是否冻结标题行
	 *
	 * @return 是否冻结标题行
	 */
	boolean freezeTitle() default false;

	/**
	 * 生成的Excel类型
	 *
	 * @return 生成Excel的类型
	 */
	ExcelType type() default ExcelType.XLSX;

	/**
	 * Sheet中单元格样式构造类
	 *
	 * @return Sheet中单元格样式构造类
	 */
	Class<? extends CellStyleBuilder> cellStyleBuilder() default DefaultCellStyleBuilder.class;

	/**
	 * Sheet中序号列数据构造类
	 *
	 * @return Sheet中序号列数据构造类
	 */
	Class<? extends IndexBuilder> indexBuilder() default DefaultIndexBuilder.class;

}
