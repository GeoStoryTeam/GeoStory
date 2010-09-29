package wu.datastore;


public class Query implements GWTSerializable{

	public Key ancestor;

	public String kind;

	public Query() {}

	public Query(Key ancestor) {
		this.ancestor = ancestor;
	}
	
	public Query(String kind) {
		this.kind = kind;
	}
	
	public Query(String kind, Key ancestor) {
		this.kind = kind;
		this.ancestor = ancestor;
	}

}
