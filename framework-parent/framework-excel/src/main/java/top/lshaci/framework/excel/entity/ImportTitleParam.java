package top.lshaci.framework.excel.entity;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import top.lshaci.framework.excel.annotation.ImportTitle;
import top.lshaci.framework.excel1.exception.ExcelHandlerException;

/**
 * 导入列定义的相关参数
 *
 * @author lshaci
 * @since 1.0.2
 */
@Data
@Slf4j
@NoArgsConstructor
@Accessors(chain = true)
public class ImportTitleParam implements Comparable<ImportTitleParam> {

	private String title;

	private int colNum;

	private String prefix = "";

	private String suffix = "";

	private Field field;

	private Method method;
	
	private boolean required;

	/**
	 * 数据转换类
	 */
	private Class<?> convertClass;

	/**
	 * 数据转换方法
	 */
	private Method convertMethod;

	/**
	 * 列数据替换信息映射
	 */
	private Map<String, String> replaceMap;

	/**
	 * 根据导入的字段创建列信息
	 *
	 * @param field 需要导入的字段
	 * @param cls 需要导入的实体类
	 */
	public ImportTitleParam(Field field, Class<?> cls) {
		this.field = field;
		this.title = field.getName();
		this.method = createByField(field, cls);

		ImportTitle importTitle = field.getAnnotation(ImportTitle.class);
		if (Objects.isNull(importTitle)) {
			return;
		}

		build(importTitle);
		buildConvertMethod(importTitle);
	}

	/**
	 * 创建转换方法
	 *
	 * @param importTitle 字段上标记的列信息
	 */
	private void buildConvertMethod(ImportTitle importTitle) {
		if (Void.class != importTitle.convertClass() && StringUtils.isNotBlank(importTitle.convertMethod())) {
			try {
				this.convertClass = importTitle.convertClass();
				this.convertMethod = this.convertClass.getMethod(importTitle.convertMethod(), String.class);
			} catch (NoSuchMethodException | SecurityException e) {
				log.error("{}.{}转换方法不存在", importTitle.convertClass(), importTitle.convertMethod());
				throw new ExcelHandlerException("转换方法不存在", e);
			}
		}
	}

	/**
	 * 根据字段和类创建字段的公共set方法
	 *
	 * @param field 字段
	 * @param cls 类
	 * @return set方法
	 */
	private Method createByField(Field field, Class<?> cls) {
		String name = field.getName();
		if (field.getType() == boolean.class) {
			throw new ExcelHandlerException("禁止使用boolean类型");
		}
		String methodName = "set" + name.substring(0, 1).toUpperCase() + name.substring(1);
		try {
			return cls.getMethod(methodName, field.getType());
		} catch (NoSuchMethodException | SecurityException e) {
			log.error("{}的{}方法不存在", cls, methodName);
			throw new ExcelHandlerException("Set方法不存在", e);
		}

	}

	/**
	 * 根据{@code @ImportTitle}注解定义列信息
	 *
	 * @see ImportTitle
	 *
	 * @param importTitle 导入列注解信息
	 */
	private void build(ImportTitle importTitle) {
		this.required = importTitle.required();
		if (StringUtils.isNotBlank(importTitle.prefix())) {
			this.prefix = importTitle.prefix();
		}
		if (StringUtils.isNotBlank(importTitle.suffix())) {
			this.suffix = importTitle.suffix();
		}
		if (StringUtils.isNotBlank(importTitle.title())) {
			this.title = importTitle.title();
		}
		if (ArrayUtils.isNotEmpty(importTitle.replaces())) {
			this.replaceMap = Arrays.stream(importTitle.replaces())
					.filter(r -> r.contains("__"))
					.map(r -> r.split("__"))
					.filter(a -> a.length > 1)
					.collect(Collectors.toMap(a -> a[0], a -> a[1], (k1, k2) -> k2));
		}
	}

	@Override
	public int compareTo(ImportTitleParam importTitleParam) {
		return this.colNum - importTitleParam.colNum;
	}


}
