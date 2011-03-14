package wu.uitree;

import java.io.Serializable;

import u.tree.ImmuTree;

import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class TreeUI<S extends Serializable> extends Composite{

	AbsolutePanel container;

	NodeToWidget converter;
	
	ImmuTree<S> tree;

	int[] xcoord;

	int[] ycoord;

	public TreeUI(){
		new TreeUI(new DefaultConverter());
	}

	public TreeUI(NodeToWidget converter){
		this.converter = converter;
		this.initWidget(container);
	}

	public void layout(){
		// change all positions
	}

	private void setPosition(S node, int i, int j){
		// first find the node's index
		// change coordinates
	}

	private class DefaultConverter<S> implements NodeToWidget{
		public Widget convert(Serializable node) {
			return new Label(node.toString());
		}
	}
	
}
