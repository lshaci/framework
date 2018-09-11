package top.lshaci.framework.service;

import java.util.List;

import top.lshaci.framework.common.model.PageResult;
import top.lshaci.framework.mybatis.mapper.TKMapper;

/**
 * Base common service interface<br><br>
 * 
 * <b>0.0.4:</b>Add method: insertSelective, updateSelective
 * 
 * @author lshaci
 * @since 0.0.1
 * @version 0.0.4
 *
 * @param <T>	The entity type
 * @param <M>	The mapper type
 */
public interface BaseService<T, M extends TKMapper<T>> {
	
	/**
	 * Insert one entity instance
	 * 
	 * @param entity the instance
	 * @return	Insert the number of data
	 */
	int insert(T entity);
	
	/**
	 * Insert one entity instance, the properties of null will not be saved and the database defaults will be used
	 * 
	 * @param entity the instance
	 * @return	Insert the number of data
	 */
	int insertSelective(T entity);

	/**
	 * Insert list entity instances
	 * 
	 * @param entities the instances
	 * @return	Insert the number of data
	 */
	int insertList(List<T> entities);

	/**
	 * Update one instance
	 * 
	 * @param entity the instance
	 * @return	Update the number of data
	 */
	int update(T entity);
	
	/**
	 * Update the value that the property is not null based on the primary key
	 * 
	 * @param entity the instance
	 * @return	Update the number of data
	 */
	int updateSelective(T entity);

	/**
	 * Delete one by primary key
	 * 
	 * @param primarykey the primary key
	 * @return	Delete the number of data
	 */
	int delete(Object primarykey);

	/**
	 * Delete many by primary keys
	 * 
	 * @param primarykeys the primary keys
	 * @return	Delete the number of data
	 */
	int deleteByIds(List<Object> primarykeys);

	/**
	 * Delete instance by condition
	 * 
	 * @param condition the condition
	 * @return	Delete the number of data
	 */
	int deleteByCondition(T condition);
	
	/**
	 * Get one by primary key
	 * 
	 * @param primarykey the primary key
	 * @return the instance of the primary key
	 */
	T get(Object primarykey);
	
	/**
	 * Get one by condition
	 * 
	 * @param condition the condition
	 * @return the instance of the condition
	 */
	T getByCondition(T condition);
	
	/**
	 * Get many by primary keys
	 * 
	 * @param primarykeys  the primary keys
	 * @return the instance list of the primary keys
	 */
	List<T> listByIds(List<Object> primarykeys);

	/**
	 * Get all
	 * 
	 * @return the all instance list
	 */
	List<T> listAll();

	/**
	 * List by Condition
	 * 
	 * @param condition the condition
	 * @return the instance list of the condition
	 */
	List<T> listByCondition(T condition);

	/**
	 * Count by condition
	 * 
	 * @param condition the condition
	 * @return the count of the condition
	 */
	int countByCondition(T condition);

	/**
	 * List page without condition
	 * 
	 * @param pageNum page number
	 * @param pageSize page size
	 * @return the page result
	 */
	PageResult<T> listPage(int pageNum, int pageSize);

	/**
	 * List page with condition
	 * 
	 * @param pageNum page number
	 * @param pageSize page size
	 * @param condition the condition
	 * @return  the page result of teh condition
	 */
	PageResult<T> listPageWithCondition(int pageNum, int pageSize, T condition);
}
