package wu.datastore;

public class Link implements GWTSerializable {

	String value;

	public Link(){}

	public Link(String v){
		if (v.length() >2038) {
			throw new IllegalArgumentException("Link Strings cannot exceed 2038 characters");
		}
		value = v;

	}

	public String getValue(){return value;}

	public String toString(){
		return "(Text "+value+")";
	}

}
