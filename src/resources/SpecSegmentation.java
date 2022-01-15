package resources;

import java.util.ArrayList;
import java.util.Comparator;
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
	@Override
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
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof SpecSegmentation) {
			SpecSegmentation specSeg = (SpecSegmentation)obj;
			return (this.specification.equals(specSeg.specification) &&
					this.length.equals(specSeg.length) &&
					this.count.equals(specSeg.count) &&
					this.componentList.equals(specSeg.componentList));
		}	
	    return false;
	}
    @Override
    public int hashCode() {
           final int prime = 31;
           int result = 1;
           result = prime * result
                        + ((specification == null) ? 0 : specification.hashCode());
           result = prime * result
                   + ((length == null) ? 0 : length.hashCode());
           result = prime * result
                   + ((componentList == null) ? 0 : componentList.hashCode());
           result = prime * result
                   + ((count == null) ? 0 : count.hashCode());
           result = prime * result
                   + ((remaining == null) ? 0 : remaining.hashCode());
           return result;
    }

}


