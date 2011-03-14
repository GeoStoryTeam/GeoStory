package wu.geostory;

import java.io.Serializable;
import java.util.Date;

public class Interval implements Comparable<Interval>, Serializable{

	private Date a,b;
	
	public Date getA() {return a;}

	public Date getB() {return b;}

	public Interval(){}
	
	public Interval(Date one, Date two){
		if (one == null || two == null || two.before(one) ) throw  new IllegalArgumentException();
		/*
		 * Strange bug in the following conditions:
		 * - a cast error occur saying JulianCalendar cannot be cast into GregorianCalendat
		 * - using GWT local development plugin (deployed application actually works)
		 * - an interval is built with date before Jesus' birth BC with negative date
		 * TODO  Make a report to GWT as it is a GWT bug
		 */
		this.a = one;
		this.b = two;
	}
	
	public boolean contains(Date d){return d.after(a) && d.before(b);}
	
	public boolean contains(Interval i){return i.a.after(a) && i.b.before(b);}
	
	public boolean overlaps(Interval i){return !(this.a.after(i.b) || this.b.before(i.a));}	
	
	public String toString(){return "["+a+" -> "+b+"]";}

	@Override
	public int hashCode(){return this.a.hashCode()*this.b.hashCode();}
	
	public boolean equals(Object o){
		Interval i = (Interval) o;
		return this.a.equals(i.a) && this.b.equals(i.b);
	}
	
	@Override
	public int compareTo(Interval o) {
		Interval i = (Interval) o;
		int startComp = this.a.compareTo(i.a);
		if (startComp != 0 ){
			return startComp;
		}
		else{return this.b.compareTo(i.b);}
	}
}
