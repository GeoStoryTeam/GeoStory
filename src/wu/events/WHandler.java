package wu.events;

import com.google.gwt.event.shared.EventHandler;

public interface WHandler<Element> extends EventHandler{

	void onEvent(WEvent<Element> elt);

}

