package top.lshaci.framework.utils;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.commons.lang3.StringUtils;

import lombok.extern.slf4j.Slf4j;
import top.lshaci.framework.utils.exception.UtilException;

/**
 * Class utils
 * 
 * @author lshaci
 * @version 0.0.1
 */
@Slf4j
public abstract class ClassUtils {

	/**
	 * Get current thread class loader
	 * 
	 * @return the current thread class loader
	 */
	public static ClassLoader getClassLoader() {
		return Thread.currentThread().getContextClassLoader();
	}
	
	/**
	 * Load class of class name
	 * 
	 * @param className the class name
	 * @param isInitialized is init
	 * @return the class of the class name
	 */
	public static Class<?> loadClass(String className, boolean isInitialized) {
		try {
			return Class.forName(className, isInitialized, getClassLoader());
		} catch (ClassNotFoundException e) {
			String msg = "This class not found of name is " + className;
			log.error(msg, e);
			throw new UtilException(msg);
		}
	}
	
	/**
	 * Get class set of the package name
	 * 
	 * @param packageName the package name
	 * @param isRecursion is recursion
	 * @return the class set of the package name
	 */
	public static Set<Class<?>> getClassSet(String packageName, boolean isRecursion) {
		if (StringUtils.isEmpty(packageName)) {
			throw new UtilException("The package name is must not be empty!");
		}
		Set<Class<?>> classSet = new HashSet<>();
		
		String packagePath = packageName.replace(".", "/");
		
		try {
			Enumeration<URL> urls = getClassLoader().getResources(packagePath);
			while (urls.hasMoreElements()) {
				URL url = urls.nextElement();
				if (url != null) {
					String protocol = url.getProtocol();
					if ("file".equals(protocol)) {
						String path = url.getPath().replaceAll("%20", " ");
						addClassFromFile(classSet, path, packageName, isRecursion);
					} else if ("jar".equals(protocol)) {
						JarURLConnection jarURLConnection = ((JarURLConnection) url.openConnection());
						if(jarURLConnection != null){
							JarFile jarFile = jarURLConnection.getJarFile();
							if (jarFile != null) {
								addClassFromJar(classSet, jarFile.entries(), packageName, isRecursion);
								jarFile.close();
							}
						}
					} else {
						addClassNameFromJars(classSet, ((URLClassLoader) getClassLoader()).getURLs(), packageName, isRecursion);
					}
				}
			}
		} catch (IOException e) {
			log.error("Get class set is error, the package is " + packageName, e);
		}
		
		return classSet;
	}

	/**
	 * Add class to class set of protocol is jars
	 * 
	 * @param classSet the class set
	 * @param urls the url class loader urls
	 * @param packageName the package name
	 * @param isRecursion is recursion
	 * @throws IOException 
	 */
	private static void addClassNameFromJars(Set<Class<?>> classSet, URL[] urls, String packageName, boolean isRecursion) throws IOException {
		for (int i = 0; i < urls.length; i++) {
			String classPath = urls[i].getPath();
			
			//不必搜索classes文件夹
			if (classPath.endsWith("classes/")) {
				continue;
			}

			JarFile jarFile = new JarFile(classPath.substring(classPath.indexOf("/")));

			addClassFromJar(classSet, jarFile.entries(), packageName, isRecursion);
			
			jarFile.close();
		}
		
	}

	/**
	 * Add class to class set of protocol is jar
	 * 
	 * @param classSet the class set
	 * @param jarEntries the jar entries
	 * @param packageName the package name
	 * @param isRecursion is recursion
	 */
	private static void addClassFromJar(Set<Class<?>> classSet, Enumeration<JarEntry> jarEntries, String packageName,
			boolean isRecursion) {
		while (jarEntries.hasMoreElements()) {
			JarEntry jarEntry = jarEntries.nextElement();
			String jarEntryName = jarEntry.getName();
			if (jarEntryName.endsWith(".class")) {
				String className = jarEntryName.substring(0, jarEntryName.lastIndexOf(".")).replaceAll("/", ".");
				if(isRecursion){
					doAddClass(classSet, className);
				} else if (!className.replace(packageName + ".", "").contains(".")) {
					doAddClass(classSet, className);
				}
			}
		}
		
	}

	/**
	 * Add class to class set of protocol is file
	 * 
	 * @param classSet the class set
	 * @param packagePath the package path
	 * @param packageName the package name
	 * @param isRecursion is recursion
	 */
	private static void addClassFromFile(Set<Class<?>> classSet, String packagePath, String packageName, boolean isRecursion) {
		File[] files = new File(packagePath).listFiles(f -> 
			(f.isFile() && f.getName().endsWith(".class")) || (isRecursion && f.isDirectory()));
		if (files != null && files.length > 0) {
			for (File file : files) {
				String fileName = file.getName();
				if (file.isFile()) {
					String className = fileName.substring(0, fileName.lastIndexOf("."));
					if (!StringUtils.isEmpty(packageName)) {
						className = packageName + "." + className;
					}
					
					doAddClass(classSet, className);
				} else if (isRecursion && file.isDirectory()) {
					String subPackagePath = fileName;
					if (!StringUtils.isEmpty(packagePath)) {
						subPackagePath = packagePath + "/" + subPackagePath;
					}
					
					String subPackageName = fileName;
					if (!StringUtils.isEmpty(packageName)) {
						subPackageName = packageName + "/" + subPackageName;
					}
					
					addClassFromFile(classSet, subPackagePath, subPackageName, isRecursion);
				}
			}
		}
	}

	/**
	 * Add class to class set of the class name
	 * 
	 * @param classSet the class set
	 * @param className the class name
	 */
	private static void doAddClass(Set<Class<?>> classSet, String className) {
		Class<?> clazz = loadClass(className, false);
		classSet.add(clazz);
	}
}
