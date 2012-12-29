package models;

import java.util.*;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.avaje.ebean.Page;

import play.data.format.*;
import play.data.validation.*;
import play.db.ebean.Model;
import play.db.ebean.Model.Finder;
import scala.Int;




@Entity
@Table(name="guest")
public class Guest extends Model {
	
	@Id
	public Long guestID;
	
	public String name;
	public String email;
	public String country;
	
	//@OneToOne(cascade=CascadeType.ALL)
	//public Reserve myResID;
	
	
	//getters and setters
  	public Long getGuestID() {
		return guestID;
	}

	public void setGuestID(Long guestID) {
		this.guestID = guestID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}


	//helper to find one instance of Guest (from the mapped database)
  	public static Finder<Long, Guest> find = new Finder<Long, Guest>(Long.class, Guest.class); 
  	
  	//helper to find all instances of Guest (from the mapped database)
	public static List<Guest> reservationList (){
  		return find.all();
  	}

}

