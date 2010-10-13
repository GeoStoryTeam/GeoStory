package wu.events;

import com.google.gwt.event.shared.EventHandler;

/**
 * The interface to implement to handle a specific WEvent type.
 * @author joris
 *
 * @param <Element>
 */
public interface WHandler<Element> extends EventHandler{

	void onEvent(WEvent<Element> elt);

}

