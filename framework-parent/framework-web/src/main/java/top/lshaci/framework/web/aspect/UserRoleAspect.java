package top.lshaci.framework.web.aspect;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import top.lshaci.framework.web.annotation.IgnoreRole;
import top.lshaci.framework.web.annotation.NeedRole;
import top.lshaci.framework.web.common.utils.HttpRequestUtils;
import top.lshaci.framework.web.exception.RolePermissionException;
import top.lshaci.framework.web.utils.SessionUserUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Optional;

/**
 * User role aspect<br><br>
 *
 * @author lshaci
 * @since 0.0.4
 */
@Slf4j
@Aspect
@Order(3)
public class UserRoleAspect {

	/**
	 * The user role point cut
	 */
	@Pointcut("@within(top.lshaci.framework.web.annotation.NeedRole) || @annotation(top.lshaci.framework.web.annotation.NeedRole) ")
	public void userRole() {
	}

	@Before("userRole()")
	public void doBefore(JoinPoint joinPoint) throws Throwable {
		HttpServletRequest request = HttpRequestUtils.get();
		String requestUrl = request.getRequestURI();
		log.debug("The request url is: {}.", requestUrl);

		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		IgnoreRole ignoreRole = signature.getMethod().getAnnotation(IgnoreRole.class);
		if (ignoreRole != null) {
			log.debug("This method does not require role control: {}", signature.getName());
			return;
		}

		String userRole = SessionUserUtils.getUserRoleInSession();
		if (StringUtils.isBlank(userRole)) {
			throw new RolePermissionException();
		}

		// Get the target controller class
		Class<?> controllerClass = signature.getDeclaringType();

		NeedRole classNeedRole = controllerClass.getAnnotation(NeedRole.class);
		NeedRole needRole = signature.getMethod().getAnnotation(NeedRole.class);
		if (needRole == null) {
			needRole = classNeedRole;
		}

		String[] needRoles = needRole.value();
		Optional<String> optional = Arrays.stream(needRoles)
				.filter(r -> userRole.equalsIgnoreCase(r))
				.findFirst();

		if (!optional.isPresent()) {
			throw new RolePermissionException();
		}
	}

}
