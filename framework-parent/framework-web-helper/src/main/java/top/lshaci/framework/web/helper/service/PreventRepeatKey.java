package top.lshaci.framework.web.helper.service;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>This use for get prevent repeat submit key</p><br>
 *
 * @author lshaci
 * @since 1.0.7
 */
@FunctionalInterface
public interface PreventRepeatKey {

    /**
     * Get the submit key
     *
     * @param request the http servlet request
     * @return the prevent repeat submit key
     */
    String key(HttpServletRequest request);
}
