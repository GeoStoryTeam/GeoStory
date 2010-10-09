package wu.questions;

import java.util.Date;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DatePicker;

public class WhenQuestion extends Question<String>{

	public WhenQuestion(String in) {
		super(in);
	}

	@Override
	public Widget answerPan() {
		HorizontalPanel panel = new HorizontalPanel();
		DatePicker a = new DatePicker();
		a.addValueChangeHandler(new ValueChangeHandler<Date>(){
			@Override
			public void onValueChange(ValueChangeEvent<Date> event) {
				answerChannel().shareEvent(event.getValue().toGMTString());
			}});
		panel.add(a);
		return panel;
	}
}

