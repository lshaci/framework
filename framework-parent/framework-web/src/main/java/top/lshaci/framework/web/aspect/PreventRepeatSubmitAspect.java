package top.lshaci.framework.web.aspect;

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
 * <p>Prevent repeat submit aspect</p>
 * <b>1.0.1: </b>Change After aspect to AfterReturning aspect; Add AfterThrowing aspect
 * <b>1.0.2: </b>修改前缀和值
 *
 * @author lshaci
 * @since 0.0.4
 * @version 1.0.2
 */
@Slf4j
@Aspect
@Order(2)
public class PreventRepeatSubmitAspect {

	/**
	 * 请求Url的前缀(用于拼接url放入session中)
	 */
	private final static String SUBMIT_KEY_PREFIX = PreventRepeatSubmitAspect.class.getSimpleName() + "_";

	/**
	 * 操作中
	 */
	private final static String IN_OPERATION = "IN_OPERATION";

	/**
	 * The prevent repeat submit point cut
	 */
	@Pointcut("@annotation(top.lshaci.framework.web.annotation.PreventRepeatSubmit)")
	public void preventRepeatSubmit() {
	}

	@Before("preventRepeatSubmit()")
	public void doBefore() {
		String requestUrl = HttpRequestUtils.get().getRequestURI();
		log.debug("PreventRepeatSubmitAspect: the request url is: {}.", requestUrl);
		String submitKey = concatSubmitKey();
		Object value = HttpSessionUtils.getAttribute(concatSubmitKey());
		if (value != null) {
			log.warn("In operation...");
			throw new RepeatSubmitException();
		}

		HttpSessionUtils.setAttribute(submitKey, IN_OPERATION);
	}

	@AfterReturning("preventRepeatSubmit()")
	public void doAfterReturning() {
		HttpSessionUtils.removeAttribute(concatSubmitKey());
		log.debug("Remove submit token key from session is succeed.");
	}

	@AfterThrowing(value = "preventRepeatSubmit()", throwing = "e")
	public void doAfterThrowing(Exception e) {
		if (e instanceof RepeatSubmitException) {
			log.warn("In operation...");
			return;
		}
		HttpSessionUtils.removeAttribute(concatSubmitKey());
		log.debug("Remove submit token key from session is succeed.");
	}

	/**
	 * 拼接存入session中的key
	 *
	 * @return 放重复提交url的key
	 */
	private String concatSubmitKey() {
		return SUBMIT_KEY_PREFIX + HttpRequestUtils.get().getRequestURI();
	}
}
