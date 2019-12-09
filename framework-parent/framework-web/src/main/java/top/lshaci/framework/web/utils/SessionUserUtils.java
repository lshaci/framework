package top.lshaci.framework.web.utils;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import top.lshaci.framework.web.common.utils.HttpSessionUtils;
import top.lshaci.framework.web.exception.WebBaseException;

import java.util.Objects;

/**
 * Session User Utils<br><br>
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
	 * The key of user role in session
	 */
	public final static String USER_ROLE_IN_SESSION = SessionUserUtils.class.getName() + ".userRoleInSession";

	/**
	 * Set user in session
	 *
	 * @param user the user(This object will be converted to json string.)
	 */
	public static void setUserInSession(Object user) {
		Objects.requireNonNull(user, "The user must not be null!");
		String jsonUser = JSON.toJSONString(user);
		HttpSessionUtils.setAttribute(USER_IN_SESSION, jsonUser);
	}

	/**
	 * Get user in session
	 *
	 * @return the user in session(json string)
	 */
	public static String getUserInSession() {
		return (String) HttpSessionUtils.getAttribute(USER_IN_SESSION);
	}

	/**
	 * Get user in session
	 *
	 * @param <T> the user class type
	 * @param userType the user class type
	 *
	 * @return the user in session
	 */
	public static <T> T getUserInSession(Class<T> userType) {
		String userInSession = getUserInSession();
		try {
			return JSON.parseObject(userInSession, userType);
		} catch (Exception e) {
			log.error("The user type error!", e);
			throw new WebBaseException("The user type error!", e);
		}
	}

	/**
	 * Set user role in session
	 *
	 * @param role the user role
	 */
	public static void setUserRoleInSession(String role) {
		if (StringUtils.isNotBlank(role)) {
			HttpSessionUtils.setAttribute(USER_ROLE_IN_SESSION, role.trim());
		}
	}

	/**
	 * Get user role in session
	 *
	 * @return the user role in session
	 */
	public static String getUserRoleInSession() {
		return (String) HttpSessionUtils.getAttribute(USER_ROLE_IN_SESSION);
	}
}
