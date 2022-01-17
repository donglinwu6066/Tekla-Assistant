package resources;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Component implements CellOutput{
	String component;
	Integer material;
	Float length;
	Integer count;
	String specification;
	String texture;
	Component(String component, int material, float length, int count, 
			String specification, String texture){
		this.component = component;
		this.material = material;
		this.length = length;
		this.count = count;
		this.specification = specification;
		this.texture = texture;
	}
	// print
	@Override
	public String toString() {
        return "component: " + component + 
        		", material: " + material +
        		", length: " + length +
        		", count: " + count +
        		", specification: " + specification +
        		", texture: " + texture;
    }
	@Override
	public List <Object> getInfo() {
		List<Object> info = new ArrayList <Object>();
		info.add(component);
		info.add(material);
		info.add(length);
		info.add(count);
		info.add(specification);
		info.add(texture);
		return info;
	}
}

class ComponentSort implements Comparator<Component> 
{ 
	// ascending
	@Override
	public int compare(Component a, Component b) 
	{
		// e.g. 76M192 vs 77M29
		String[] tokenA = a.component.split("M");
		String[] tokenB = b.component.split("M");
		if(Integer.parseInt(tokenA[0]) != Integer.parseInt(tokenB[0])) {
			return Integer.parseInt(tokenA[0]) - Integer.parseInt(tokenB[0]);
		}
		return Integer.parseInt(tokenA[1]) - Integer.parseInt(tokenB[1]);
	} 
} 