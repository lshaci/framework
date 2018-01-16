package top.lshaci.framework.web.utils;

import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Http request utils
 * 
 * @author lshaci
 * @since 0.0.3
 */
public class HttpRequestUtils {

	/**
	 * Get the http servlet request
	 * 
	 * @return the http servlet request
	 */
	public static HttpServletRequest getRequest() {
		ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
				.getRequestAttributes();
		
		HttpServletRequest request = requestAttributes.getRequest();
		Objects.requireNonNull(request, "The http request is null!");
		
		return request;
	}
	
	/**
	 * Set value in http servlet request attribute
	 * 
	 * @param name the name
	 * @param value the value of the name
	 */
	public static void setRequestAttribute(String name, Object value) {
		HttpServletRequest request = getRequest();
		request.setAttribute(name, value);
	}
	
	/**
	 * Get value from http servlet request attribute with name
	 * 
	 * @param name the name
	 * @return the value of the name
	 */
	public static Object getRequestAttribute(String name) {
		HttpServletRequest request = getRequest();
		return request.getAttribute(name);
	}
	
	/**
	 * Get the http session
	 * 
	 * @return the http session
	 */
	public static HttpSession getSession() {
		HttpSession session = getRequest().getSession();
		Objects.requireNonNull(session, "The http session is null!");
		
		return session;
	}
	
	/**
	 * Set value in http session attribute
	 * 
	 * @param name the name
	 * @param value the value of the name
	 */
	public static void setSessionAttribute(String name, Object value) {
		HttpSession session = getSession();
		session.setAttribute(name, value);
	}
	
	/**
	 * Get value from http session attribute with name
	 * 
	 * @param name the name
	 * @return the value of the name
	 */
	public static Object getSessionAttribute(String name) {
		HttpSession session = getSession();
		return session.getAttribute(name);
	}
}
