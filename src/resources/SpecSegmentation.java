package resources;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;



public class SpecSegmentation implements CellOutput{
	static Integer MAX_A4_COLUMN = 30;
	static enum listFormat {
	    COMPACTED, GENERAL
	}
	private String specification;
	private Float length;
	private ArrayList<Pair<String, Float>> componentList;
	private Float remaining;
	
	public Integer count;
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
	public String getSpec() {
		return specification;
	};
	public Float getRemaining() {
		return remaining;
	};
	public float getSpecLength() {
		return length;
	}
	public int getComponentSize() {
		return componentList.size();
	}
	public listFormat getListformat() {
		if(componentList.size()*2 <= MAX_A4_COLUMN)
			return listFormat.GENERAL;
		return listFormat.COMPACTED;
	}
	public ArrayList<Pair<String, Float>> getComponentList() {
		return componentList;
	}
	public String getFirstComponent() {
		return componentList.get(0).getFirst();
	}
	// print
	@Override
	public String toString() {
		return "Specification: " + specification + 
				", length: " + length +
				", componentList: " + componentList  +
				", count: " + count;
	}
	@Override
	public List <Object> getInfo() {
		List<Object> info = new ArrayList <Object>();
		info.add(specification);
		info.add(length);
		if(componentList.size()*2 <= MAX_A4_COLUMN) {
			for(Pair<String, Float> item : componentList) {
				info.add(item.getFirst());
				info.add(item.getSecond());
			}
		}
		else {
			LinkedHashMap <Pair<String, Float>, Integer> linkedHashMap = compressComponentsList();
			for (Entry<Pair<String, Float>, Integer> entry : linkedHashMap.entrySet()) {
				info.add(entry.getKey().getFirst());
				info.add(entry.getKey().getSecond());
				info.add(entry.getValue());
			}
		}
		return info;
	}
	private LinkedHashMap <Pair<String, Float>, Integer> compressComponentsList(){
		LinkedHashMap <Pair<String, Float>, Integer> linkedHashMap = new LinkedHashMap <Pair<String, Float>, Integer>();
		for(Pair<String, Float> item : componentList) {
			if(!linkedHashMap.containsKey(item)) {
				linkedHashMap.put(item, 1);
			} else {
				int value = linkedHashMap.get(item) +1;
				linkedHashMap.put(item, value);
			}
		}
		return linkedHashMap;
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


