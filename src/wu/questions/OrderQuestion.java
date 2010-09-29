package wu.questions;

import java.util.List;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * puts some choices in order of preferences.
 * @author joris
 *
 */
public class OrderQuestion<E> extends Question<List<E>>{

	E[] order;
	
	public OrderQuestion(String in, E... options) {
		super(in);
		//should use permutation to represent it.
		order = options;
		// TODO Auto-generated constructor stub
	}

	@Override
	public Widget answerPan() {
		VerticalPanel vert = new VerticalPanel();
		populate(vert);
		return vert;
	}

	private void populate(final Panel vert){
		vert.clear();
		vert.setWidth("100%");
		for (int i = 0; i < order.length ; i++){
			final int j = i;
			DockLayoutPanel pan = new DockLayoutPanel(Unit.PT);
			pan.setWidth("100%");
			pan.setHeight("2em");
			Button up = new Button("^");
			up.addClickHandler(new ClickHandler(){
				public void onClick(ClickEvent event) {
					switchOptions(j,j-1);
					populate(vert);
				}});
			Button down = new Button("v");
			down.addClickHandler(new ClickHandler(){
				public void onClick(ClickEvent event) {
					switchOptions(j,j+1);
					populate(vert);
				}});
			pan.addEast(up,20);
			pan.addEast(down,20);
			pan.add(new Label(order[i].toString()));
			vert.add(pan);
		}
	}
	
	private void switchOptions(int i, int j){
		E temp = order[i];
		order[i] = order[j];
		order[j] = temp;
	}
}
