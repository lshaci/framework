package top.lshaci.framework.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import lombok.extern.slf4j.Slf4j;
import top.lshaci.framework.utils.constants.Constants;

/**
 * Date utils<br><br>
 * 
 * version 0.0.4: add method formateDate
 * 
 * @author lshaci
 * @since 0.0.1
 * @version 0.0.4
 */
@Slf4j
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
	
	/**
	 * Format date with pattern
	 * 
	 * @param date the date
	 * @param pattern the pattern
	 * @return the string type date
	 */
	public static String formatDate(Date date, String pattern) {
		if (date == null) {
			log.warn("the date is null!");
			return null;
		}
		
		if (StringUtils.isBlank(pattern)) {
			log.warn("the pattern is empty!");
			return null;
		}
		
		try {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
			return simpleDateFormat.format(date);
		} catch (Exception e) {
			log.error("Format date error!", e);
		}
		return null;
	}
}
