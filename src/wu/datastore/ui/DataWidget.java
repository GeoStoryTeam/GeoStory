package wu.datastore.ui;

import wu.datastore.Entity;
import wu.datastore.Key;
import wu.datastore.PreparedQuery;
import wu.datastore.Query;
import wu.datastore.RemoteDatastoreAsync;


import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class DataWidget extends Composite{

	FlexTable table;

	Panel panel;

	RemoteDatastoreAsync datastore;

	public DataWidget(RemoteDatastoreAsync d){
		this.datastore = d;
		panel = new VerticalPanel();
		{
			Button refresh = new Button("Refresh");
			panel.add(refresh);
		}
		{
			Button getChildren = new Button("Get Children");
			panel.add(getChildren);
		}
		{
			table = new FlexTable();
			//entriesToTable();
			panel.add(table);
		}
		this.initWidget(panel);
	}

	private void entriesToTable(){
		datastore.prepare(new Query(), new AsyncCallback<PreparedQuery>(){

			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}

			public void onSuccess(PreparedQuery result) {
				System.out.println("Query result is "+result.asIterable());
				
			}});
	}
	
	private void showEntityInPopup(Key key){
		datastore.get(key, new AsyncCallback<Entity>(){

			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}

			public void onSuccess(Entity result) {
				// TODO Auto-generated method stub
				
			}});
	}
}
