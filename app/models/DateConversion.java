package models;
import java.util.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class DateConversion {
	
	public static Date todaysDate = new Date();
	public static Calendar todaysCalendar = Calendar.getInstance();
	public static int defaultMonth = Calendar.getInstance().get(Calendar.MONTH);
	public static int defaultYear = Calendar.getInstance().get(Calendar.YEAR);
	public static String defaultDateString = ("January "+ Calendar.getInstance().get(Calendar.YEAR));

	public static String convertStringFormat(String format1){
		String format2 = null;
		Date date = null;

		try {
			date = new SimpleDateFormat("yyyy-MM-dd").parse(format1);
			format2 = new SimpleDateFormat("MMMM yyyy").format(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return format2;
	}
	
	public static String convertDateToString(Date date){
		String queryDate = new SimpleDateFormat("MMMM yyyy").format(date);
		return queryDate;
	}
	
	public static Date convertStringToDate(String date){
		Date queryDate = null;
		try {
			queryDate = new SimpleDateFormat("MMMM yyyy").parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return queryDate;
	}
}
