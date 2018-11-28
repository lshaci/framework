package top.lshaci.framework.permission.model;

import lombok.Data;

/**
 * The user role model
 * 
 * @author lshaci
 * @since 0.0.4
 */
@Data
public class UserRole {

	private String user; // the unique user id
	private Long roleId; // the role id
	
}
