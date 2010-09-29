package wu.questions;

import java.util.ArrayList;
import java.util.List;

import wu.events.WEventType;

import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.control.MapTypeControl;
import com.google.gwt.maps.client.control.SmallMapControl;
import com.google.gwt.maps.client.event.MapClickHandler;
import com.google.gwt.maps.client.event.MarkerClickHandler;
import com.google.gwt.maps.client.event.MarkerDragEndHandler;
import com.google.gwt.maps.client.event.MapClickHandler.MapClickEvent;
import com.google.gwt.maps.client.event.MarkerClickHandler.MarkerClickEvent;
import com.google.gwt.maps.client.event.MarkerDragEndHandler.MarkerDragEndEvent;
import com.google.gwt.maps.client.geocode.Geocoder;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.overlay.Icon;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.maps.client.overlay.MarkerOptions;
import com.google.gwt.maps.client.overlay.Polygon;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Composite;

public class PlaceEditor extends Composite{

	final Geocoder geocoder = new Geocoder();

	final MapWidget map;

	Polygon polygon;

	Marker marker;
	
	WEventType<LatLng> answerChannel;

	public PlaceEditor(WEventType<LatLng> wEventType){
		this.answerChannel = wEventType;
		LatLng mtl = LatLng.newInstance(45.44,-73.6);
		map = new MapWidget(mtl, 4);
		map.setCenter(mtl);
		// Add some controls for the zoom level
		map.setWidth("100%");
		map.setHeight("100%");
		map.setScrollWheelZoomEnabled(true);
		map.addControl(new MapTypeControl());
		map.addControl(new SmallMapControl());
		map.addMapClickHandler(new MapClickHandler(){
			public void onClick(MapClickEvent event) {
				MarkerOptions options = MarkerOptions.newInstance();
				options.setDragCrossMove(true);
				options.setIcon(Icon.getDefaultIcon());
				options.setDraggable(true);
				Marker marker = new Marker(event.getLatLng(), 
						options);
				marker.addMarkerClickHandler(new MarkerClickHandler(){
					public void onClick(MarkerClickEvent event) {
						map.removeOverlay(event.getSender());
					}});
				map.addOverlay(marker);
				answerChannel.shareEvent(marker.getLatLng());
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


	
}
