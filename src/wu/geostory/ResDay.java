package wu.geostory;

import java.util.Date;

public class ResDay extends Resolution{

	final int numberOfDays;
	
	public ResDay(int n) {
		super(n*24*36000);
		this.numberOfDays = n;
	}

	@Override
	public Date add(int i, Date d) {
		return DateUtils.addDay(numberOfDays*i, d);
	}

	@Override
	public Date round(Date d) {
		return DateUtils.roundToPrecedingDay(d,numberOfDays);
	}

	@Override
	public String labelForDate(Date d) {
		return d.getDate()+" "+DateUtils.monthFor(d.getMonth());
	}

}
