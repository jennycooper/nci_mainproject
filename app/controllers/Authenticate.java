package controllers;


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
	
	
// -- Authentication
    
    public static class Login {
        
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
        return ok(views.html.login.render(form(Login.class)));
    }
    
    /**
     * Handle login form submission.
     */
    public static Result authenticate() {
        Form<Login> loginForm = form(Login.class).bindFromRequest();
        if(loginForm.hasErrors()) {
            return badRequest(views.html.login.render(loginForm));
        } else {
        	
        	session().clear();
        	//find the user from their login email address, and use their role in the 'session' cookie
        	User user = User.findByEmail(loginForm.get().email);
            session("role", user.role);
            return redirect(routes.Application.index(""));
        }
    }

    /**
     * Logout and clean the session.
     */
    public static Result logout() {
        session().clear();
        flash("success", "You've been logged out");
        return redirect(routes.Authenticate.login());
    }
	
}