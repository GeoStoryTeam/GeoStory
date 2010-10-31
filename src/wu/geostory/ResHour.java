package wu.geostory;

import java.util.Date;

public class ResHour extends Resolution{

	int numberOfHours;
	
	public ResHour(int number) {
		super(number*36000);
		this.numberOfHours = number;
	}

	@Override
	public Date add(int i, Date d) {
		return DateUtils.addHour(numberOfHours * i, d);
	}

	@Override
	public Date round(Date d) {
		return DateUtils.roundToPrecedingHour(d);
	}

	@Override
	public String labelForDate(Date d) {
		return d.getHours()+"h";
	}

}
