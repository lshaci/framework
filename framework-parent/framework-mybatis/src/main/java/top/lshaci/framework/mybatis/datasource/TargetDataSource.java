package top.lshaci.framework.mybatis.datasource;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The target data source annotation
 * <br>
 * Use in method to change data source with DynamicDataSourceType(FIRST, SECOND)
 * 
 * @author lshaci
 * @since 0.0.1
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TargetDataSource {

	/**
	 * Get the dynamic data source type
	 * 
	 * @return the dynamic data source type
	 */
	DynamicDataSourceType value();
}
