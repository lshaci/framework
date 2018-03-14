package top.lshaci.framework.web.utils;

import java.util.Objects;

/**
 * Session User Utils
 * 
 * @author lshaci
 * @since 0.0.4
 */
public class SessionUserUtils {
	
	/**
	 * The key of user in session
	 */
	private final static String USER_IN_SESSION = SessionUserUtils.class.getName() + ".userInSession";

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
}
