package wu.questions;

import wu.events.WEvent;
import wu.events.WEventType;
import wu.events.WHandler;
import wu.geostory.GeoStoryItem;

import com.google.gwt.maps.client.GoogleBarAdsOptions;
import com.google.gwt.maps.client.GoogleBarOptions;
import com.google.gwt.maps.client.MapOptions;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.control.MapTypeControl;
import com.google.gwt.maps.client.control.SmallMapControl;
import com.google.gwt.maps.client.event.MapClickHandler;
import com.google.gwt.maps.client.event.MarkerClickHandler;
import com.google.gwt.maps.client.geocode.Geocoder;
import com.google.gwt.maps.client.geocode.LatLngCallback;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.overlay.Icon;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.maps.client.overlay.MarkerOptions;
import com.google.gwt.maps.client.overlay.Polygon;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;

public class PlaceEditor extends Composite{

	final Geocoder geocoder = new Geocoder();

	final MapWidget map;

	Polygon polygon;

	Marker marker;

	WEventType<String> answerChannel;

	public PlaceEditor(WEventType<String> wEventType, WEventType<String> placeIndicator){
		this.answerChannel = wEventType;
		// 

		LatLng mtl = LatLng.newInstance(45.44,-73.6);

		map = new MapWidget(mtl,4);
		map.setCenter(mtl);
		// Add some controls for the zoom level
		map.setWidth("100%");
		map.setHeight("100%");
		map.setScrollWheelZoomEnabled(true);
		map.addControl(new MapTypeControl());
		map.addControl(new SmallMapControl());
		map.addMapClickHandler(new MapClickHandler(){
			public void onClick(MapClickEvent event) {
				setMarker(event.getLatLng());
			}
		});
		if (placeIndicator != null){
			placeIndicator.registerHandler(new WHandler<String>(){
				@Override
				public void onEvent(final WEvent<String> elt) {
					if (marker == null){
						geocoder.getLatLng(elt.getElement(), new LatLngCallback(){
							@Override
							public void onFailure() {
								System.out.println("Failed geocoder for   "+"---"+elt.toString()+" ");
							}
							@Override
							public void onSuccess(LatLng point) {
								map.setCenter(point);
								setMarker(point);
								map.checkResizeAndCenter();
							}
						});
					}
				}});
		}
		Timer resize = new Timer(){
			@Override
			public void run() {
				map.checkResizeAndCenter();
			}
		};
		resize.schedule(1);
		map.checkResizeAndCenter();
		this.initWidget(map);
	}


	private void setMarker(LatLng ll){
		deleteMarker();
		MarkerOptions options = MarkerOptions.newInstance();
		options.setDragCrossMove(true);
		options.setIcon(Icon.getDefaultIcon());
		options.setDraggable(true);
		marker = new Marker(ll, options);
		marker.addMarkerClickHandler(new MarkerClickHandler(){
			public void onClick(MarkerClickEvent event) {
				map.removeOverlay(event.getSender());
				deleteMarker();
			}});
		map.addOverlay(marker);
		map.checkResizeAndCenter();
		answerChannel.shareEvent(marker.getLatLng().getLatitude()+"|"+marker.getLatLng().getLongitude());
	}

	private void deleteMarker(){
		if (marker != null){
			map.removeOverlay(marker);
			marker = null;
			answerChannel.shareEvent(null);
		}
	}

}
