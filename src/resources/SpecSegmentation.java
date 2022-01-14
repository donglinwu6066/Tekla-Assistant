package resources;

import java.util.ArrayList;
import java.util.List;

public class SpecSegmentation {
	String specification;
	Float length;
	ArrayList<Pair<String, Float>> componentList;
	Integer count;
	Float remaining;
	SpecSegmentation(){
		this.specification = "null";
		this.componentList = new ArrayList<Pair<String, Float>>();
		this.length = (float) 0;
		this.count = 0;
		this.remaining = (float) 0;
	}
	
	public void addComponent(Pair<String, Float> component) {
		componentList.add(component);
		this.remaining -= component.getSecond();
	}
	public void setSpecification(String specification) {
		this.specification = specification;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public void setLength(float length) {
		this.length = length;
		this.remaining = length;
	}
	// print
	public String toString() {
		return "Specification: " + specification + 
				", length: " + length +
				", componentList: " + componentList  +
				", count: " + count;
	}
	public List <Object> getSegInfo() {
		List<Object> info = new ArrayList <Object>();
		info.add(specification);
		info.add(length);
		for(Pair<String, Float> item : componentList) {
			info.add(item.getFirst());
			info.add(item.getSecond());
		}
		return info;
	}
	public int getComponentLength() {
		return componentList.size();
	}
}
