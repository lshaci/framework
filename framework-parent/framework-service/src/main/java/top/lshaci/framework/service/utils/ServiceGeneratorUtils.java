package top.lshaci.framework.service.utils;

import java.io.File;
import java.net.URL;
import java.util.Objects;

import top.lshaci.framework.mybatis.utils.MybatisGeneratorUtils;
import top.lshaci.framework.service.model.ServiceGeneratorModel;
import top.lshaci.framework.utils.ClassUtils;
import top.lshaci.framework.utils.FreemarkerUtils;

/**
 * Framework service generator util
 * 
 * @author lshaci
 * @since 0.0.3
 * @version 0.0.4
 */
public class ServiceGeneratorUtils {
	
	/**
	 * the java file suffix
	 */
	private static final String JAVA_FILE_SUFFIX = ".java";
	
	/**
	 * the maven project java file location prefix
	 */
	private static final String MAVEN_SRC_MAIN_JAVA = "src\\main\\java\\";
	
	/**
	 * Generator target project domain, mapper interface and xml file, service interface and implement
	 * 
	 * @param clazz the class that invoke this method
	 * @param servicePackage the target project service package
	 * @param domainPackage the target project domain package
	 * @param mapperPackage the target project mapper package
	 * @throws Exception if has exception
	 */
	public static void generator(Class<?> clazz, String servicePackage, String domainPackage, String mapperPackage) throws Exception  {
		Objects.requireNonNull(clazz, "The class must not be null!");
		Objects.requireNonNull(servicePackage, "The service package must not be null!");
		Objects.requireNonNull(domainPackage, "The domain package must not be null!");
		Objects.requireNonNull(mapperPackage, "The mapper package must not be null!");
		
		MybatisGeneratorUtils.generator(clazz);
		
		URL resource = ClassUtils.getClassLoader().getResource(".");
		
		ServiceGeneratorModel model = new ServiceGeneratorModel();
		model.setDomainPackage(domainPackage);
		model.setMapperPackage(mapperPackage);
		model.setServicePackage(servicePackage);
		
		if (resource != null) {
			String protocol = resource.getProtocol();
			if ("file".equals(protocol)) {
				String path = resource.getPath().replaceAll("%20", " ");
				String projectPath = path.substring(0, path.indexOf("target")) + MAVEN_SRC_MAIN_JAVA;
				String domainPath = projectPath + domainPackage.replace(".", File.pathSeparator);
				String servicePath = projectPath + servicePackage.replace(".", File.pathSeparator);
				File[] domainfiles = new File(domainPath)
						.listFiles(f -> (f.isFile() && f.getName().endsWith(JAVA_FILE_SUFFIX)));
				
				FreemarkerUtils freemarkerUtils = FreemarkerUtils.build(ServiceGeneratorUtils.class, "/templates");
				for (File domainFile : domainfiles) {
					String domainFileName = domainFile.getName();
					String domainName = domainFileName.substring(0, domainFileName.lastIndexOf(JAVA_FILE_SUFFIX));
					
					model.setDomainName(domainName);
					
					File serviceFile = new File(servicePath, domainName + "Service.java");
					File serviceImplFile = new File(servicePath + File.pathSeparator + "impl", domainName + "ServiceImpl.java");
					freemarkerUtils.setTemplate("ServiceTemplate").generate(model, serviceFile);
					freemarkerUtils.setTemplate("ServiceImplTemplate").generate(model, serviceImplFile);
				}
			}
		}
	}
	
}
