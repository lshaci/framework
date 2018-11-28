package top.lshaci.framework.permission.service;

import java.util.List;

import top.lshaci.framework.permission.model.UserRole;

/**
 * The user role service
 * 
 * @author lshaci
 * @since 0.0.4
 */
public interface UserRoleService {

	/**
	 * Insert user role list
	 * 
	 * @param userRoles the user role list
	 */
	void insertList(List<UserRole> userRoles);
}
