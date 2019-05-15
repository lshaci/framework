package top.lshaci.framework.excel.entity;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang3.StringUtils;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import top.lshaci.framework.excel.annotation.export.ExportTitle;
import top.lshaci.framework.excel1.exception.ExcelHandlerException;

/**
 * 导出列定义的相关参数
 *
 * @author lshaci
 * @since 1.0.2
 */
@Data
@Slf4j
@NoArgsConstructor
@Accessors(chain = true)
public class ExportTitleEntity implements Comparable<ExportTitleEntity> {

	/**
	 * 列标题
	 */
	private String title;

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
	 * 导出数据需要拼接的前缀
	 */
	private String prefix;

	/**
	 * 导出数据需要拼接的后缀
	 */
	private String suffix;

	/**
	 * 分组下的二级标题信息
	 */
	private List<ExportTitleEntity> children;

	/**
	 * 是否为序号列
	 */
	private boolean isIndex;

	/**
	 * 该列数据是否从字段获取
	 */
	private boolean isField;

	/**
	 * 列数据字段
	 */
	private Field field;

	/**
	 * 该列数据是否从方法获取
	 */
	private boolean isMethod;

	/**
	 * 列数据方法
	 */
	private Method method;

	/**
	 * 该列数据是否从实体获取
	 */
	private boolean isEntity;

	/**
	 * 该列数据的实体字段
	 */
	private Field entityField;

	/**
	 * 该列数据是否需要转换
	 */
	private boolean needConvert;

	/**
	 * 生成序号的对象
	 */
	private AtomicInteger index;

	/**
	 * 数据转换类
	 */
	private Class<?> convertClass;

	/**
	 * 数据转换方法
	 */
	private Method convertMethod;

	/**
	 * 根据Sheet参数创建序号列信息
	 *
	 * @param sheetParam Sheet参数
	 */
	public ExportTitleEntity(ExportSheetParam sheetParam) {
		this.isIndex = true;
		this.title = sheetParam.getIndexName();
		this.order = Integer.MIN_VALUE;
		this.width = 8;
		this.index = new AtomicInteger(1);
	}

	/**
	 * 根据导出的字段创建序号列信息
	 *
	 * @param field 需要导出的字段
	 * @param cls 需要导出的实体类
	 */
	public ExportTitleEntity(Field field, Class<?> cls) {
		this.field = field;
		this.isField = true;
		this.title = field.getName();
		this.method = createByField(field, cls);
		
		ExportTitle exportTitle = field.getAnnotation(ExportTitle.class);
		if (Objects.isNull(exportTitle)) {
			return;
		}

		build(exportTitle);

		if (Void.class != exportTitle.convertClass() && StringUtils.isNotBlank(exportTitle.convertMethod())) {
			this.needConvert = true;
			this.convertClass = exportTitle.convertClass();
			try {
				this.convertMethod = this.convertClass.getMethod(exportTitle.convertMethod(), this.field.getType());
			} catch (NoSuchMethodException | SecurityException e) {
				log.error("{}.{}转换方法不存在", exportTitle.convertClass(), exportTitle.convertMethod());
				throw new ExcelHandlerException("转换方法不存在", e);
			}
		}
	}
	
	/**
	 * 根据字段和类创建字段的公共get方法
	 * 
	 * @param field 字段
	 * @param cls 类
	 * @return get方法
	 */
	private Method createByField(Field field, Class<?> cls) {
		String name = field.getName();
		if (field.getType() == boolean.class) {
			throw new ExcelHandlerException("禁止使用boolean类型");
		}
		String methodName = "get" + name.substring(0, 1).toUpperCase() + name.substring(1);
		try {
			return cls.getMethod(methodName);
		} catch (NoSuchMethodException | SecurityException e) {
			log.error("字段{}.{}的get方法不存在", cls, name);
			throw new ExcelHandlerException("Get方法不存在", e);
		}
		
	}

	/**
	 * 根据导出的方法创建序号列信息
	 *
	 * @param method 需要导出的方法
	 */
	public ExportTitleEntity(Method method) {
		this.method = method;
		this.isMethod = true;
		this.title = method.getName();

		ExportTitle exportTitle = method.getAnnotation(ExportTitle.class);
		if (Objects.isNull(exportTitle)) {
			return;
		}

		build(exportTitle);
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
	}

	@Override
	public int compareTo(ExportTitleEntity exportTitleEntity) {
		return new BigDecimal(this.order).compareTo(new BigDecimal(exportTitleEntity.order));
	}


}
