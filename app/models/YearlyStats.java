/*
 * Name: YearlyStats.java
 * Description: class to calculate statistics for a given year
 * Written On: 14/11/2012
 * @author Jenny Cooper, x12101303
 * 
 */

package models;

import java.util.*;
import java.util.Calendar;
import java.util.Date;


public class YearlyStats extends Statistics {
	protected ArrayList<MonthlyStats> mStats; 
	
	
	public ArrayList<MonthlyStats> getMStats() {
		return mStats;
	}
	public void setMStats(ArrayList<MonthlyStats> mStats) {
		this.mStats = mStats;
	}
	

	//default constructor
	public YearlyStats(){
		this.numRes =0;
		this.numRooms=0;
		this.totWhale=0;
		this.totAccomm=0;
		this.totMeals=0;
		this.totTransfers=0;
		this.total=0;
		this.mStats = new ArrayList<MonthlyStats>();
	}
	
	//constructor with date as param
	public YearlyStats(String year){

		//convert from string of year param to date starting the 1st Jan of that year
		int y = Integer.parseInt(year);
		Calendar cal = Calendar.getInstance();
		cal.set(y, 00, 01);
		this.date = cal.getTime();
		this.mStats = new ArrayList<MonthlyStats>();
		
		//calculate the statistics
		this.calcStats(this.date, year);	
	}
	/*
	 * (non-Javadoc)
	 * @see models.Statistics#calcStats(java.util.Date, java.lang.String)
	 * calculate statistics for a given year
	 */
	@Override
	public void calcStats(Date year, String y) {
		//get all reservations for the year
		List<Reserve> list =  Reserve.listYear(year);
		this.numRes = list.size();
		//loop through each reservation for the year, and calculate yearly totals
		for (int i=0;i<list.size();i++){
			Reserve res = list.get(i);
			res.calcValues();
			res.getCosts().addTotals(res);
			this.numRooms = numRooms + (res.getLen() * res.getNumRooms());
			this.totWhale = totWhale + res.getWhaleRate();
			this.totAccomm = totAccomm + res.getCosts().getTotAccom();
			this.totMeals = totMeals + res.getCosts().getTotAdMeals() + res.getCosts().getTotChMeals();
			this.totTransfers = totTransfers + res.getTransfer();
			this.total = totWhale + totAccomm + totMeals + totTransfers;
		}	
		
		this.calcMStats(y);
	}
	
	
	/*
	 * calculate statistics for each month of a given year and add them to the mStats arraylist
	 * @param args String (to denote the year)
	 */
	public void calcMStats(String year){
		for (int month=0; month<=11; month++){
			this.mStats.add(new MonthlyStats(month, year));
		}
	}
	
}
