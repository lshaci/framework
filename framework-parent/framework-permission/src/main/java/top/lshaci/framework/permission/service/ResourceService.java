package top.lshaci.framework.permission.service;

import java.util.List;

import top.lshaci.framework.permission.model.Resource;

/**
 * The resource service
 * 
 * @author lshaci
 * @since 0.0.4
 */
public interface ResourceService {

	/**
	 * Insert resource list
	 * 
	 * @param resources the resource list
	 */
	void insertList(List<Resource> resources);
	
	/**
	 * Select resource by unique user id
	 * 
	 * @param user the unique user id
	 * @return the resource list of the user
	 */
	List<Resource> selectByUser(String user);
}
