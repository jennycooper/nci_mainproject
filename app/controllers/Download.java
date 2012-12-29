package controllers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import models.*;
import play.mvc.*;
import views.html.*;

public class Download extends Controller{

	//handle the creation of a pdf version of the reservation
	public static Result downloadPDF(Long id){
		Reserve res = Reserve.find.byId(id);
		DownloadPDF.createPDF(res);

		//convert checkin date to a string
		DateFormat df = new SimpleDateFormat("MMMM yyyy");
		return redirect(routes.Application.index(df.format(res.getCheckin())));
	}

}
