
/*
 * Name: Secured.java
 * Description: authorises a user based on the user's role
 * Written On: 01/12/2012
 * @reference https://github.com/playframework/Play20/tree/master/samples/java/zentasks
 * 			(using the authentication code from Play sample app)
 * @author Jenny Cooper, x12101303
 * 
 */
package controllers;

import play.mvc.*;
import play.mvc.Http.*;



public class AuthorisedRole extends Security.Authenticator  {
	
	/*
	 * (non-Javadoc)
	 * @see play.mvc.Security.Authenticator#getUsername(play.mvc.Http.Context)
	 */
	@Override
	public String getUsername(Context ctx){
		return ctx.session().get("role");
	}
	
	/*
	 * (non-Javadoc)
	 * @see play.mvc.Security.Authenticator#getUsername(play.mvc.Http.Context)
	 */
	@Override
	public Result onUnauthorized(Context ctx){
		return redirect(routes.Authenticate.login());
	}

	/*
	 * @author Jenny Cooper, x12101303
	 * checks if the user has the correct role, specified in the session
	 * @params args Context
	 * return: true if user has role of 'reservation'
	 */
	public static boolean checkRole(Context ctx) {  
		String role = ctx.session().get("role"); 
		return (role.equals("reservation"));	 
		}

}