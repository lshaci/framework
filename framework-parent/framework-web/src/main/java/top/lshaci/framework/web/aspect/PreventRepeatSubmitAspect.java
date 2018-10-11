package top.lshaci.framework.web.aspect;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;

import lombok.extern.slf4j.Slf4j;
import top.lshaci.framework.web.exception.WebBaseException;
import top.lshaci.framework.web.utils.HttpRequestUtils;
import top.lshaci.framework.web.utils.HttpSessionUtils;

/**
 * Prevent repeat submit aspect<br><br>
 * 
 * @author lshaci
 * @since 0.0.4
 */
@Slf4j
@Aspect
public class PreventRepeatSubmitAspect {
	
	/**
	 * Repeat operation prompt message
	 */
	private final static String REPEAT_SUBMIT_MESSAGE = "上次操作未完成，请勿重复操作";
	
	/**
	 * Submit token prefix
	 */
	private final static String SUBMIT_TOKEN_PREFIX = "SUBMIT_TOKEN";
	
	/**
	 * Submit token separator
	 */
	private final static String SUBMIT_TOKEN_SEPARATOR = "_";
	
	/**
	 * The prevent repeat submit point cut
	 */
	@Pointcut("@annotation(top.lshaci.framework.web.annotation.PreventRepeatSubmit)")
	public void preventRepeatSubmit() {
	}

	@Order(2)
	@Before("preventRepeatSubmit()")
	public void doBefore(JoinPoint joinPoint) throws Throwable {
		HttpServletRequest request = HttpRequestUtils.get();
		String requestUrl = request.getRequestURI();
		log.info("The request url is: {}.", requestUrl);
		Object tokenKey = HttpSessionUtils.getAttribute(requestUrl);
		if (tokenKey != null) {
		    log.warn("In operation...");
			throw new WebBaseException(REPEAT_SUBMIT_MESSAGE);
		}
		String token = new StringBuilder()
				.append(SUBMIT_TOKEN_PREFIX)
				.append(SUBMIT_TOKEN_SEPARATOR)
				.append(UUID.randomUUID().toString())
				.append(SUBMIT_TOKEN_SEPARATOR)
				.append(System.currentTimeMillis())
				.toString();
		HttpSessionUtils.setAttribute(requestUrl, token);
	}

	@Order(2)
	@AfterReturning("preventRepeatSubmit()")
	public void doAfterReturning() throws Throwable {
		String requestUrl = HttpRequestUtils.get().getRequestURI();
		HttpSessionUtils.removeAttribute(requestUrl);
		log.info("Remove submit token key from session is succeed.");
	}
}
