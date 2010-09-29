package wu.geostory;

import java.util.Date;

import com.google.gwt.i18n.client.DateTimeFormat;

/**
 * used to deal with the absence of DateFormat and Calendar in GWT
 * TODO work on that if we go beyond prototype
 * @author joris
 *
 */
public class DateUtils {

	public static Date roundToPrecedingHour(Date d){
		Date res = (Date) d.clone();
		res.setMinutes(0);
		res.setSeconds(0);
		return res;
	}
	
	public static Date addHour(int n, Date d){
		Date res = (Date) d.clone();
		res.setHours(res.getHours()+n);
		return res;
	}
	
	
	public static Date roundToPrecedingDay(Date d, int modulo){
		Date res = (Date) d.clone();
		res.setHours(0);
		res.setMinutes(0);
		res.setSeconds(0);
		res.setDate((res.getDate()/modulo)*modulo); //TODO
		return res;
	}
	
	public static Date addDay(int n, Date d){
		Date res = (Date) d.clone();
		res.setHours(res.getHours()+n*24);
		return res;
	}

	public static Date roundToPrecedingYear(Date d, int modulo) {
		Date res = (Date) d.clone();
		res.setYear( (res.getYear()/modulo) * modulo);
		res.setDate(1);
		res.setMonth(0);
		res.setHours(0);
		res.setMinutes(0);
		res.setSeconds(0);
		return res;
	}

	public static Date addYear(int i, Date d) {
		Date res = (Date) d.clone();
		res.setYear(d.getYear()+i);
		return res;
	}

	public static Date roundToPrecedingMonth(Date d, int modulo) {
		Date res = (Date) d.clone();
		res.setDate(1);
		res.setHours(0);
		res.setMinutes(0);
		res.setSeconds(0);
		res.setMonth((res.getMonth()/modulo) *modulo);
		return res;
	}

	public static Date addMonth(int i, Date d) {
		Date res = (Date) d.clone();
		res.setMonth(d.getMonth()+i);
		return res;
	}

	public static String monthFor(int i){//TODO remove as it does not takes locale into account and is not translated
		if (i==0) return "Jan";
		if (i==1) return "Fev";
		if (i==2) return "Mar";
		if (i==3) return "Avr";
		if (i==4) return "Mai";
		if (i==5) return "Juin";
		if (i==6) return "Juil";
		if (i==7) return "Aout";
		if (i==8) return "Sep";
		if (i==9) return "Oct";
		if (i==10) return "Nov";
		if (i==11) return "Dec";
		return "Prout";
	}

	public static Date roundToPrecedingMinute(Date d){
		Date res = (Date) d.clone();
		res.setSeconds(0);
		return res;
	}
	
	public static Date addMinute(int n, Date d){
		Date res = (Date) d.clone();
		res.setMinutes(res.getMinutes()+n);
		return res;
	}
	
	
	public static Date fromYear(String year){
		DateTimeFormat dtf = DateTimeFormat.getFormat("yyyy");
		return dtf.parse(year);
	}
	
	public static Interval parse(String s){
		DateTimeFormat dtf = DateTimeFormat.getFormat("yyyy-MM-dd");
		
		if (s.contains("->")){
			String[] bounds = s.split("->");
			Date a = dtf.parse(bounds[0].trim());
			Date b = dtf.parse(bounds[1].trim());
			return new Interval(a,b);
		}
		else{
			Date d = dtf.parse(s);
			return new Interval(d,addDay(1,d));
		}
	}
}
