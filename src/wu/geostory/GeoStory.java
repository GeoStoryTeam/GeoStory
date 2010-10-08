package wu.geostory;

import java.util.Date;
import java.util.List;

import wu.events.WHandler;
import wu.events.WEvent;
import wu.questions.ContentQuestion;
import wu.questions.OrderQuestion;
import wu.questions.QPanel;
import wu.questions.Question;
import wu.questions.Questionnaire;
import wu.questions.WhenQuestion;
import wu.questions.WhereQuestion;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.event.shared.HandlerManager;

import com.google.gwt.maps.client.geocode.Geocoder;
import com.google.gwt.maps.client.geocode.LatLngCallback;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.overlay.Polyline;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Implement a geostory widget with a timeline where action are the same as google maps meaning:
 * - roll the mouse to zoom in or out in time
 * - click and move to go to past or future
 * - use color to link map marker to timeline marker. make saturation higher when an event is close from center of timeline.
 * 
 * @author joris
 *
 */
public class GeoStory extends Composite implements ResizeHandler{
	
	SplitLayoutPanel panel;

	ListBox list;

	TimeLine time;

	final TextBox email;
	
	// Event types
	GeoEventTypes types;

	public GeoEventTypes getTypes() {
		return types;
	}

	// The map thanks to Google
	Space space;

	// The model with things related to position and dates
	GeoStoryModel pad;

	VerticalPanel controls;

	public GeoStory(){
		panel = new SplitLayoutPanel();
		types = new GeoEventTypes();
		pad = new GeoStoryModel(types);
		//pad.populateRandomly(10);
		{
			{
				space = new Space(LatLng.newInstance(45.50,-73.60),4, types, pad);
			}
			{
				time = new TimeLine(pad, types);
			}
			{
				list = new ListBox(true);
				list.setVisibleItemCount(20);
				for (GeoStoryItem i : pad.getItems()){
					list.addItem(i.toString());
				}
				//time.setSize("100%", "100%");
			}
			{
				controls = new VerticalPanel();
				{
					email = new TextBox();
					controls.add(email);
					email.addChangeHandler(new ChangeHandler(){
						@Override
						public void onChange(ChangeEvent event) {
							
						}});
				}
				{
					Button getFromServer = new Button("Get from server");
					getFromServer.addClickHandler(new ClickHandler(){
						@Override
						public void onClick(ClickEvent event) {
							types.getEventFromServer.shareEvent(getEmail());
						}});
					controls.add(getFromServer);
				}
				{
					Button popRandom = new Button("Populate at random");
					controls.add(popRandom);
					popRandom.addClickHandler(new ClickHandler(){

						@Override
						public void onClick(ClickEvent event) {
							pad.populateRandomly(10);
							types.centerEvent.shareEvent(null);
						}
					});
				}
				{
					Button popFromText = new Button("Populate from text");
					controls.add(popFromText);
					popFromText.addClickHandler(new ClickHandler(){

						@Override
						public void onClick(ClickEvent event) {
							// TODO Auto-generated method stub
							Question<String> q = new ContentQuestion("Description des evenements");
							final QPanel textContent = new QPanel(new Questionnaire(q));
							//tested.setSize("50%", "50%");
							textContent.center();textContent.show();
							q.answerChannel().registerHandler(new WHandler<String>(){
								@Override
								public void onEvent(WEvent<String> elt) {
									//Window.alert(elt.getElement());
									List<Triple<String,Interval,String>> res = GeoStoryParser.parse(elt.getElement());
									int delay = 0;
									final Geocoder geo = new Geocoder();
									for (final Triple<String,Interval,String> indiv : res){
										geodecode(geo, indiv, delay = delay + 100);
									}	
									textContent.hide();
								}});
						}
					});
				}
				{
					Button cia = new Button("Draw borders");
					controls.add(cia);
					cia.addClickHandler(new ClickHandler(){
						@Override
						public void onClick(ClickEvent event) {
							for (final Polyline poly : CIA.segments(CIA.northamerica)){
								//System.out.println(poly);
								space.space.addOverlay(poly);
							}
							for (final Polyline poly : CIA.segments(CIA.europe)){
								//System.out.println(poly);
								space.space.addOverlay(poly);
							}
						}
					});
				}
				{
					Button family = new Button("Genealogy info");
					controls.add(family);
					family.addClickHandler(new ClickHandler(){
						@Override
						public void onClick(ClickEvent event) {
							Geocoder geo  = new Geocoder();
							for (final Triple<String,Interval,String> indiv : Genea.people()){
									geodecode(geo, indiv, 100);
							}	
						}
					});
				}
				{
					Button addOne = new Button("Add one");
					addOne.addClickHandler(new ClickHandler(){
						@Override
						public void onClick(ClickEvent event) {
							Questionnaire q = new Questionnaire(
									new ContentQuestion("Description?"),
									new WhenQuestion("Quand?"),
									new ContentQuestion("Nom du lieu?"),
									new WhereQuestion("Où ça sur la carte?")
							);
							
							QPanel tested = new QPanel(q);
							//tested.setSize("50%", "50%");
							tested.center();tested.show();
						}});
					controls.add(addOne);
				}
				{
					Button reset = new Button("Reset");
					reset.addClickHandler(new ClickHandler(){
						@Override
						public void onClick(ClickEvent event) {
							pad.reset();
							types.centerEvent.shareEvent(null);
						}});
					controls.add(reset);
				}
				{
					TextBox search = new TextBox();
					search.setTitle("Search");
					controls.add(search);
				}
			}
			panel.addSouth(time,150);
			panel.addWest(controls,150);
			panel.add(space);
			Window.addResizeHandler(this);
		}
		this.initWidget(panel);
		// Deal with new Center TODO should be the time window.
		types.centerEvent.registerHandler(new WHandler<Date>(){
			public void onEvent(WEvent<Date> elt) {
				space.drawSpace();
				drawTime();
			}});
		types.itemSelected.registerHandler(new WHandler<GeoStoryItem>(){
			public void onEvent(WEvent<GeoStoryItem> elt) {
				space.centerOnItem(elt.getElement());
			}});
		types.itemOver.registerHandler(new WHandler<GeoStoryItem>(){
			public void onEvent(WEvent<GeoStoryItem> elt) {
				//TODO
			}});
		// Just a short term timer to check and resize the badly initialized map.
		Timer t = new Timer(){public void run() {
			space.checkResizeAndCenter();
			time.init();
		}};
		t.schedule(1); 
	}

	public void geodecode(final Geocoder geo,final Triple<String,Interval,String> event, final int delay){
		Timer t = new Timer(){
			public void run() {
				if (!event.get1().equals("")){
					//geo.getCache().reset();
					geo.getLatLng(event.get1(), new LatLngCallback(){
						@Override
						public void onFailure() {
							System.out.println("Failed geocoder for   "+"---"+event+" ");
							geodecode(geo,event,delay+1000);
						}
						@Override
						public void onSuccess(LatLng point) {
							GeoStoryItem gsi = new GeoStoryItem(event.get2(),event.get1(),point,event.get3());
							//System.out.println("Item is "+gsi);
							types.itemAdded.shareEvent(gsi);
							types.centerEvent.shareEvent(null);
						}
					});
				}
			}
		};
		t.schedule(delay);
	}
	
	public void drawTime(){
		time.refresh();
	}

	public void onResize(ResizeEvent event) {
		space.setSize("100%", "100%");
		time.setSize("100%", "100%");
		space.checkResizeAndCenter();
	}

	public String getEmail() {
		return this.email.getText();
	}

}
