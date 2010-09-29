package wu.geostory;

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.Button;

public class Bar extends Button{
	
	GeoStoryItem item;
	
	public Bar(int width, int height, GeoStoryItem item, GeoColor color){
		this.setPixelSize(width, height);
		this.item = item;
		//this.setText(item.item.toString());
		Style s = this.getElement().getStyle();
		//s.setProperty("font_size", height/2+"px");
		//s.setProperty("border", "1px");
		s.setProperty("background", color.cssString());
		s.setProperty("textAlign", "left");
		//s.setProperty("fontSize", height+"pt");
		this.getElement().getStyle().setProperty("userSelect", "false");
		
	}
	
}
