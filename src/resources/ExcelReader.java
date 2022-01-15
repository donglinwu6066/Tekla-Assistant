package resources;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;
import java.io.InputStreamReader;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

public class ExcelReader extends ExcelBase{

	public ExcelReader(String fileName){
		super(fileName);
	}
	
	public LinkedHashMap <SpecSegmentation, Integer> readPrediction() {
		Sheet sheet = wb.getSheet(variables.PREDICTION);
		Iterator<Row> rows = sheet.rowIterator();
		
		// omit first menu row
		rows.next();

		LinkedHashMap <SpecSegmentation, Integer> linkedHashMap = new LinkedHashMap <SpecSegmentation, Integer>();
		while (rows.hasNext()) {
			ArrayList<String> item = new ArrayList<String>();
			for(Cell cell: rows.next()) {
				item.add(cell.getStringCellValue());
			}
			
			SpecSegmentation specSeg = new SpecSegmentation();
			// set Specification
			specSeg.setSpecification(item.get(0));
			item.remove(0);
			// set Count
			specSeg.setCount(1);
			// set Length
			specSeg.setLength(Float.parseFloat(item.get(0)));
			item.remove(0);
			// remove remaining
			item.remove(item.size()-1);
			// add component in pair
			while(!item.isEmpty()) {
				specSeg.addComponent(new Pair<String, Float>(item.get(0), Float.parseFloat(item.get(1))));
				item.remove(0);
				item.remove(0);
			}
			
			if(!linkedHashMap.containsKey(specSeg)) {
				linkedHashMap.put(specSeg, 1);
			} else {
				int value = linkedHashMap.get(specSeg) +1;
				linkedHashMap.put(specSeg, value);
			}
		}

		return linkedHashMap;
	}
	public ArrayList<CompSummarization> getSummarization() {
		ArrayList<CompSummarization> sumList = new ArrayList<CompSummarization>();
		Sheet sheet = wb.getSheet(variables.PREDICTION);
		Iterator<Row> rows = sheet.rowIterator();
		Row row;
		Cell cell;
		// omit first menu row
		rows.next();
		
		String material = "";
		float length = 0;
		
		while (rows.hasNext()) {	
			row = rows.next();
			cell = row.getCell(1);
			length = Float.parseFloat(cell.getStringCellValue());
			cell = row.getCell(2);
			material = cell.getStringCellValue().split("M")[0];
			
			CompSummarization compSum = new CompSummarization(material, length);
			if(!sumList.contains(compSum)) {
				sumList.add(compSum);
			}
			else {
				// since equals is not include cnt, therefore, we can treat different cnt but the same 
				// material & length equal.
				int idx = sumList.indexOf(compSum);
				compSum = sumList.get(idx);
				compSum.increase();
				sumList.set(idx, compSum);
			}
			
		}
		System.out.println("sumList" + sumList);
		return sumList;
	}
}
