package top.lshaci.framework.permission.utils;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;

import lombok.extern.slf4j.Slf4j;
import top.lshaci.framework.permission.annotation.ResourceName;
import top.lshaci.framework.permission.model.Resource;
import top.lshaci.framework.permission.service.ResourceService;
import top.lshaci.framework.utils.ClassUtils;

/**
 * The resource util
 * 
 * @author lshaci
 * @since 0.0.4
 */
@Slf4j
public class ResourceUtils {
	
	/**
	 * The resource service bean
	 */
	public static ResourceService resourceService;
	
	/**
	 * Full permission suffix
	 */
	public final static String RESOURCE_ALL_SUFFIX = ":ALL";

	/**
	 * Generate resource by package name
	 * 
	 * @param packageName the package that need to generate resources
	 */
	public static void generateResource(String packageName) {
		Set<Class<?>> classSet = ClassUtils.getClassSet(packageName, true);
		if (CollectionUtils.isEmpty(classSet)) {
			log.warn("This package[{}] does not contain any class!", packageName);
			return;
		}
		List<Resource> resources = classSet.stream()
				.map(ResourceUtils::handleClass)
				.filter(CollectionUtils::isNotEmpty)
				.flatMap(r -> r.stream())
				.collect(Collectors.toList());
		if (CollectionUtils.isNotEmpty(resources)) {
			resourceService.insertList(resources);
		}
	}
	
	/**
	 * Handle the resource class
	 * 
	 * @param clazz the resource class
	 * @return the resource list of the resource class
	 */
	private static List<Resource> handleClass(Class<?> clazz) {
		StringBuilder classResourceName = new StringBuilder();
		ResourceName classResourceAnnotation = clazz.getAnnotation(ResourceName.class);
		if (classResourceAnnotation != null) {
			classResourceName.append(classResourceAnnotation.value() + "-");
		}
		
		Method[] methods = clazz.getDeclaredMethods();
		if (ArrayUtils.isEmpty(methods)) {
			log.warn("This class[{}] does not contain any method!", clazz);
			return null;
		}
		
		 List<Resource> resources = Arrays.stream(methods)
				.filter(m -> m.getAnnotation(ResourceName.class) != null)
				.map(m -> createResource(clazz, m, classResourceName))
				.collect(Collectors.toList());
		 if (CollectionUtils.isNotEmpty(resources)) {
			 resources.add(new Resource(clazz.getSimpleName() + RESOURCE_ALL_SUFFIX, clazz.getName() + RESOURCE_ALL_SUFFIX));
		}
		return resources;
	}
	
	/**
	 * Create resource
	 * 
	 * @param clazz the resource class
	 * @param method the resource method
	 * @param classResourceName the class resource name
	 * @return the resource instance
	 */
	private static Resource createResource(Class<?> clazz, Method method, StringBuilder classResourceName) {
		String resourceName = classResourceName.toString() + method.getAnnotation(ResourceName.class).value();
		
		return new Resource(resourceName, clazz.getName() + ":" + method.getName());
	}
	
}
