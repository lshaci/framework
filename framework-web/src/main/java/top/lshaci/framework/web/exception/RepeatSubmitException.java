package top.lshaci.framework.web.exception;

/**
 * Repeat Submit Exception
 *
 * @author lshaci
 * @since 1.0.5
 */
public class RepeatSubmitException extends RuntimeException {

	private static final long serialVersionUID = -547471788824710478L;

	/**
	 * The repeat submit exception default message
	 */
	private static final String MESSAGE = "上次操作未完成，请勿重复操作";

	/**
	 * Constructor a repeat submit exception with the default message.
	 */
	public RepeatSubmitException() {
		super(MESSAGE);
	}

}
