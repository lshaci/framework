package top.lshaci.framework.web.utils;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * Http request utils<br><br>
 *
 * @author lshaci
 * @since 0.0.3
 * @version 0.0.4
 */
public class HttpRequestUtils {

	/**
	 * Get the http servlet request
	 *
	 * @return the http servlet request
	 */
	public static HttpServletRequest get() {
		ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
				.getRequestAttributes();

		HttpServletRequest request = requestAttributes.getRequest();
		Objects.requireNonNull(request, "The http request is null!");

		return request;
	}

	/**
	 * Set value in http servlet request attribute
	 *
	 * @param key the key of the request attribute
	 * @param value the value of the name
	 */
	public static void setAttribute(String key, Object value) {
		HttpServletRequest request = get();
		request.setAttribute(key, value);
	}

	/**
	 * Get value from http servlet request attribute with the key
	 *
	 * @param key the key of the request attribute
	 * @return the value of the name
	 */
	public static Object getAttribute(String key) {
		HttpServletRequest request = get();
		return request.getAttribute(key);
	}

	/**
	 * Remove value from http servlet request attribute with the key
	 *
	 * @param key the key of the request attribute
	 */
	public static void removeAttribute(String key) {
		HttpServletRequest request = get();
		request.removeAttribute(key);
	}

	/**
	 * Get the client IP
	 *
	 * @return ip of the client
	 */
	public static String getIp() {
	    HttpServletRequest request = get();
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip =  request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
		return ip;
	}
}
