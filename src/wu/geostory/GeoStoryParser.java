package wu.geostory;

/**
 * ISO 8601
 */
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.user.client.Window;


/**
 * Implements the GeoStory parser for the first format.
 * @author joris
 *
 */
public class GeoStoryParser {

	static String grammar = "file=entry,{'----',entry};\n" +
			"entry=place,time,description;\n" +
			"place=<character>;\n" +
			"time=date|date,'->',date;\n" +
			"description=<character>;\n" +
			"character=\"'\",' '...'Ÿ',\"'\";\n" +
			"date=[-],year,[month,[day]]\n" +
			"year=;\n" +
			"month='1'|'2'|'3'|'4'|'5'|'6'|'7'|'8'|'9'|'10'|'11'|'12';\n" +
			"day=['1'...'3'],['0'...'9'];\n";
	
	public static List<Triple<String,Interval,String>> parse(String s){
		List<Triple<String,Interval,String>> res = new ArrayList<Triple<String,Interval,String>>();
		String[] lines = s.split("\n");
		System.out.println(lines.length);
		
		String place = null;
		String date = null;
		String description = null;
		
		for(int i = 0 ; i < lines.length; i++){
			//System.out.println(">>>   "+lines[i]);
			if (lines[i].contains("----") && i+3 < lines.length){
				place = lines[i+1];
				date = lines[i+2];
				System.out.println(date+">>");
				try{
				System.out.println(DateUtils.parse(date.trim()));
				}
				catch(Throwable t){Window.alert(t.getLocalizedMessage());}
				description =  lines [i+3];
				res.add(new Triple(place,DateUtils.parse(date),description));
				i = i+3;
			}
			else{
				i++;
			}
		}
		//System.out.println(res);
		return res;
	}
	
}
