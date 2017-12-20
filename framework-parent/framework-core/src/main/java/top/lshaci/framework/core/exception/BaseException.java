package top.lshaci.framework.core.exception;

/**
 * Framework Base Exception
 *
 * @author lshaci
 * @version 0.0.1
 */
public class BaseException extends RuntimeException {

    private static final long serialVersionUID = -7240441078832165350L;

    /**
     * Constructs a new base exception
     */
    public BaseException() {
        super();
    }

    /**
     * Constructs a new base exception with the specified detail message
     *
     * @param message the specified detail message
     */
    public BaseException(String message) {
        super(message);
    }
}
