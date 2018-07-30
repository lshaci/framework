package top.lshaci.framework.permission.mapper;

import java.util.List;

import top.lshaci.framework.permission.model.RoleResource;

/**
 * The role resource mapper
 * 
 * @author lshaci
 * @since 0.0.4
 */
public interface RoleResourceMapper {

	/**
	 * Create table
	 */
	void createTable();
	
	/**
	 * Insert role resource list
	 * 
	 * @param roleResources the role resource list
	 * @return the number of rows affected
	 */
	int insertList(List<RoleResource> roleResources);
}

