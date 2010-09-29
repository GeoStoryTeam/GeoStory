package wu.events;

import com.google.gwt.event.shared.GwtEvent;

/**
 * This is a wrapper class to deal with events whose type is objectified.
 * @author joris
 *
 * @param <Element>
 */
public class WEvent<Element> extends GwtEvent<WHandler<Element>>{

	private Element element;
	
	public Type<WHandler<Element>> typ;
	
	public WEvent(Element elt, Type<WHandler<Element>> t){element = elt;this.typ = t;}
	
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