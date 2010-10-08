package wu.questions;

import java.util.Map;

import com.google.gwt.event.shared.HandlerManager;

import wu.events.WEventType;

public class Questionnaire {

	//AbLabDiGraph graph;
	
	Question[] questions;
	
	WEventType<Map<Question,Answer>> answerChannel;
	
	public Questionnaire(Question... qs){
		this.answerChannel = new WEventType<Map<Question,Answer>>();
		this.questions = qs;
	}
}
