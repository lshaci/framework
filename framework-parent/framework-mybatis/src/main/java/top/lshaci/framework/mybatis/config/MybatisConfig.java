package top.lshaci.framework.mybatis.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import lombok.extern.slf4j.Slf4j;
import top.lshaci.framework.mybatis.datasource.DynamicDataSource;

@Configuration
@Slf4j
public class MybatisConfig {
	
	@Value("${mybatis.type-aliases-package}")
	private String mybatisTypeAliasesPackage;
	
	@Value("${mybatis.mapper-locations}")
	private String mybatisMapperLocations;
	
	/**
	 * Define mybatis sql session factory
	 * 
	 * @param dataSource the primary data source
	 * @return mybatis sql session factory
	 * @throws Exception if resolver mapper locations or get object has error
	 */
	@ConditionalOnProperty(value = "datasource.dynamic", matchIfMissing = false)
    @Bean
    public SqlSessionFactory sqlSessionFactory(@Qualifier("dataSource") DynamicDataSource dataSource) throws Exception {
		log.info("Init Mybatis Sql Session Factory...");
		
        SqlSessionFactoryBean sqlSessionFactory = new SqlSessionFactoryBean();
        
        sqlSessionFactory.setDataSource(dataSource);
        sqlSessionFactory.setTypeAliasesPackage(mybatisTypeAliasesPackage);
        sqlSessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(mybatisMapperLocations));

        return sqlSessionFactory.getObject();
    }

}
