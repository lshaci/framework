package top.lshaci.framework.mybatis.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;

/**
 * Mybatis generator util
 * 
 * @author lshaci
 * @since 0.0.3
 */
public abstract class MybatisGeneratorUtils {

	/**
	 * Generator method
	 * 
	 * @param clazz the class that invoke this method
	 * @throws Exception if has exception
	 */
	public static void generator(Class<?> clazz) throws Exception {
		List<String> warnings = new ArrayList<>();
		boolean overwrite = true;
		File configFile = new File(clazz.getResource("/generatorConfig.xml").getFile());
		ConfigurationParser cp = new ConfigurationParser(warnings);
		Configuration config = cp.parseConfiguration(configFile);

		DefaultShellCallback callback = new DefaultShellCallback(overwrite);
		MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);

		myBatisGenerator.generate(null);
	}
}
