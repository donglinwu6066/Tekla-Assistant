package resources;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;

public class CNCInfo implements CellOutput{
	private final Integer serialNumber;
	private final String specification;
	private final Float specificationLength;
	private final Integer specificationWidth;
	private final Integer specificationHeight;
	private final Float specificationThickness;
	private final String texture;
	private final Integer materialCount;
	private final Float check;
	private final Date dNow;
	private final SimpleDateFormat date;
	private CNCLocation location;
	
	private ArrayList<CompSummarization> compSumList;
	
	CNCInfo(int serialNumber, Entry<SpecSegmentation, Integer> entry,
			String texture){
		this.serialNumber = serialNumber+1;
		this.specification = entry.getKey().getSpec();
		
		this.specificationLength = entry.getKey().getSpecLength();
		String tokens[] = specification.split("\\*");
		// remove alphabets
		tokens[0] = tokens[0].replaceAll("[^\\d.]", "");
		this.specificationWidth = Integer.parseInt(tokens[0]);
		this.specificationHeight = Integer.parseInt(tokens[1]);
		this.specificationThickness = Float.parseFloat(tokens[2]);
		
		this.texture = texture;
		compSumList = new ArrayList<CompSummarization>();
		ArrayList<Pair<String, Float>> component = entry.getKey().getComponentList();
		// count components numbers
		for(Pair<String, Float> item : component) {
			CompSummarization compSum = new CompSummarization(item.getFirst(), item.getSecond());
			if(!compSumList.contains(compSum)) {
				compSumList.add(compSum);
			}else {
				int idx = compSumList.indexOf(compSum);
				compSum = compSumList.get(idx);
				compSum.increase();
				compSumList.set(idx, compSum);
			}
		}

		this.materialCount = entry.getValue();
		
		float check = 10 + 3;
		for(CompSummarization comSum : compSumList) {
			check += comSum.useLength()+3;
		}
		this.check = check;
		
		dNow = new Date( );
		date = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
		location = new CNCLocation();
		location.setInfoLayout(compSumList.size());
	}
	public ArrayList<CompSummarization> getCompSumList(){
		return compSumList;
	}
	@Override
	public List<Object> getInfo() {
		List<Object> info = new ArrayList <Object>();
		info.add(serialNumber);
		info.add(specification);
		info.add(specificationLength);
		info.add(specificationWidth);
		info.add(specificationHeight);
		info.add(specificationThickness);
		info.add(texture);
		info.add(materialCount);
		info.add(check);
		info.add(date.format(dNow));
		for(CompSummarization comSum : compSumList) {
			List<Object> tmp = comSum.getInfo();
			Collections.swap(tmp, 0, 1);
			info.addAll(tmp);
			
		}
		return info;
	}
	public List<Object> getLayout() {
		return location.getInfo();
	}
}
