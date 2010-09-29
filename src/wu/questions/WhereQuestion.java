package wu.questions;

import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.user.client.ui.Widget;

public class WhereQuestion extends Question<LatLng>{

	PlaceEditor map;
	
	Marker marker;
	
	public WhereQuestion(String in) {
		super(in);
		map = new PlaceEditor(answerChannel());
	}

	@Override
	public Widget answerPan() {
		return map;
	}

}
