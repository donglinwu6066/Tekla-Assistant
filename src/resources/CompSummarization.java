package resources;

import java.util.ArrayList;
import java.util.List;

public class CompSummarization {
	String material;
	Float length;
	Integer count;
	CompSummarization(String material, float length){
		this.material = material;
		this.length = length;
		this.count = (Integer) 1;
	}
	
	public void increase() {
		count += 1;
	}
	@Override
	public String toString() {
		return "material = " + material + ", length = " + 
				length.toString() + ", count = " + count.toString();
	}
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof CompSummarization) {
			CompSummarization comSum = (CompSummarization)obj;
			// count different is not the problem, we will increase() it
			return (this.material.equals(comSum.material) &&
					this.length.equals(comSum.length));
		}	
	    return false;
	}
	public List <Object> getInfo() {
		List<Object> info = new ArrayList <Object>();
		info.add(material);
		info.add(length);
		info.add(count);
		return info;
	}
}
