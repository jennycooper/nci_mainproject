package models;

/*
 * Name: BookedRoom.java
 * Description: class to represent a room that is currently booked for any day
 * Written On: 14/10/2012
 * @author Jenny Cooper, x12101303
 * 
 */

import java.util.*;

import javax.persistence.*;

import play.db.ebean.*;
import play.db.ebean.Model.Finder;
import play.data.format.*;
import play.data.validation.*;
import scala.Int;

import com.avaje.ebean.*;


public class BookedRoom extends Model {

    private int roomNum;
    private Date date;
    private Long reservationID;
    private String guestName;
    private double deposit;

    
    public int getRoomNum() {
		return roomNum;
	}
	public void setRoomNum(int roomNum) {
		this.roomNum = roomNum;
	}

	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}

	public Long getReservationID() {
		return reservationID;
	}
	public void setReservationID(Long reservationID) {
		this.reservationID = reservationID;
	}
	public String getGuestName() {
		return guestName;
	}
	public void setGuestName(String guestName) {
		this.guestName = guestName;
	}
	
	public double getDeposit() {
		return deposit;
	}
	public void setDeposit(double deposit) {
		this.deposit = deposit;
	}
	
	public BookedRoom(int roomNum, Date date, Long reservationID,
			String guestName, double deposit) {
		this.roomNum = roomNum;
		this.date = date;
		this.reservationID = reservationID;
		this.guestName = guestName;
		this.deposit = deposit;
	}
	
	
  	
}

