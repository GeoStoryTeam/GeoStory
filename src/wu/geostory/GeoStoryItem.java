package wu.geostory;

import java.io.Serializable;
import java.util.Date;

import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.maps.client.geom.LatLng;

/**
 * TODO
 * - add the natural name for the place
 * @author joris
 *
 */

public class GeoStoryItem implements Comparable<GeoStoryItem>, Serializable {
	
	boolean isVisible = true; // visible by default
	
	public boolean isVisible() {
		return isVisible;
	}

	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
	}

	Interval period;

	LatLng point;

	String place;

	String description;

	public GeoStoryItem(){}

	public GeoStoryItem(Interval d, String pl, LatLng ll, String desc){
		this.period = d;
		this.point = ll;
		this.place = pl;
		this.description  = desc;
	}

	public String toString(){
		return this.place.toString()+" @ "+this.period+";"+this.description+";"+this.point;
	}

	@Override
	public int hashCode(){
		return this.period.hashCode();
	}

	@Override
	public boolean equals(Object o){
		if (o instanceof GeoStoryItem){
			GeoStoryItem bis = (GeoStoryItem) o;
			return this.period.equals(bis.period) && this.place.equals(bis.place) && description.equals(bis.description);
		}
		else{return false;}
	}

	@Override
	public int compareTo(GeoStoryItem gsi) {
		int res = this.period.compareTo(gsi.period);
		if (res != 0) return res;
		return place.toString().compareTo(gsi.place.toString());
	}

	public Interval getInterval() {
		return period;
	}

	public LatLng getPoint() {
		return this.point;
	}

	public String getDescription() {
		return this.description;
	}

	public String getPlace() {
		return place;
	}

	public JSONObject toJSON(){
		JSONObject result = new JSONObject();
		result.put("description", new JSONString(description));
		result.put("place", new JSONString(this.place));
		result.put("latitude", new JSONNumber(point.getLatitude()));
		result.put("longitude", new JSONNumber(point.getLongitude()));
		result.put("begin", new JSONNumber(period.getA().getTime()));
		result.put("end", new JSONNumber(period.getB().getTime()));
		return result;
	}

	public static GeoStoryItem fromJSON(JSONObject object){
		System.out.println(" Decoding " + object);
		Interval interval = new Interval(
				new Date((long) object.get("begin").isNumber().doubleValue()), 
				new Date((long) object.get("end").isNumber().doubleValue())
		);
		LatLng ll = LatLng.newInstance(
				object.get("latitude").isNumber().doubleValue(), 
				object.get("longitude").isNumber().doubleValue()
		);
		String desc = object.get("description").isString().stringValue();
		String plac = object.get("place").isString().stringValue();
		return new GeoStoryItem(interval, plac, ll, desc);
	}
}
