package top.lshaci.framework.mybatis.datasource;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;

/**
 * 切换数据源Advice
 * 
 * @author lshaci
 * @since 0.0.1
 */
@Aspect
@Order(-10) // 保证该AOP在@Transactional之前执行
public class DynamicDataSourceAspect {

	/**
	 * Before the method invoke change data source
	 * 
	 * @param point the point
	 * @param targetDataSource the target data source annotation
	 */
	@Before("@annotation(targetDataSource)")
	public void changeDataSource(JoinPoint point, TargetDataSource targetDataSource) {
		DynamicDataSourceType dataSourceType = targetDataSource.value();
		DynamicDataSourceContextHolder.setDataSourceType(dataSourceType);
	}

	/**
	 * After method invoke clear data source type
	 * 
	 * @param point the point
	 * @param targetDataSource the target data source annotation
	 */
	@After("@annotation(targetDataSource)")
    public void restoreDataSource(JoinPoint point, TargetDataSource targetDataSource) {
        DynamicDataSourceContextHolder.clearDataSourceType();
    }

}
