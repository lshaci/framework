package top.lshaci.framework.web.service;

/**
 * <p>This use for get, set, remove the submit key</p><br>
 *
 * <b>1.0.7:</b>This method <code>getAndSet</code> add parameter <code>timeout</code>
 *
 * @author lshaci
 * @since 1.0.5
 * @version 1.0.7
 */
public interface PreventRepeat {

    String VALUE = "true";

    /**
     * Get the submit key last value, and set next value
     *
     * @param key the submit key
     * @param timeout the operation timeout
     * @return the last value; if null, is the first submit
     */
    String getAndSet(String key, long timeout);

    /**
     * Remove the value of the submit key
     *
     * @param key  the submit key
     */
    void remove(String key);
}
