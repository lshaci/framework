package top.lshaci.framework.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.NullCacheStorage;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import lombok.extern.slf4j.Slf4j;
import top.lshaci.framework.common.exception.BaseException;

/**
 * Freemarker Utils
 * 
 * @author lshaci
 * @since 0.0.4
 */
@Slf4j
public   class FreemarkerUtils {
	
	/**
	 * template configuration
	 */
	private Configuration configuration;
		
	/**
	 * the key prefix for the cached template
	 */
	private String prefix;
	
	/**
	 * the current template in thread local
	 */
	private ThreadLocal<Template> currentTemplate = new ThreadLocal<>();
	
	/**
	 * template cache; the key is template name, the value is template
	 */
	private static final Map<String, Template> TEMPLATE_CACHE = new HashMap<>();
	
	/**
	 * Constructs an instance with source class and template path
	 * 
	 * @param sourceClass the class that uses this util
	 * @param templatePath the template path
	 */
	private FreemarkerUtils(Class<?> sourceClass, String templatePath) {
		this.configuration = new Configuration(Configuration.VERSION_2_3_22);
		this.prefix = sourceClass.getName() + ":";
		
		configuration.setTemplateLoader(new ClassTemplateLoader(sourceClass, templatePath));
		configuration.setDefaultEncoding("UTF-8");
		configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
		configuration.setCacheStorage(NullCacheStorage.INSTANCE);
	}

	/**
	 * Build freemarker util instance by source class and template path
	 * 
	 * @param sourceClass the class that uses this util
	 * @param templatePath the template path
	 * @return the freemarker util instance
	 */
	public static FreemarkerUtils build(Class<?> sourceClass, String templatePath) {
		log.debug("The template path is: {}", templatePath);
		return new FreemarkerUtils(sourceClass, templatePath);
	}
	
	/**
	 * Set template by name
	 * 
	 * @param templateName the template name
	 * @return this
	 */
	public FreemarkerUtils setTemplate(String templateName) {
		try {
			String cacheKey = prefix + templateName;
			Template template = TEMPLATE_CACHE.get(cacheKey);
			if (template == null) {
				template = configuration.getTemplate(templateName);
				TEMPLATE_CACHE.put(cacheKey, template);
			}
			currentTemplate.set(template);
			return this;
		} catch (IOException e) {
			log.error("Get template has error! The template name is : " + templateName, e);
			throw new BaseException("Get template has error! The template name is : " + templateName, e);
		}
	}
	
	/**
	 * Generate to string(invoke this method must after setTemplate)
	 * 
	 * @param model the holder of the variables visible from the template (name-value pairs); usually a
     *            {@code Map<String, Object>} or a JavaBean (where the JavaBean properties will be the variables)
	 * @return the html string
	 */
	public String generate(Object model){
		try (
			StringWriter stringWriter = new StringWriter();
			BufferedWriter writer = new BufferedWriter(stringWriter);
		) {
			generate(model, writer);
			return stringWriter.toString();
		} catch (IOException e) {
			log.error("Template rendering exception!", e);
			throw new BaseException("Template rendering exception!", e);
		}
	}
	
	/**
	 * Generate to file(invoke this method must after setTemplate)
	 * 
	 * @param model the holder of the variables visible from the template (name-value pairs); usually a
     *            {@code Map<String, Object>} or a JavaBean (where the JavaBean properties will be the variables)
	 * @param file the target file
	 */
	public void generate(Object model, File file) {
		try (
			FileOutputStream fos = new FileOutputStream(file);	
		) {
			generate(model, fos);
		} catch (IOException e) {
			log.error("Create a file output stream exception!", e);
			throw new BaseException("Create a file output stream exception!", e);
		}
	}
	
	/**
	 * Generate to output stream(invoke this method must after setTemplate)
	 * 
	 * @param model the holder of the variables visible from the template (name-value pairs); usually a
     *            {@code Map<String, Object>} or a JavaBean (where the JavaBean properties will be the variables)
	 * @param outputStream the target output stream
	 */
	public void generate(Object model, OutputStream outputStream) {
		try (
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "utf-8"), 10240);	
		) {
			generate(model, writer);
		} catch (IOException e) {
			log.error("Create a buffered writer exception!", e);
			throw new BaseException("Create a buffered writer exception!", e);
		}
	}
	
	/**
	 * Generate to buffered writer(invoke this method must after setTemplate)
	 * 
	 * @param model the holder of the variables visible from the template (name-value pairs); usually a
     *            {@code Map<String, Object>} or a JavaBean (where the JavaBean properties will be the variables)
	 * @param writer the target buffered writer
	 */
	public void generate(Object model, BufferedWriter writer) {
		try {
			Template current = currentTemplate.get();
			if (current == null) {
				throw new BaseException("Current Template is null!");
			}
			current.process(model, writer);
			writer.flush();
		} catch (TemplateException | IOException e) {
			log.error("Template rendering exception!", e);
			throw new BaseException("Template rendering exception!", e);
		}
	}
}
