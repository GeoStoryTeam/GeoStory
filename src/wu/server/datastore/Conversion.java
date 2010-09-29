package wu.server.datastore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Text;

/**
 * Makes all relevant conversion between formats for GWT RPC and GAE classes
 * @author joris
 *
 */
public class Conversion {

	////////////////////Keys





	public static wu.datastore.Key convert(Key k){
		if (k == null) return null;
		return new wu.datastore.Key(KeyFactory.keyToString(k),k.getKind(),convert(k.getParent()));

	}

	public static Key convert(wu.datastore.Key k){
		if (k==null) return null;
		return KeyFactory.stringToKey(k.key);
	}


	////////////////////TODO Entity handle properties

	public static wu.datastore.Entity convert(Entity k){
		// Entity taken from the datastore always have a Key that holds necessary information
		wu.datastore.Entity result = 
			new wu.datastore.Entity(convert(k.getKey()));
		for (Map.Entry<String,Object> entry : k.getProperties().entrySet()){
			if (entry.getValue() instanceof Text){
				result.setProperty(entry.getKey(), Conversion.convert((Text)entry.getValue()));
			}
		}
		return result;
	}

	/**
	 * Takes an entity from the datastore and build a wire entity to bring to the GWT Client
	 * @param k
	 * @return
	 */
	public static Entity convert(wu.datastore.Entity k){
		Entity result = null;
		if (k.getKind() == null ){
			result =  new Entity(convert(k.getKey()));
		}
		else{
			if (k.getKeyName() == null){
				if (k.getParent() == null){
					result = new Entity(k.getKind());
				}
				else{
					result =  new Entity(k.getKind(),convert(k.getParent()));
				}
			}
			else{
				if (k.getParent() == null){
					result =  new Entity(k.getKind(), k.getKeyName());
				}
				else{
					result =  new Entity(k.getKind(),k.getKeyName(),convert(k.getParent()));
				}
			}
		}
		if (k.getProperties() != null){
			for (Map.Entry<String,Object> entry : k.getProperties().entrySet()){
				System.out.println("Conversion Kokkoonn vers GAE propriétés "+ entry);
				//DataTypeUtils.checkSupportedValue(Conversion.convert(entry.getValue()));
				if (entry.getValue() instanceof wu.datastore.Text){
					result.setProperty(entry.getKey(), 
							Conversion.convert((wu.datastore.Text)entry.getValue()));
				}
				else if (entry.getValue() instanceof wu.datastore.StringWrap){
					result.setProperty(entry.getKey(), 
							((wu.datastore.StringWrap)entry.getValue()).getValue());
				}
			}
		}
		return result;
	}


	/////////////////////////////////TODO Query
	public static wu.datastore.Query convert(Query query) {
		throw new IllegalArgumentException("You should not convert DataStore query into GWT Query");
		// There is no need to convert queries back see PreparedQuery
	}

	/**
	 * Convert the query built with the GWT client into a PreparedQuery.
	 * @param query
	 * @return
	 */
	public static Query convert(wu.datastore.Query query) {
		if (query.kind == null){
			return new Query(convert(query.ancestor));
		}
		else{
			if (query.ancestor==null){
				return new Query(query.kind);
			}
			else{
				return new Query(query.kind, convert(query.ancestor));
			}
		}
	}

	public static wu.datastore.PreparedQuery convert(
			com.google.appengine.api.datastore.PreparedQuery pq) {
		List<wu.datastore.Entity> entities = 
			new ArrayList<wu.datastore.Entity>();
		for (com.google.appengine.api.datastore.Entity e : pq.asIterable()){
			entities.add(convert(e));
		}
		return new wu.datastore.PreparedQuery(entities.toArray(new wu.datastore.Entity[entities.size()]));
	}

	/////////////////////////////////TODO data types
	public static wu.datastore.Text convert(Text t) {
		return new wu.datastore.Text(t.getValue());
	}
	
	public static Text convert(wu.datastore.Text t) {
		return new Text(t.getValue());
	}
}
