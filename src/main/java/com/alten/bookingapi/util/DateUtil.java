/**
 * 
 */
package com.alten.bookingapi.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;

/**
 * @author Danilo Cavalcanti
 *
 */
public class DateUtil {

	public static final String DATE_PATTERN = "yyyy-MM-dd";
	
	public static boolean isValid(String date) {
		
		if (Objects.isNull(date) || date.isBlank()) return false;

		DateTimeFormatter dtf = DateTimeFormatter.ofPattern(DATE_PATTERN);

		try {

			LocalDate.parse(date, dtf);

		} catch (DateTimeParseException e) {

			return false;
		}

		return true;
	}

	public static LocalDate toLocalDate(String date) {

		if (Objects.isNull(date))
			return null;

		DateTimeFormatter dtf = DateTimeFormatter.ofPattern(DATE_PATTERN);

		try {

			return LocalDate.parse(date, dtf);

		} catch (DateTimeParseException e) {

			return null;
		}
	}

	public static String toStringDate(LocalDate date) {

		if (Objects.isNull(date))
			return null;

		return date.format(DateTimeFormatter.ofPattern(DATE_PATTERN));
	}
}
