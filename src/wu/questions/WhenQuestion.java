package wu.questions;

import java.util.Date;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.user.datepicker.client.DatePicker;

public class WhenQuestion extends Question<String>{

	Date startDate;
	Date endDate;
	public static final DateTimeFormat dtf = DateTimeFormat.getFormat("yyyy-MM-dd");
	
	public WhenQuestion(String in) {
		super(in);
	}

	@Override
	public Widget answerPan() {
		FlexTable panel = new FlexTable();
		startDate = new Date();
		endDate   = new Date();
		
		Label start = new Label("Start");
		panel.setWidget(0, 0, start);
		
		DateBox startbox = new DateBox();
		startbox.addValueChangeHandler(new ValueChangeHandler<Date>(){
			@Override
			public void onValueChange(ValueChangeEvent<Date> event) {
				startDate = event.getValue();
				share();
			}});
		startbox.setFormat(new DateBox.Format(){
			DateTimeFormat dtf = DateTimeFormat.getFormat("yyyy-MM-dd");
			@Override
			public String format(DateBox dateBox, Date date) {
				return dtf.format(date!=null?date:new Date());
			}
			@Override
			public Date parse(DateBox dateBox, String text, boolean reportError) {
				return dtf.parse(text);
			}
			@Override
			public void reset(DateBox dateBox, boolean abandon) {}});
		panel.setWidget(0, 1, startbox);
		
		
		Label end = new Label("End");
		panel.setWidget(1, 0, end);
		
		DateBox endbox = new DateBox();
		endbox.addValueChangeHandler(new ValueChangeHandler<Date>(){
			@Override
			public void onValueChange(ValueChangeEvent<Date> event) {
				endDate = event.getValue();
				share();
			}});
		endbox.setFormat(new DateBox.Format(){
			DateTimeFormat dtf = DateTimeFormat.getFormat("yyyy-MM-dd");
			@Override
			public String format(DateBox dateBox, Date date) {
				return dtf.format(date!=null?date:new Date());
			}
			@Override
			public Date parse(DateBox dateBox, String text, boolean reportError) {
				return dtf.parse(text);
			}
			@Override
			public void reset(DateBox dateBox, boolean abandon) {}});
		panel.setWidget(1, 1, endbox);
		return panel;
	}
	
	private void share(){
		answerChannel().shareEvent(dtf.format(startDate)+"->"+dtf.format(endDate));
	}
	
	
}

