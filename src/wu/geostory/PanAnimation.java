package wu.geostory;

import java.util.Date;

import com.google.gwt.animation.client.Animation;

public class PanAnimation extends Animation{
	
	Integer end;
	
	Date endDate;
	
	Band band;
	
	public PanAnimation(Date start, Date end, Band b, Res res, int width, int panelOffset){
		super();
		this.end = res.dateToPixel(end, width, start) - res.dateToPixel(start, width, start);
		this.endDate = end;
		this.band = b;
	}
	
	@Override
	protected void onUpdate(double progress) {
		int offset = (int) (end*progress);
		band.shiftBy(-offset);
	}

	@Override
	protected void onComplete() {
		band.refresh(endDate);
	}
	
}
