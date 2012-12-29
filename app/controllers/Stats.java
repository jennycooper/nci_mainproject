package controllers;

import java.util.ArrayList;

import models.*;

import play.*;
import play.data.*;
import play.mvc.*;

import views.html.*;

public class Stats extends Controller {
	
	public static Result viewStats(String year){
		YearlyStats stat = new YearlyStats(year);
		
		//ArrayList<MonthlyStats> mStats= new ArrayList<MonthlyStats>();
		//for (int month=0; month<=11; month++){
			//stat.getMStats().add(new MonthlyStats(month, year));
		//}
		//System.out.println("mstats size: ");
		//System.out.println(stat.getMStats().size());
		//Form<Statistics> myForm = form(Statistics.class).fill(stat);
		return ok(views.html.statistics.render(stat, year));
	}
}
