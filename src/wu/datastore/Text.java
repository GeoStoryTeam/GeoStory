package wu.datastore;


public class Text implements GWTSerializable {

	String value;
	
	public Text(){}
	
	public Text(String v){value = v;}
	
	public String getValue(){return value;}
	
	public String toString(){
		return "(Text "+value+")";
	}
}
