
/*
 * Name: Calendars.java
 * Description:controller to handle the display of the reservations calendars
 * Written On: 14/10/2012
 * @author Jenny Cooper, x12101303
 * 
 */
package controllers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import models.*;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;

public class Calendars extends Controller{

	/**
	 * display home/index page, sending a calendar of reservations for a month to the index html page
	 * @params args String (to denote the month)
	 */
	@Security.Authenticated(AuthorisedRole.class)
	public static Result index(String myDate) {
	  	if (myDate==null || myDate== (" ") || myDate=="")
	  			myDate=DateConversion.defaultDateString;
		//split the date into a month and year parameter
		String[] str=myDate.split(" ");
		String month=str[0];
		String year = str[1];
		//convert the date String to type Date
		Date queryDate = DateConversion.convertStringToDate(myDate);
		return ok(views.html.index.render(new MonthlyBookings(queryDate).makeCalendar(), month,year));
	}

	/**
	 * redirect to index page
	 * @params args String (to denote the month)
	 * converts this to the correct format to be used in the index method
	 */
	@Security.Authenticated(AuthorisedRole.class)
	public static Result GO_HOME(String myDate) {
		if (myDate==null || myDate== (" ") || myDate=="")
			myDate = DateConversion.defaultDateString;
	  
		//convert the date to the correct String format
		String query = DateConversion.convertStringFormat(myDate);
		return redirect(routes.Calendars.index(query));
	}
	
	
	/**
	 * Redirect to index page.
	 * @params args Date (to denote the year)
	 */
	public static Result GO_HOME(Date date) {
		return redirect(routes.Calendars.index(DateConversion.convertDateToString(date)));
	}
	
	
	
	/**
	 * create a yearly calendar and render it to the html view
	 * @params args String (to denote the year)
	 */
	public static Result yearCalendar(String year){
		YearlyBookings yb = new YearlyBookings(year);
		return ok(views.html.offline.render(yb.getYearlyCalendar(), year) );
	}

	
	/**
	 * handle the downloading of the yearly calendar to the user's local pc
	 * @params args String (to denote the year)
	 */
	public static Result downloadCopy(String year){
		//call a method to download an offline copy to the user's computer
		OfflineCopy.downloadCopy(year);
		
		return ok(views.html.downloaded.render(year));
	}
	

}
