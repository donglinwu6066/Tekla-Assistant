package resources;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IgnoredErrorType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellReference;

public class ExcelWriter extends ExcelBase{
	private CNCTable cncTable;
	private HashMap<String, ArrayList<ConnectionInfo>> connectionInfo;
	private CellStyle style;
	private CellStyle redStyle;
	private CellStyle borderStyle;
	private Font redFont;
	private final int WIDER_COLUMN_WIDTH = 5000;
	private final int COLUMN_WIDTH = 2500;
	private final int SHIFT = CellOutput.MAX_ROW;
	
	public ExcelWriter(String fileName){
		super(fileName);
		this.cncTable = new CNCTable();
		this.redFont = wb.createFont();
		this.redFont.setColor(HSSFColor.HSSFColorPredefined.RED.getIndex());
		
		this.style = wb.createCellStyle();
		this.style.setAlignment(HorizontalAlignment.CENTER);
		
		this.redStyle = wb.createCellStyle();
		this.redStyle.setFont(redFont);
		this.redStyle.setAlignment(HorizontalAlignment.CENTER);
		
		this.borderStyle = wb.createCellStyle();
		this.borderStyle.setBorderBottom(BorderStyle.THIN);
		this.borderStyle.setBorderTop(BorderStyle.THIN);
		this.borderStyle.setBorderRight(BorderStyle.THIN);
		this.borderStyle.setBorderLeft(BorderStyle.THIN);
		this.borderStyle.setAlignment(HorizontalAlignment.CENTER);
	}
	
	// for component sheet
	public void writeNCL(ArrayList<Component> ncl) {

		Sheet sheet;
		if(isSheetExist(variables.COMPONENTS_SPECIFICATION)) {
			System.out.println("Remove old content from sheet " + 
				variables.COMPONENTS_SPECIFICATION+ "(移除舊的"+variables.COMPONENTS_SPECIFICATION+"頁面)");
			removeSheetByName(variables.COMPONENTS_SPECIFICATION);
		}
		System.out.println("Create new sheet " + variables.COMPONENTS_SPECIFICATION + 
				"(創造新的"+variables.COMPONENTS_SPECIFICATION+"頁面)");
		sheet = wb.createSheet(variables.COMPONENTS_SPECIFICATION);
		// add new COMPONENTS_SPECIFICATION here
		Row row = null;
		Cell cell = null;
		int rowCnt = 0;
		int cellCnt = 0;
		String COMPONENTS_SPECIFICATION_MENU[] = {variables.COMPONENTS_SPECIFICATION, 
				variables.MATERIAL, variables.LENGTH, 
				variables.COUNT, variables.SPECIFICATION, variables.TEXTURE};
		
		for (int i =0 ; i<  COMPONENTS_SPECIFICATION_MENU.length ; i++) {
		    sheet.setColumnWidth(i, COLUMN_WIDTH);;
		}
		//spec need more space
		sheet.setColumnWidth(4 ,WIDER_COLUMN_WIDTH);
		// write first menu
		row = sheet.createRow(rowCnt);
		for(String str : COMPONENTS_SPECIFICATION_MENU) {
			cell = row.createCell(cellCnt);	
			cell.setCellValue(str);
			cellCnt += 1;
		}
		
		for(Component item : ncl) {
			rowCnt++;
			cellCnt = 0;
			row = sheet.createRow(rowCnt);
			XSSFSheet xsheet = (XSSFSheet)sheet;
			ArrayList<Object> info = (ArrayList<Object>) item.getInfo();
			for(Object obj : info) {
				cell = row.createCell(cellCnt);
				if(obj.getClass() == String.class)
					cell.setCellValue(obj.toString());
				else if(obj.getClass() == Integer.class) {
					cell.setCellValue(obj.toString());
				}
				else if(obj.getClass() == Float.class) {
					cell.setCellValue(Utils.fmt((float)obj));
				}
				CellReference cellR = new CellReference(cell);
				xsheet.addIgnoredErrors(cellR, IgnoredErrorType.NUMBER_STORED_AS_TEXT);
				cellCnt += 1;
			}
		}
	}
	// for prediction sheet
	public void writeSYM(ArrayList<SpecSegmentation> sym) {

		Sheet sheet;
		if(isSheetExist(variables.PREDICTION)) {
			System.out.println("Remove old content from sheet " + variables.PREDICTION + "(移除舊的"+variables.PREDICTION+"頁面)");
			removeSheetByName(variables.PREDICTION);
		}
		System.out.println("Create new sheet " + variables.PREDICTION+ "(創造新的"+variables.PREDICTION+"頁面)");
		sheet = wb.createSheet(variables.PREDICTION);
		// add new COMPONENTS_SPECIFICATION here
		Row row = null;
		Cell cell = null;
		int rowCnt = 0;
		int cellCnt = 0;
		int MIN_COLUMN = SpecSegmentation.MAX_A4_COLUMN;
		String COMPONENTS_SPECIFICATION_MENU[] = {variables.SPECIFICATION_LENGTH, variables.LENGTH,
													variables.COMPONENTS, variables.LENGTH,
													variables.REMAINING, variables.WEIGHT
													};
		
		// 4 is for other items
		for (int i =0 ; i<  MIN_COLUMN + 5 ; i++) {
		    sheet.setColumnWidth(i, COLUMN_WIDTH);;
		}
		//spec need more space
		sheet.setColumnWidth(0 ,WIDER_COLUMN_WIDTH);

		// write first menu
		row = sheet.createRow(rowCnt);
		for(int i=0 ; i<2 ; i++) {
			cell = row.createCell(cellCnt);	
			cell.setCellValue(COMPONENTS_SPECIFICATION_MENU[i]);
			cellCnt += 1;
		}
		// components
		for(int i=0 ; i<MIN_COLUMN ; i++) {
			cell = row.createCell(cellCnt);	
			cell.setCellValue(COMPONENTS_SPECIFICATION_MENU[2+i%2]);
			cellCnt += 1;
		}
		for(int i=0 ; i<2 ; i++) {
			cell = row.createCell(cellCnt);	
			cell.setCellValue(COMPONENTS_SPECIFICATION_MENU[4+i]);
			cellCnt += 1;
		}
		
		redStyle = wb.createCellStyle();
		redStyle.setFont(this.redFont);
		// write content
		XSSFSheet xsheet = (XSSFSheet)sheet;
		for(SpecSegmentation item : sym) {
			ArrayList<Object> info = (ArrayList<Object>) item.getInfo();
			//test new layout
			for(int i=0 ; i<item.count ; i++) {
				rowCnt++;
				row = sheet.createRow(rowCnt);
				cellCnt = 0;
				for(Object obj : info) {
					cell = row.createCell(cellCnt);
					// odd column is red
					if(cellCnt%2 == 0 && cellCnt>2) {
						cell.setCellStyle(redStyle);
					}
					if(obj.getClass() == String.class)
						cell.setCellValue(obj.toString());
					else if(obj.getClass() == Integer.class) {
						cell.setCellValue(obj.toString());
					}
					else if(obj.getClass() == Float.class) {
						cell.setCellValue(Utils.fmt((float)obj));
					}
					CellReference cellR = new CellReference(cell);
					xsheet.addIgnoredErrors(cellR, IgnoredErrorType.NUMBER_STORED_AS_TEXT);
					cellCnt += 1;
				}
				// 3 is to locate remaining
				cell = row.createCell(MIN_COLUMN + 2);
				cell.setCellValue(Utils.fmt((float)item.getRemaining()));
				CellReference cellR = new CellReference(cell);
				xsheet.addIgnoredErrors(cellR, IgnoredErrorType.NUMBER_STORED_AS_TEXT);
			}
		}
		
		
	}
	public void writeSum(ArrayList<CompSummarization> sumList) {
		Sheet sheet;
		if(isSheetExist(variables.SUMMARIZATION)) {
			System.out.println("Remove old content from sheet " + variables.SUMMARIZATION + "(移除舊的"+variables.SUMMARIZATION+"頁面)");
			removeSheetByName(variables.SUMMARIZATION);
		}
		System.out.println("Create new sheet " + variables.SUMMARIZATION+ "(創造新的"+variables.SUMMARIZATION+"頁面)");
		sheet = wb.createSheet(variables.SUMMARIZATION);
		Row row = null;
		Cell cell = null;
		String SUMMARIZATION_MENU[] = {variables.MATERIAL, variables.LENGTH, variables.COUNT};
		
		for (int i =0 ; i<  SUMMARIZATION_MENU.length ; i++) {
		    sheet.setColumnWidth(i, COLUMN_WIDTH);;
		}
		int rowCnt = 0;
		int cellCnt = 0;
		// write first menu
		row = sheet.createRow(rowCnt);
		for(String str : SUMMARIZATION_MENU) {
			cell = row.createCell(cellCnt);	
			cell.setCellValue(str);
			cellCnt += 1;
		}
		
		for(CompSummarization comSum : sumList) {
			rowCnt++;
			cellCnt = 0;
			row = sheet.createRow(rowCnt);
			XSSFSheet xsheet = (XSSFSheet)sheet;
			ArrayList<Object> info = (ArrayList<Object>) comSum.getInfo();
			for(Object obj : info) {
				cell = row.createCell(cellCnt);
				if(obj.getClass() == String.class)
					cell.setCellValue(obj.toString());
				else if(obj.getClass() == Integer.class) {
					cell.setCellValue(obj.toString());
				}
				else if(obj.getClass() == Float.class) {
					cell.setCellValue(Utils.fmt((float)obj));
				}
				CellReference cellR = new CellReference(cell);
				xsheet.addIgnoredErrors(cellR, IgnoredErrorType.NUMBER_STORED_AS_TEXT);
				cellCnt += 1;
			}
		}
	}
	public void writeCNC(LinkedHashMap <SpecSegmentation, Integer> CNCList, HashMap<String, String> textureHash) {
		Sheet sheet;
		
		// remove old CNC sheet
		if(isCNCExist()) {
			System.out.println("Remove old CNC from sheet (移除舊的CNC頁面)");
			removeAllCNC();
		}
		// create new CNC sheet
		ArrayList<Integer> cncSheet = new ArrayList<Integer>();
		for (Entry<SpecSegmentation, Integer> entry : CNCList.entrySet()) {
//			System.out.println(entry.getKey() + ":" + entry.getValue().toString());
			int serialNumber = Integer.parseInt(entry.getKey().getFirstComponent().toString().split("M")[0]);
			if(!cncSheet.contains(serialNumber)) {
				cncSheet.add(serialNumber);
			}
		}
		HashMap<String, Integer> shiftMap = new HashMap<String, Integer>();
		Collections.sort(cncSheet);
		for(Integer serialNumber : cncSheet) {
			String sheetName = serialNumber.toString() + "M";
			wb.createSheet(sheetName);
			shiftMap.put(sheetName, 0);
		}
		
		
		for(Entry<SpecSegmentation, Integer> entry : CNCList.entrySet()) {
			String componentReference = entry.getKey().getFirstComponent();
			String sheetName = componentReference.toString().split("M")[0] + "M";
			sheet = wb.getSheet(sheetName);
			int shiftIdx = shiftMap.get(sheetName) ;
			createCNCBlock(sheet, shiftIdx * SHIFT);
			String texture = textureHash.get(componentReference);
			fillCNCBlock(sheet, shiftIdx, entry, texture);
			shiftMap.replace(sheetName, (shiftIdx+1));
			
		}
		
	}
	public void setConnection(HashMap<String, ArrayList<ConnectionInfo>> connectionInfo) {
		this.connectionInfo = connectionInfo;
	}
	private void createCNCBlock(Sheet sheet, int shift) {
		Row row;
		for(int i=0 ; i < cncTable.getMaxRow() ; i ++) {
			row = sheet.createRow(shift + i);
			for(int j=0 ; j <cncTable.getMaxColumn() ; j++) {
				row.createCell(j);
			}
		}
		for (int i =0 ; i<  cncTable.getMaxColumn() ; i++) {
		    sheet.setColumnWidth(i, COLUMN_WIDTH);;
		}
		// merge range
		ArrayList<Pair<Point, Point>> mergeList = this.cncTable.getMergeRange();
        for(Pair<Point, Point> range : mergeList) {
        	sheet.addMergedRegion(new CellRangeAddress(range.getFirst().getRow() + shift, range.getSecond().getRow() + shift, 
        			range.getFirst().getColumn(),range.getSecond().getColumn()));
        }
	}
	private void fillCNCBlock(Sheet sheet, int shiftIdx, Entry<SpecSegmentation, Integer> entry , String texture) {
		fillCNCInfo(sheet, shiftIdx, entry, texture);
		plotCNCTable(sheet, shiftIdx * SHIFT);
	}
	private void fillCNCInfo(Sheet sheet, int shiftIdx, Entry<SpecSegmentation, Integer> entry, String texture) {
		CNCInfo cncInfo = new CNCInfo(shiftIdx, entry, texture);
		List<Object> infoList = cncInfo.getInfo();
		List<Object> locationList = cncInfo.getLayout();
		Row row = null;
		Cell cell = null;
		
		// fill text
		Iterator<Object> iter = locationList.iterator();
		for(Object obj : infoList) {
	        Point point;
			if(iter.hasNext()) {
				point = (Point)iter.next();
				row = sheet.getRow(point.getRow()+shiftIdx * SHIFT);
				cell = row.getCell(point.getColumn());
				if(obj.getClass() == String.class) {
					cell.setCellValue(obj.toString());
				}
				else if(obj.getClass() == Integer.class) {
					cell.setCellValue(obj.toString());
				}else if(obj.getClass() == Float.class) {
					cell.setCellValue(Utils.fmt((float)obj));
				}
				
				XSSFSheet xsheet = (XSSFSheet)sheet;
				CellReference cellR = new CellReference(cell);
				xsheet.addIgnoredErrors(cellR, IgnoredErrorType.NUMBER_STORED_AS_TEXT);
			}
		}
		if(connectionInfo!=null) {
			ArrayList<ConnectionInfo> cncConnList = getCNCConn(cncInfo.getCompSumList());
			fillCNCConn(sheet, shiftIdx * SHIFT, cncConnList);
		}
	}
	

	private void plotCNCTable(Sheet sheet, int shift) {
		List<Object> infoList = this.cncTable.getInfo();
		List<Object> locationList = this.cncTable.getLayout();
		Row row = null;
		Cell cell = null;
		
		// fill text
		Iterator<Object> iter = locationList.iterator();
		for(Object obj : infoList) {
	        Point point;
			if(iter.hasNext()) {
				point = (Point)iter.next();
				row = sheet.getRow(point.getRow()+shift);
				cell = row.getCell(point.getColumn());
				if(obj.getClass() == String.class) {
					cell.setCellValue(obj.toString());
				}
				else if(obj.getClass() == Integer.class) {
					cell.setCellValue(obj.toString());
				}
				cell.setCellStyle(style);
				XSSFSheet xsheet = (XSSFSheet)sheet;
				CellReference cellR = new CellReference(cell);
				xsheet.addIgnoredErrors(cellR, IgnoredErrorType.NUMBER_STORED_AS_TEXT);
			}
		}
		// plot wire frame 
		ArrayList<Pair<Point, Point>> frameList = this.cncTable.getWireFrameeRange();
        for(Pair<Point, Point> range : frameList) {
        	for(int i = range.getFirst().getRow() ; i <= range.getSecond().getRow() ; i ++) {
        		row = sheet.getRow(i + shift);
        		for(int j = range.getFirst().getColumn() ; j<= range.getSecond().getColumn() ; j ++) {
        			cell = row.getCell(j);
        			cell.setCellStyle(borderStyle);
        		}
        	}
        }
	}
	private void fillCNCConn(Sheet sheet, int shift, ArrayList<ConnectionInfo> cncConnList) {
		int cncRowStart = cncTable.getCNCTableStart() + shift;
		// 0 for left, 1 for right
		Point positions[] = {new Point(cncRowStart, 7), new Point(cncRowStart, 9)};
		int leftOrRight = 1;
		
		for(ConnectionInfo conn : cncConnList) {
			Row row;
			Cell cell;
			List<Object> info = conn.getInfo();
			leftOrRight = (positions[0].getRow() <= positions[1].getRow()) ? 0 : 1;
			int putRowIdx = positions[leftOrRight].getRow();
			int putColIdx = positions[leftOrRight].getColumn();
			row = sheet.getRow(putRowIdx);
			cell = row.getCell(putColIdx);
			
			// for menu
			sheet.addMergedRegion(new CellRangeAddress(putRowIdx, putRowIdx, 
					putColIdx, putColIdx+1));
			row.getCell(putColIdx).setCellStyle(borderStyle);
			row.getCell(putColIdx+1).setCellStyle(borderStyle);
			
			cell.setCellValue(info.get(0).toString());
			putRowIdx++;
			
			// prepare for next
			int tableLength = (int)((conn.getInfo().size()+1)/2);
			positions[leftOrRight].jumpRow(tableLength);
			// fill content
			for(int i=1 ; i<tableLength ; i++) {
				row = sheet.getRow(putRowIdx);
				putColIdx = positions[leftOrRight].getColumn();
				for(int j=0 ; j<2 ; j++) {
					cell = row.getCell(putColIdx);
					cell.setCellValue(info.get((i*2)-1+j).toString());
					XSSFSheet xsheet = (XSSFSheet)sheet;
					CellReference cellR = new CellReference(cell);
					xsheet.addIgnoredErrors(cellR, IgnoredErrorType.NUMBER_STORED_AS_TEXT);
					cell.setCellStyle(borderStyle);
					putColIdx++;
				}
				putRowIdx++;
			}
		}
	}
	private ArrayList<ConnectionInfo> getCNCConn(ArrayList<CompSummarization> compSumList){
		ArrayList<ConnectionInfo> CNCConn = new ArrayList<ConnectionInfo>();
		for(CompSummarization comSum : compSumList) {
			
			ArrayList<ConnectionInfo> connInfo = connectionInfo.get(comSum.getMaterial());
			if(connInfo==null) {
				continue;
			}
			for(ConnectionInfo conn : connInfo) {
				if(conn==null) {
					continue;
				}
				if(!CNCConn.contains(conn)) {
					CNCConn.add(conn);
				}
			}
		}
		return CNCConn;
	}

	public void removeSheetByName(String sheetname) {
		for(int i = 0 ; i<this.wb.getNumberOfSheets() ; i++) {
			if(sheetname.equals(wb.getSheetName(i))) {
				wb.removeSheetAt(i);
			}
		}
	}
	public void removeAllCNC() {
		for(int i = this.wb.getNumberOfSheets() - 1 ; i>=0 ; i--) {
			CharSequence chSeq = "M";
			if(wb.getSheetName(i).contains(chSeq)) {
				wb.removeSheetAt(i);
			}
		}
	}
	public void save() {
		try {
			FileOutputStream fileOut = new FileOutputStream(fileName);
			wb.write(fileOut);
			fileOut.close();
			wb.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public boolean isCNCExist() {
		for(Sheet sheet : wb) {
			CharSequence chSeq = "M";
			if(sheet.getSheetName().contains(chSeq))
				return true;
		}
		return false;
	}
	
	
}
