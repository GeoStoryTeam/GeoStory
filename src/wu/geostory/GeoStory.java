package wu.geostory;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import wu.events.WEvent;
import wu.events.WHandler;
import wu.questions.ContentQuestion;
import wu.questions.QPanel;
import wu.questions.Question;
import wu.questions.Questionnaire;
import wu.questions.WhenQuestion;
import wu.questions.WhereQuestion;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.maps.client.geocode.Geocoder;
import com.google.gwt.maps.client.geocode.LatLngCallback;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.overlay.Polyline;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.TextArea;
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

	final SplitLayoutPanel panel;

	final ListBox list;

	final TimeLine time;

	final TextBox email;

	// Event types
	GeoEventTypes types;

	// The map thanks to Google
	Space space;

	// The model with things related to position and dates
	GeoStoryModel model;

	VerticalPanel controls;

	public GeoStory(){
		panel = new SplitLayoutPanel();
		types = new GeoEventTypes();
		model = new GeoStoryModel(types);
		space = new Space(LatLng.newInstance(45.50,-73.60),4, types, model);
		time  = new TimeLine(model, types);
		list  = new ListBox(true);
		list.setVisibleItemCount(20);
		for (GeoStoryItem i : model.getItems()){
			list.addItem(i.toString());
		}
		{
			controls = new VerticalPanel();
			{
				email = new TextBox();
				email.setText("joris.deguet@gmail.com");
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
				Button dump = new Button("Json dump");
				dump.addClickHandler(new ClickHandler(){
					@Override
					public void onClick(ClickEvent event) {
						StringBuilder sb = new StringBuilder();
						for (GeoStoryItem item : model.getItems()){
							sb.append(item.toJSON()+"\n");
						}
						DialogBox db = new DialogBox(true);
						TextArea ta = new TextArea();
						ta.setEnabled(true);
						ta.setText(sb.toString());
						db.add(ta);
						db.center();db.show();
					}});
				controls.add(dump);
			}
			{
				Button jsonload = new Button("Json load");
				jsonload.addClickHandler(new ClickHandler(){
					@Override
					public void onClick(ClickEvent event) {
						Question<String> q = new ContentQuestion("Entrer la cha”ne JSON");
						final QPanel textContent = new QPanel(new Questionnaire(q));
						textContent.center();textContent.show();
						q.answerChannel().registerHandler(new WHandler<String>(){
							@Override
							public void onEvent(WEvent<String> elt) {
								String[] lines = elt.getElement().split("\n");
								for (String item : lines){
									JSONValue json = JSONParser.parse(item);
									JSONObject object = json.isObject();
									GeoStoryItem gsi = GeoStoryItem.fromJSON(object);
									types.itemAdded.shareEvent(gsi);
									types.centerEvent.shareEvent(null);
								}
							}});
					}
				});
				controls.add(jsonload);
			}
			{
				Button popRandom = new Button("Populate at random");
				controls.add(popRandom);
				popRandom.addClickHandler(new ClickHandler(){

					@Override
					public void onClick(ClickEvent event) {
						model.populateRandomly(10);
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
						Question<String> q = new ContentQuestion("Description des evenements");
						final QPanel textContent = new QPanel(new Questionnaire(q));
						textContent.center();textContent.show();
						q.answerChannel().registerHandler(new WHandler<String>(){
							@Override
							public void onEvent(WEvent<String> elt) {
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
							space.space.addOverlay(poly);
						}
						for (final Polyline poly : CIA.segments(CIA.europe)){
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
						ContentQuestion desc = new ContentQuestion("Description?");
						WhenQuestion    when = new WhenQuestion("Quand?");
						ContentQuestion plac = new ContentQuestion("Nom du lieu?");
						WhereQuestion   wher = new WhereQuestion("Sur la carte?",plac.answerChannel());
						Questionnaire q = new Questionnaire(
								desc,
								when,
								plac,
								wher
						);
						QPanel tested = new QPanel(q);
						//tested.setSize("50%", "50%");
						q.answerChannel().registerHandler(new WHandler<Map<String,String>>(){
							@Override
							public void onEvent(
									WEvent<Map<String, String>> elt) {
								Map<String,String> map = elt.getElement();
								//Window.alert("Questionnaire are  "+ elt.getElement().toString());
								String[] coord = map.get("Sur la carte?").split("\\|");
								//Window.alert("Questionnaire are  "+ elt.getElement().toString()+" "+Arrays.toString(coord));
								LatLng ll = LatLng.newInstance(
										Double.parseDouble(coord[0]), 
										Double.parseDouble(coord[1]));//;
								String[] dates = map.get("Quand?").split("->");
								Interval interval = new Interval(
										WhenQuestion.dtf.parse(dates[0]),
										WhenQuestion.dtf.parse(dates[1]));
								GeoStoryItem geo = new GeoStoryItem(
										interval,
										map.get("Nom du lieu?"),
										ll,
										map.get("Description?"));
								Window.alert("GeoStory "+geo+" "+Arrays.toString(dates)+" "+map.get("Quand?"));
								types.itemAdded.shareEvent(geo);
								types.centerEvent.shareEvent(null);
							}});
						tested.center();tested.show();
					}});
				controls.add(addOne);
			}
			{
				Button reset = new Button("Reset");
				reset.addClickHandler(new ClickHandler(){
					@Override
					public void onClick(ClickEvent event) {
						model.reset();
						types.centerEvent.shareEvent(null);
					}});
				controls.add(reset);
			}
			{
				final TextBox search = new TextBox();
				search.setTitle("Search");
				final Button go = new Button("go");
				go.addClickHandler(new ClickHandler(){
					@Override
					public void onClick(ClickEvent event) {
						Set<GeoStoryItem> itemsSet = model.getItems();
						// filtering
						if (search.getText() != null && !search.getText().isEmpty()) {
							for (GeoStoryItem geoStoryItem : itemsSet) {
								if (geoStoryItem.getDescription().contains(search.getText())) {
									geoStoryItem.setVisible(true);
								} else {
									geoStoryItem.setVisible(false);
								}
							}
						}
						types.centerEvent.shareEvent(null);
					}});
				controls.add(go);
				controls.add(search);
			}
			{
				final Button removeFilter = new Button("remove filter");
				removeFilter.addClickHandler(new ClickHandler(){
					@Override
					public void onClick(ClickEvent event) {
						Set<GeoStoryItem> itemsSet = model.getItems();
						// filtering
						for (GeoStoryItem geoStoryItem : itemsSet) {
							geoStoryItem.setVisible(true);
						}
						types.centerEvent.shareEvent(null);
					}});
				controls.add(removeFilter);
			}
			panel.addSouth(time,150);
			panel.addWest(controls,150);
			panel.add(space);
			Window.addResizeHandler(this);
		}
		this.initWidget(panel);
		// Short term timer to check and resize the badly initialized map.
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
					geo.getLatLng(event.get1(), new LatLngCallback(){
						@Override
						public void onFailure() {
							System.out.println("Failed geocoder for   "+"---"+event+" ");
							geodecode(geo,event,delay+1000);
						}
						@Override
						public void onSuccess(LatLng point) {
							GeoStoryItem gsi = new GeoStoryItem(event.get2(),event.get1(),point,event.get3());
							Window.alert("Item is "+gsi);
							types.itemAdded.shareEvent(gsi);
							types.centerEvent.shareEvent(null);
						}
					});
				}
			}
		};
		t.schedule(delay);
	}

	public void onResize(ResizeEvent event) {
		space.setSize("100%", "100%");
		time.setSize("100%", "100%");
		space.checkResizeAndCenter();
	}

	public GeoEventTypes getTypes() {return types;}
	
	public String getEmail() {return this.email.getText();}

}
