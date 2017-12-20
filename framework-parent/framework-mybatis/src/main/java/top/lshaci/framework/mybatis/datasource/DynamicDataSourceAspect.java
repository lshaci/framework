package top.lshaci.framework.mybatis.datasource;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 切换数据源Advice
 * 
 * @author lshaci
 * @version 0.0.1
 */
@Aspect
@Order(-10) // 保证该AOP在@Transactional之前执行
@Component
public class DynamicDataSourceAspect {

	/**
	 * 在方法开始前根据{@TargetDataSource}切换数据源
	 */
	@Before("@annotation(targetDataSource)")
	public void changeDataSource(JoinPoint point, TargetDataSource targetDataSource) {
		DynamicDataSourceType dataSourceType = targetDataSource.value();
		DynamicDataSourceContextHolder.setDataSourceType(dataSourceType);
	}

	/**
	 * 方法执行完成后清除数据源类型
	 */
	@After("@annotation(targetDataSource)")
    public void restoreDataSource(JoinPoint point, TargetDataSource targetDataSource) {
        DynamicDataSourceContextHolder.clearDataSourceType();
    }

}
