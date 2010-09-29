package wu.client;

import wu.datastore.RemoteDatastore;
import wu.datastore.RemoteDatastoreAsync;
import wu.datastore.ui.DataWidget;
import wu.geostory.GeoStory;
import wu.questions.QPanel;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class WebUtils implements EntryPoint {
	
	public final RemoteDatastoreAsync datastore = GWT
	.create(RemoteDatastore.class);
	
	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		RootPanel root = RootPanel.get();
		root.setSize("100%", "100%");
		{
			VerticalPanel tabs = new VerticalPanel();
			tabs.setSize("100%", "100%");
			root.add(tabs);
			{
				GeoStory tested = new GeoStory();
				tested.setSize("100%", "100%");
				tabs.add(tested);	
			}
			{
				QPanel tested = new QPanel();
				tested.setSize("100%", "100%");
				//tested.center();tested.show();
				//tabs.add(tested);	
			}
			{
				
				DataWidget tested = new DataWidget(datastore);
				//tested.setSize("100%", "100%");
				//tested.center();
				//tested.show();
				//tabs.add(tested);	
			}
		}
		
	}
}
