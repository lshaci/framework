package top.lshaci.framework.permission.mapper;

import java.util.List;

import top.lshaci.framework.permission.model.Resource;

/**
 * The resource mapper
 * 
 * @author lshaci
 * @since 0.0.4
 */
public interface ResourceMapper {

	/**
	 * Create table
	 */
	void createTable();
	
	/**
	 * Insert resource list
	 * 
	 * @param resources the resource list
	 * @return the number of rows affected
	 */
	int insertList(List<Resource> resources);
	
	/**
	 * Select resource by unique user id
	 * 
	 * @param user the unique user id
	 * @return the resource list of the user
	 */
	List<Resource> selectByUser(String user);
}
