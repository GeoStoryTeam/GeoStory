package wu.questions;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.control.MapTypeControl;
import com.google.gwt.maps.client.control.SmallMapControl;
import com.google.gwt.maps.client.event.MapClickHandler;
import com.google.gwt.maps.client.event.MarkerClickHandler;
import com.google.gwt.maps.client.event.MarkerDragEndHandler;
import com.google.gwt.maps.client.geocode.Geocoder;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.overlay.Icon;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.maps.client.overlay.MarkerOptions;
import com.google.gwt.maps.client.overlay.Polygon;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Composite;

/**
 * The outline editor to enter polygon
 * TODO
 * - add a set of reference segments to help building polygons
 * - add a set of points to link together to build the polygon
 * - when adding a new point to the polygon, create it next to the closest point A and place it between A and the closest nieghbor.
 * - add a Geocoder helper to add reference points based on latitude or textual description
 * 
 * - Always add another point in the middle of two. When a point is moved add another. right click can delete a point.
 * @author joris
 *
 */
public class OutlineEditor extends Composite{

	final Geocoder geocoder = new Geocoder();

	final MapWidget map;

	Polygon polygon;

	List<Marker> markers;

	public OutlineEditor(){
		markers = new ArrayList<Marker>();
		LatLng mtl = LatLng.newInstance(45.44,-73.6);
		map = new MapWidget(mtl, 11);
		map.setCenter(mtl);
		// Add some controls for the zoom level
		map.setWidth("100%");
		map.setHeight("100%");
		map.setScrollWheelZoomEnabled(true);
		map.addControl(new MapTypeControl());
		map.addControl(new SmallMapControl());
		Polygon temp = new Polygon(new LatLng[]{});
		map.addOverlay(temp);
		map.addMapClickHandler(new MapClickHandler(){
			public void onClick(MapClickEvent event) {
				MarkerOptions options = MarkerOptions.newInstance();
				options.setDragCrossMove(true);
				options.setIcon(Icon.getDefaultIcon());
				options.setDraggable(true);
				Marker marker = new Marker(event.getLatLng(), 
						options);
				marker.addMarkerDragEndHandler(new MarkerDragEndHandler(){
					public void onDragEnd(MarkerDragEndEvent event) {
						buildPoly();
					}});
				marker.addMarkerClickHandler(new MarkerClickHandler(){
					public void onClick(MarkerClickEvent event) {
						markers.remove(event.getSender());
						map.removeOverlay(event.getSender());
						buildPoly();
					}});
				// find the two closest
				int closeIndex = 0;
				int secondIndex = 0;
				if (markers.size() > 0) {
					double dist = Double.POSITIVE_INFINITY;

					for (int i = 0 ; i < markers.size() ; i++){
						double d = marker.getLatLng().distanceFrom(markers.get(i).getLatLng());
						if (d < dist){
							closeIndex = i;
							dist = d;
						}
					}
					int prev = (closeIndex-1+markers.size())% markers.size();
					int succ = (closeIndex+1+markers.size())% markers.size();
					secondIndex = (markers.get(prev).getLatLng().distanceFrom(marker.getLatLng())<markers.get(succ).getLatLng().distanceFrom(marker.getLatLng())?prev:succ);
				}
				//
				markers.add(Math.min(closeIndex,secondIndex),marker);
				map.addOverlay(marker);
				buildPoly();
			}
		});
		// add a filed to place the point thanks to the Geocoder (easiest way to get right on the point)
		Timer resize = new Timer(){
			@Override
			public void run() {
				map.checkResizeAndCenter();
			}
		};
		resize.schedule(10);
		this.initWidget(map);
	}



	private void buildPoly() {
		LatLng[] points = new LatLng[this.markers.size()+1];
		for(int i=0; i < this.markers.size(); i++ ){
			points[i] = this.markers.get(i).getLatLng();
		}
		points[this.markers.size()] = this.markers.get(0).getLatLng();
		if (polygon !=null) {
			map.removeOverlay(polygon);	
		}
		polygon = new Polygon(points,"#0000FF",2,0.8,"#000FF0",0.1);
		map.addOverlay(polygon);
	}
}
