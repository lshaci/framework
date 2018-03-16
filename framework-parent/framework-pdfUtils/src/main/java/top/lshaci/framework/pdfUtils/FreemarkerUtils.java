package top.lshaci.framework.pdfUtils;

import java.io.BufferedWriter;
import java.io.IOException;
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
public class FreemarkerUtils {
	
	/**
	 * template configuration
	 */
	private Configuration configuration;
	
	/**
	 * template
	 */
	private Template template;
	
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
	private <T> FreemarkerUtils(Class<T> sourceClass, String templatePath) {
		this.configuration = new Configuration(Configuration.VERSION_2_3_22);
		
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
	public static <T> FreemarkerUtils build(Class<T> sourceClass, String templatePath) {
		log.info("The template path is: ", templatePath);
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
			Template template = TEMPLATE_CACHE.get(templateName);
			if (template == null) {
				template = configuration.getTemplate(templateName);
				TEMPLATE_CACHE.put(templateName, template);
			}
			this.template = template;
			return this;
		} catch (IOException e) {
			log.error("Get template has error! The template name is : " + templateName, e);
			throw new BaseException("Get template has error! The template name is : " + templateName, e);
		}
	}
	
	/**
	 * Generate html string(invoke this method must after setTemplate)
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
			this.template.process(model, writer);
			writer.flush();
			return stringWriter.toString();
		} catch (TemplateException | IOException e) {
			log.error("Template rendering exception!", e);
			throw new BaseException("Template rendering exception!", e);
		}
	}
}
