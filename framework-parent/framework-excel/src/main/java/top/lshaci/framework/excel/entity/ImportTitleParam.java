package top.lshaci.framework.excel.entity;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import top.lshaci.framework.excel.annotation.ImportTitle;

/**
 * 导入列定义的相关参数
 *
 * @author lshaci
 * @since 1.0.2
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class ImportTitleParam extends BaseTitleParam implements Comparable<ImportTitleParam> {

	/**
	 * 标题对应的列号
	 */
	private int colNum;

	/**
	 * 该列的值是否必须
	 */
	private boolean required;

	/**
	 * 根据导入的字段创建列信息
	 *
	 * @param field 需要导入的字段
	 * @param cls 需要导入的实体类
	 */
	public ImportTitleParam(Field field, Class<?> cls) {
		this.field = field;
		this.title = field.getName();
		this.method = createByField(field, cls, MethodType.SET, field.getType());

		ImportTitle importTitle = field.getAnnotation(ImportTitle.class);
		if (Objects.isNull(importTitle)) {
			return;
		}

		build(importTitle);
		buildConvertMethod(importTitle.convertClass(), importTitle.convertMethod(), String.class);
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
