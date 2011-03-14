package wu.questions;

import wu.events.WEventType;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.StackLayoutPanel;
import com.google.gwt.user.client.ui.Widget;


/**
 * Simple extension to intercept the stack change event and forward it to the map widgets for google map initialisation.
 * @author joris
 *
 */
public class MyStackLayoutPanel extends StackLayoutPanel{

	WEventType<Integer> change;
	
	public MyStackLayoutPanel(Unit unit/*, WEventType<Integer> change*/) {
		super(unit);
		this.change = change;
	}

	@Override
	public void showWidget(Widget widget) {
	    super.showWidget(widget);
	    //change.shareEvent(this.getWidgetIndex(widget));
	}
}
