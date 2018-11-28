package top.lshaci.framework.permission.mapper;

import java.util.List;

import top.lshaci.framework.permission.model.UserRole;

/**
 * The user role mapper
 * 
 * @author lshaci
 * @since 0.0.4
 */
public interface UserRoleMapper {
	
	/**
	 * Create table
	 */
	void createTable();
	
	/**
	 * Insert user role list
	 * 
	 * @param userRoles the user role list
	 * @return the number of rows affected
	 */
	int insertList(List<UserRole> userRoles);
}
