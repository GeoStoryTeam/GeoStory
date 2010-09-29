package wu.events;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.GwtEvent.Type;


/**
 * Just a wrapper to get different type id for different wrappers around the same generic parameter.
 * 
 * Propagates the type id to the Type and to the different Events.
 * 
 * The philosophy behind this approach is to objectify event type and not to create a new class each time.
 * 
 * Then the type acts as a factory to build events or as a type holder for handler registration.
 * 
 * @author joris
 *
 * @param <Element>
 */
public class WEventType<Element> {
	
	private final GwtEvent.Type<WHandler<Element>> type;
	
	private HandlerManager bus;
	
	public WEventType(HandlerManager b){
		type = new GwtEvent.Type<WHandler<Element>>();
		bus = b;
	}
	
	private Type<WHandler<Element>> getType(){
		return type;
	}
	
	private WEvent<Element> buildEvent(Element elt){
		return new WEvent<Element>(elt, type);
	}
	
	public void shareEvent(Element elt){
		bus.fireEvent(this.buildEvent(elt));
	}
	
	// could be useful to track the Handler here
	public void registerHandler(WHandler<Element> handler){
		bus.addHandler(this.type, handler);
	}
	
	public static <Element> void multipleRegister(WHandler<Element> handler,WEventType<Element>... types){
		for (WEventType<Element> type : types){
			type.registerHandler(handler);
		}
	}
	
	
}

