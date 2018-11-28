package top.lshaci.framework.permission.service;

import java.util.List;

import top.lshaci.framework.permission.model.RoleResource;

/**
 * The role resource service
 * 
 * @author lshaci
 * @since 0.0.4
 */
public interface RoleResourceService {

	/**
	 * Insert role resource list
	 * 
	 * @param roleResources the role resource list
	 */
	void insertList(List<RoleResource> roleResources);
}
