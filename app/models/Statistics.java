/*
 * Name: Application.java
 * Description: main controller for application
 * Written On: 14/11/2012
 * @author Jenny Cooper, x12101303
 * 
 */

package models;

import java.util.Date;
import play.db.ebean.Model;

public class Statistics extends Model {
	
	protected Date date;
	protected int numRes;
	protected int numRooms;
	protected double totWhale;
	protected double totAccomm;
	protected double totMeals;
	protected double totTransfers;
	protected double total;
	
	
	
	public Date getDate() {
		return date;
	}

	public void setYear(Date date) {
		this.date = date;
	}

	public int getNumRes() {
		return numRes;
	}

	public void setNumRes(int numRes) {
		this.numRes = numRes;
	}

	public int getNumRooms() {
		return numRooms;
	}

	public void setNumRooms(int numRooms) {
		this.numRooms = numRooms;
	}

	public double getTotWhale() {
		return totWhale;
	}

	public void setTotWhale(double totWhale) {
		this.totWhale = totWhale;
	}

	public double getTotAccomm() {
		return totAccomm;
	}

	public void setTotAccomm(double totAccomm) {
		this.totAccomm = totAccomm;
	}

	public double getTotMeals() {
		return totMeals;
	}

	public void setTotMeals(double totMeals) {
		this.totMeals = totMeals;
	}

	public double getTotTransfers() {
		return totTransfers;
	}

	public void setTotTransfers(double totTransfers) {
		this.totTransfers = totTransfers;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	//default constructor
	public Statistics(){
		this.numRes =0;
		this.numRooms=0;
		this.totWhale=0;
		this.totAccomm=0;
		this.totMeals=0;
		this.totTransfers=0;
		this.total=0;
	}
	
	
	/*
	 * calculate statistics for the year
	 * @params args (Date, String)
	 */
	public void calcStats(Date year, String y){
		//implemented in sub-classes

	}
}
