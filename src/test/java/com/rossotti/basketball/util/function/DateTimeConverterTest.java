package com.rossotti.basketball.util.function;

import org.junit.Assert;
import org.junit.Test;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

public class DateTimeConverterTest {

	@Test
	public void getStringDate_FromDate() {
		String date = DateTimeConverter.getStringDate(new LocalDate(2013, 3, 30));
		Assert.assertEquals("2013-03-30", date);
	}

	@Test
	public void getStringDate_FromDateTime() {
		String dateTime = DateTimeConverter.getStringDate(new LocalDateTime(2013, 3, 30, 10, 30));
		Assert.assertEquals("2013-03-30", dateTime);
	}

	@Test
	public void getStringDateTime() {
		String dateTime = DateTimeConverter.getStringDateTime(new LocalDateTime(2013, 3, 30, 10, 30));
		Assert.assertEquals("2013-03-30T10:30", dateTime);
	}

	@Test
	public void getStringDateNaked_FromDateTime() {
		String dateTime = DateTimeConverter.getStringDateNaked(new LocalDateTime(2013, 3, 30, 10, 30));
		Assert.assertEquals("20130330", dateTime);
	}

	@Test
	public void getStringDateNaked_FromDate() {
		String date = DateTimeConverter.getStringDateNaked(new LocalDate(2013, 3, 30));
		Assert.assertEquals("20130330", date);
	}

	@Test
	public void getLocalDate_FromString() {
		LocalDate date = DateTimeConverter.getLocalDate("2014-06-30");
		Assert.assertEquals(new LocalDate(2014, 6, 30), date);
	}

	@Test
	public void getLocalDate_FromLocalDateTime() {
		LocalDateTime dateTime = DateTimeConverter.getLocalDateTime("2014-06-30T10:30");
		Assert.assertEquals(new LocalDate(2014, 6, 30), DateTimeConverter.getLocalDate(dateTime));
	}

	@Test
	public void getLocalDateTime_FromLocalDateTime() {
		LocalDateTime dateTime = DateTimeConverter.getLocalDateTime("2014-06-30T10:30");
		Assert.assertEquals(new LocalDateTime(2014, 6, 30, 10, 30), dateTime);
	}

	@Test
	public void getLocalDateTimeMin() {
		LocalDateTime dateTime = DateTimeConverter.getLocalDateTimeMin(new LocalDate(2014, 6, 30));
		Assert.assertEquals(new LocalDateTime(2014, 6, 30, 0, 0), dateTime);
	}

	@Test
	public void getLocalDateTimeMax() {
		LocalDateTime dateTime = DateTimeConverter.getLocalDateTimeMax(new LocalDate(2014, 6, 30));
		Assert.assertEquals(new LocalDateTime(2014, 6, 30, 23, 59), dateTime);
	}

	@Test
	public void getLocalDateSeasonMin_SeasonStart() {
		LocalDate date = DateTimeConverter.getLocalDateSeasonMin(new LocalDate(2013, 7, 1));
		Assert.assertEquals(new LocalDate(2013, 7, 1), date);
	}
	@Test
	public void getLocalDateSeasonMin_YearEnd() {
		LocalDate date = DateTimeConverter.getLocalDateSeasonMin(new LocalDate(2013, 12, 31));
		Assert.assertEquals(new LocalDate(2013, 7, 1), date);
	}
	@Test
	public void getLocalDateSeasonMin_YearStart() {
		LocalDate date = DateTimeConverter.getLocalDateSeasonMin(new LocalDate(2014, 1, 1));
		Assert.assertEquals(new LocalDate(2013, 7, 1), date);
	}
	@Test
	public void getLocalDateSeasonMin_SeasonEnd() {
		LocalDate date = DateTimeConverter.getLocalDateSeasonMin(new LocalDate(2014, 6, 30));
		Assert.assertEquals(new LocalDate(2013, 7, 1), date);
	}

	@Test
	public void getLocalDateSeasonMax_SeasonStart() {
		LocalDate date = DateTimeConverter.getLocalDateSeasonMax(new LocalDate(2014, 7, 1));
		Assert.assertEquals(new LocalDate(2015, 6, 30), date);
	}
	@Test
	public void getLocalDateSeasonMax_YearEnd() {
		LocalDate date = DateTimeConverter.getLocalDateSeasonMax(new LocalDate(2014, 12, 31));
		Assert.assertEquals(new LocalDate(2015, 6, 30), date);
	}
	@Test
	public void getLocalDateSeasonMax_YearStart() {
		LocalDate date = DateTimeConverter.getLocalDateSeasonMax(new LocalDate(2015, 1, 1));
		Assert.assertEquals(new LocalDate(2015, 6, 30), date);
	}
	@Test
	public void getLocalDateSeasonMax_SeasonEnd() {
		LocalDate date = DateTimeConverter.getLocalDateSeasonMax(new LocalDate(2015, 6, 30));
		Assert.assertEquals(new LocalDate(2015, 6, 30), date);
	}

	@Test
	public void getLocalDateTimeSeasonMin_SeasonStart() {
		LocalDateTime dateTime = DateTimeConverter.getLocalDateTimeSeasonMin(new LocalDate(2013, 7, 1));
		Assert.assertEquals(new LocalDateTime(2013, 7, 1, 0, 0), dateTime);
	}
	@Test
	public void getLocalDateTimeSeasonMin_YearEnd() {
		LocalDateTime dateTime = DateTimeConverter.getLocalDateTimeSeasonMin(new LocalDate(2013, 12, 31));
		Assert.assertEquals(new LocalDateTime(2013, 7, 1, 0, 0), dateTime);
	}
	@Test
	public void getLocalDateTimeSeasonMin_YearStart() {
		LocalDateTime dateTime = DateTimeConverter.getLocalDateTimeSeasonMin(new LocalDate(2014, 1, 1));
		Assert.assertEquals(new LocalDateTime(2013, 7, 1, 0, 0), dateTime);
	}
	@Test
	public void getLocalDateTimeSeasonMin_SeasonEnd() {
		LocalDateTime dateTime = DateTimeConverter.getLocalDateTimeSeasonMin(new LocalDate(2014, 6, 30));
		Assert.assertEquals(new LocalDateTime(2013, 7, 1, 0, 0), dateTime);
	}

	@Test
	public void getLocalDateTimeSeasonMax_SeasonStart() {
		LocalDateTime dateTime = DateTimeConverter.getLocalDateTimeSeasonMax(new LocalDate(2014, 7, 1));
		Assert.assertEquals(new LocalDateTime(2015, 6, 30, 23, 59), dateTime);
	}
	@Test
	public void getLocalDateTimeSeasonMax_YearEnd() {
		LocalDateTime dateTime = DateTimeConverter.getLocalDateTimeSeasonMax(new LocalDate(2014, 12, 31));
		Assert.assertEquals(new LocalDateTime(2015, 6, 30, 23, 59), dateTime);
	}
	@Test
	public void getLocalDateTimeSeasonMax_YearStart() {
		LocalDateTime dateTime = DateTimeConverter.getLocalDateTimeSeasonMax(new LocalDate(2015, 1, 1));
		Assert.assertEquals(new LocalDateTime(2015, 6, 30, 23, 59), dateTime);
	}
	@Test
	public void getLocalDateTimeSeasonMax_SeasonEnd() {
		LocalDateTime dateTime = DateTimeConverter.getLocalDateTimeSeasonMax(new LocalDate(2015, 6, 30));
		Assert.assertEquals(new LocalDateTime(2015, 6, 30, 23, 59), dateTime);
	}

	@Test
	public void calculateDateDiff_23Hours() {
		LocalDateTime minDate = new LocalDateTime(2013, 3, 31, 19, 0);
		LocalDateTime maxDate = new LocalDateTime(2013, 4, 1, 18, 0);
		int days = DateTimeConverter.getDaysBetweenTwoDateTimes(minDate, maxDate);
		Assert.assertEquals(0, days);
	}

	@Test
	public void calculateDateDiff_25Hours() {
		LocalDateTime minDate = new LocalDateTime(2013, 3, 31, 19, 0);
		LocalDateTime maxDate = new LocalDateTime(2013, 4, 1, 20, 0);
		int days = DateTimeConverter.getDaysBetweenTwoDateTimes(minDate, maxDate);
		Assert.assertEquals(1, days);
	}

	@Test
	public void calculateDateDiff_Over30Days() {
		LocalDateTime minDate = new LocalDateTime(2013, 3, 31, 19, 0);
		LocalDateTime maxDate = new LocalDateTime(2013, 6, 1, 20, 0);
		int days = DateTimeConverter.getDaysBetweenTwoDateTimes(minDate, maxDate);
		Assert.assertEquals(0, days);
	}

	@Test
	public void calculateDateDiff_NullMinDate() {
		LocalDateTime maxDate = new LocalDateTime(2013, 6, 1, 20, 0);
		int days = DateTimeConverter.getDaysBetweenTwoDateTimes(null, maxDate);
		Assert.assertEquals(0, days);
	}

	@Test
	public void createDateMinusOneDay_EndOfMonth() {
		LocalDate date = DateTimeConverter.getDateMinusOneDay(new LocalDate(2013, 6, 30));
		Assert.assertEquals(new LocalDate(2013, 6, 29), date);
	}
	@Test
	public void createDateMinusOneDay_BeginingOfMonth() {
		LocalDate date = DateTimeConverter.getDateMinusOneDay(new LocalDate(2013, 7, 1));
		Assert.assertEquals(new LocalDate(2013, 6, 30), date);
	}

	@Test
	public void isDate_PastDate() {
		Assert.assertTrue(DateTimeConverter.isDate("1969-12-31"));
	}
	@Test
	public void isDate_FutureDate() {
		Assert.assertTrue(DateTimeConverter.isDate("2069-12-31"));
	}
	@Test
	public void isDate_InvalidDate() {
		Assert.assertFalse(DateTimeConverter.isDate("2069-12"));
	}
}
