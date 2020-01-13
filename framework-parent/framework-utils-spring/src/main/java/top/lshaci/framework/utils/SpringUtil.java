package top.lshaci.framework.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * <p>Utility class for get bean.</p>
 *
 * @author lshaci
 * @since 1.0.7
 */
public class SpringUtil implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringUtil.applicationContext = applicationContext;
    }

    /**
     * 获取applicationContext
     */
    /**
     * Get application context
     *
     * @return the application context
     */
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * Get bean by the given name
     *
     * @param name the name of the bean to retrieve
     * @return an instance of the bean
     */
    public static Object getBean(String name) {
        return applicationContext.getBean(name);
    }

    /**
     * Get bean by the given object type
     *
     * @param clazz type the bean must match; can be an interface or superclass
     * @return an instance of the single bean matching the required type
     */
    public static <T> T getBean(Class<T> clazz) {
        return applicationContext.getBean(clazz);
    }

    /**
     * Get bean by the name and given object type
     *
     * @param name the name of the bean to retrieve
     * @param clazz type the bean must match; can be an interface or superclass
     * @return an instance of the bean
     */
    public static <T> T getBean(String name, Class<T> clazz) {
        return applicationContext.getBean(name, clazz);
    }


}
