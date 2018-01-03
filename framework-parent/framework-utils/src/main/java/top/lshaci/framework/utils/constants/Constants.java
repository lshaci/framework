package top.lshaci.framework.utils.constants;

import java.text.SimpleDateFormat;

/**
 * Framework util Constants
 *
 * @author lshaci
 * @since 0.0.1
 */
public interface Constants {

    /**
     * Long date format string
     */
    String LONG_DATE_FORMAT_STR = "yyyy-MM-dd HH:mm:ss";

    /**
     * Short date format string
     */
    String SHORT_DATE_FORMAT_STR = "yyyy-MM-dd";

    /**
     * Long date formatter(yyyy-MM-dd HH:mm:ss)
     */
    SimpleDateFormat LONG_DATE_FORMATTER = new SimpleDateFormat(LONG_DATE_FORMAT_STR);

    /**
     * Short date formatter(yyyy-MM-dd)
     */
    SimpleDateFormat SHORT_DATE_FORMATTER = new SimpleDateFormat(SHORT_DATE_FORMAT_STR);
}
