package resources;

import java.awt.GraphicsEnvironment;
import java.io.Console;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;

public class Utils {
	// first is component prefix, second is specification
	public static Hashtable<String, String> componentToSpecification(ArrayList<Component> ncl){
		
		Hashtable<String, String> result = new Hashtable<String, String>();
		for(Component item : ncl) {
			String []tokens = item.component.split("M");
			if(!result.containsKey(item.component)) {
				result.put(tokens[0], item.specification);
			}
		}
		return result;
	}
	public static Hashtable<String, Float> componentToLength(ArrayList<Component> ncl){
		
		Hashtable<String, Float> result = new Hashtable<String, Float>();
		for(Component item : ncl) {
			if(!result.containsKey(item.component)) {
				result.put(item.component, item.length);
			}
		}
		return result;
	}
	public static String fmt(float d)
	{
	    if(d == (long) d) {
	        return String.format("%d",(long)d);
	    }
	    else {
	    	String str_tmp = String.format("%s",(int)((100*d+5))/10);
	    	String str = str_tmp.substring(0, str_tmp.length()-1) + "." + str_tmp.substring(str_tmp.length()-1);
		    if(str.charAt(str.lastIndexOf(".")+1) == '0') {
		    	return str.substring(0, str.lastIndexOf("."));
		    }
	    	return str;
	    }
	}
	public static void terminal() {
		// create console for user
    	Console console = System.console();
        if(console == null && !GraphicsEnvironment.isHeadless()){
            String filename = Utils.class.getProtectionDomain().getCodeSource().getLocation().toString().substring(6);
            try {
				Runtime.getRuntime().exec(new String[]{"cmd","/c","start","cmd","/k","java -jar \"" + filename + "\"", "UTF-8"});
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
	}
}
