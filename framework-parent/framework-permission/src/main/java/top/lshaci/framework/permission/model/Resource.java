package top.lshaci.framework.permission.model;

import lombok.Data;

/**
 * The resource model
 * 
 * @author lshaci
 * @since 0.0.4
 */
@Data
public class Resource {

	private Long id;
	private String name; // the resource name
	private String resource; // the resource(class name ":" method name)
	
	/**
	 * Constructs a new resource
	 */
	public Resource() {}
	
	/**
	 * Constructs a new resource with the name and resource
	 * 
	 * @param name the resource name
	 * @param resource the resource(class name ":" method name)
	 */
	public Resource(String name, String resource) {
		this.name = name;
		this.resource = resource;
	}
}
