package com.rossotti.basketball.util.function;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;

public class DateTimeConverter {

	private static final DateTimeFormatter dateNakedFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
	private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
	
	static public String getStringDate(LocalDate date) {
		return date.format(dateFormatter);
	}

	static public String getStringDate(LocalDateTime dateTime) {
		return dateTime.format(dateFormatter);
	}

	static public String getStringDateNaked(LocalDateTime dateTime) {
		return dateTime.format(dateNakedFormatter);
	}

	static public String getStringDateNaked(LocalDate date) {
		return date.format(dateNakedFormatter);
	}

	static public String getStringDateTime(LocalDateTime dateTime) {
		return dateTime.format(dateTimeFormatter);
	}

	static public LocalDate getLocalDate(String strDate) {
		return LocalDate.parse(strDate);
	}

	static public LocalDate getLocalDate(LocalDateTime dateTime) {
		return dateTime.toLocalDate();
	}

	static public LocalDateTime getLocalDateTime(String strDateTime) {
		return LocalDateTime.parse(strDateTime);
	}

	static public LocalDateTime getLocalDateTime(ZonedDateTime zonedDateTime) { return LocalDateTime.ofInstant(zonedDateTime.toInstant(), ZoneId.of("US/Eastern")); }

	static public LocalDateTime getLocalDateTimeMin(LocalDate localDate) {
		String stringDate = DateTimeConverter.getStringDate(localDate);
		return LocalDateTime.parse(stringDate + "T00:00");
	}

	static public LocalDateTime getLocalDateTimeMax(LocalDate localDate) {
		String stringDate = DateTimeConverter.getStringDate(localDate);
		return LocalDateTime.parse(stringDate + "T23:59");
	}

	static public LocalDate getLocalDateSeasonMin(LocalDate localDate) {
		if (localDate.getMonthValue() <= 6) {
			return LocalDate.of(localDate.getYear() - 1, 7, 1);
		}
		else {
			return LocalDate.of(localDate.getYear(), 7, 1);
		}
	}

	static public LocalDate getLocalDateSeasonMax(LocalDate localDate) {
		if (localDate.getMonthValue() <= 6) {
			return LocalDate.of(localDate.getYear(), 6, 30);
		}
		else {
			return LocalDate.of(localDate.getYear() + 1, 6, 30);
		}
	}

	static public LocalDateTime getLocalDateTimeSeasonMin(LocalDate localDate) {
		if (localDate.getMonthValue() <= 6) {
			return LocalDateTime.of(localDate.getYear() - 1, 7, 1, 0, 0);
		}
		else {
			return LocalDateTime.of(localDate.getYear(), 7, 1, 0, 0);
		}
	}

	static public LocalDateTime getLocalDateTimeSeasonMax(LocalDate localDate) {
		if (localDate.getMonthValue() <= 6) {
			return LocalDateTime.of(localDate.getYear(), 6, 30, 23, 59);
		}
		else {
			return LocalDateTime.of(localDate.getYear() + 1, 6, 30, 23, 59);
		}
	}

//	static public String getSeason(LocalDate date) {
//		LocalDate minDate = getDateMinSeason(date);
//		String minYear = minDate.toString(DateTimeFormat.forPattern("yyyy"));
//		
//		LocalDate  maxDate = getDateMaxSeason(date);
//		String maxYear = maxDate.toString(DateTimeFormat.forPattern("yy"));
//		
//		return minYear + "-" + maxYear; 
//	}

	static public boolean isDate(String strDate)  {
		try {
			LocalDate.parse(strDate);
			return true;
		}
		catch (DateTimeParseException e) {
			return false;
		}
	}

	static public LocalDate getDateMinusOneDay(LocalDate date) {
		return date.minusDays(1);
	}

//	static public LocalDate getLocalDateFromDateTime(DateTime date) {
//		return new LocalDate(date.getYear(), date.getMonthOfYear(), date.getDayOfMonth());
//	}

	static public int getDaysBetweenTwoDateTimes(LocalDateTime minDate, LocalDateTime maxDate) {
		if(minDate != null) {
			int days = (int) ChronoUnit.DAYS.between(minDate, maxDate);
			if (days < 30) {
				return days;
			} else {
				return 0;
			}
		}
		else {
			return 0;
		}
	}
}