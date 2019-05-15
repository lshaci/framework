package top.lshaci.framework.excel.annotation.export;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 将一个字段或者公共方法标记为需要导出的列
 *
 * @author lshaci
 * @since 1.0.2
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.METHOD })
public @interface ExportTitle {

	/**
	 * 列标题
	 *
	 * @return 列标题
	 */
	String title();

	/**
	 * 列顺序
	 *
	 * @return 列的顺序
	 */
	int order();

	/**
	 * 列宽度
	 *
	 * @return 列的宽度
	 */
	int width() default 12;

	/**
	 * 内容的行高
	 *
	 * @return 内容的行高
	 */
	int height() default 20;

	/**
	 * 导出数据拼接前缀
	 *
	 * @return 需要拼接的前缀
	 */
	String prefix() default "";

	/**
	 * 导出数据拼接后缀
	 *
	 * @return 需要拼接的后缀
	 */
	String suffix() default "";

	/**
	 * 分组名称(相同组名会生成二级标题)
	 *
	 * @return  分组名称
	 */
	String groupName() default "";

	/**
	 * 导出数据转换对象类
	 *
	 * @return 转换对象类
	 */
	Class<?> convertClass() default Void.class;

	/**
	 * 导出数据转换对象类中的方法名称
	 *
	 * @return 转换对象类中的方法名称
	 */
	String convertMethod() default "";
}
