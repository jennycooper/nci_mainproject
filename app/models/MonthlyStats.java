/*
 * Name: MonthlyStats.java
 * Description: class to calculate statistics for a given month
 * Written On: 14/11/2012
 * @author Jenny Cooper, x12101303
 * 
 */

package models;

import java.util.*;
import java.util.Calendar;
import java.util.Date;


import play.db.ebean.Model;

public class MonthlyStats extends Statistics {
	
	//default constructor
	public MonthlyStats(){
		super();
		this.numRes =0;
		this.numRooms=0;
		this.totWhale=0;
		this.totAccomm=0;
		this.totMeals=0;
		this.totTransfers=0;
		this.total=0;
	}
	
	//constructor with date as param
	public MonthlyStats(int month, String year){
		//convert from string of year param to date starting the 1st Jan of that year
		int y = Integer.parseInt(year);
		Calendar cal = Calendar.getInstance();
		cal.set(y, month, 01);
		this.date = cal.getTime();
		this.calcStats(this.date, year);
	}

	/*
	 * @see models.Statistics#calcStats(java.util.Date, java.lang.String)
	 */
	@Override
	public void calcStats(Date year, String y) {
		List<Reserve> list =  Reserve.listMonth(year);

		this.numRes = list.size();
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
	}
	
}
