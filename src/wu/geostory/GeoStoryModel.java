package wu.geostory;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import wu.events.WEvent;
import wu.events.WHandler;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.ui.Button;

/**
 * TODO add a set of filters under the form of a set of Predicate<GeoStoryItem>
 * - algorithm to find colors as distant as possible for points that are close on the map.
 * @author joris
 *
 */
public class GeoStoryModel {

	private Set<GeoStoryItem> items;
	//private JSONParser json;
	
	private GeoEventTypes types;
	private Map<GeoStoryItem,Button> widgets;
	Map<GeoStoryItem,GeoColor> colors;
	private Map<GeoStoryItem,Integer> lines;
	private Integer numberOfLines;

	public GeoStoryModel(GeoEventTypes types){
		this.types = types;
		items = new HashSet<GeoStoryItem>();
		colors = new HashMap<GeoStoryItem,GeoColor>();
		widgets = new HashMap<GeoStoryItem,Button>();
		types.itemFromServer.registerHandler(new WHandler<GeoStoryItem>(){
			@Override
			public void onEvent(WEvent<GeoStoryItem> elt) {
				add(elt.getElement());
			}});
		types.itemAdded.registerHandler(new WHandler<GeoStoryItem>(){
			@Override
			public void onEvent(WEvent<GeoStoryItem> elt) {
				add(elt.getElement());
			}});
	}

	private void add(final GeoStoryItem item){
		if (!items.contains(item)){
			items.add(item);
			// dealing with color.
			GeoColor cand = null;
			double value = 0;
			Map<GeoStoryItem,GeoColor> neighs = this.neighbours(item, colors);
			for (int i = 0 ; i < 30 ; i++){
				GeoColor col = GeoColor.fromHSV(Random.nextDouble()*360.0, .3, 0.8);
				double score = GeoColor.distWithColor(neighs, col);
				if (score > value){
					value = score;
					cand = col;
				}
			}
			colors.put(item, cand);
			Bar bar  = new Bar(10,10,item,cand);
			bar.addClickHandler(new ClickHandler(){
				@Override
				public void onClick(ClickEvent event) {
					types.itemSelected.shareEvent(item);
				}});
			widgets.put(item, bar);
			lines = null; //invalidate cache
		}
	}

	public void populateRandomly(int number){
		for (int i = 0 ; i < number ; i++){
			String key = "key="+Random.nextInt()%256;
			// Build a date between now and tomorrow
			Date start = new Date();
			start.setTime(start.getTime()+Random.nextInt(1000*60*60*24));
			Date end = new Date();
			end.setTime(start.getTime()+1000*60*60*Random.nextInt(3));
			// Build latitude to fit in montreal
			float lat = (float) (45.45 + Random.nextDouble()/4);
			// Build longitude to fit in montreal
			float lng = (float) (-74.0 + Random.nextDouble());
			Interval inter = new Interval(start,end);
			// Insert the new random one			
			this.add(new GeoStoryItem(
					inter,
					key.toString(),
					LatLng.newInstance(lat,lng),
					key));	
		}
	}

	private Map<GeoStoryItem,GeoColor> neighbours(GeoStoryItem newby, Map<GeoStoryItem,GeoColor> colours){
		Map<GeoStoryItem,GeoColor> res = new HashMap<GeoStoryItem,GeoColor>();
		for (GeoStoryItem i : colours.keySet()){
			if (i.point.distanceFrom(newby.point) < 10000 || i.period.overlaps(newby.period)) res.put(i, colours.get(i));
		}
		return res;
	}

	// gives the number of simultaneous event on intervals
	private static Map<Interval,Integer> totalEvents(List<Interval> events){
		Map<Date, Integer> extremas = new TreeMap<Date,Integer>();
		for (Interval event : events){
			if (!extremas.containsKey(event.getA())) extremas.put(event.getA(), 0);
			if (!extremas.containsKey(event.getB())) extremas.put(event.getB(), 0);
			extremas.put(event.getA(), extremas.get(event.getA())+1);
			extremas.put(event.getB(), extremas.get(event.getB())-1);
		}
		Map<Interval, Integer> steps = new TreeMap<Interval,Integer>();
		List<Date> ext = new ArrayList<Date>();
		ext.addAll(extremas.keySet());
		Date previous = ext.get(0);
		int openIntervals = 0;
		for (Date e : ext){
			openIntervals += extremas.get(previous);
			steps.put(new Interval(previous,e), openIntervals);
			previous = e;
		}
		return steps;
	}

	/**
	 * Defines lines of non overlapping Interval that can be displayed as gantt-like things 
	 * TODO warning this is based on the fact that equality is identity so two logically similar intervals can be distinguished.
	 * This is bad
	 * @param events
	 * @return
	 */
	public void updateLines(Predicate<GeoStoryItem>... filters){
		int currentline = 0;
		// working copy
		Map<GeoStoryItem, Button> events = new HashMap<GeoStoryItem,Button>(widgets);
		// Apply filters to remove some of the elements TODO might not be the right solution as we would like to dynamically reduce the set.
		for(GeoStoryItem gsi : widgets.keySet()){
			for(Predicate<GeoStoryItem> filter : filters){
				if (filter.eval(gsi)){
					events.remove(gsi);
					break;
				}
			}
		}
		Map<GeoStoryItem,Integer> res = new TreeMap<GeoStoryItem,Integer>();
		while (!events.isEmpty()){
			Set<GeoStoryItem> actualLine = new HashSet<GeoStoryItem>();
			for (GeoStoryItem event : events.keySet()){
				boolean overlapLine = false;
				for (GeoStoryItem inTheLine : actualLine){
					if (inTheLine.period.overlaps(event.period)){
						overlapLine = true;
						break;
					}
				}
				if (!overlapLine){
					actualLine.add(event);
					res.put(event, currentline);
				}
			}
			for (GeoStoryItem gsi : actualLine){
				events.remove(gsi);
			}
			currentline++;
		}
		lines = res;
		this.numberOfLines = currentline;
	}

	public Set<GeoStoryItem> getItems() {
		return items;
	}

	
	public Integer numberOfLines(){
		if (lines == null) this.updateLines();
		return this.numberOfLines;
	}

	public Button widgetFor(GeoStoryItem gsi){
		return this.widgets.get(gsi);
	}

	public Integer lineFor(GeoStoryItem gsi){
		if (lines == null) this.updateLines();
		return this.lines.get(gsi);
	}

	public void reset() {
		this.items.clear();
		this.colors.clear();
		this.lines = null;
		this.widgets.clear();
	}
}
