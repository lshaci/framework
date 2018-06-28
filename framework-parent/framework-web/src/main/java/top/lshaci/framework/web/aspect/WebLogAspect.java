package top.lshaci.framework.web.aspect;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;

import com.alibaba.fastjson.JSON;

import lombok.extern.slf4j.Slf4j;
import top.lshaci.framework.web.utils.HttpRequestUtils;
import top.lshaci.framework.web.utils.SessionUserUtils;

/**
 * Web log aspect
 * 
 * @author lshaci
 * @since 0.0.4
 * @version 0.0.4
 */
@Slf4j
@Aspect
public class WebLogAspect {
	
	/**
	 * Save the start time of the request
	 */
	private final static ThreadLocal<Long> START_TIME = new ThreadLocal<>();

	/**
	 * The web log point cut
	 */
	@Pointcut("@annotation(org.springframework.web.bind.annotation.RequestMapping) "
			+ "|| @annotation(org.springframework.web.bind.annotation.GetMapping)"
			+ "|| @annotation(org.springframework.web.bind.annotation.PostMapping)")
	public void webLog() {
	}

	@Order(1)
	@Before("webLog()")
	public void doBefore(JoinPoint joinPoint) throws Throwable {
		START_TIME.set(System.currentTimeMillis());
		HttpServletRequest request = HttpRequestUtils.getRequest();
		Object userInSession = SessionUserUtils.getUserInSession();
		log.warn("***IN*********LOGIN USER: {}", JSON.toJSONString(userInSession));
		log.warn("***IN*********REQUEST URL: {}", request.getRequestURL());
		log.warn("***IN*********REQUEST METHOD: {}", request.getMethod());
		log.warn("***IN*********CLIENT IP: {}", HttpRequestUtils.getIp());
		log.warn("***IN*********REQUEST CONTROLLER: {}", joinPoint.getSignature().getDeclaringTypeName());
		log.warn("***IN*********REQUEST METHOD: {}", joinPoint.getSignature().getName());
		log.warn("***IN*********ARGS: {}", Arrays.toString(joinPoint.getArgs()));
	}

	@Order(1)
	@AfterReturning(returning = "ret", pointcut = "webLog()")
	public void doAfterReturning(Object ret) throws Throwable {
		log.warn("***OUT********RESPONSE: {}", ret);
		log.warn("***OUT********SPEND TIME: {}", (System.currentTimeMillis() - START_TIME.get()));
	}
}
