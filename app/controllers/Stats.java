package controllers;

/*
 * Name: Stats.java
 * Description: controller for displaying yearly statistics
 * Written On: 01/11/2012
 * @author Jenny Cooper, x12101303
 * 
 */

import models.*;

import play.mvc.*;


public class Stats extends Controller {
	
	/*
	 * call a method to calculate statistics, and send the results to the html view
	 */
	public static Result viewStats(String year){
		YearlyStats stat = new YearlyStats(year);

		return ok(views.html.statistics.render(stat, year));
	}
}
