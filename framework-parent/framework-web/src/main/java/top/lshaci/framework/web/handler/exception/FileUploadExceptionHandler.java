package top.lshaci.framework.web.handler.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MultipartException;

import lombok.extern.slf4j.Slf4j;
import top.lshaci.framework.web.model.JsonResponse;

/**
 * File upload exception handler
 *
 * @author lshaci
 * @since 0.0.4
 */
@Slf4j
@ControllerAdvice
public class FileUploadExceptionHandler {

    @ExceptionHandler(MultipartException.class)
    public JsonResponse<Object> handleError(MultipartException e) {
        log.error("文件大小超出限制", e);
        return JsonResponse.failure("文件大小超出限制");
    }
}
