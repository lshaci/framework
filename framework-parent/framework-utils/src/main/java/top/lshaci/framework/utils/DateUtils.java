package top.lshaci.framework.utils;

import java.util.Date;

import top.lshaci.framework.utils.constants.Constants;

/**
 * Date utils
 * 
 * @author lshaci
 * @since 0.0.1
 */
public abstract class DateUtils {

	/**
	 * Format long date use <b>yyyy-MM-dd HH:mm:ss</b>
	 * 
	 * @param date the date
	 * @return the string type date
	 */
	public static String formatLongDate(Date date) {
		if (date != null) {
			return Constants.LONG_DATE_FORMATTER.format(date);
		}
		return null;
	}
	
	/**
	 * Format short date use <b>yyyy-MM-dd</b>
	 * 
	 * @param date the date
	 * @return the string type date
	 */
	public static String formatShortDate(Date date) {
		if (date != null) {
			return Constants.SHORT_DATE_FORMATTER.format(date);
		}
		return null;
	}
}
