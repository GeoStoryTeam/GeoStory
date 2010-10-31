package wu.questions;

import wu.events.WEventType;

import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.user.client.ui.Widget;

public class WhereQuestion extends Question<String>{

	PlaceEditor map;
	
	Marker marker;
	
	public WhereQuestion(String in, WEventType<String> where) {
		super(in);
		map = new PlaceEditor(answerChannel(), where);
	}

	@Override
	public Widget answerPan() {
		return map;
	}

}
