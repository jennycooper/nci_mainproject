package models;

import java.util.*;

import com.avaje.ebean.Page;

import play.data.format.*;
import play.data.validation.*;
import play.db.ebean.Model;
import play.db.ebean.Model.Finder;
import scala.Int;





public class MonthlyBookings extends Model {
	
	private ArrayList<BookedRoom> myList;
	private Date date; //comprising of a month and a year
	private int totalNumDays; //total number of days in the month
	final int NUMROOMS=8;
	

	public ArrayList<BookedRoom> getMyList() {
		return myList;
	}
	public void setMyList(ArrayList<BookedRoom> myList) {
		this.myList = myList;
	}
	public int getTotalNumDays() {
		return totalNumDays;
	}
	public void setTotalNumDays(int totalNumDays) {
		this.totalNumDays = totalNumDays;
	}
	public Date getQueryDate() {
		return date;
	}
	public void setQueryDate(Date queryDate) {
		this.date = queryDate;
	}

	//default constructor
	public MonthlyBookings(){
		this.myList = new ArrayList<BookedRoom>();
		this.date = null;
		this.totalNumDays = 0;
	}
	
	//constructor with a date
	public MonthlyBookings(Date date) {
		this.date = date;
		this.totalNumDays = daysInMonth(date);
		this.myList = new ArrayList<BookedRoom>();
		this.myList = createBookingList(date);
	}

	

	//method to convert a list of roombookings for the month to a 2D array, which will be sent to the html view
	public BookedRoom[][] makeCalendar(){
		BookedRoom calendarMonth[][] = new BookedRoom[totalNumDays][NUMROOMS];
		Calendar thisDate = Calendar.getInstance();
		  
        for (int index=0; index<myList.size(); index++){
            BookedRoom rb = (BookedRoom)myList.get(index);
            thisDate.setTime(rb.getDate());
            int thisDay = thisDate.get(Calendar.DAY_OF_MONTH);
            calendarMonth[thisDay-1][rb.getRoomNum()-1]=rb;
        }        
        return calendarMonth;
    }
	
	
	
	public int daysInMonth(Date myDate){
		if (myDate==null)
			myDate = new Date();
		int numDays = 0;
		//code here to find number of days in a given month, including checking for a leap year for feb
		Calendar calMonth = Calendar.getInstance();
		calMonth.setTime(myDate);
		numDays = calMonth.getActualMaximum(Calendar.DAY_OF_MONTH);
		
		return numDays;
	}
	
	
	//method to create a list of RoomBooking objects
		public ArrayList<BookedRoom> createBookingList (Date date){
		    
			//get a list of Reservations, for the month being queried
			List<Reserve> bookingList = Reserve.listMonth(date);
			if (bookingList.isEmpty())
				return myList;

		    int dayOfMonth;
		    Reserve res = new Reserve();
		    
			//iterate through each item in the list of reservations, to create a list of type RoomBooking	
			for (int i=0; i<bookingList.size(); i++){
				//calculate how many nights are in the booking, then convert the answer (in milliseconds) to days
				Long diff = (bookingList.get(i).getCheckout().getTime()) - (bookingList.get(i).getCheckin().getTime());
				int numNights = (int) (diff/(1000*60*60*24));
				
				//get the start day of the booking (from checkin date), and initialise the dayOfMonth variable to this day
				//convert checkin date and this months date to type calendar in order to perform comparisons
				Calendar cal = Calendar.getInstance();
				cal.setTime(bookingList.get(i).getCheckin());
				Calendar thisMonth = Calendar.getInstance();
		  		thisMonth.setTime(date);
		  		
		  		//check if booking starts in this month, or if it started at the end of the previous month but continued into this month
		  		//if it started in this month, set dayOfMonth variable to this day
		  		if (cal.get(Calendar.MONTH) == thisMonth.get(Calendar.MONTH)) {
		  			dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
		  			
		  			for(int j=0; j<numNights; j++){
		  				if (dayOfMonth<=this.totalNumDays){
		  					res = bookingList.get(i);
		  					//call a method to create a roombooking, and add it to a list of roombookings
		  					addToList(res, cal, dayOfMonth);
		  					
		  					dayOfMonth++;	
		  				}
		  			}
		  		}
		  		
		  		//else dayOfMonth will start the day before checkout date, and work backwards to start of month
		  		else{
			  		cal.setTime(bookingList.get(i).getCheckout());
			  		dayOfMonth = cal.get(Calendar.DAY_OF_MONTH)-1;
			  		while (dayOfMonth>=1){
			  			res = bookingList.get(i);
			  			addToList(res, cal, dayOfMonth);
			  			
		  				dayOfMonth--;	
			  		}
		  		}	
			}
			return myList;	
	  	}
		
		//method to create objects of type RoomBooking, and add them to a list of RoomBookings
		public void addToList(Reserve res, Calendar cal, int dayOfMonth){
			int roomNum;
		    double deposit;
		    Long reservationID = res.getReservationID();
		    String guestName = res.getGuest().getName();
		    
			//loop through each room in the reservation (from the list of resdetails)
			for (int x=0; x<res.getDetailList().size(); x++){
				roomNum = res.getDetailList().get(x).getRoomNum(); 
				cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
				deposit = res.getCosts().getDepositPaid(); 
				myList.add(new BookedRoom(roomNum, cal.getTime(), reservationID, guestName, deposit));
			}
		}
		
		
			
		//method to check if there is already an existing reservation for particular dates
		public boolean alreadyExists(Reserve res){
			//this method creates a list of RoomBookings for the whole month that the checkin date of the reservation starts
			
			//(this means that only one sql query is run on the database, even though it may bring back some rows of unnecessary data)
			//List<RoomBooking> existingList = createBookingList(res.getCheckin());
			
			//convert checkin and checkout dates to type Calendar
			Calendar calCheckin = Calendar.getInstance();
	  		calCheckin.setTime(res.getCheckin());
			Calendar calCheckout = Calendar.getInstance();
	  		calCheckout.setTime(res.getCheckout());
	  		//if the checkout date is in the next month, get all reservations for the next month also
	  		if (calCheckin.get(Calendar.MONTH)!=calCheckout.get(Calendar.MONTH)){
	  			List<BookedRoom> existingList2 = createBookingList(res.getCheckout());
	  			if (existingList2.isEmpty()==false)
	  				myList.addAll(existingList2);
	  		}
			//if the list is empty (ie no bookings at all for these months) return false at this stage
	  		if (myList.isEmpty())
	  			return false ;
	  		
	  		//else check for matching reservations from the existingList, for each date and room no from query reservation
	  		
	  		//start date at checkin date, converting it to type calendar
	  		Calendar calDate = Calendar.getInstance();
	  		calDate.setTime(res.getCheckin());

	  		//while loop to go through each date in the query reservation
	  		while (calDate.compareTo(calCheckout)<=0){
	  			//convert calDate back to Date format
	  			Date d = calDate.getTime();
	  			
	  			//loop through each room in the reservation
	  			for (int i=0;i<res.getDetailList().size(); i++){
	  				//loop through each item in the existingList to compare date and roomNum
	  				for (int j=0; j<myList.size(); j++){
	  					//don't compare the dates of the reservation to itself!
	  					if (myList.get(j).getReservationID()!=res.getReservationID()){
	  						if ((d.equals(myList.get(j).getDate())&&res.getDetailList().get(i).getRoomNum()==myList.get(j).getRoomNum())){
	  							return true;
	  						}
	  					}
	  				}
	  			//increment the date by one day
	  	  		calDate.add(Calendar.DAY_OF_MONTH, 1);
	  			}
	  		}
	  		return false;
		}
		
	    //
}




