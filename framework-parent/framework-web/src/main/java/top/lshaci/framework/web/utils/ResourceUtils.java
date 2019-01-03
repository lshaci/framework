package top.lshaci.framework.web.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import top.lshaci.framework.utils.ClassUtils;
import top.lshaci.framework.utils.exception.UtilException;
import top.lshaci.framework.web.annotation.ResourceName;

/**
 * The resource util
 *
 * @author lshaci
 * @since 1.0.1
 */
@Slf4j
public class ResourceUtils {

    /**
     * Full permission suffix
     */
	private static final String RESOURCE_ALL_SUFFIX = ":ALL";

	/**
	 * Select last insert id sql
	 */
    private static final String LAST_INSERT_ID = "select @parent_id := LAST_INSERT_ID() from t_resource;";
    
    /**
     * Sql file name
     */
    private static final String FILE_NAME = "resourceSql.sql";

    /**
     * Generate resource by package name
     *
     * @param packageName the package that need to generate resources
     * @param path the save path of the sql file
     * @param appId application id
     */
    public static void generateResource(String packageName, String path, Long appId) {
        Set<Class<?>> classSet = ClassUtils.getClassSet(packageName, true);
        if (CollectionUtils.isEmpty(classSet)) {
            log.warn("This package[{}] does not contain any class!", packageName);
            return;
        }

        File sqlFile = new File(path, FILE_NAME);
        try (
        		BufferedWriter bw = new BufferedWriter(new FileWriter(sqlFile));
        ) {
            classSet.stream()
                    .map(ResourceUtils::handleClass)
                    .filter(CollectionUtils::isNotEmpty)
                    .forEach(r -> {
                        generateSql(appId, bw, r);
                    });
        } catch (IOException e) {
        	throw new UtilException("SQL generation error.", e);
        }
    }

    /**
     * Generate SQL and write to the file
     * 
     * @param appId application id
     * @param bw file buffered writer
     * @param resources generated resources
     */
	private static void generateSql(Long appId, BufferedWriter bw, List<Resource> resources) {
		try {
			int size = resources.size();
		    for (int i = size - 1; i >= 0; i--) {
		        Resource resource = resources.get(i);
		        resource.setAppId(appId);
		        if (i == size - 1) {
		        	bw.write(resource.parentSql());
		            bw.newLine();
		            bw.write(LAST_INSERT_ID);
		        } else {
		        	bw.write(resource.childSql());
		        }
		        bw.newLine();
		    }
		} catch (Exception e) {
			throw new UtilException("SQL generation error.", e);
		}
	}
    
    

    /**
     * Handle the resource class
     *
     * @param clazz the resource class
     * @return the resource list of the resource class
     */
    private static List<Resource> handleClass(Class<?> clazz) {
        ResourceName classResourceAnnotation = clazz.getAnnotation(ResourceName.class);
        if (Objects.isNull(classResourceAnnotation)) {
            return null;
        }

        String classResourceName = classResourceAnnotation.value();
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
            resources.add(new Resource(classResourceName, clazz.getName() + RESOURCE_ALL_SUFFIX));
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
    private static Resource createResource(Class<?> clazz, Method method, String classResourceName) {
        String name = classResourceName + "-" + method.getAnnotation(ResourceName.class).value();
        String address = clazz.getName() + ":" + method.getName();

        return new Resource(name, address);
    }

    /**
     * Controller resource
     * 
     * @author lshaci
     * @since 1.0.1
     */
    @Data
    static class Resource {
        private Long appId;
        private String name;
        private String address;
        private Long parentId;
        
        /**
         * The sql of parent insert prefix
         */
        private static final String PARENT_INSERT_PREFIX = "insert into t_resource(app_id, name, address) values ";
        /**
         * The sql of child insert prefix
         */
        private static final String CHILD_INSERT_PREFIX = "insert into t_resource(app_id, name, address, parent_id) values ";

        /**
         * Construct resource with name and address
         * 
         * @param name the resource name
         * @param address the resource name
         */
        public Resource(String name, String address) {
            this.name = name;
            this.address = address;
        }

        /**
         * Generate parent insert sql 
         * 
         * @return parent insert sql 
         */
        public String parentSql() {
            StringBuilder sb = new StringBuilder(PARENT_INSERT_PREFIX);
            sb.append("(").append(appId).append(", ")
               .append("'").append(name).append("', ")
               .append("'").append(address).append("');");
            return sb.toString();
        }

        /**
         * Generate child insert sql 
         * 
         * @return child insert sql 
         */
        public String childSql() {
            StringBuilder sb = new StringBuilder(CHILD_INSERT_PREFIX);
            sb.append("(").append(appId).append(", ")
               .append("'").append(name).append("', ")
               .append("'").append(address).append("', @parent_id);");
            return sb.toString();
        }
    }

}

