package top.lshaci.framework.utils.constants;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;

/**
 * <p>Framework util Constants</p>
 * <p><b>0.0.4: </b>Add {@code MSEC_DATE_FORMAT_STR} and {@code MSEC_DATE_FORMATTER}</p>
 * <p><b>1.0.2: </b>添加DateTimeFormatter</p>
 *
 * @author lshaci
 * @since 0.0.1
 * @version 1.0.2
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
    
    /**
     * Msec date time formatter(yyyy-MM-dd HH:mm:ss.SSS)
     */
    DateTimeFormatter MSEC_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(MSEC_DATE_FORMAT_STR);
    
    /**
     * Long date time formatter(yyyy-MM-dd HH:mm:ss)
     */
    DateTimeFormatter LONG_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(LONG_DATE_FORMAT_STR);
    
    /**
     * Short date time formatter(yyyy-MM-dd)
     */
    DateTimeFormatter SHORT_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(SHORT_DATE_FORMAT_STR);
}
