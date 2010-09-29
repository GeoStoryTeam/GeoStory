package wu.questions;

public class LocalID {

	static int counter = 0;
	
	final int identifier; 
	
	public static LocalID getOne() {
		// TODO Auto-generated method stub
		return new LocalID(counter++);
	}

	private LocalID(int n){
		this.identifier = n;
	}
	
	
	public String toString(){
		return ""+identifier;
	}
}
