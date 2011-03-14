package wu.uitree;

import java.io.Serializable;

import com.google.gwt.user.client.ui.Widget;

public interface NodeToWidget<S extends Serializable> {

	public Widget convert(S node);
	
}
