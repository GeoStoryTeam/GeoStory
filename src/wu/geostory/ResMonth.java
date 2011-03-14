package wu.geostory;

import java.util.Date;

public class ResMonth extends Resolution{

	int numberOfMonths;
	
	public ResMonth(int n) {
		super(n*1000*24*30);
		this.numberOfMonths = n;
	}

	@Override
	public Date add(int i, Date d) {
		return DateUtils.addMonth(numberOfMonths*i, d);
	}

	@Override
	public String labelForDate(Date d) {
		return DateUtils.monthFor(d.getMonth())+" "+(d.getYear()+1900);
	}

	@Override
	public Date round(Date d) {
		return DateUtils.roundToPrecedingMonth(d, numberOfMonths);
	}

}
