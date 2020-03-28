package top.lshaci.framework.excel.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import top.lshaci.framework.excel.exception.ExcelHandlerException;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * 导入导出公共的列标题信息
 *
 * @author lshaci
 * @since 1.0.2
 */
@Data
@Slf4j
@Accessors(chain = true)
class BaseTitleParam {

	/**
	 * 列标题
	 */
	protected String title;

	/**
	 * 前缀(导出添加, 导入去掉)
	 */
	protected String prefix = "";

	/**
	 * 后缀(导出添加, 导入去掉)
	 */
	protected String suffix = "";

	/**
	 * 需要导入(出)的字段(导出内嵌对象为内嵌对象中的字段)
	 */
	protected Field field;

	/**
	 * 字段的set/get方法
	 */
	protected Method method;

	/**
	 * 数据转换类
	 */
	protected Class<?> convertClass;

	/**
	 * 数据转换方法
	 */
	protected Method convertMethod;

	/**
	 * 列数据替换信息映射
	 */
	protected Map<String, String> replaceMap;

	/**
	 * 创建转换方法
	 *
	 * @param convertClass 转换类
	 * @param methodName 转换方法名称
	 * @param args 方法参数类型
	 */
	protected void buildConvertMethod(Class<?> convertClass, String methodName, Class<?>...args) {
		if (Void.class != convertClass && StringUtils.isNotBlank(methodName)) {
			try {
				this.convertClass = convertClass;
				this.convertMethod = convertClass.getMethod(methodName, args);
			} catch (NoSuchMethodException | SecurityException e) {
				log.error("{}.{}转换方法不存在", convertClass, methodName);
				throw new ExcelHandlerException("转换方法不存在", e);
			}
		}
	}

	/**
	 * 根据字段和类创建字段的公共set/get方法
	 *
	 * @param field 字段
	 * @param cls 类
	 * @param methodType 方法类型
	 * @return set/get方法
	 */
	protected Method createByField(Field field, Class<?> cls, MethodType methodType, Class<?>...args) {
		String name = field.getName();
		if (field.getType() == boolean.class) {
			throw new ExcelHandlerException("禁止使用boolean类型");
		}
		String methodName = methodType.des + name.substring(0, 1).toUpperCase() + name.substring(1);
		try {
			return cls.getMethod(methodName, args);
		} catch (NoSuchMethodException | SecurityException e) {
			log.error("{}的{}方法不存在", cls, methodName);
			throw new ExcelHandlerException(methodType + "方法不存在", e);
		}
	}

	/**
	 * 方法类型
	 *
	 * @author lshaci
	 * @since 1.0.2
	 */
	@AllArgsConstructor
	protected enum MethodType {
		SET("set"), GET("get");

		private String des;
	}

}
