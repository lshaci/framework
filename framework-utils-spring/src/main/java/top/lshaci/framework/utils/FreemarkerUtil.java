package top.lshaci.framework.utils;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.*;

/**
 * <p>Utility class for working with FreeMarker.Provides convenience methods to process a FreeMarker template name with a model.</p>
 *
 * Please put the template files in the <b>templates directory</b> under the resources folder
 *
 * @author lshaci
 * @since 1.0.7
 */
@Slf4j
public class FreemarkerUtil {

	/**
	 * template configuration
	 */
	private static Configuration configuration;

	/**
	 * Process the specified FreeMarker template with the given model and write the result to the given Writer
	 *
     * @param name the template name
	 * @param model the holder of the variables visible from the template (name-value pairs); usually a
     *            {@code Map<String, Object>} or a JavaBean (where the JavaBean properties will be the variables)
     *
     * @return the result as String
	 */
	public static String generate(String name, Object model) throws IOException, TemplateException {
	    StringWriter writer = new StringWriter();
        generate(name, model, writer);
        return writer.toString();
    }

	/**
	 * Process the specified FreeMarker template with the given model and write the result to the given Writer
	 *
     * @param name the template name
	 * @param model the holder of the variables visible from the template (name-value pairs); usually a
     *            {@code Map<String, Object>} or a JavaBean (where the JavaBean properties will be the variables)
	 * @param writer The {@link Writer} where the output of the template will go. Note that unless you have used
     *      {@link Configuration#setAutoFlush(boolean)} to disable this, {@link Writer#flush()} will be called at
     *      the when the template processing was finished. {@link Writer#close()} is not called. Can't be
     *      {@code null}.
	 */
	public static void generate(String name, Object model, Writer writer) throws IOException, TemplateException {
        configuration.getTemplate(name).process(model, writer);
    }

	/**
	 * Process the specified FreeMarker template with the given model and write the result to the given OutputStream
	 *
     * @param name the template name
	 * @param model the holder of the variables visible from the template (name-value pairs); usually a
     *            {@code Map<String, Object>} or a JavaBean (where the JavaBean properties will be the variables)
	 * @param outputStream the given OutputStream
	 */
	public static void generate(String name, Object model, OutputStream outputStream) throws IOException, TemplateException {
        generate(name, model, new BufferedWriter(new OutputStreamWriter(outputStream, "utf-8"), 10240));
	}

    /**
     * Set freemarker template configuration
     *
     * @param configuration the freemarker template configuration
     */
	@Autowired
    public void setConfiguration(Configuration configuration) {
        FreemarkerUtil.configuration = configuration;
    }
}
