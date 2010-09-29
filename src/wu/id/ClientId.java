package wu.id;

import wu.datastore.Entity;
import wu.datastore.StringWrap;


public class ClientId {

	String kindForGAE;
	
	public void createNewUser(String refmail, String pass){
		Entity newUser = new Entity("user",refmail);
		newUser.setProperty("email", new StringWrap(refmail));
		newUser.setProperty("password", new StringWrap(pass));
		
	}
	
}
