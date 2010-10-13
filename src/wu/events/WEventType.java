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
 * TODO 
 * - make an event log that gives the events and their timestamps.
 * - try to know what event triggered another one to build a dependancy graph.
 * - record who registered handlers for what to show whose connected on the event type.
 * 
 * 
 * Describe an example.
 * 
 * @author joris
 *
 * @param <Element>
 */
public class WEventType<Element> {
	
	private final GwtEvent.Type<WHandler<Element>> type;
	
	private HandlerManager bus;
	
	private static HandlerManager defaultManager = new HandlerManager(null);
	
	private WEventType(HandlerManager b){
		type = new GwtEvent.Type<WHandler<Element>>();
		bus = b;
	}
	
	/**
	 * This builds a new Event type that will convey attachment of the given type.
	 */
	public WEventType(){
		this(defaultManager);
	}
	
	private Type<WHandler<Element>> getType(){
		return type;
	}
	
	private WEvent<Element> buildEvent(Element elt){
		return new WEvent<Element>(elt, type);
	}
	
	/**
	 * Use this method to create and share an event.
	 * @param elt provides the attachment for the event.
	 */
	public void shareEvent(Element elt){
		// TODO find a transparent way to record what triggered this event
		// - shareEvent(Element et, WEvent trigger)
		// - does reflexion allows to see the stack (the calling method would be a onEvent from WHandler)
		bus.fireEvent(this.buildEvent(elt));
	}
	
	/**
	 * Register a specific handler that will be triggered when an event is shared for this event type.
	 * @param handler the handler that will be triggered on each event for this event type.
	 */
	public void registerHandler(WHandler<Element> handler){
		bus.addHandler(this.type, handler);
	}
	
	/**
	 * Allows to register one handler for multiple Event types which share the same attachment class.
	 * @param <Element>
	 * @param handler The handler you want to trigger for these event types
	 * @param types The list of types.
	 */
	public static <Element> void multipleRegister(WHandler<Element> handler,WEventType<Element>... types){
		for (WEventType<Element> type : types){
			type.registerHandler(handler);
		}
	}
	
	
}

