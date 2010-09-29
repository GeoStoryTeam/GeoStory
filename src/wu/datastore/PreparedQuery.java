package wu.datastore;

import java.util.Arrays;

public class PreparedQuery implements GWTSerializable{

	Entity[] results;
	

	public PreparedQuery(){}
	
	public PreparedQuery(Entity[] entities){
		this.results = entities;
	}

	public java.lang.Iterable<Entity>	asIterable() {
		return Arrays.asList(results);
		}

	public java.util.Iterator<Entity>	asIterator() {
		return Arrays.asList(results).iterator();
		}

	public String toString(){
		return Arrays.toString(results);
	}
	
}
