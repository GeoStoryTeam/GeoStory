package wu.questions;

import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * To implement the multiple selection, we could use a BitLine with size equal to number of choice for response.
 * Convert it into an integer if less that 32??? choices.
 * @author joris
 *
 * @param <S>
 */
public class WhichQuestion<S> extends Question{

	S[] options;

	public WhichQuestion(String intitule, S...  opt){
		super(intitule);
		options = opt;
	}

	@Override
	public Widget answerPan() {
		ListBox list = new ListBox();
		for (S option : options){
			list.addItem(option.toString());
		}
		return list;
	}
	
}
