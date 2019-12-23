package top.lshaci.framework.excel.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import top.lshaci.framework.excel.annotation.ExportTitle;
import top.lshaci.framework.excel.builder.IndexBuilder;
import top.lshaci.framework.excel.exception.ExcelHandlerException;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 导出列定义的相关参数
 *
 * @author lshaci
 * @since 1.0.2
 */
@Slf4j
@Data
@NoArgsConstructor
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ExportTitleParam extends BaseTitleParam implements Comparable<ExportTitleParam> {

	/**
	 * 列顺序
	 */
	private double order;

	/**
	 * 列分组名称
	 */
	private String groupName;

	/**
	 * 列宽度
	 */
	private int width = 12;

	/**
	 * 内容行高
	 */
	private int height = 20;

	/**
	 * 分组下的二级标题信息
	 */
	private List<ExportTitleParam> children;

	/**
	 * 是否为序号列
	 */
	private boolean isIndex;

	/**
	 * 是否合并
	 */
	private boolean merge;

	/**
	 * 不合并非集合列时, 是否填充相同数据
	 */
	private boolean fillSame;

	/**
	 * 不合并集合列行时, 不填充相同数据, 可以指定填充数据
	 */
	private String fillValue;

	/**
	 * 是否为集合
	 */
	private boolean isCollection;

	/**
	 * 导出实体中的内嵌对象或集合字段, 用于获取实体中该字段的值
	 * <p>{@code @ExportTitle} 注解中<b>isEntity</b>或<b>isCollection</b>为<b>true</b></p>
	 */
	private Field entityField;

	/**
	 * 生成序号的对象
	 */
	private IndexBuilder indexBuilder;

	/**
	 * 枚举方法
	 */
	private Method enumMethod;

	/**
	 * 根据Sheet参数创建序号列信息
	 *
	 * @param sheetParam Sheet参数
	 * @return 序号列信息
	 */
	public static ExportTitleParam indexTitle(ExportSheetParam sheetParam) {
		ExportTitleParam indexTitle = new ExportTitleParam();
		indexTitle.isIndex = true;
		indexTitle.fillSame = true;
		indexTitle.merge = sheetParam.isMergeIndex();
		indexTitle.title = sheetParam.getIndexName();
		indexTitle.order = Integer.MIN_VALUE;
		indexTitle.width = sheetParam.getIndexWidth();
		indexTitle.indexBuilder = sheetParam.getIndexBuilder();
		return indexTitle;
	}

	/**
	 * 根据导出的方法创建序号列信息
	 *
	 * @param method 需要导出的方法
	 */
	public ExportTitleParam(Method method) {
		this.method = method;
		this.title = method.getName();

		ExportTitle exportTitle = method.getAnnotation(ExportTitle.class);
		if (Objects.isNull(exportTitle)) {
			return;
		}

		build(exportTitle);
		buildConvertMethod(exportTitle.convertClass(), exportTitle.convertMethod(), this.method.getReturnType());
		buildEnumMethod(method, exportTitle);
	}

	/**
	 * 根据导出的字段创建列信息
	 *
	 * @param field 需要导出的字段
	 * @param cls 需要导出的实体类
	 */
	public ExportTitleParam(Field field, Class<?> cls) {
		this.field = field;
		this.title = field.getName();
		this.method = createByField(field, cls, MethodType.GET);

		ExportTitle exportTitle = field.getAnnotation(ExportTitle.class);
		if (Objects.isNull(exportTitle)) {
			return;
		}

		build(exportTitle);
		buildConvertMethod(exportTitle.convertClass(), exportTitle.convertMethod(), this.method.getReturnType());
		buildEnumMethod(field, exportTitle);
	}

	/**
	 * 创建枚举字段需要执行的枚举中的方法
	 *
	 * @param exportTitle 字段上标记的列信息
	 */
	private void buildEnumMethod(Field field, ExportTitle exportTitle) {
		if (field.getType().isEnum() && StringUtils.isNotBlank(exportTitle.enumMethod())) {
			try {
				this.enumMethod = field.getType().getMethod(exportTitle.enumMethod());
			} catch (NoSuchMethodException | SecurityException e) {
				log.error("{}中的{}方法不存在", field.getType(), exportTitle.enumMethod());
				throw new ExcelHandlerException("枚举中方法不存在", e);
			}
		}
	}

	/**
	 * 创建返回为枚举的方法需要执行的枚举中的方法
	 *
	 * @param exportTitle 方法上标记的列信息
	 */
	private void buildEnumMethod(Method method, ExportTitle exportTitle) {
		if (method.getReturnType().isEnum() && StringUtils.isNotBlank(exportTitle.enumMethod())) {
			try {
				this.enumMethod = method.getReturnType().getMethod(exportTitle.enumMethod());
			} catch (NoSuchMethodException | SecurityException e) {
				log.error("{}中的{}方法不存在", method.getReturnType(), exportTitle.enumMethod());
				throw new ExcelHandlerException("枚举中方法不存在", e);
			}
		}
	}

	/**
	 * 根据{@code @ExportTitle}注解定义列信息
	 *
	 * @see ExportTitle
	 *
	 * @param exportTitle 导出列注解信息
	 */
	private void build(ExportTitle exportTitle) {
		this.order = exportTitle.order();
		this.merge = exportTitle.merge();
		this.fillSame = exportTitle.fillSame();
		this.fillValue = exportTitle.fillValue();
		if (StringUtils.isNotBlank(exportTitle.prefix())) {
			this.prefix = exportTitle.prefix();
		}
		if (StringUtils.isNotBlank(exportTitle.suffix())) {
			this.suffix = exportTitle.suffix();
		}
		if (StringUtils.isNotBlank(exportTitle.title())) {
			this.title = exportTitle.title();
		}
		if (exportTitle.width() > 0) {
			this.width = exportTitle.width();
		}
		if (exportTitle.height() > 0) {
			this.height = exportTitle.height();
		}
		if (StringUtils.isNotBlank(exportTitle.groupName())) {
			this.groupName = exportTitle.groupName();
		}
		if (ArrayUtils.isNotEmpty(exportTitle.replaces())) {
			this.replaceMap = Arrays.stream(exportTitle.replaces())
					.filter(r -> r.contains("__"))
					.map(r -> r.split("__"))
					.filter(a -> a.length > 1)
					.collect(Collectors.toMap(a -> a[0], a -> a[1], (k1, k2) -> k2));
		}
	}

	/**
	 * 获取序号
	 *
	 * @return 字符串类型的序号
	 */
	public String getIndexNumber() {
		if (isIndex) {
			return this.indexBuilder.get();
		}
		throw new ExcelHandlerException("当前列不是序号列");
	}

	@Override
	public int compareTo(ExportTitleParam exportTitleParam) {
		return new BigDecimal(this.order).compareTo(new BigDecimal(exportTitleParam.order));
	}


}
