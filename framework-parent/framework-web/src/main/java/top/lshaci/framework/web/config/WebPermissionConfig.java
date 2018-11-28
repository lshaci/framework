package top.lshaci.framework.web.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;
import top.lshaci.framework.permission.service.ResourceService;
import top.lshaci.framework.permission.service.RoleService;
import top.lshaci.framework.web.interceptor.AbstractPermissionInterceptor;

/**
 * The web permission config
 * 
 * @author lshaci
 * @since 0.0.4
 */
@Slf4j
@Configuration
@ConditionalOnProperty(value = "web.webPermission.enabled", havingValue = "true")
public class WebPermissionConfig {
	
	/**
	 * Set the AbstractPermissionInterceptor resourceService
	 * 
	 * @param resourceService the resourceService bean
	 */
	@Autowired
	public void setResourceService(ResourceService resourceService) {
		log.debug("Set AbstractPermissionInterceptor.resourceService...");
		AbstractPermissionInterceptor.resourceService = resourceService;
	}
	
	/**
	 * Set the AbstractPermissionInterceptor roleService
	 * 
	 * @param roleService the roleService bean
	 */
	@Autowired
	public void setRoleService(RoleService roleService) {
		log.debug("Set AbstractPermissionInterceptor.roleService...");
		AbstractPermissionInterceptor.roleService = roleService;
	}
}
