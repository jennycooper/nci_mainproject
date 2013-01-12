package controllers;

/*
 * Name: Download.java
 * Description: controller for downloading offline copy of calendar
 * Written On: 01/11/2012
 * @author Jenny Cooper, x12101303
 * 
 */

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import models.*;
import play.mvc.*;
import views.html.*;

public class Download extends Controller{

	/*
	 * handle the creation of a pdf version of the reservation
	 */
	public static Result downloadPDF(Long id){
		Reserve res = Reserve.find.byId(id);
		//download the pdf invoice to the user's local pc
		DownloadPDF.createPDF(res);
		
		//convert checkin date to a string, so index page will be displayed for appropriate date
		String queryDate = DateConversion.convertDateToString(res.getCheckin());
		return redirect(routes.Calendars.index(queryDate));
	}

}
