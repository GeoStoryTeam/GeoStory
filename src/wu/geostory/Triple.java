package wu.geostory;

import java.io.Serializable;

public final class Triple<A,B,C> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1230659617097735017L;
	private final A one;
	private final B two;
	private final C three;
	
	private Integer hash;
	
	public Triple(
			final A o,
			final B t,
			final C th){
		this.one = o;
		this.two = t;
		this.three = th;
	}
	
	public A get1() {
		return one;
	}

	public B get2() {
		return two;
	}
	
	public C get3() {
		return three;
	}
	
	@Override
	public int hashCode(){
		if (hash == null) this.hash = this.one.hashCode()*this.two.hashCode()+this.three.hashCode();
		return hash;
	}
	
	@Override
	public boolean equals(Object o){
		if (o instanceof Triple){
			Triple bis = (Triple) o;
			return 
				bis.get1().equals(this.get1()) && 
				bis.get2().equals(this.get2()) &&
				bis.get3().equals(this.get3());
		}
		else{
			return false;
		}
	}
	
	@Override
	public String toString(){
		return "{" + this.one + "|" + this.two +"|" + this.three +"}";
	}
	
}
