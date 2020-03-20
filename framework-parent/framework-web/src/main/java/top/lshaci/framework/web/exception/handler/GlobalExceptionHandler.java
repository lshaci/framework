package top.lshaci.framework.web.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import top.lshaci.framework.common.exception.BaseException;
import top.lshaci.framework.common.model.JsonResponse;
import top.lshaci.framework.web.model.ExceptionMessage;
import top.lshaci.framework.web.utils.GlobalExceptionUtils;

/**
 * Global exception handler<br><br>
 *
 * <b>0.0.4: </b>Add method argumentExceptionHandler<br>
 * <b>1.0.4: </b>使用GlobalExceptionUtils来获取自定义的异常消息
 *
 * @author lshaci
 * @since 0.0.3
 * @version 1.0.4
 */
@Slf4j
@RestController
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * System exception log message
     */
    private static final String SYSTEM_EXCEPTION = "System anomaly!";

    /**
     * Argument exception log message
     */
	private final static String FIELD = "字段:";

	/**
	 * Base exception handler
	 *
	 * @param e the exception
	 * @return json response
	 */
    @ExceptionHandler(BaseException.class)
    public JsonResponse<Object> baseExceptionHandler(BaseException e) {
        return JsonResponse
        		.failure(e.getMessage())
        		.setCode(e.getCode());
    }

	/**
	 * BindException and MethodArgumentNotValidException handler
	 *
	 * @param e the exception
	 * @return json response
	 */
    @ExceptionHandler(value = { BindException.class, MethodArgumentNotValidException.class })
    public JsonResponse<Object> argumentExceptionHandler(Exception e) {
    	log.error(SYSTEM_EXCEPTION, e);

        StringBuilder message = new StringBuilder();
        FieldError fieldError = null;

        if (e instanceof BindException) {
            BindException bindException = (BindException) e;
            fieldError = bindException.getBindingResult().getFieldErrors().get(0);
        } else if (e instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException argumentNotValidException = (MethodArgumentNotValidException) e;
            fieldError = argumentNotValidException.getBindingResult().getFieldErrors().get(0);
        }

        if (fieldError != null) {
        	String field = fieldError.getField();
        	log.warn(FIELD + field);
        	String msg = fieldError.getDefaultMessage();
        	message.append(msg);
		}

        return JsonResponse.failure(message.toString());
    }

	/**
	 * Default exception handler
	 *
	 * @param e the exception
	 * @return json response
	 */
    @ExceptionHandler(Exception.class)
    public JsonResponse<Object> defaultExceptionHandler(Exception e) {
    	log.error(SYSTEM_EXCEPTION, e);

		ExceptionMessage message = GlobalExceptionUtils.get(e.getClass());

		return JsonResponse
    			.failure(message.getMessage())
    			.setCode(message.getCode())
    			.addOtherData("detail", e.getMessage());
    }

}
