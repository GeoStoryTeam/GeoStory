package wu.id;

public interface IdInterface {

	public boolean isLogedIn();
	
	public void logAs(String id, String password);
	
	public void resetPassword(String id);
	
	
	
}
