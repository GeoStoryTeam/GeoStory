package wu.geostory;

import com.google.gwt.animation.client.Animation;
import com.google.gwt.user.client.ui.Button;

public class HighLightAnimation extends Animation {

	Button b;

	String color;

	public HighLightAnimation(Button widg) {
		this.b = widg;
		color = b.getElement().getStyle().getBackgroundColor();
	}

	@Override
	protected void onUpdate(double progress) {
		int onOff = (int) (progress*20);
		int level = (int) (progress*254);
		b.getElement().getStyle().setBackgroundColor(onOff%2==0?color:"rgb("+level+","+level+","+level+")");
	}

	@Override
	protected void onComplete() {
		b.getElement().getStyle().setBackgroundColor(color);
	}

}
