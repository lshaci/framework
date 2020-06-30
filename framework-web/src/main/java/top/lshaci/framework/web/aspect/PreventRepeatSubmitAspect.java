package top.lshaci.framework.web.aspect;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.*;
import org.springframework.core.annotation.Order;
import top.lshaci.framework.web.annotation.PreventRepeatSubmit;
import top.lshaci.framework.web.exception.RepeatSubmitException;
import top.lshaci.framework.web.service.PreventRepeat;
import top.lshaci.framework.web.service.PreventRepeatKey;
import top.lshaci.framework.web.utils.HttpRequestUtils;

import java.util.Objects;

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
     * 防重复
     */
    private final PreventRepeat preventRepeat;

    /**
     * 防重复key
     */
    private final PreventRepeatKey preventRepeatKey;

	/**
	 * The prevent repeat submit point cut
	 */
	@Pointcut("@annotation(top.lshaci.framework.web.annotation.PreventRepeatSubmit)")
	public void preventRepeatSubmit() {
	}

	@Before("@annotation(preventRepeatSubmit)")
	public void doBefore(PreventRepeatSubmit preventRepeatSubmit) {
		String submitKey = submitKey();
		log.debug("PreventRepeatSubmitAspect: the submit key is: {}.", submitKey);

        String value = preventRepeat.getAndSet(submitKey, preventRepeatSubmit.timeout());
        if (Objects.nonNull(value)) {
            log.warn("In operation...");
            throw new RepeatSubmitException();
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
		return preventRepeatKey.key(HttpRequestUtils.get());
	}
}
