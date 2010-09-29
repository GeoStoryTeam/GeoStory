package wu.datastore;

import java.util.HashMap;
import java.util.Map;


public class Entity implements GWTSerializable{

	Key key;
	Key parent;
	String kind;
	String keyName;

	java.util.Map<String,GWTSerializable> props;

	public Entity(){}

	public Entity(Key key) {
		this.key = key;
	}
	public Entity(java.lang.String kind) {
		this.kind = kind;
	}
	public Entity(java.lang.String kind, Key parent) {
		this.kind = kind;
		this.parent = parent;
	}
	public Entity(java.lang.String kind, java.lang.String keyName) {
		this.kind = kind;
		this.keyName = keyName;
	}
	public Entity(java.lang.String kind, java.lang.String keyName, Key parent) {
		this.kind = kind;
		this.keyName = keyName;
		this.parent = parent;
	}

	public String getKind() {
		return kind;
	}
	public Key getKey() {
		return key;
	}

	public Key getParent() {
		return parent;
	}

	public String getKeyName() {
		return keyName;
	}

	public java.util.Map<java.lang.String,java.lang.Object>	getProperties() {
		Map<java.lang.String,java.lang.Object> result = new HashMap<java.lang.String,java.lang.Object>();
		result.putAll(props);
		return result;

	}

	public String toString(){
		String result = "{Entity key = "+key+" ";
		if (props != null){
			for (Map.Entry<String, GWTSerializable> prop : this.props.entrySet()){
				result += "("+prop.getKey()+" -> "+prop.getValue()+")";
			}
		}
		result +=" }";
		return result;
	}

	public void	setProperty(java.lang.String propertyName, GWTSerializable value) {
		if (props == null) props = new HashMap<String,GWTSerializable>();
		props.put(propertyName, value);
	}

	public void	setUnindexedProperty(java.lang.String propertyName, GWTSerializable value) {
		if (props == null) props = new HashMap<String,GWTSerializable>();
		props.put(propertyName, value);
	}

}
