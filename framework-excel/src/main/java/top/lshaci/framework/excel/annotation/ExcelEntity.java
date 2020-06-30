package top.lshaci.framework.excel.annotation;

import java.lang.annotation.*;

/**
 * 将一个对象标记Excel导出的关联对象
 *
 * @author lshaci
 * @since 1.0.2
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelEntity {

}
