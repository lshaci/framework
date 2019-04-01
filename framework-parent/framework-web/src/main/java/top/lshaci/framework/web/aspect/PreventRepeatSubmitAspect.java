package top.lshaci.framework.web.aspect;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;

import lombok.extern.slf4j.Slf4j;
import top.lshaci.framework.web.exception.RepeatSubmitException;
import top.lshaci.framework.web.utils.HttpRequestUtils;
import top.lshaci.framework.web.utils.HttpSessionUtils;

/**
 * Prevent repeat submit aspect<br><br>
 * <b>1.0.1: </b>Change After aspect to AfterReturning aspect; Add AfterThrowing aspect
 * 
 * @author lshaci
 * @since 0.0.4
 * @version 1.0.1
 */
@Slf4j
@Aspect
@Order(2)
public class PreventRepeatSubmitAspect {
	
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

	@Before("preventRepeatSubmit()")
	public void doBefore() {
		HttpServletRequest request = HttpRequestUtils.get();
		String requestUrl = request.getRequestURI();
		log.info("PreventRepeatSubmitAspect: the request url is: {}.", requestUrl);
		Object tokenKey = HttpSessionUtils.getAttribute(requestUrl);
		if (tokenKey != null) {
			log.warn("In operation...");
			throw new RepeatSubmitException();
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

	@AfterReturning("preventRepeatSubmit()")
	public void doAfterReturning() {
		String requestUrl = HttpRequestUtils.get().getRequestURI();
		HttpSessionUtils.removeAttribute(requestUrl);
		log.info("Remove submit token key from session is succeed.");
	}

	@AfterThrowing(value = "preventRepeatSubmit()", throwing = "e")
	public void doAfterThrowing(Exception e) {
		if (e instanceof RepeatSubmitException) {
			log.warn("In operation...");
			return;
		}
		String requestUrl = HttpRequestUtils.get().getRequestURI();
		HttpSessionUtils.removeAttribute(requestUrl);
		log.info("Remove submit token key from session is succeed.");
	}
}
