package top.lshaci.framework.web.helper.service;

/**
 * This use for get, set, remove the submit key
 *
 * @author lshaci
 * @since 1.0.5
 */
public interface PreventRepeat {

    String VALUE = "true";

    /**
     * Get the submit key last value, and set next value
     *
     * @param key the submit key
     * @return the last value; if null, is the first submit
     */
    String getAndSet(String key);

    /**
     * Remove the value of the submit key
     *
     * @param key  the submit key
     */
    void remove(String key);
}
