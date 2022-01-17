package resources;

import java.util.ArrayList;
import java.util.List;

public class CNCLocation implements CellOutput{
	final int CONNECTION_TABLE_START = 19;
	final int COMPONENT_TABLE_START = 4;
	final int COMPONENT_TABLE_SIZE = 5;
	final int CNC_TABLE_START = 9;
	final int CNC_TABLE_SIZE = 25;
	private ArrayList<Point> points;
	CNCLocation(){points = new ArrayList<Point>();};
	
	public void setInfoLayout(int compSumSize){
		points.add(new Point(2, 1)); // serialNumber
		points.add(new Point(2, 7)); // specification
		points.add(new Point(3, 3)); // specificationLength
		points.add(new Point(4, 3)); // specificationWidth
		points.add(new Point(5, 3)); // specificationHeight
		points.add(new Point(6, 3)); // specificationThickness
		points.add(new Point(7, 3)); // texture

		points.add(new Point(11, 9)); // materialCount
		points.add(new Point(12, 9)); // check
		points.add(new Point(17, 9)); // date
		for(int i=4 ; i<4+compSumSize ; i++) {// compSummarization
			for(int j=9; j<=11 ; j++)
				points.add(new Point(i, j)); 
		}
	}
	
	public void setTableLayout(){
		points.add(new Point(0, 0)); // companyName
		points.add(new Point(1, 0)); // projectName
		points.add(new Point(1, 7)); // tableName
		points.add(new Point(2, 0)); // serialNumber(table)
		points.add(new Point(3, 2)); // materialLength
		points.add(new Point(3, 7)); // serialNumber(component)
		points.add(new Point(3, 8)); // structuralMenber
		points.add(new Point(3, 9)); // length
		points.add(new Point(3, 10)); // components
		points.add(new Point(3, 11)); // count
		points.add(new Point(4, 2)); // width
		points.add(new Point(5, 0)); // fileStr
		points.add(new Point(5, 2)); // girderHeight
		points.add(new Point(6, 0)); // pro
		points.add(new Point(6, 2)); // girderThickness
		points.add(new Point(7, 0)); // no
		points.add(new Point(7, 2)); // texture
		points.add(new Point(8, 0)); // step
		points.add(new Point(8, 1)); // position
		points.add(new Point(8, 2)); // leftGirder
		points.add(new Point(8, 3)); // webGirder
		points.add(new Point(8, 4)); // rightGirder
		points.add(new Point(11, 7)); // materialCount
		points.add(new Point(12, 7)); // check
		points.add(new Point(13, 7)); // useLength
		points.add(new Point(14, 7)); // tabulator
		points.add(new Point(14, 9)); // auditor
		points.add(new Point(17, 7)); // tabulatingDate
		points.add(new Point(18, 7)); // releasingDate
		
		for(int i=COMPONENT_TABLE_START ; i<COMPONENT_TABLE_START+COMPONENT_TABLE_SIZE ; i++) { // upper right table serial number
				points.add(new Point(i, 7)); 
		}
		
		for(int i=CNC_TABLE_START ; i<CNC_TABLE_START+CNC_TABLE_SIZE ; i++) { // lower left table serial number
			points.add(new Point(i, 0)); 
		}
	}

	@Override
	public List<Object> getInfo() {
		List<Object> info = new ArrayList <Object>(points);
		return info;
	}

	
}
