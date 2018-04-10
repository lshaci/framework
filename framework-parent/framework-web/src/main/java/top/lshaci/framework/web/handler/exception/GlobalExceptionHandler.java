package top.lshaci.framework.web.handler.exception;

import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import top.lshaci.framework.common.exception.BaseException;
import top.lshaci.framework.web.enums.ErrorCode;
import top.lshaci.framework.web.model.JsonResponse;

/**
 * Global exception handler
 * 
 * @author lshaci
 * @since 0.0.3
 */
@ConditionalOnProperty(value = "globalExceptionHandler.enabled", havingValue = "true", matchIfMissing = false)
@RestController
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

	/**
	 * Base exception handler
	 * 
	 * @param req the http servlet request
	 * @param e the exception
	 * @return json response
	 */
    @ExceptionHandler({BaseException.class})
    public JsonResponse baseExceptionHandler(HttpServletRequest req, Exception e) {
    	log.error("System be happend exception!", e);
    	
    	return JsonResponse
        		.failure(e.getMessage())
        		.setCode(ErrorCode.INTERNAL_PROGRAM_ERROR.getCode());
    }

	/**
	 * Default exception handler
	 * 
	 * @param req the http servlet request
	 * @param e the exception
	 * @return json response
	 */
    @ExceptionHandler({Exception.class})
    public JsonResponse defaultExceptionHandler(HttpServletRequest req, Exception e) {
    	log.error("System be happend exception!", e);
    	
    	ErrorCode errorCode = ErrorCode.getByException(e);
    	
    	return JsonResponse
    			.failure(errorCode.getMsg())
    			.setCode(errorCode.getCode())
    			.addParam("detail", e.getMessage());
    }
}
