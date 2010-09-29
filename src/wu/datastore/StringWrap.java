package wu.datastore;


public class StringWrap implements GWTSerializable {

	String content;
	
	public StringWrap(){}
	
	public StringWrap(String c){
		if (c.length() >500) {
			throw new IllegalArgumentException("Strings cannot exceed 500 characters");
		}
		content = c;
	}

	public String getValue() {
		return content;
	}
	
}
