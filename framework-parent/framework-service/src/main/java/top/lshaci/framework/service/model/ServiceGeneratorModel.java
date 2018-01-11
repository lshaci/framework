package top.lshaci.framework.service.model;

import lombok.Data;

/**
 * Service generator model of freemarker
 * 
 * @author lshaci
 * @since 0.0.3
 */
@Data
public class ServiceGeneratorModel {

	private String servicePackage;
	private String domainPackage;
	private String domainName;
	private String mapperPackage;

}
