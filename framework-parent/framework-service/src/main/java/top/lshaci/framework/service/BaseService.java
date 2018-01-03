package top.lshaci.framework.service;

import java.util.List;

import top.lshaci.framework.mybatis.model.PageResult;

/**
 * Base common service interface
 * 
 * @author lshaci
 * @since 0.0.1
 *
 * @param <T>	The entity type
 * @param <P>	The primary key type
 */
public interface BaseService<T, P> {
	
	/**
	 * Insert one entity instance
	 * 
	 * @param entity the instance
	 * @return	Insert the number of data
	 */
	int insert(T entity);

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
	 * Delete one by primary key
	 * 
	 * @param primarykey the primary key
	 * @return	Delete the number of data
	 */
	int delete(P primarykey);

	/**
	 * Delete many by primary keys
	 * 
	 * @param primarykeys the primary keys
	 * @return	Delete the number of data
	 */
	int deleteByIds(List<P> primarykeys);

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
	 * @return
	 */
	T get(P primarykey);
	
	/**
	 * Get one by condition
	 * 
	 * @param condition the condition
	 * @return
	 */
	T getByCondition(T condition);
	
	/**
	 * Get many by primary keys
	 * 
	 * @param primarykeys  the primary keys
	 * @return
	 */
	List<T> listByIds(List<P> primarykeys);

	/**
	 * Get all
	 * 
	 * @return
	 */
	List<T> listAll();

	/**
	 * List by Condition
	 * 
	 * @param condition the condition
	 * @return
	 */
	List<T> listByCondition(T condition);

	/**
	 * Count by condition
	 * 
	 * @param condition the condition
	 * @return
	 */
	int countByCondition(T condition);

	/**
	 * List page without condition
	 * 
	 * @param pageNum page number
	 * @param pageSize page size
	 * @return
	 */
	PageResult<T> listPage(int pageNum, int pageSize);

	/**
	 * List page with condition
	 * 
	 * @param pageNum page number
	 * @param pageSize page size
	 * @param condition the condition
	 * @return
	 */
	PageResult<T> listPageWithCondition(int pageNum, int pageSize, T condition);
}
