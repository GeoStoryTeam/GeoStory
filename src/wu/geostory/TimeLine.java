package wu.geostory;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.event.dom.client.HasKeyPressHandlers;
import com.google.gwt.event.dom.client.HasMouseWheelHandlers;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.dom.client.MouseWheelEvent;
import com.google.gwt.event.dom.client.MouseWheelHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;


/**
 * TODO:
 * - define an algebra of time (never, always, now, before, after and dates and intervals)
 * - use CSS to change cursor image when mouse is down
 * - draw line for the different time separation.
 * - render only relevant lines,
 * - make it faster
 * - could be vertical for genealogy display (combined with graph and dependancy handling)
 * - drawing with CSS :: most of what I do does not require a Canvas. 
 * - http://nicolasgallagher.com/progressive-enhancement-pure-css-speech-bubbles/
 * @author joris
 *
 */
public class TimeLine extends Composite 
implements 
HasMouseWheelHandlers, MouseWheelHandler,
HasKeyPressHandlers, KeyPressHandler
{
	FlowPanel container;
	// the integer represents the portion of the timeline the band should take
	Map<Band,Integer> bands;
	Band upper;
	//Band lower;

	Band mainBand;
	GeoEventTypes types;
	GeoStoryModel model;

	// the different zoom level we can move within actually we use Couple
	Res[] zoomLevels = new Res[]{
			new ResHour(1),
			new ResHour(6),
			new ResHour(12),
			new ResDay(1),
			new ResDay(7),
			new ResDay(15),
			new ResMonth(1),
			new ResMonth(3),
			new ResYear(1),
			new ResYear(10)
			//new ResYear(100)
	};

	int zoom = 7;

	public TimeLine(GeoStoryModel model, GeoEventTypes typ){
		this.model = model;
		container = new FlowPanel();
		bands = new HashMap<Band,Integer>();
		this.types = typ;
		// when the second event source is null, it means the duration does not count.
		//this.addBand(new Band(this,model,new ResHour(),new Date(),types),		25);
		//this.addBand(new Band(this,model,new ResDay(),new Date(),types),		50);

		upper = new Band(this,model,zoomLevels[zoom],new Date(),types);
		//lower = new Band(this,model,zoomLevels[zoom+1],new Date(),types);
		this.addBand(upper,		50);
		//this.addBand(lower,		10);

		// Mouse wheel mechanism
		this.addMouseWheelHandler(this);
		this.addKeyPressHandler(this);
		
		this.initWidget(container);

		// Required to initialize bands. Ugly
		Timer t = new Timer(){
			public void run() {
				types.centerEvent.shareEvent(null);
			}
		};
		// To first initialize correctly the component
		t.schedule(1);
		this.getElement().getStyle().setProperty("userSelect", "false");
	}

	/**
	 * add a band and recompute each band size based on given proportion.
	 * @param d
	 * @param proportion
	 */
	private void addBand(Band d, Integer proportion){
		if (mainBand == null) {
			mainBand = d;
			d.setMain();
		}
		container.add(d);
		bands.put(d,proportion);
		int total = 0;
		for (Band band : bands.keySet()){
			total += bands.get(band);
		}
		for (Band band : bands.keySet()){
			// dynamically compute the sizes for each band
			band.setSize("100%", (int)(bands.get(band)*100.0/total)+"%");
		}
	}

	public HandlerRegistration addMouseDownHandler(MouseDownHandler handler) {
		return addDomHandler(handler, MouseDownEvent.getType());}

	public HandlerRegistration addMouseWheelHandler(MouseWheelHandler handler) {
		return addDomHandler(handler, MouseWheelEvent.getType());}

	public HandlerRegistration addMouseMoveHandler(MouseMoveHandler handler) {
		return addDomHandler(handler, MouseMoveEvent.getType());}

	public HandlerRegistration addMouseUpHandler(MouseUpHandler handler) {
		return addDomHandler(handler, MouseUpEvent.getType());}

	@Override
	public HandlerRegistration addKeyPressHandler(KeyPressHandler handler) {
		return addDomHandler(handler, KeyPressEvent.getType());}

	public Interval getBounds() {
		if (mainBand == null) return null;
		return mainBand.getDisplayedInterval();
	}

	// called by one band to refresh all bands
	public void refresh(Band emitter, int pixel, Res originResolution){
		for (Band b : bands.keySet()){
			if (b!= emitter){
				b.shiftBand(pixel,originResolution);
			}
		}
	}

	public void refresh() {
		upper.completePaint();
		//lower.completePaint();
	}

	private void updateRes(){
		upper.setResolution(this.zoomLevels[zoom]);
		//lower.setResolution(this.zoomLevels[zoom+1]);
	}

	@Override
	public void onMouseWheel(MouseWheelEvent event) {
		// one increment only
		int direction = (int) Math.signum(event.getDeltaY());
		zoom = Math.min(Math.max(0, zoom + direction),zoomLevels.length-2);
		this.updateRes();
		types.centerEvent.shareEvent(null);
	}

	public void init() {
		upper.onResize(null);
		//lower.onResize(null);
	}

	@Override
	public void onKeyPress(KeyPressEvent event) {
		System.out.println("Key pressed in Timeline "+ event.getCharCode());
	}


}
