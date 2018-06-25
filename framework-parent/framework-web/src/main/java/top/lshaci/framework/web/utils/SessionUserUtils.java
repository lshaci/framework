package top.lshaci.framework.web.utils;

import java.util.Objects;

import lombok.extern.slf4j.Slf4j;
import top.lshaci.framework.web.exception.WebBaseException;

/**
 * Session User Utils
 * 
 * @author lshaci
 * @since 0.0.4
 */
@Slf4j
public class SessionUserUtils {
	
	/**
	 * The key of user in session
	 */
	public final static String USER_IN_SESSION = SessionUserUtils.class.getName() + ".userInSession";

	/**
	 * Set user in session
	 * 
	 * @param user the user
	 */
	public static void setUserInSession(Object user) {
		Objects.requireNonNull(user, "The user must not be null!");
		HttpRequestUtils.setSessionAttribute(USER_IN_SESSION, user);
	}
	
	/**
	 * Get user in session
	 * 
	 * @return the user in session
	 */
	public static Object getUserInSession() {
		return HttpRequestUtils.getSessionAttribute(USER_IN_SESSION);
	}
	
	/**
	 * Get user in session
	 * 
	 * @param <T> the user class type
	 * @param userType the user class type
	 * 
	 * @return the user in session
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getUserInSession(Class<T> userType) {
		Object userInSession = getUserInSession();
		try {
			return (T) userInSession;
		} catch (Exception e) {
			log.error("The user type error!", e);
			throw new WebBaseException("The user type error!", e);
		}
	}
}
