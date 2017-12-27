package top.lshaci.framework.core.utils;

import java.util.Date;

import top.lshaci.framework.core.constants.Constants;

/**
 * Date utils
 * 
 * @author lshaci
 * @version 0.0.1
 */
public abstract class DateUtils {

	/**
	 * Format long date use {@Constants.LONG_DATE_FORMATTER} <b>yyyy-MM-dd HH:mm:ss</b>
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
	 * Format short date use {@Constants.SHORT_DATE_FORMATTER} <b>yyyy-MM-dd</b>
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
