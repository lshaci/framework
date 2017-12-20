package top.lshaci.framework.service.exception;

import top.lshaci.framework.core.exception.BaseException;

/**
 * Framework base service exception
 */
public class BaseServiceException extends BaseException {

    private static final long serialVersionUID = -6561904721408033517L;

    /**
     * Constructs a new base service exception
     */
    public BaseServiceException() {
    }

    /**
     * Constructs a new base service exception with the specified detail message
     *
     * @param message the specified detail message
     */
    public BaseServiceException(String message) {
        super(message);
    }
}
