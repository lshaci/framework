package top.lshaci.framework.permission.model;

import lombok.Data;

/**
 * The role model
 * 
 * @author lshaci
 * @since 0.0.4
 */
@Data
public class Role {

	private Long id;
	private String name; // the role name
	private String description; // the role description
}
