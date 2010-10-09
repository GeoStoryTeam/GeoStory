package wu.questions;

import java.util.Map;

import com.google.gwt.event.shared.HandlerManager;

import wu.events.WEventType;

public class Questionnaire {

	//AbLabDiGraph graph;
	
	Question[] questions;
	
	WEventType<Map<String,String>> answerChannel;
	
	public Questionnaire(Question... qs){
		this.answerChannel = new WEventType<Map<String,String>>();
		this.questions = qs;
	}

	public WEventType<Map<String,String>> answerChannel() {
		return answerChannel;
	}
}
