package top.lshaci.framework.mybatis.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import lombok.extern.slf4j.Slf4j;
import top.lshaci.framework.mybatis.datasource.DynamicDataSource;

/**
 * Config mybatis<br><br>
 * 
 * <b>0.0.4: </b>Change to auto configuration
 * 
 * @author lshaci
 * @since 0.0.1
 * @version 0.0.4
 */
@Slf4j
@Configuration
@AutoConfigureAfter(DynamicDataSourceConfig.class)
@ConditionalOnProperty(value = "datasource.dynamic", havingValue = "true")
public class MybatisConfig {
	
	@Value("${mybatis-plus.type-aliases-package}")
	private String mybatisTypeAliasesPackage;
	
	@Value("${mybatis-plus.mapper-locations}")
	private String mybatisMapperLocations;
	
	/**
	 * Define mybatis sql session factory
	 * 
	 * @param dataSource the primary data source
	 * @return mybatis sql session factory
	 * @throws Exception if resolver mapper locations or get object has error
	 */
	@Bean
    public SqlSessionFactory sqlSessionFactory(@Qualifier("dataSource") DynamicDataSource dataSource) throws Exception {
		log.debug("Init Mybatis Sql Session Factory...");
		
        SqlSessionFactoryBean sqlSessionFactory = new SqlSessionFactoryBean();
        
        sqlSessionFactory.setDataSource(dataSource);
        sqlSessionFactory.setTypeAliasesPackage(mybatisTypeAliasesPackage);
        sqlSessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(mybatisMapperLocations));

        return sqlSessionFactory.getObject();
    }

}
