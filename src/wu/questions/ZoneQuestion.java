package wu.questions;

import java.util.List;

import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.user.client.ui.Widget;

public class ZoneQuestion extends Question<List<LatLng>>{

	OutlineEditor map;
	
	public ZoneQuestion(String in) {
		super(in);
		map = new OutlineEditor();
	}

	@Override
	public Widget answerPan() {
		return map;
	}

}