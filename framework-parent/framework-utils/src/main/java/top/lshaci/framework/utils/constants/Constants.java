package top.lshaci.framework.utils.constants;

import java.text.SimpleDateFormat;

/**
 * Framework util Constants<br><br>
 * <b>0.0.4</b>: Add {@code MSEC_DATE_FORMAT_STR} and {@code MSEC_DATE_FORMATTER}
 *
 * @author lshaci
 * @since 0.0.1
 * @version 0.0.4
 */
public interface Constants {

	/**
	 * Millisecond date format string
	 */
	String MSEC_DATE_FORMAT_STR = "yyyy-MM-dd HH:mm:ss.SSS";
	
    /**
     * Long date format string
     */
    String LONG_DATE_FORMAT_STR = "yyyy-MM-dd HH:mm:ss";

    /**
     * Short date format string
     */
    String SHORT_DATE_FORMAT_STR = "yyyy-MM-dd";

    /**
     * Millisecond date formatter(yyyy-MM-dd HH:mm:ss.SSS)
     */
    SimpleDateFormat MSEC_DATE_FORMATTER = new SimpleDateFormat(MSEC_DATE_FORMAT_STR);
    
    /**
     * Long date formatter(yyyy-MM-dd HH:mm:ss)
     */
    SimpleDateFormat LONG_DATE_FORMATTER = new SimpleDateFormat(LONG_DATE_FORMAT_STR);

    /**
     * Short date formatter(yyyy-MM-dd)
     */
    SimpleDateFormat SHORT_DATE_FORMATTER = new SimpleDateFormat(SHORT_DATE_FORMAT_STR);
}
