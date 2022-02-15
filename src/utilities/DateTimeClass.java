package utilities;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;


/**
 * This is a class for formatting date and time.
 * <p>
 * @author Aleksander Demichev
 * @version 1.0.0.0
 */

public final class DateTimeClass {
	
	
	/* 
	 * Returns formatted date like yyyy.MM.dd.
	 */
	public final String getFormattedDate() {
		LocalDate currentDate = LocalDate.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");

		return new String (currentDate.format(formatter));
	}

	
	/*
	 * Returns formated time as HH:MM:SS
	 */
	public final String getFormattedTime() {
		LocalTime time = LocalTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

		return new String (time.format(formatter));
	}


	
	/* 
	 * Parse string and return formatted date like dd.MM.yyyy
	 */
	public final String getFormattedIsoDate(String isoDate) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date newDate = null;
		newDate = (Date) format.parse(isoDate);
		format = new SimpleDateFormat("dd.MM.yyyy");
	
		return new String (format.format(newDate));
	}
	
}
