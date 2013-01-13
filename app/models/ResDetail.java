package models;

/*
 * Name: ResDetail.java
 * Description: class to represent the room details of a reservation
 * Written On: 14/10/2012
 * @author Jenny Cooper, x12101303
 * 
 */


import java.util.*;
import javax.persistence.*;
import play.db.ebean.*;

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
	
 
  	
}


