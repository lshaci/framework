package top.lshaci.framework.service.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.NullCacheStorage;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;

/**
 * Free marker template util
 * 
 * @author lshaci
 * @since 0.0.3
 */
public abstract class FreemarkerTemplateUtils {

	/**
	 * template configuration
	 */
	private static final Configuration CONFIGURATION = new Configuration(Configuration.VERSION_2_3_22);
	
	/**
	 * template cache; the key is template name, the value is template
	 */
	private static final Map<String, Template> TEMPLATE_CACHE = new HashMap<>();

	static {
		// 这里比较重要，用来指定加载模板所在的路径
		CONFIGURATION.setTemplateLoader(new ClassTemplateLoader(FreemarkerTemplateUtils.class, "/templates"));
		CONFIGURATION.setDefaultEncoding("UTF-8");
		CONFIGURATION.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
		CONFIGURATION.setCacheStorage(NullCacheStorage.INSTANCE);
	}

	/**
	 * Get template by name
	 * 
	 * @param templateName the template name
	 * @return the template
	 * @throws IOException  if has exception
	 */
	public static Template getTemplate(String templateName) throws IOException {
		try {
			return CONFIGURATION.getTemplate(templateName);
		} catch (IOException e) {
			throw e;
		}
	}
	
	/**
	 * Generator file by template name
	 * 
	 * @param templateName	the template name
	 * @param file	the target file
	 * @param model	 the holder of the variables visible from the template (name-value pairs)
	 * @throws Exception if has exception
	 */
	public static void generateFileByTemplateName(final String templateName, File file, Object model)
			throws Exception {
		Template template = TEMPLATE_CACHE.get(templateName);
		if (template == null) {
			template = getTemplate(templateName);
			TEMPLATE_CACHE.put(templateName, template);
		}
		
		FileOutputStream fos = new FileOutputStream(file);

		Writer out = new BufferedWriter(new OutputStreamWriter(fos, "utf-8"), 10240);
		template.process(model, out);
	}

	/**
	 * Clear template cache
	 */
	public static void clearCache() {
		CONFIGURATION.clearTemplateCache();
		TEMPLATE_CACHE.clear();
	}
}
