package wu.geostory;

import java.util.Date;

public class ResYear extends Resolution{

	int numberOfYears;
	
	public ResYear(int n) {
		super(n*24*36000*365);
		this.numberOfYears = n;
	}

	@Override
	public Date add(int i, Date d) {
		return DateUtils.addYear(numberOfYears*i, d);
	}

	@Override
	public Date round(Date d) {
		return DateUtils.roundToPrecedingYear(d, numberOfYears);
	}

	@Override
	public String labelForDate(Date d) {return d.getYear()+1900+"";}
	
}
