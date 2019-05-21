package top.lshaci.framework.excel.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 将一个字段标记为需要导入的列
 *
 * @author lshaci
 * @since 1.0.2
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ImportTitle {

	/**
	 * 列标题
	 *
	 * @return 列标题
	 */
	String title();
	
	/**
	 * 字段的值是否必须
	 * 
	 * @return 标记字段的值是否必须
	 */
	boolean required() default true;

	/**
	 * 导入数据需要替换掉的前缀
	 *
	 * @return 需要替换掉的前缀
	 */
	String prefix() default "";

	/**
	 * 导入数据需要替换掉的后缀
	 *
	 * @return 需要替换掉的后缀
	 */
	String suffix() default "";

	/**
	 * 列数据替换信息, 使用两个下划线(<b>__</b>)进行分割
	 * <p><b>source__target</b></p>
	 * <i>例: 1__男</i>
	 *
	 * @return 列数据替换信息
	 */
	String[] replaces() default {};

	/**
	 * 导出数据转换对象类
	 *
	 * @return 转换对象类
	 */
	Class<?> convertClass() default Void.class;

	/**
	 * 导出数据转换对象类中的方法名称, <b>优先级最高</b>
	 *
	 * @return 转换对象类中的方法名称
	 */
	String convertMethod() default "";
}
