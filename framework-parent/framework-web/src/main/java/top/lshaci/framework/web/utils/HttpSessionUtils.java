package top.lshaci.framework.web.utils;

import java.util.Objects;

import javax.servlet.http.HttpSession;

/**
 * Http request utils<br><br>
 * 
 * @author lshaci
 * @since 0.0.4
 */
public class HttpSessionUtils {

	/**
	 * Get the http session
	 * 
	 * @return the http session
	 */
	public static HttpSession get() {
		HttpSession session = HttpRequestUtils.get().getSession();
		Objects.requireNonNull(session, "The http session is null!");
		
		return session;
	}
	
	/**
	 * Set value in http session attribute
	 * 
	 * @param key the key of the session attribute
	 * @param value the value of the name
	 */
	public static void setAttribute(String key, Object value) {
		HttpSession session = get();
		session.setAttribute(key, value);
	}
	
	/**
	 * Get value from http session attribute with the key
	 * 
	 * @param key the key of the session attribute
	 * @return the value of the name
	 */
	public static Object getAttribute(String key) {
		HttpSession session = get();
		return session.getAttribute(key);
	}
	
	/**
	 * Remove value from http session attribute with the key
	 * 
	 * @param key the key of the session attribute
	 */
	public static void removeAttribute(String key) {
		HttpSession session = get();
		session.removeAttribute(key);
	}
	
	/**
	 * Destroy session
	 */
	public static void destroy() {
		HttpSession session = get();
		session.invalidate();
	}
	
}
