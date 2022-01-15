package resources;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IgnoredErrorType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellReference;

public class ExcelWriter extends ExcelBase{

	CellStyle style;
	Font red_font;
	final int WIDER_COLUMN_WIDTH = 5000;
	final int COLUMN_WIDTH = 2500;
	
	public ExcelWriter(String fileName){
		super(fileName);
		
		this.style = wb.createCellStyle();
		this.red_font = wb.createFont();
		red_font.setColor(HSSFColor.HSSFColorPredefined.RED.getIndex());
        style.setFont(red_font);
	}

	// for component sheet
	public void writeNCL(ArrayList<Component> ncl) {

		Sheet sheet;
		if(isSheetExist(variables.COMPONENTS_SPECIFICATION)) {
			System.out.println("Remove old content from sheet " + variables.COMPONENTS_SPECIFICATION+ "(移除舊的"+variables.COMPONENTS_SPECIFICATION+"頁面)");
			removeSheetByName(variables.COMPONENTS_SPECIFICATION);
		}
		System.out.println("Create new sheet " + variables.COMPONENTS_SPECIFICATION + "(創造新的"+variables.COMPONENTS_SPECIFICATION+"頁面)");
		sheet = wb.createSheet(variables.COMPONENTS_SPECIFICATION);
		// add new COMPONENTS_SPECIFICATION here
		Row row = null;
		Cell cell = null;
		int rowCnt = 0;
		int cellCnt = 0;
		String COMPONENTS_SPECIFICATION_MENU[] = {variables.COMPONENTS_SPECIFICATION, variables.MATERIAL, variables.LENGTH, 
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
			ArrayList<Object> info = (ArrayList<Object>) item.getSpecInfo();
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
		int MIN_COLUMN = 20;
		String COMPONENTS_SPECIFICATION_MENU[] = {variables.SPECIFICATION_LENGTH, variables.LENGTH,
													variables.COMPONENTS, variables.LENGTH,
													variables.REMAINING, variables.WEIGHT
													};
		for(SpecSegmentation item : sym) {
			if(MIN_COLUMN < item.getComponentLength()*2){
				MIN_COLUMN = item.getComponentLength()*2;
			}
		}
		
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
		
		// write content
		XSSFSheet xsheet = (XSSFSheet)sheet;
		for(SpecSegmentation item : sym) {
			ArrayList<Object> info = (ArrayList<Object>) item.getSegInfo();
			//test new layout
			for(int i=0 ; i<item.count ; i++) {
				rowCnt++;
				row = sheet.createRow(rowCnt);
				cellCnt = 0;
				for(Object obj : info) {
					cell = row.createCell(cellCnt);
					// odd column is red
					if(cellCnt%2 == 0 && cellCnt>2) {
						cell.setCellStyle(style);
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
				cell.setCellValue(Utils.fmt((float)item.remaining));
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
	
//	for (Entry<SpecSegmentation, Integer> entry : linkedHashMap.entrySet()) {
//    System.out.println(entry.getKey() + ":" + entry.getValue().toString());
//}
	public void removeSheetByName(String sheetname) {
		for(int i = 0 ; i<this.wb.getNumberOfSheets() ; i++) {
			if(sheetname.equals(wb.getSheetName(i))) {
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
	
}
