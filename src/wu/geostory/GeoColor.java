package wu.geostory;

import java.util.Map;

public class GeoColor {

	private final int red;

	private final int green;

	private final int blue;

	public GeoColor(int r, int g, int b){
		this.red = (r+255)%255;
		this.green = (g+255)%255;
		this.blue = (b+255)%255;
	}

	public String hexString(){
		return "#"+ compo(red)+compo(green)+compo(blue);
	}

	public String cssString(){
		return "rgb("+red+","+green+","+blue+")";
	}

	private String compo(int i){
		String res = Integer.toHexString(i);
		if (res.length() == 1) res = "0"+res;
		if (res.length() == 0) res = "00";
		return res;
	}

	public String toString(){
		return this.hexString()+" " +this.cssString()+"  "+hue();
	}

	public double hue(){
		int max = Math.max(Math.max(red,green), blue);
		int min = Math.min(Math.min(red,green), blue);
		int mm = max-min;
		// Results
		double h = 0.0;
		if (min == max){
			return h;
		}
		else if (max == red){
			if (green >= blue){
				h = 60.0*(green-blue)/mm+0;
			}
			else{
				h = 60.0*(green-blue)/mm+360;
			}
		}
		else if (max == green){
			h = 60.0 * (blue-red)/mm+120;
		}
		else{
			h = 60.0 * (red-green)/mm + 240;
		}
		return h;
	}

	/*public HSV(int red, int green, int blue){
	int red   = (rgb >> 16) & 0xff;  // all values are atually byte values
	int green = (rgb >>  8) & 0xff;
	int blue  = (rgb      ) & 0xff;
	int max = Math.max(Math.max(red,green), blue);
	int min = Math.min(Math.min(red,green), blue);
	int mm = max-min;
	// Results
	double h = 0.0;
	double s = max==0?0.0:1-1.0*min/max;
	double v = max*1.0/255;

	if (min == max){
		// this is gray
	}
	else if (max == red){
		if (green >= blue){
			h = 60.0*(green-blue)/mm+0;
		}
		else{
			h = 60.0*(green-blue)/mm+360;
		}
	}
	else if (max == green){
		h = 60.0 * (blue-red)/mm+120;
	}
	else{
		h = 60.0 * (red-green)/mm + 240;
	}
	this.hue = h;
	this.sat = s;
	this.val = v;
}*/

	public static GeoColor fromHSV(double hue, double sat, double val ){
		// Recover RGB components
		int H = ((int)Math.floor(hue/60)) % 6;
		double f = hue/60 - H;
		double p = val*(1-sat);
		double q = val*(1-f*sat);
		double t = val*(1-(1-f)*sat);

		double r = 0.0;
		double g = 0.0;
		double b = 0.0;
		switch (H){
		case 0:
			r = val;
			g = t;
			b = p;
			break;
		case 1:
			r = q;
			g = val;
			b = p;
			break;
		case 2:
			r = p;
			g = val;
			b = t;
			break;
		case 3:
			r = p;
			g = q;
			b = val;
			break;
		case 4:
			r = t;
			g = p;
			b = val;
			break;
		case 5:
			r = val;
			g = p;
			b = q;
			break;
		}

		// Make it a RGB
		int red = (int)(r*255);
		int green = (int)(g*255);
		int blue = (int)(b*255);
		//System.out.println(" R"+red+" G"+green+" B"+blue);
		GeoColor rgb = new GeoColor(red,green,blue);
		return rgb;
	}

	public static double distWithColor(Map<GeoStoryItem,GeoColor> neighbours, GeoColor candidateColor){
		double infbound = Double.POSITIVE_INFINITY;
		for (GeoStoryItem neighbour : neighbours.keySet()){
			double dist = GeoColor.dist(candidateColor, neighbours.get(neighbour));
			infbound = Math.min(infbound, dist);
		}
		return infbound;
	}
	
	public static double dist(GeoColor a, GeoColor b){
		return Math.min(
				Math.abs(a.hue()-b.hue()), 
				Math.min(Math.abs(a.hue()+360-b.hue()), Math.abs(+360+b.hue()-a.hue()))
				);//Math.abs(a.red-b.red)+Math.abs(a.green-b.green)+Math.abs(a.blue-b.blue);
	}

}
