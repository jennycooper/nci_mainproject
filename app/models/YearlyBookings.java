package models;

import play.db.ebean.Model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class YearlyBookings extends Model {

	private String year;
	private String[] months;
	private ArrayList<BookedRoom[][]> yearlyCalendar;
	

	public YearlyBookings(){
		this.year = "";
		this.months = new String[] {"January", "February", "March", "April", "May", "June",
										"July", "August", "September", "October", "November", "December"};
	}
	
	public YearlyBookings(String year){
		this.year = year;
		this.months = new String[] {"January", "February", "March", "April", "May", "June",
				"July", "August", "September", "October", "November", "December"};
		this.yearlyCalendar = new ArrayList<BookedRoom[][]>();
		this.yearlyCalendar = fillCalendar();
	}
	
	
	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public ArrayList<BookedRoom[][]> getYearlyCalendar() {
		return yearlyCalendar;
	}

	public void setYearlyCalendar(ArrayList<BookedRoom[][]> yearlyCalendar) {
		this.yearlyCalendar = yearlyCalendar;
	}

	public ArrayList<BookedRoom[][]> fillCalendar(){
		//go through each month in the year (in the array of months)
		for (int i=0; i<this.months.length;i++){
		
			String myMonth = (this.months[i] + " " + this.year);
			Date queryDate = null;
			try {
				queryDate = new SimpleDateFormat("MMMM yyyy").parse(myMonth);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			MonthlyBookings mb = new MonthlyBookings(queryDate);
			if (mb!=null){
				yearlyCalendar.add(mb.makeCalendar());
			}
		}
		return yearlyCalendar;
	}
}
