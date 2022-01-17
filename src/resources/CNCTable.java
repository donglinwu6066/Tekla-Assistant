package resources;

import java.util.ArrayList;
import java.util.List;

public class CNCTable implements CellOutput{
	private final String companyName;
	private final String projectName;
	private final String tableName;
	private final String serialNumber;
	private final String materialLength;
	private final String structuralMenber;
	private final String length;
	private final String components;
	private final String count;
	private final String width;
	private final String fileStr;
	private final String girderHeight;
	private final String pro;
	private final String girderThickness;
	private final String no;
	private final String texture;
	private final String step;
	private final String position;
	private final String leftGirder;
	private final String webGirder;
	private final String rightGirder;
	private final String materialCount;
	private final String check;
	private final String useLength;
	private final String tabulator;
	private final String auditor;
	private final String tabulatingDate;
	private final String releasingDate;
	private ArrayList<Integer> materialSerialNumber;
	private ArrayList<Integer> stepSerialNumber;
	CNCLocation location;
	CellBlock cellblock;
	
	CNCTable(){
		location = new CNCLocation();
		location.setTableLayout();
		cellblock = new CellBlock();
		this.companyName = variables.COMPANY_NAME ;
		this.projectName = variables.PROJECT_NAME ;
		this.tableName = variables.CNC_TABLE_NAME ;
		this.serialNumber = variables.SERIAL_NUMBER ;
		this.materialLength = variables.MATERIAL_LENGTH ;
		this.structuralMenber = variables.STRUCTURAL_MEMBER ;
		this.length = variables.LENGTH ;
		this.components = variables.COMPONENTS ;
		this.count = variables.COUNT ;
		this.width = variables.WIDTH ;
		this.fileStr = variables.FILE_STR ;
		this.girderHeight = variables.GIRDER_HEIGHT ;
		this.pro = variables.PRO ;
		this.girderThickness = variables.GIRDER_THICKNESS ;
		this.no = variables.NO ;
		this.texture = variables.TEXTURE ;
		this.step = variables.STEP ;
		this.position = variables.POSITION ;
		this.leftGirder = variables.LEFT_GIRDER ;
		this.webGirder = variables.WEB_GIRDER;
		this.rightGirder = variables.RIGHT_GIRDER ;
		this.materialCount = variables.MATERIAL_COUNT ;
		this.check = variables.CHECK ;
		this.useLength = variables.USE_LENGTH ;
		this.tabulator = variables.TABULATOR ;
		this.auditor = variables.AUDITOR ;
		this.tabulatingDate = variables.TABULATING_DATE ;
		this.releasingDate = variables.RELEASING_DATE ;
		this.materialSerialNumber = new ArrayList<Integer>();
		for(int i=1 ; i<=location.COMPONENT_TABLE_SIZE ; i++) {
			this.materialSerialNumber.add(i);
		}
		this.stepSerialNumber = new ArrayList<Integer>();
		for(int i=1 ; i<=location.CNC_TABLE_SIZE ; i++) {
			this.stepSerialNumber.add(i);
		}

		
	}
	@Override
	public List<Object> getInfo() {
		List<Object> info = new ArrayList <Object>();
		info.add(companyName);
		info.add(projectName);
		info.add(tableName);
		info.add(serialNumber); // serialNumber(table)
		info.add(materialLength);
		info.add(serialNumber); // serialNumber(component)
		info.add(structuralMenber);
		info.add(length);
		info.add(components);
		info.add(count);
		info.add(width);
		info.add(fileStr);
		info.add(girderHeight);
		info.add(pro);
		info.add(girderThickness);
		info.add(no);
		info.add(texture);
		info.add(step);
		info.add(position);
		info.add(leftGirder);
		info.add(webGirder);
		info.add(rightGirder);
		info.add(materialCount);
		info.add(check);
		info.add(useLength);
		info.add(tabulator);
		info.add(auditor);
		info.add(tabulatingDate);
		info.add(releasingDate);
		info.addAll(this.materialSerialNumber);
		info.addAll(this.stepSerialNumber);
		return info;
	}
	public List<Object> getLayout() {
		return location.getInfo();
	}
	public ArrayList<Pair<Point, Point>> getMergeRange() {
		return cellblock.getCNCBlock();
	}
	public ArrayList<Pair<Point, Point>> getWireFrameeRange() {
		return cellblock.getCNCWireFrame();
	}
	public int getMaxRow() {
		return MAX_ROW;
	}
	public int getMaxColumn() {
		return MAX_COLUMN;
	}
	public int getCNCTableStart() {
		return location.CONNECTION_TABLE_START;
	}
}
