package wu.geostory;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.maps.client.InfoWindow;
import com.google.gwt.maps.client.InfoWindowContent;
import com.google.gwt.maps.client.MapType;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.control.LargeMapControl;
import com.google.gwt.maps.client.control.MapTypeControl;
import com.google.gwt.maps.client.control.SmallMapControl;
import com.google.gwt.maps.client.event.MapMoveHandler;
import com.google.gwt.maps.client.event.MarkerClickHandler;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.maps.client.overlay.Polygon;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;

public class Space extends Composite{

	MapWidget space;

	Map<GeoStoryItem,Marker> markers;
	Map<GeoStoryItem,Marker> polygons;

	final GeoStoryModel model;
	
	final GeoEventTypes types;

	public Space(LatLng point, int zoom, GeoEventTypes typ, GeoStoryModel mod){
		space = new MapWidget(point,zoom);
		space.setCenter(point, zoom, MapType.getSatelliteMap());
		this.types = typ;
		this.model = mod;
		space.setScrollWheelZoomEnabled(true);
		space.addControl(new MapTypeControl());
		space.addControl(new LargeMapControl());
		
		space.addMapType(MapType.getSatelliteMap());
		
		markers = new HashMap<GeoStoryItem,Marker>();
		polygons = new HashMap<GeoStoryItem,Marker>();
		
		space.addMapMoveHandler(new MapMoveHandler(){
			public void onMove(MapMoveEvent event) {
				types.mapViewChanged.shareEvent(space.getBounds());
			}});
		this.initWidget(space);
	}

	public void addOverlay(Marker m) {this.space.addOverlay(m);}

	public void drawSpace(){
		// TODO the remove phase 
		//space.clearOverlays();
		for (GeoStoryItem i : markers.keySet()){
			if (!model.getItems().contains(i)){
				space.removeOverlay(markers.get(i));
			}
		}
		for (final GeoStoryItem item : model.getItems()){
			if (!markers.containsKey(item)){
				final Marker m = new Marker(item.point);
				m.addMarkerClickHandler(new MarkerClickHandler(){
					public void onClick(MarkerClickEvent event) {
						//centerBothOnItem(item);
						types.itemSelected.shareEvent(item);
						//m.showMapBlowup(new InfoWindowContent("content is "+item));
					}});
				double d = 0.001;
				Polygon p = new Polygon(new LatLng[]{
						LatLng.newInstance(item.point.getLatitude()+d, item.point.getLongitude()+d),
						LatLng.newInstance(item.point.getLatitude()+d, item.point.getLongitude()-d),
						LatLng.newInstance(item.point.getLatitude()-d, item.point.getLongitude()-d),
						LatLng.newInstance(item.point.getLatitude()-d, item.point.getLongitude()+d),
						LatLng.newInstance(item.point.getLatitude()+d, item.point.getLongitude()+d)
				}, 
				model.colors.get(item).hexString(), 
				4, 
				1.0, 
				model.colors.get(item).hexString(),  
				0.5);
				space.addOverlay(p);
				space.addOverlay(m);
				polygons.put(item,m);
				markers.put(item, m);
			}
		}
	}

	public void centerOnItem(GeoStoryItem item){
		if (!item.place.equals(space.getCenter())){
			// handle the info window on the map
			InfoWindow existing = space.getInfoWindow();
			if (existing != null) existing.close();
			InfoWindowContent iwc = new InfoWindowContent(new HTML(
					item.description.toString()+"<br/>"+
					item.place+"<br/>"+
					item.point+"<br/>"+
					item.period+"<br/>"+
					model.colors.get(item)+"<br/>"
					));
			existing.open(markers.get(item), iwc);
			// center the map on the item to show
			space.panTo(item.point);
		}
	}
	
	public void checkResizeAndCenter() {
		this.space.checkResizeAndCenter();		
	}
}
