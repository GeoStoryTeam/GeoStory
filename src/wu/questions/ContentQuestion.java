package wu.questions;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class ContentQuestion extends Question<String>{

	public ContentQuestion(String in) {
		super(in);
	}

	@Override
	public Widget answerPan() {
		final TextArea res = new TextArea();
		res.addChangeHandler(new ChangeHandler(){

			@Override
			public void onChange(ChangeEvent event) {
				// TODO Auto-generated method stub
				answerChannel().shareEvent(res.getText());
			}
		});
		return res;
	}

}
