package resources;

import java.util.ArrayList;

public class CellBlock{
	private static Pair<Point, Point> companyBlock;
	private static Pair<Point, Point> projectBlock;
	private static Pair<Point, Point> tableNameBlock;
	private static Pair<Point, Point> specificationBlock;
	private static Pair<Point, Point> tabulatorBlock;
	private static Pair<Point, Point> auditorBlock;
	private static Pair<Point, Point> tabulatorSignBlock;
	private static Pair<Point, Point> auditorSignBlock;
	
	private static Pair<Point, Point> upperRightWireFrame;
	private static Pair<Point, Point> lowerLeftWireFrame;
	private static Pair<Point, Point> tabulatorWireFrame;
	private static Pair<Point, Point> tabulatorSignWireFrame;
	private static Pair<Point, Point> auditorWireFrame;
	private static Pair<Point, Point> auditorSignWireFrame;
	CellBlock(){
		companyBlock = new Pair<Point, Point>(new Point(0, 0), new Point(0, 11));
		projectBlock = new Pair<Point, Point>(new Point(1, 0), new Point(1, 4));
		tableNameBlock = new Pair<Point, Point>(new Point(1, 7), new Point(1, 8));
		specificationBlock = new Pair<Point, Point>(new Point(2, 7), new Point(2, 9));
		tabulatorBlock = new Pair<Point, Point>(new Point(14, 7), new Point(14, 8));
		auditorBlock = new Pair<Point, Point>(new Point(14, 9), new Point(14, 10));
		tabulatorSignBlock = new Pair<Point, Point>(new Point(15, 7), new Point(16, 8));
		auditorSignBlock = new Pair<Point, Point>(new Point(15, 9), new Point(16, 10));
		
		upperRightWireFrame = new Pair<Point, Point>(new Point(3, 7), new Point(8, 11));
		lowerLeftWireFrame = new Pair<Point, Point>(new Point(8, 0), new Point(33, 5));
		// this is for block with border
		tabulatorWireFrame = new Pair<Point, Point>(new Point(14, 7), new Point(14, 8));
		tabulatorSignWireFrame = new Pair<Point, Point>(new Point(15, 7), new Point(16, 8));
		auditorWireFrame = new Pair<Point, Point>(new Point(14, 9), new Point(14, 10));
		auditorSignWireFrame = new Pair<Point, Point>(new Point(15, 9), new Point(16, 10));
	}
	public ArrayList<Pair<Point, Point>> getCNCBlock(){
		ArrayList<Pair<Point, Point>> region = new ArrayList<Pair<Point, Point>>();
		region.add(companyBlock);
		region.add(projectBlock);
		region.add(tableNameBlock);
		region.add(specificationBlock);
		region.add(tabulatorSignBlock);
		region.add(auditorSignBlock);
		region.add(tabulatorBlock);
		region.add(auditorBlock);
		return region;
	}
	public ArrayList<Pair<Point, Point>> getCNCWireFrame() {
		ArrayList<Pair<Point, Point>> region = new ArrayList<Pair<Point, Point>>();
		region.add(upperRightWireFrame);
		region.add(lowerLeftWireFrame);
		region.add(tabulatorWireFrame);
		region.add(tabulatorSignWireFrame);
		region.add(auditorWireFrame);
		region.add(auditorSignWireFrame);
		return region;
	}
	
}
