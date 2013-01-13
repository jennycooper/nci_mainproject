package controllers;

/*
 * Name: Application.java
 * Description: main controller for application
 * Written On: 14/10/2012
 * @reference https://github.com/playframework/Play20/tree/master/samples/java/zentasks
 * 			(using the authentication code from Play sample app)
 * @author Jenny Cooper, x12101303
 * 
 */

import java.text.SimpleDateFormat;
import java.util.*;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.SqlRow;


import models.*;

import play.*;
import play.data.*;
import play.mvc.*;
import play.mvc.Http.Session;

import views.html.*;




public class Authenticate extends Controller  {

    
    public static class Login {
    	
        /*
         * @reference https://github.com/playframework/Play20/tree/master/samples/java/zentasks
         */
        public String email;
        public String password;
        
        public String validate() {
            if(User.authenticate(email, password) == null) {
                return "Invalid user or password";
            }
            return null;
        }
        
    }

    /**
     * Login page.
     */
    public static Result login() {
    	/*
    	 * @reference https://github.com/playframework/Play20/tree/master/samples/java/zentasks
    	 */
        return ok(views.html.login.render(form(Login.class)));
    }
    
    /**
     * Handle login form submission.
     */
    public static Result authenticate() {
    	/*
    	 * @author Jenny Cooper, x12101303
    	 */
        Form<Login> loginForm = form(Login.class).bindFromRequest();
        if(loginForm.hasErrors()) {
            return badRequest(views.html.login.render(loginForm));
        } else {
        	
        	session().clear();
        	//find the user from their login email address, and use their role in the 'session' cookie
        	User user = User.findByEmail(loginForm.get().email);
            session("role", user.role);
            return redirect(routes.Calendars.index(""));
        }
    }

    /**
     * Logout and clean the session.
     */
    public static Result logout() {
    	/*
    	 * @reference https://github.com/playframework/Play20/tree/master/samples/java/zentasks
    	 */
        session().clear();
        flash("success", "You've been logged out");
        return redirect(routes.Authenticate.login());
    }
	
}