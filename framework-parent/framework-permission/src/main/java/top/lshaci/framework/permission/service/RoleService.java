package top.lshaci.framework.permission.service;

import java.util.List;

import top.lshaci.framework.permission.model.Role;

/**
 * The role service
 * 
 * @author lshaci
 * @since 0.0.4
 */
public interface RoleService {
	
	/**
	 * Insert role list
	 * 
	 * @param roles the role list
	 */
	void insertList(List<Role> roles);
	
	/**
	 * Select role by unique user id
	 * 
	 * @param user the unique user id
	 * @return the role list of the user
	 */
	List<Role> selectByUser(String user);
}
