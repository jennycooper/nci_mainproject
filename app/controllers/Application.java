package controllers;


import globaldate.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.SqlRow;
import com.itextpdf.text.DocumentException;

import models.*;

import play.*;
import play.api.GlobalPlugin;
import play.data.*;
import play.mvc.*;

import views.html.*;






public class Application extends Controller {
    
	final static Form<Reserve> contactForm = form(Reserve.class);
    
	/**
	 * This result directly redirect to application home.
	 */
	public static Result GO_HOME(Date date) {
		return redirect(routes.Application.index(new SimpleDateFormat("MMMM yyyy").format(date)));
	}
	    

	@Security.Authenticated(Secured.class)
	public static Result index(String myDate) {
	  	if (myDate==null || myDate== (" ") || myDate=="")
	  			myDate=GlobalDate.defaultDateString;
		Date queryDate = null;
		try {
			queryDate = new SimpleDateFormat("MMMM yyyy").parse(myDate);
			System.out.println(queryDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String[] str=myDate.split(" ");
		String month=str[0];
		String year = str[1];
		return ok(views.html.index.render(new MonthlyBookings(queryDate).makeCalendar(), month,year));
	}

	@Security.Authenticated(Secured.class)
	public static Result returnToIndex(String myDate) {
		if (myDate==null || myDate== (" ") || myDate=="")
			myDate = GlobalDate.defaultDateString;
	  
		Date queryDate = null;
		String query = null;
		try {
			queryDate = new SimpleDateFormat("yyyy-MM-dd").parse(myDate);
			query = new SimpleDateFormat("MMMM yyyy").format(queryDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String[] str=query.split(" ");
		String month=str[0];
		String year = str[1];

		return ok(views.html.index.render(new MonthlyBookings(queryDate).makeCalendar(), month,year));
	}
 
	
	public static Result nextpage() {
	  
		return ok(nextpage.render("msg sent from nextpage.render", Reserve.reservationList()));
	}
  
  
	 public static Result newpage(String myDate) {
	
		Date queryDate = null;
		try {
			queryDate = new SimpleDateFormat("MMMM yyyy").parse(myDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		return ok(views.html.newpage.render(new MonthlyBookings(queryDate).getMyList()));
	 }




	/*
	public static Result getBookings(int myMonth){
		 	
		//test the connection to the database
		String sql = "select count(*) as count from RoomBooking";  
	    SqlRow row =   
	        Ebean.createSqlQuery(sql)  
	        .findUnique();  
	      
	    Integer i = row.getInteger("count");  
			
	
	    return GO_HOME("");
		
	}
	*/
	 /**
	  * finds a reservation by id and renders it to a html page which shows fields in readonly format 
	  *
	  */
 
	 @Security.Authenticated(Secured.class)
	public static Result viewReservation(Long id){
		//create an instance of Reserve (based on it's id) and then call methods which will calculate the values 
		//for the rest of the attributes in the Reserve and ResCosts classes, which can then be included on the form
		Reserve r =  Reserve.find.byId(id);
		r.calcValues();
		r.getCosts().addTotals(r);
		Form<Reserve> myForm = form(Reserve.class).fill(r);
	
		int len = myForm.get().getDetailList().size();
		return ok(views.html.viewReservation.render(id, myForm, len));
	}
	 
	/**
	 * finds a reservation by id and renders it to a html page which shows fields in editable format 
	 * this html page can only be viewed if the user has the correct role
	 */
	@Security.Authenticated(Secured.class)
	public static Result getReservation(Long id){
		//create an instance of Reserve (based on it's id) and then call a method, which will calculate the values 
		//for the rest of the attributes, which can then be included on the form
		Reserve r =  Reserve.find.byId(id);
		r.calcValues();
		r.getCosts().addTotals(r);
		Form<Reserve> myForm = form(Reserve.class).fill(r);
		
		int len = myForm.get().getDetailList().size();
		if (Secured.checkRole(ctx()))
			return ok(views.html.reservation.render(id, myForm, len));
		else
			return ok("you do not have user rights to edit this reservation");
	}

	/**
	 * Handle the 'edit form' submission 
	 * @throws IOException 
	 * @throws DocumentException 
	 */

	public static Result update(Long id) throws IOException {
		
	    Form<Reserve> reservationForm = form(Reserve.class).bindFromRequest();
	    if(reservationForm.hasErrors()) {
	    	
	        return badRequest(views.html.reservation.render(id, reservationForm, Reserve.find.byId(id).getDetailList().size()));
	    }
	
	    Reserve res = reservationForm.get();
	    //check if the change of dates is trying to overwrite an existing reservation
		//do this by creating an instance of MonthlyBookings (which will contain all the bookings for the month, based on the checkin date)
		MonthlyBookings mbook = new MonthlyBookings(res.getCheckin());
		
		if(mbook.alreadyExists(res)==true){
			flash("failure", "Those dates are already reserved!");
			return badRequest(views.html.reservation.render(id, reservationForm, Reserve.find.byId(id).getDetailList().size()));
		   }
		else 
			//System.out.println(res.getCosts().getDepositPaid());
			res.updateReservation(res, id);
			res.calcValues();
			res.getCosts().addTotals(res);
			//res.updateCosts(res);
			DownloadPDF.createPDF(res);
		    flash("success", "Reservation for " + res.getMyGuest().getName() + " has been updated");
		    return ok(views.html.viewReservation.render(id, reservationForm.fill(res), res.getDetailList().size()));
		
	}
	/*
	public static Result downloadPDF(Long id){
	//Form<Reserve> reservationForm = form(Reserve.class).fill(Reserve.find.byId(id)).bindFromRequest();
	    System.out.println("here!");
	    //if(reservationForm.hasErrors()) {
	        //return badRequest(views.html.reservation.render(id, reservationForm, Reserve.find.byId(id).getDetailList().size()));
	    //}
	    Reserve res = Reserve.find.byId(id);
	    try {
			DownloadPDF.createPDF(res);
		} catch (DocumentException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
		return GO_HOME(res.getCheckin());
	}
	*/


	/**
	 * Display the 'new reservation form'.
	 */
	
	public static Result create() {
		Form<Reserve> resForm = form(Reserve.class);
	    return ok(views.html.createReservation.render(resForm,1));
	}

	//add more rooms to the reservation form
	public static Result addRooms(String rooms){
		//takes in the number of rooms for the reservation, parses this value to an integer, and passes the value back to the html view
		Form<Reserve> reservationForm = form(Reserve.class).bindFromRequest();
		if(reservationForm.hasErrors()) {
	        return badRequest(views.html.createReservation.render(reservationForm,1));
	    }
	
		return ok(views.html.createReservation.render(reservationForm,Integer.parseInt(rooms)));
		
	}	

	/**
	 // Handle the 'new reservation form' submission
	 */
	public static Result save() {
		Form<Reserve> reservationForm = form(Reserve.class).bindFromRequest();
		if(reservationForm.hasErrors()) {
	        return badRequest(views.html.createReservation.render(reservationForm,1));
	    }
		Reserve r = reservationForm.get();
		//check if date is already booked, by creating an instance of MonthlyBookings (which contains all current bookings 
		//for the month, based on the checkin date of the new reservation)
		MonthlyBookings mb = new MonthlyBookings(r.getCheckin());
		if(mb.alreadyExists(r)==true){
	        return badRequest(views.html.createReservation.render(reservationForm,1));
	    }
		else{	
	    //else save the reservation to the database and download the invoice
			r.save(); 
			Download.downloadPDF(r.getReservationID());
			flash("success", "New reservation has been created");
			return GO_HOME(reservationForm.get().getCheckin());
		}
		
	}


	public static Result delete(Long id) {
		Long guestid = Reserve.find.byId(id).getMyGuest().guestID;
		//store the date of the reservation before deleting it, to pass as a parameter on return
		Date date = Reserve.find.byId(id).getCheckin();
	    Guest.find.byId(guestid).delete();
	    flash("success", "Reservation has been deleted");
	    return GO_HOME(date);
	}


	public static Result yearCalendar(String year){
		YearlyBookings yb = new YearlyBookings(year);
		return ok(views.html.offline.render(yb.getYearlyCalendar(), year) );
	}

	
	public static Result downloadCopy(String year){
		//call a method to download an offline copy to the user's computer
		OfflineCopy.downloadCopy(year);
		
		return ok(views.html.downloaded.render(year));
	}
}

