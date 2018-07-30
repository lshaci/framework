package top.lshaci.framework.permission.mapper;

import java.util.List;

import top.lshaci.framework.permission.model.Role;

/**
 * The role mapper
 * 
 * @author lshaci
 * @since 0.0.4
 */
public interface RoleMapper {

	/**
	 * Create table
	 */
	void createTable();
	
	/**
	 * Insert role list
	 * 
	 * @param roles the role list
	 * @return the number of rows affected
	 */
	int insertList(List<Role> roles);
	
	/**
	 * Select role by unique user id
	 * 
	 * @param user the unique user id
	 * @return the role list of the user
	 */
	List<Role> selectByUser(String user);
}
