package top.lshaci.framework.utils;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import lombok.extern.slf4j.Slf4j;
import top.lshaci.framework.utils.constants.Constants;

/**
 * Date utils<br><br>
 * 
 * <b>0.0.4:</b> Add method formateDate, formatMsecDate, Date and LocalDate interconvert
 * 
 * @author lshaci
 * @since 0.0.1
 * @version 0.0.4
 */
@Slf4j
public abstract class DateUtils {
	
	private final static ZoneId ZONE = ZoneId.systemDefault();

	/**
	 * Format millisecond date use <b>yyyy-MM-dd HH:mm:ss.SSS</b>
	 * 
	 * @param date the date
	 * @return the string type date
	 */
	public static String formatMsecDate(Date date) {
		if (date != null) {
			return Constants.MSEC_DATE_FORMATTER.format(date);
		}
		return null;
	}
	
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
	
	/**
	 * Convert Date to LocalDate
	 * 
	 * @param date the Date instance
	 * @return the LocalDate instance
	 */
	public static LocalDate date2LocalDate(Date date) {
		if (date == null) {
			log.warn("the date is null!");
			return null;
		}
		LocalDateTime localDateTime = date2LocalDateTime(date);
	    return localDateTime.toLocalDate();
	}
	
	/**
	 * Convert Date to LocalDateTime
	 * 
	 * @param date the Date instance
	 * @return the LocalDateTime instance
	 */
	public static LocalDateTime date2LocalDateTime(Date date) {
		if (date == null) {
			log.warn("the date is null!");
			return null;
		}
		Instant instant = date.toInstant();
		return LocalDateTime.ofInstant(instant, ZONE);
	}
	
	/**
	 * Convert LocalDate to Date
	 * 
	 * @param localDate the LocalDate instance
	 * @return the Date instance
	 */
	public static Date localDate2Date(LocalDate localDate) {
		if (localDate == null) {
			log.warn("the localDate is null!");
			return null;
		}
		Instant instant = localDate.atStartOfDay(ZONE).toInstant();
	    return Date.from(instant);
	}
	
	/**
	 * Convert LocalDateTime to Date
	 * 
	 * @param localDateTime the LocalDateTime instance
	 * @return the Date instance
	 */
	public static Date localDateTime2Date(LocalDateTime localDateTime) {
		if (localDateTime == null) {
			log.warn("the localDateTime is null!");
			return null;
		}
		Instant instant = localDateTime.atZone(ZONE).toInstant();
		return Date.from(instant);
	}
	
}
