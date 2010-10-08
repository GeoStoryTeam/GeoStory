package wu.questions;

import wu.events.WEventType;
import wu.geostory.Interval;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.Widget;

/**
 * How to deal with multiple languages TODO TODO 
 * @author joris
 *
 */
public abstract class Question<Answer> {

	LocalID id;
	
	Interval period; // the period where this question can ba answered.
	
	String intitule;
	
	WEventType<Answer> answerChannel;
	
	public Question(String in){
		id = LocalID.getOne();
		this.intitule = in;
		this.answerChannel = new WEventType<Answer>();
	}
	
	public WEventType<Answer> answerChannel(){
		return this.answerChannel;
	}
	
	/**
	 * Should enable to recover the answer.
	 * @return
	 */
	public abstract Widget answerPan();
	
	@Override
	public String toString(){
		return "\""+intitule+"\" ["+id+"]";
	}
	
}
