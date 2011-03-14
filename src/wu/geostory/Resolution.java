package wu.geostory;

import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

/**
 * This abstract class is the basis to compute all things relative to the resolution of a band.
 * How to go from pixel positions (x coord on screen) to dates (in time)
 * What markers should be displayed on the timeline as delimitation.
 * @author joris
 *
 */
public abstract class Resolution {

	/**
	 * The number of second per pixel
	 * Used to compute pixel to Date conversion.
	 */
	long minutesPerPixel;

	private long pixMaxStep;

	private static int largerJump = 10000;

	public Resolution(long minutes){
		this.minutesPerPixel = minutes;
		long temp = largerJump / this.minutesPerPixel;
		this.pixMaxStep = temp>0?temp:1L;
		//System.out.println("step "+pixMaxStep+" seconds "+this.minutesPerPixel+" larger "+ largerJump);
	}

	/**
	 * Convert a Date to a pixel position given the width of the band and the center date
	 */
	public int dateToPixel(Date date, int width, Date center){
		double diff = (date.getTime() - center.getTime())/60;
		int result = (int) (width/2 + (1.0*diff/(minutesPerPixel)));
		return result;
	}

	public Date pixelToDate(int pixel, int width, Date center){
		return offSetByNPix(center,pixel-width/2);
	}

	public Date offSetByNPix(Date center, int offset) {
		Date d = new Date(center.getTime());
		int sign = offset>0?+1:-1;
		int abso = offset>0?offset:-offset;
		long completeSteps = abso / pixMaxStep;
		for (int i = 0 ; i < completeSteps ; i++){
			d.setTime(d.getTime()+sign*pixMaxStep*this.minutesPerPixel*60);
		}
		d.setTime(d.getTime()+sign*(abso%pixMaxStep)*this.minutesPerPixel*60);
		return d;
	}

	/**
	 * Compute the markers given the abstract functions.
	 */
	public Map<Integer, Date> markers(int width, Date center, int factor){
		Interval interval = new Interval(
				pixelToDate(-factor*width, width, center),
				pixelToDate(width+factor*width,width,center));
		Map<Integer, Date> res = new TreeMap<Integer,Date>();
		Date rounded = round(center);
		res.put(dateToPixel(rounded,width,center),rounded);
		int offset = 1;
		Date ante,post;
		do {
			ante = add(-offset,rounded);
			int left = dateToPixel(ante,width,center);
			res.put(left,ante);
			post = add(offset,rounded);
			int right = dateToPixel(post,width,center);
			res.put(right,post);
			offset++;
		} while(interval.contains(ante)); // this is to have marker on the left and the right in case of sliding
		return res;
	}

	/**
	 * Round the date to the preceding marker.
	 * @param d
	 * @return
	 */
	public abstract Date round(Date d);

	/**
	 * Goes from one marker to the other one. parameter can be negative.
	 * @param i
	 * @param d
	 * @return
	 */
	public abstract Date add(int i, Date d);

	/**
	 * Return the String value to attach to the markers.
	 * @param d
	 * @return
	 */
	public abstract String labelForDate(Date d);
}
