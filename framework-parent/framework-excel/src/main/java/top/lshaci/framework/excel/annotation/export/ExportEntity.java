package top.lshaci.framework.excel.annotation.export;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标记需要导出的对象为一个实体类
 *
 * @author lshaci
 * @since 1.0.2
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExportEntity {

    /**
     * 列标题
     *
     * @return 列标题
     */
	String title();

    /**
     * 列对应的顺序
     *
     * @return 列的顺序
     */
	int order();

}
