package com.rossotti.basketball.util;

import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class DateTimeUtilTest {

	@Test
	public void getStringDate_FromDate() {
		String date = DateTimeUtil.getStringDate(LocalDate.of(2013, 3, 30));
		Assert.assertEquals("2013-03-30", date);
	}

	@Test
	public void getStringDate_FromDateTime() {
		String dateTime = DateTimeUtil.getStringDate(LocalDateTime.of(2013, 3, 30, 10, 30));
		Assert.assertEquals("2013-03-30", dateTime);
	}

	@Test
	public void getStringDateTime() {
		String dateTime = DateTimeUtil.getStringDateTime(LocalDateTime.of(2013, 3, 30, 10, 30));
		Assert.assertEquals("2013-03-30T10:30", dateTime);
	}

	@Test
	public void getStringDateNaked_FromDateTime() {
		String dateTime = DateTimeUtil.getStringDateNaked(LocalDateTime.of(2013, 3, 30, 10, 30));
		Assert.assertEquals("20130330", dateTime);
	}

	@Test
	public void getStringDateNaked_FromDate() {
		String date = DateTimeUtil.getStringDateNaked(LocalDate.of(2013, 3, 30));
		Assert.assertEquals("20130330", date);
	}

	@Test
	public void getLocalDate_FromString() {
		LocalDate date = DateTimeUtil.getLocalDate("2014-06-30");
		Assert.assertEquals(LocalDate.of(2014, 6, 30), date);
	}

	@Test
	public void getLocalDate_FromLocalDateTime() {
		LocalDateTime dateTime = DateTimeUtil.getLocalDateTime("2014-06-30T10:30");
		Assert.assertEquals(LocalDate.of(2014, 6, 30), DateTimeUtil.getLocalDate(dateTime));
	}

	@Test
	public void getLocalDateTime() {
		LocalDateTime dateTime = DateTimeUtil.getLocalDateTime("2014-06-30T10:30");
		Assert.assertEquals(LocalDateTime.of(2014, 6, 30, 10, 30), dateTime);
	}
	
	@Test
	public void getLocalDateTimeMin() {
		LocalDateTime dateTime = DateTimeUtil.getLocalDateTimeMin(LocalDate.of(2014, 6, 30));
		Assert.assertEquals(LocalDateTime.of(2014, 6, 30, 0, 0), dateTime);
	}

	@Test
	public void getLocalDateTimeMax() {
		LocalDateTime dateTime = DateTimeUtil.getLocalDateTimeMax(LocalDate.of(2014, 6, 30));
		Assert.assertEquals(LocalDateTime.of(2014, 6, 30, 23, 59), dateTime);
	}

	@Test
	public void getLocalDateSeasonMin_SeasonStart() {
		LocalDate date = DateTimeUtil.getLocalDateSeasonMin(LocalDate.of(2013, 7, 1));
		Assert.assertEquals(LocalDate.of(2013, 7, 1), date);
	}
	@Test
	public void getLocalDateSeasonMin_YearEnd() {
		LocalDate date = DateTimeUtil.getLocalDateSeasonMin(LocalDate.of(2013, 12, 31));
		Assert.assertEquals(LocalDate.of(2013, 7, 1), date);
	}
	@Test
	public void getLocalDateSeasonMin_YearStart() {
		LocalDate date = DateTimeUtil.getLocalDateSeasonMin(LocalDate.of(2014, 1, 1));
		Assert.assertEquals(LocalDate.of(2013, 7, 1), date);
	}
	@Test
	public void getLocalDateSeasonMin_SeasonEnd() {
		LocalDate date = DateTimeUtil.getLocalDateSeasonMin(LocalDate.of(2014, 6, 30));
		Assert.assertEquals(LocalDate.of(2013, 7, 1), date);
	}

	@Test
	public void getLocalDateSeasonMax_SeasonStart() {
		LocalDate date = DateTimeUtil.getLocalDateSeasonMax(LocalDate.of(2014, 7, 1));
		Assert.assertEquals(LocalDate.of(2015, 6, 30), date);
	}
	@Test
	public void getLocalDateSeasonMax_YearEnd() {
		LocalDate date = DateTimeUtil.getLocalDateSeasonMax(LocalDate.of(2014, 12, 31));
		Assert.assertEquals(LocalDate.of(2015, 6, 30), date);
	}
	@Test
	public void getLocalDateSeasonMax_YearStart() {
		LocalDate date = DateTimeUtil.getLocalDateSeasonMax(LocalDate.of(2015, 1, 1));
		Assert.assertEquals(LocalDate.of(2015, 6, 30), date);
	}
	@Test
	public void getLocalDateSeasonMax_SeasonEnd() {
		LocalDate date = DateTimeUtil.getLocalDateSeasonMax(LocalDate.of(2015, 6, 30));
		Assert.assertEquals(LocalDate.of(2015, 6, 30), date);
	}

	@Test
	public void getLocalDateTimeSeasonMin_SeasonStart() {
		LocalDateTime dateTime = DateTimeUtil.getLocalDateTimeSeasonMin(LocalDate.of(2013, 7, 1));
		Assert.assertEquals(LocalDateTime.of(2013, 7, 1, 0, 0), dateTime);
	}
	@Test
	public void getLocalDateTimeSeasonMin_YearEnd() {
		LocalDateTime dateTime = DateTimeUtil.getLocalDateTimeSeasonMin(LocalDate.of(2013, 12, 31));
		Assert.assertEquals(LocalDateTime.of(2013, 7, 1, 0, 0), dateTime);
	}
	@Test
	public void getLocalDateTimeSeasonMin_YearStart() {
		LocalDateTime dateTime = DateTimeUtil.getLocalDateTimeSeasonMin(LocalDate.of(2014, 1, 1));
		Assert.assertEquals(LocalDateTime.of(2013, 7, 1, 0, 0), dateTime);
	}
	@Test
	public void getLocalDateTimeSeasonMin_SeasonEnd() {
		LocalDateTime dateTime = DateTimeUtil.getLocalDateTimeSeasonMin(LocalDate.of(2014, 6, 30));
		Assert.assertEquals(LocalDateTime.of(2013, 7, 1, 0, 0), dateTime);
	}

	@Test
	public void getLocalDateTimeSeasonMax_SeasonStart() {
		LocalDateTime dateTime = DateTimeUtil.getLocalDateTimeSeasonMax(LocalDate.of(2014, 7, 1));
		Assert.assertEquals(LocalDateTime.of(2015, 6, 30, 23, 59), dateTime);
	}
	@Test
	public void getLocalDateTimeSeasonMax_YearEnd() {
		LocalDateTime dateTime = DateTimeUtil.getLocalDateTimeSeasonMax(LocalDate.of(2014, 12, 31));
		Assert.assertEquals(LocalDateTime.of(2015, 6, 30, 23, 59), dateTime);
	}
	@Test
	public void getLocalDateTimeSeasonMax_YearStart() {
		LocalDateTime dateTime = DateTimeUtil.getLocalDateTimeSeasonMax(LocalDate.of(2015, 1, 1));
		Assert.assertEquals(LocalDateTime.of(2015, 6, 30, 23, 59), dateTime);
	}
	@Test
	public void getLocalDateTimeSeasonMax_SeasonEnd() {
		LocalDateTime dateTime = DateTimeUtil.getLocalDateTimeSeasonMax(LocalDate.of(2015, 6, 30));
		Assert.assertEquals(LocalDateTime.of(2015, 6, 30, 23, 59), dateTime);
	}

	@Test
	public void calculateDateDiff_23Hours() {
		LocalDateTime minDate = LocalDateTime.of(2013, 3, 31, 19, 0);
		LocalDateTime maxDate = LocalDateTime.of(2013, 4, 1, 18, 0);
		int days = DateTimeUtil.getDaysBetweenTwoDateTimes(minDate, maxDate);
		Assert.assertEquals(0, days);
	}

	@Test
	public void calculateDateDiff_25Hours() {
		LocalDateTime minDate = LocalDateTime.of(2013, 3, 31, 19, 0);
		LocalDateTime maxDate = LocalDateTime.of(2013, 4, 1, 20, 0);
		int days = DateTimeUtil.getDaysBetweenTwoDateTimes(minDate, maxDate);
		Assert.assertEquals(1, days);
	}

	@Test
	public void calculateDateDiff_Over30Days() {
		LocalDateTime minDate = LocalDateTime.of(2013, 3, 31, 19, 0);
		LocalDateTime maxDate = LocalDateTime.of(2013, 6, 1, 20, 0);
		int days = DateTimeUtil.getDaysBetweenTwoDateTimes(minDate, maxDate);
		Assert.assertEquals(0, days);
	}

	@Test
	public void calculateDateDiff_NullMinDate() {
		LocalDateTime maxDate = LocalDateTime.of(2013, 6, 1, 20, 0);
		int days = DateTimeUtil.getDaysBetweenTwoDateTimes(null, maxDate);
		Assert.assertEquals(0, days);
	}

	@Test
	public void createDateMinusOneDay_EndOfMonth() {
		LocalDate date = DateTimeUtil.getDateMinusOneDay(LocalDate.of(2013, 6, 30));
		Assert.assertEquals(LocalDate.of(2013, 6, 29), date);
	}
	@Test
	public void createDateMinusOneDay_BeginingOfMonth() {
		LocalDate date = DateTimeUtil.getDateMinusOneDay(LocalDate.of(2013, 7, 1));
		Assert.assertEquals(LocalDate.of(2013, 6, 30), date);
	}

	@Test
	public void isDate_PastDate() {
		Assert.assertTrue(DateTimeUtil.isDate("1969-12-31"));
	}
	@Test
	public void isDate_FutureDate() {
		Assert.assertTrue(DateTimeUtil.isDate("2069-12-31"));
	}
	@Test
	public void isDate_InvalidDate() {
		Assert.assertFalse(DateTimeUtil.isDate("2069-12"));
	}
}
