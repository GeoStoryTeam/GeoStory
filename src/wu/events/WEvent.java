package wu.events;

import com.google.gwt.event.shared.GwtEvent;

/**
 * This is a wrapper class to deal with events whose type is objectified.
 * These events are not directly instantiated but are created by the event type.
 * @author joris deguet
 *
 * @param <Element> describes the class of the attachment for th
 */
public class WEvent<Element> extends GwtEvent<WHandler<Element>>{

	private Element element;
	
	public Type<WHandler<Element>> typ;
	
	/**
	 * You cannot instantiate events directly, this is done through the WEventType.
	 * @param elt
	 * @param t
	 */
	protected WEvent(Element elt, Type<WHandler<Element>> t){element = elt;this.typ = t;}
	
	/**
	 * Returns the attachment for this event.
	 * @return
	 */
	public Element getElement(){
		return this.element;
	}
	
	@Override
	protected void dispatch(WHandler<Element> handler) {
		handler.onEvent(this);
	}

	@Override
	public Type<WHandler<Element>> getAssociatedType() {
		return typ;
	}
}