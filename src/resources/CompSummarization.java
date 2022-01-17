package resources;

import java.util.ArrayList;
import java.util.List;

public class CompSummarization implements CellOutput{
	private String material;
	private Float length;
	Integer count;
	
	CompSummarization(String material, float length){
		this.material = material;
		this.length = length;
		this.count = (Integer) 1;
	}
	public float useLength() {
		return this.length * this.count;
	}
	public void increase() {
		count++;
	}
	public String getMaterial() {
		return material;
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
		else if(obj instanceof Pair) {
			Pair<?, ?> pair = (Pair<?, ?>)obj;
			// count different is not the problem, we will increase() it
			return (this.material.equals(pair.getFirst()) &&
					this.length.equals(pair.getSecond()));
		}	
	    return false;
	}
	@Override
	public List <Object> getInfo() {
		List<Object> info = new ArrayList <Object>();
		info.add(material);
		info.add(length);
		info.add(count);
		return info;
	}
}
