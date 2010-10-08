package wu.questions;

import java.util.HashMap;
import java.util.Map;

import wu.events.WEvent;
import wu.events.WHandler;

import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.StackLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
/**
 * TODO:
 * - Add an interface for a converter that builds an Object based on the answerss
 * - should hold a GRAPH OF QUESTIONS plus some dynamic ones
 * - should remember the language, the scope etc. as SCOPE (filters on other questions)
 * - should give a duration, a deadline.
 * - should allow to link identical questions in different languages.
 * 
 * @author joris
 *
 */
public class QPanel extends DialogBox{

	StackLayoutPanel pan;

	Questionnaire questionnaire;

	Map<Question,String> answers;

	Button validate;
	
	public QPanel(Questionnaire q){
		super(true);
		answers = new HashMap<Question,String>();
		this.setText("Enter a new Event");
		this.questionnaire = q;
		DockLayoutPanel wrapper = new DockLayoutPanel(Style.Unit.PCT);
		wrapper.setSize("600px", "500px");
		{
			HorizontalPanel buttons = new HorizontalPanel();
			wrapper.addSouth(buttons, 10);
			{
				validate = new Button("Validate");
				validate.addClickHandler(new ClickHandler(){
					@Override
					public void onClick(ClickEvent event) {
						Window.alert("Answers are  "+ answers.toString());
					}});
				buttons.add(validate);
				validate.setEnabled(false);
				validate.getElement().getStyle().setBackgroundColor("red");
			}
			{
				Button reset = new Button("Cancel");
				reset.addClickHandler(new ClickHandler(){
					@Override
					public void onClick(ClickEvent event) {
						hide();
					}});
				buttons.add(reset);
				reset.getElement().getStyle().setBackgroundColor("green");
			}
		}
		pan = new StackLayoutPanel(Unit.EM);
		wrapper.add(pan);
		for (final Question<String> question : q.questions){
			Widget typeDependant = question.answerPan();
			final HTML header = new HTML(question.intitule);
			pan.add(typeDependant, header, 3);
			question.answerChannel().registerHandler(new WHandler<String>(){
				@Override
				public void onEvent(WEvent<String> elt) {
					if (elt.getElement() != null) {
						answers.put(question, elt.getElement());
						header.getElement().getStyle().setColor("green");
						validate();
					}
				}
			});
		}
		this.add(wrapper);
	}

	private void validate(){
		boolean finished = answers.size() == this.questionnaire.questions.length;
		validate.setEnabled(finished);
	}
	
	public QPanel(){
		this(buildTestQuestionnaire());
	}

	static Questionnaire buildTestQuestionnaire(){
		Questionnaire q = new Questionnaire(
				new WhereQuestion("Where this event happens?"),
				new WhenQuestion("When this events happens?"),
				//new WhichQuestion<String>("What is this event's category?","home","family","hobbies","work"),
				new OrderQuestion<String>("order","a","b","c","d","e"),
				new ContentQuestion("Describe this event?")
		);
		return q;
	}
}
