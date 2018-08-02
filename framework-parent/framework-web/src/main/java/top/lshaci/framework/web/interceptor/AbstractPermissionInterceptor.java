package top.lshaci.framework.web.interceptor;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;
import top.lshaci.framework.permission.annotation.NeedRole;
import top.lshaci.framework.permission.annotation.ResourceName;
import top.lshaci.framework.permission.enums.PermissionType;
import top.lshaci.framework.permission.model.Resource;
import top.lshaci.framework.permission.model.Role;
import top.lshaci.framework.permission.service.ResourceService;
import top.lshaci.framework.permission.service.RoleService;
import top.lshaci.framework.permission.utils.ResourceUtils;
import top.lshaci.framework.web.constant.WebConstant;
import top.lshaci.framework.web.enums.ErrorCode;
import top.lshaci.framework.web.model.JsonResponse;
import top.lshaci.framework.web.utils.HttpRequestUtils;
import top.lshaci.framework.web.utils.HttpResponseUtils;

/**
 * Permission Interceptor
 * 
 * @author lshaci
 * @since 0.0.4
 */
@Slf4j
public abstract class AbstractPermissionInterceptor implements HandlerInterceptor {
	
	/**
	 * The role service
	 */
	public static RoleService roleService;
	
	/**
	 * The resource service
	 */
	public static ResourceService resourceService;
	
	/**
	 * Cache permission or not, default is true
	 */
	private boolean cachePermission = true;
	
	/**
	 * Permission type, default is role
	 */
	private PermissionType permissionType = PermissionType.ROLE;
	
	/**
	 * No permission redirect url
	 */
	private String noPermissionUrl;
	
	/**
	 * The key of permission[role] in session
	 */
	private final static String ROLE_IN_SESSION =  AbstractPermissionInterceptor.class.getName() + ":ROLE";
	
	/**
	 * The key of permission[resource] in session
	 */
	private final static String RESOURCE_IN_SESSION =  AbstractPermissionInterceptor.class.getName() + ":RESOURCE";
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		log.debug("PermissionInterceptor: " + request.getRequestURI());
		
		if (!(handler instanceof HandlerMethod)) {
			log.warn("This request does not access controller!");
			return true;
		}
		
		boolean hasRole = false;
		HandlerMethod handlerMethod = (HandlerMethod) handler;
		
		if (WebConstant.SWAGGER_CONTROLLER.equals(handlerMethod.getBeanType().getName())) {
			log.warn("This request is to access the swagger ui!");
			return true;
		}
		
		if (PermissionType.ROLE.equals(permissionType)) {
			hasRole = roleType(handlerMethod.getMethod());
		} else if (PermissionType.RESOURCE.equals(permissionType)) {
			hasRole = resourceType(handlerMethod);
		}
		
		if (hasRole) {
			return true;
		} else {
			log.warn("No permission.");
			/*
			 * Determines whether the user exists, does not exist to return to the login
			 * interface, continues to intercept, exists by intercepting, released to the
			 * access page.
			 */
			if (isAjaxRequest(request)) {
				log.info("This request is an ajax request.");
	
				JsonResponse jsonResponse = JsonResponse
						.failure(ErrorCode.NO_PERMISSION_EXCEPTION.getMsg())
						.setCode(ErrorCode.NO_PERMISSION_EXCEPTION.getCode())
						.addParam("redirectUrl", noPermissionUrl);
	
				log.warn("No permission, response json.");
				HttpResponseUtils.responseJson(jsonResponse);
			} else {
				log.warn("No permission, redirect no permission page.");
				response.sendRedirect(noPermissionUrl);
			}
		}
		
		return false;
	}

	/**
	 * Determine the login user have role permissions
	 * 
	 * @param handlerMethod the access handler method
	 * @return if has permission return true
	 */
	@SuppressWarnings("unchecked")
	private boolean roleType(Method method) {
		NeedRole needRole = method.getAnnotation(NeedRole.class);
		if (needRole == null || ArrayUtils.isEmpty(needRole.value())) {
            log.info("No permissions required.");
            return true;
        }
		
		List<String> userRoleList = (List<String>) HttpRequestUtils.getSessionAttribute(ROLE_IN_SESSION);
		if (userRoleList == null) {
			userRoleList = roleService.selectByUser(getUser())
					.stream()
					.map(Role::getName)
					.collect(Collectors.toList());
			if (cachePermission) {
				HttpRequestUtils.setSessionAttribute(ROLE_IN_SESSION, userRoleList);
			}
		}
		
		List<String> needRoleList = new ArrayList<>(Arrays.asList(needRole.value()));
		needRoleList.retainAll(userRoleList);
		
		return needRoleList.size() > 0;
	}
	
	/**
	 * Determine the login user have resource permissions
	 * 
	 * @param handlerMethod the access handler method
	 * @return if has permission return true
	 */
	@SuppressWarnings("unchecked")
	private boolean resourceType(HandlerMethod handlerMethod) {
		Method method = handlerMethod.getMethod();
		ResourceName resourceName = method.getAnnotation(ResourceName.class);
        if (resourceName == null) {
            log.info("No permissions required.");
            return true;
        }
		Class<?> controllerClass = handlerMethod.getBeanType();
		
		List<String> userReourceList = (List<String>) HttpRequestUtils.getSessionAttribute(RESOURCE_IN_SESSION);
		if (userReourceList == null) {
			userReourceList = resourceService.selectByUser(getUser())
					.stream()
					.map(Resource::getResource)
					.collect(Collectors.toList());
			if (cachePermission) {
				HttpRequestUtils.setSessionAttribute(RESOURCE_IN_SESSION, userReourceList);
			}
		}
		
		List<String> needResourceList = new ArrayList<>(Arrays.asList(
				controllerClass.getName() + ":" + method.getName(), 
				controllerClass.getName() + ResourceUtils.RESOURCE_ALL_SUFFIX));
		needResourceList.retainAll(userReourceList);
		
		return needResourceList.size() > 0;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {

	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {

	}
	
	public AbstractPermissionInterceptor setCachePermission(boolean cachePermission) {
		this.cachePermission = cachePermission;
		return this;
	}
	
	public AbstractPermissionInterceptor setPermissionType(PermissionType permissionType) {
		Objects.requireNonNull(permissionType, "The permission type must not be null!");
		this.permissionType = permissionType;
		return this;
	}
	
	/**
	 * Get the unique identity of the user
	 * 
	 * @return the unique identity of the user
	 */
	protected abstract String getUser();
	
	/**
	 * Determine whether an ajax request is made.
	 * 
	 * @param request the http servlet request<br><br>
	 * <i><b>For example:</b></i><br>
	 * <code>
	 * 		request.getHeader("x-requested-with") != null &amp;&amp; 
	 * 		request.getHeader("x-requested-with").equalsIgnoreCase("XMLHttpRequest")
	 * </code>
	 * @return if true means this request is ajax request
	 */
	protected abstract boolean isAjaxRequest(HttpServletRequest request);
	
}

