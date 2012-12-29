package models;

import java.sql.Timestamp;
import java.util.*;

import javax.persistence.*;

import play.db.ebean.*;
import play.db.ebean.Model.Finder;
import play.data.format.*;
import play.data.validation.*;

import com.avaje.ebean.*;

//@Embeddable

@Entity 
@Table(name="resdetail")
public class ResDetail extends Model{
	
	@Version
	@Id
	public Long detailID;
	
	public int roomNum;

	@ManyToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="res_reservation_id")
	public Reserve res;
	
	//getters and setters
	
	public Long getDetailID() {
		return detailID;
	}
	public void setDetailID(Long detailID) {
		this.detailID = detailID;
	}
	public int getRoomNum() {
		return roomNum;
	}
	public void setRoomNum(int roomNum) {
		this.roomNum = roomNum;
	}

	public Reserve getRes() {
		return res;
	}
	public void setRes(Reserve res) {
		this.res = res;
	}
	
	
	//generic helper
  	public static Finder<Long, ResDetail> find = new Finder<Long, ResDetail>(Long.class, ResDetail.class); 
	
	
  	public static List<ResDetail> resDetailList (){
  		return find.all();
  	}
	
    //method to get a list of reservations based on a date(month)
  	public static List<ResDetail> listMonth(Date queryDate){
  		if (queryDate==null)
  			queryDate = new Date();
  		//get the month from the incoming date parameter
  		Calendar cal = Calendar.getInstance();
  		cal.setTime(queryDate);
  		//int month = cal.get(Calendar.MONTH);
  		
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
  		
   		return	ResDetail.find
  				//.select("reservationID, checkin, checkout, roomNum")
   				.fetch("res")
  				.where()
  					.or(Expr.between("res.checkin", sqlStartQuery, sqlEndQuery), Expr.between("res.checkout", sqlStartQuery, sqlEndQuery))
  					.findList();			
 
  	}
}


