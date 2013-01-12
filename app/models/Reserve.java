package models;

/*
 * Name: Reserve.java
 * Description: class to represent a reservation
 * Written On: 01/10/2012
 * @author Jenny Cooper, x12101303
 * 
 */

import java.sql.Timestamp;
import java.util.*;
import javax.persistence.*;
import com.avaje.ebean.Expr;
import play.db.ebean.Model;




@Entity
@Table(name="reserve")
public class Reserve extends Model {
	
	//the following fields are mapped directly to the reserve table in the database
	@Id
	private Long reservationID;
	private Date checkin;
	private Date checkout;
	private int numAdults;
	private int numChild;
	private String pickupTime;
	private String dropoffTime;
	private String pickup;
	private String dropoff;
	private double roomRate;
	private double mealAdult;
	private double mealChild;
	private double transfer;
	private double whaleRate;
	private String notes;
	@Version
	private Timestamp updtime;
	//mapping to related tables
	@OneToOne(cascade=CascadeType.ALL)
	private Guest myGuest;
	@OneToMany(cascade = CascadeType.ALL, mappedBy="res")
	private List<ResDetail> detailList;
	@OneToOne(cascade=CascadeType.ALL)
	private ResCosts costs;
	//calculated fields for number of nights/length of booking and number of rooms
	@Transient
	private int len;
	@Transient
	private int numRooms;
	
	

	//getters and setters
	public Long getReservationID() {
		return reservationID;
	}
	public void setReservationID(Long reservationID) {
		this.reservationID = reservationID;
	}
	public Guest getGuest() {
		return myGuest;
	}
	public void setGuest(Guest guest) {
		this.myGuest = guest;
	}
	public double getRoomRate() {
		return roomRate;
	}
	public void setRoomRate(double roomRate) {
		this.roomRate = roomRate;
	}
	public Date getCheckin() {
		return checkin;
	}
	public void setCheckin(Date checkin) {
		this.checkin = checkin;
	}
	public Date getCheckout() {
		return checkout;
	}
	public void setCheckout(Date checkout) {
		this.checkout = checkout;
	}
	public Guest getMyGuest() {
		return myGuest;
	}
	public void setMyGuest(Guest myGuest) {
		this.myGuest = myGuest;
	}
	public List<ResDetail> getDetailList() {
		return detailList;
	}
	public void setDetailList(List<ResDetail> detailList) {
		this.detailList = detailList;
	}
	public Timestamp getUpdtime() {
		return updtime;
	}
	public void setUpdtime(Timestamp updtime) {
		this.updtime = updtime;
	}
	public int getLen() {
		return len;
	}
	public void setLen(int len) {
		this.len = len;
	}
	public int getNumRooms() {
		return numRooms;
	}
	public void setNumRooms(int numRooms) {
		this.numRooms = numRooms;
	}
	public ResCosts getCosts() {
		return costs;
	}
	public void setCosts(ResCosts costs) {
		this.costs = costs;
	}
	public int getNumAdults() {
		return numAdults;
	}
	public void setNumAdults(int numAdults) {
		this.numAdults = numAdults;
	}
	public int getNumChild() {
		return numChild;
	}
	public void setNumChild(int numChild) {
		this.numChild = numChild;
	}
	public String getPickupTime() {
		return pickupTime;
	}
	public void setPickupTime(String pickupTime) {
		this.pickupTime = pickupTime;
	}
	public String getDropoffTime() {
		return dropoffTime;
	}
	public void setDropoffTime(String dropoffTime) {
		this.dropoffTime = dropoffTime;
	}
	public String getPickup() {
		return pickup;
	}
	public void setPickup(String pickup) {
		this.pickup = pickup;
	}
	public String getDropoff() {
		return dropoff;
	}
	public void setDropoff(String dropoff) {
		this.dropoff = dropoff;
	}
	public double getMealAdult() {
		return mealAdult;
	}
	public void setMealAdult(double mealAdult) {
		this.mealAdult = mealAdult;
	}
	public double getMealChild() {
		return mealChild;
	}
	public void setMealChild(double mealChild) {
		this.mealChild = mealChild;
	}
	public double getWhaleRate() {
		return whaleRate;
	}
	public void setWhaleRate(double whaleRate) {
		this.whaleRate = whaleRate;
	}
	public double getTransfer() {
		return transfer;
	}
	public void setTransfer(double transfer) {
		this.transfer = transfer;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}

	
	/*
	 * method to calculate the derived/transient attributes/fields for a newly created instance of Reserve
	 */
	public void calcValues(){
		if (this.checkin!=null && this.checkout!= null){
			this.len = (int) ((this.checkout.getTime() - this.checkin.getTime())/(1000*60*60*24));
		}
		this.numRooms = this.detailList.size();
	}

	
	//generic helper
  	public static Finder<Long, Reserve> find = new Finder<Long, Reserve>(Long.class, Reserve.class); 
	
	
  	public static List<Reserve> reservationList (){
  		return find.all();
  	}
  	
  /*
   * method to get a list of reservations for a particular month (date is passed in as a parameter)
   */
  	public static List<Reserve> listMonth(Date queryDate){
  		
  		if (queryDate==null)
  			queryDate = new Date();
  		//get the month from the incoming date parameter
  		Calendar cal = Calendar.getInstance();
  		cal.setTime(queryDate);
  		
  		//make start date for query to be the first day of the month 		
  		cal.set(Calendar.DAY_OF_MONTH, 01);
  		int numDays = cal.getActualMaximum(Calendar.DAY_OF_MONTH);		
  		Date startQuery = cal.getTime();
  		
  		//make end date for query to be the last day of the month
  		cal.add(Calendar.DAY_OF_MONTH, numDays-1);
  		Date endQuery = cal.getTime();
  		
  		//convert the start and end query dates to sql format
  		java.sql.Date sqlStartQuery = new java.sql.Date(startQuery.getTime());
  		java.sql.Date sqlEndQuery = new java.sql.Date(endQuery.getTime());

   		return	find
  				.where()
  					.or(Expr.between("checkin", sqlStartQuery, sqlEndQuery), Expr.between("checkout", sqlStartQuery, sqlEndQuery))
  					.findList();
  	}
    
  /*
   * method to get a list of reservations for a particular year (date is passed in as a parameter)
   */
  	public static List<Reserve> listYear(Date queryDate){
  		
  		if (queryDate==null)
  			queryDate = new Date();

  		Date startQuery = queryDate;
  		// make end of query to be 31st Dec (need to convert to calendar first)
  		Calendar cal = Calendar.getInstance();
  		cal.setTime(queryDate);
  		cal.set(Calendar.DAY_OF_MONTH, 31);
  		cal.set(Calendar.MONTH, 12);
  		Date endQuery = cal.getTime();
  		
  		//convert the start and end query dates to sql format
  		java.sql.Date sqlStartQuery = new java.sql.Date(startQuery.getTime());
  		java.sql.Date sqlEndQuery = new java.sql.Date(endQuery.getTime());

   		return	find
  				.where(Expr.between("checkin", sqlStartQuery, sqlEndQuery))
  					.findList();
  	}
  	
  	/*
  	 * method to update an instance of Reserve
  	 * needs to find unique ids for associated object attirbutes (related table entries)
  	 * (ebean will automatically save older versions of these entries in the database, 
  	 * then create new rows and increment these ids.)
  	 * However this method will restore the original id's stop a new entry being created in the database 
  	 */
  	public void updateReservation(long id){

  			Long guestid = Reserve.find.byId(id).getMyGuest().guestID;
  			//get all fields for guest that have been bound from the user form, then set the guestid back to the original value, and update the guest entity
  			Guest g = this.getGuest();
  		    g.setGuestID(guestid);
  		    g.update();
  	    
  		    //do the same for all values of resdetail (from the detailList)
  		    for (int i=0; i<this.getDetailList().size(); i++){
  		    	Long resid = Reserve.find.byId(id).getDetailList().get(i).detailID;
  		    	ResDetail rd1 = this.getDetailList().get(i);
  		    	rd1.setDetailID(resid);
  		    	rd1.update();
  		    }
  		    
  		    //do the same for rescosts
  		    Long costsid = Reserve.find.byId(id).getCosts().getCostsID();
  		    ResCosts rescosts = this.getCosts();
  		    rescosts.setCostsID(costsid);
  		    rescosts.update();
  		    
  		    //finally update the reservation (for the persisted fields)
  		    this.update();	 
  		    //calculate totals and transient fileds, based on updated information
  		    this.calcValues();
			this.getCosts().addTotals(this);
  	}
  	

  	
}

