package wu.datastore;


public class Key implements GWTSerializable{

	public String key;
	public Key parent;
	public String kind;
	
	public Key(){}
	
	public Key(String keyToString, String kind, Key parent) {
		key = keyToString;
		this.kind = kind;
		this.parent = parent;
	}

	public String toString(){
		return "{Key = "+key+"   parent = "+parent+"}";
	}
	
}
