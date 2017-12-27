package top.lshaci.framework.common.constants;

import java.text.SimpleDateFormat;

/**
 * Framework-core Constants
 *
 * @author lshaci
 * @version 0.0.1
 */
public interface Constants {

    /**
     * The page query default page size
     */
    int DEFAULT_PGSZ = 10;

    /**
     * The page query default current page number
     */
    int DEFAULT_PGCT = 1;

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
