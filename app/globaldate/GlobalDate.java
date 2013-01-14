package globaldate;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class GlobalDate {
	
	public static Date todaysDate = new Date();
	public static Calendar todaysCalendar = Calendar.getInstance();
	public static int defaultMonth = Calendar.getInstance().get(Calendar.MONTH);
	public static int defaultYear = Calendar.getInstance().get(Calendar.YEAR);
	public static String defaultDateString = ("January "+ Calendar.getInstance().get(Calendar.YEAR));

}
