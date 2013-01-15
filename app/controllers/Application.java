
/*
 * Name: Application.java
 * Description: controller for CRUD operations for a reservation
 * Written On: 14/10/2012
 * @author Jenny Cooper, x12101303
 * 
 */

package controllers;

import models.*;
import java.util.*;
import play.data.*;
import play.mvc.*;


public class Application extends Controller {
    

	 /**
	  * handles the viewing of a reservations (for read-only access)
	  *@params Long (id of the reservation to view)
	  */
	@Security.Authenticated(AuthorisedRole.class)
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
	 * finds a reservation by id and renders it to a html page (for editable access)
	 * this html page can only be viewed if the user has the correct role
	 */
	@Security.Authenticated(AuthorisedRole.class)
	public static Result getReservation(Long id){
		//create an instance of Reserve (based on it's id) and then call a method, which will calculate the values 
		//for the rest of the attributes, which can then be included on the form
		Reserve r =  Reserve.find.byId(id);
		r.calcValues();
		r.getCosts().addTotals(r);
		Form<Reserve> myForm = form(Reserve.class).fill(r);
		
		int len = myForm.get().getDetailList().size();
		if (AuthorisedRole.checkRole(ctx()))
			return ok(views.html.reservation.render(id, myForm, len));
		else
			return ok("you do not have user rights to edit this reservation");
	}

	/**
	 * Handle the 'edit form' submission 
	 * @params Long (id of the reservation to update)
	 */
	public static Result update(Long id) {
		
	    Form<Reserve> reservationForm = form(Reserve.class).bindFromRequest();
	    if(reservationForm.hasErrors()) {
	    	flash("failure", "errors on form or empty fields");
	        return badRequest(views.html.reservation.render(id, reservationForm, Reserve.find.byId(id).getDetailList().size()));
	    }
	
	    Reserve res = reservationForm.get();
	    
	    //check that checkout date is not before checkin date
	    if(res.getCheckout().getTime()<=res.getCheckin().getTime()){
	    	flash("failure", "Check-out date is before check-in date!");
			return badRequest(views.html.reservation.render(id, reservationForm, Reserve.find.byId(id).getDetailList().size()));
	    }
	    
	    //check if the change of dates is trying to overwrite an existing reservation (using a method in the MonthlyBookings class)
		MonthlyBookings mbook = new MonthlyBookings(res.getCheckin());
		if(mbook.alreadyExists(res.getCheckin(), res.getCheckout(), res.getReservationID(), res.getDetailList())){
			flash("failure", "Those dates are already reserved!");
			return badRequest(views.html.reservation.render(id, reservationForm, Reserve.find.byId(id).getDetailList().size()));
		}
		
		//check user has correct role
		if (!AuthorisedRole.checkRole(ctx())){
			System.out.println(AuthorisedRole.checkRole(ctx()));
			return ok("you do not have user rights to edit this reservation");
		}
		//update the reservation, and update and download the pdf invoice
		res.updateReservation(id);
		DownloadPDF.createPDF(res);
		flash("success", "Reservation for " + res.getMyGuest().getName() + " has been updated");
		return ok(views.html.viewReservation.render(id, reservationForm.fill(res), res.getDetailList().size()));
			
	}


	/**
	 * Display the 'new reservation' form.
	 */
	public static Result create() {
		Form<Reserve> resForm = form(Reserve.class);
	    return ok(views.html.createReservation.render(resForm,1));
	}

	
	/**
	 * handle the new reservation form, if more than one room is requested
	 * @param String (to denote no of required rooms)
	 */
	public static Result addRooms(String rooms){
		//takes in the number of rooms for the reservation, parses this value to an integer, and passes the value back to the html view
		Form<Reserve> reservationForm = form(Reserve.class).bindFromRequest();
		if(reservationForm.hasErrors()) {
	        return badRequest(views.html.createReservation.render(reservationForm,1));
	    }
		return ok(views.html.createReservation.render(reservationForm,Integer.parseInt(rooms)));	
	}	
	

	/**
	* Handle the 'new reservation form' submission
	*/
	public static Result save() {
		Form<Reserve> reservationForm = form(Reserve.class).bindFromRequest();
		if(reservationForm.hasErrors()) {
			flash("failure", "errors on form or empty fields");
	        return badRequest(views.html.createReservation.render(reservationForm,1));
	    }
		
		Reserve r = reservationForm.get();
		
		//check that checkout date is not before checkin date
	    if(r.getCheckout().getTime()<=r.getCheckin().getTime()){
	    	flash("failure", "Check-out date is before check-in date!");
	    	return badRequest(views.html.createReservation.render(reservationForm,1));
	    }
	    
		//check if date is already booked, by calling a method in the MonthlyBookings class
		MonthlyBookings mb = new MonthlyBookings(r.getCheckin());
		if(mb.alreadyExists(r.getCheckin(), r.getCheckout(), r.getReservationID(), r.getDetailList())){
			flash("failure", "Those dates are already booked for that room!");
	        return badRequest(views.html.createReservation.render(reservationForm,1));
	    }
			
		//else save the reservation to the database and download the invoice
		r.save(); 
		Download.downloadPDF(r.getReservationID());
		flash("success", "New reservation has been created");
		return Calendars.GO_HOME(reservationForm.get().getCheckin());
		
	}

	/**
	 * Handle the deletion of a reservation
	 * @params Long (id of the resrevation to delete)
	 */
	public static Result delete(Long id) {
		Long guestid = Reserve.find.byId(id).getMyGuest().guestID;
		//store the date of the reservation before deleting it, to pass as a parameter on return
		Date date = Reserve.find.byId(id).getCheckin();
	    Guest.find.byId(guestid).delete();
	    flash("success", "Reservation has been deleted");
	    return Calendars.GO_HOME(date);
	}

	
}

