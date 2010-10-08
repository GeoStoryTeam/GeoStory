package wu.geostory;

import java.util.Date;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.maps.client.geom.LatLngBounds;

import wu.events.WEventType;

public class GeoEventTypes {

	// Server interaction events
	
	public final WEventType<String> getEventFromServer;
	
	public final WEventType<Date> centerEvent;// used to align different bands of a timeline

	public final WEventType<Date> panToCenterEvent;
	
	public final WEventType<Interval> timeWindowEvent;

	public final WEventType<GeoStoryItem> itemSelected;// lets everyone know that some event GeoStoryItem was selected.
	
	public final WEventType<GeoStoryItem> itemOver;// lets everyone know that the mouse is over some event GeoStoryItem.
	
	public final WEventType<LatLngBounds> mapViewChanged; // notify when the map view has changed
	
	public final WEventType<GeoStoryItem> itemAdded; // notify when the map view has changed
	
	public final WEventType<GeoStoryItem> itemFromServer; // notify when the map view has changed
	
	public GeoEventTypes(){
		// Server interaction events
		getEventFromServer = new WEventType<String>();
		itemFromServer = new WEventType<GeoStoryItem>();
		// Date modifications events
		centerEvent = new WEventType<Date>();
		panToCenterEvent = new WEventType<Date>();
		timeWindowEvent = new WEventType<Interval>();
		// Items selected 
		itemSelected = new WEventType<GeoStoryItem>();
		itemOver = new WEventType<GeoStoryItem>();
		itemAdded = new WEventType<GeoStoryItem>();
		// Map events
		mapViewChanged = new WEventType<LatLngBounds>();
	}
	
}
