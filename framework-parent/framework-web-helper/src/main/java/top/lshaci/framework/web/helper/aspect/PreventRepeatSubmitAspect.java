package top.lshaci.framework.web.helper.aspect;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.*;
import org.springframework.core.annotation.Order;
import top.lshaci.framework.web.common.utils.HttpRequestUtils;
import top.lshaci.framework.web.common.utils.HttpSessionUtils;
import top.lshaci.framework.web.helper.exception.RepeatSubmitException;
import top.lshaci.framework.web.helper.service.PreventRepeat;

import java.util.Objects;
import java.util.concurrent.locks.ReentrantLock;

/**
 * <p>Prevent repeat submit aspect</p>
 *
 * @author lshaci
 * @since 1.0.5
 */
@Slf4j
@Aspect
@Order(2)
@AllArgsConstructor
public class PreventRepeatSubmitAspect {

	/**
	 * 用于获取数据的锁
	 */
	private static ReentrantLock lock = new ReentrantLock();

    /**
     * 防重复
     */
    private final PreventRepeat preventRepeat;

	/**
	 * The prevent repeat submit point cut
	 */
	@Pointcut("@annotation(top.lshaci.framework.web.helper.annotation.PreventRepeatSubmit)")
	public void preventRepeatSubmit() {
	}

	@Before("preventRepeatSubmit()")
	public void doBefore() {
		String submitKey = submitKey();
		log.debug("PreventRepeatSubmitAspect: the submit key is: {}.", submitKey);
		try {
			lock.lock();
            String value = preventRepeat.getAndSet(submitKey);
			if (Objects.nonNull(value)) {
				log.warn("In operation...");
				throw new RepeatSubmitException();
			}
		} finally {
		    lock.unlock();
		}
	}

	@AfterReturning("preventRepeatSubmit()")
	public void doAfterReturning() {
		String submitKey = submitKey();
        preventRepeat.remove(submitKey);
		log.debug("Remove the submit key: {}.", submitKey);
	}

	@AfterThrowing(value = "preventRepeatSubmit()", throwing = "e")
	public void doAfterThrowing(Exception e) {
		if (e instanceof RepeatSubmitException) {
			log.warn("In operation...");
			return;
		}
		String submitKey = submitKey();
		preventRepeat.remove(submitKey);
		log.debug("Remove the submit key: {}.", submitKey);
	}

	/**
	 * 拼接提交地址的key
	 *
	 * @return 提交地址的key
	 */
	private String submitKey() {
		return HttpSessionUtils.get().getId() + ":" + HttpRequestUtils.get().getMethod() + ":" + HttpRequestUtils.get().getRequestURI();
	}
}
