package controllers;




import models.*;

import play.*;
import play.data.*;
import play.mvc.*;
import play.mvc.Http.*;

import views.html.*;




public class Secured extends Security.Authenticator  {
	
	@Override
	public String getUsername(Context ctx){
		return ctx.session().get("role");
	}
	
	@Override
	public Result onUnauthorized(Context ctx){
		return redirect(routes.Authenticate.login());
	}
	
	
	public static boolean checkRole(Context ctx) {  
		String role = ctx.session().get("role"); 
		return (role.equals("reservation"));	 
		}

}