package resources;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

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
				if(cell.getCellType() == CellType.STRING) {
					item.add(cell.getStringCellValue());
				}
				else if(cell.getCellType() == CellType.NUMERIC) {
					String tmp = String.valueOf(cell.getNumericCellValue());
					if(Utils.isInteger(tmp)) {
						tmp = tmp.substring(0, tmp.indexOf('.'));
					}
						item.add(tmp);
					
				}
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
			// item.remove(item.size()-1);
			// detect prediction format
			SpecSegmentation.listFormat dataFormat = SpecSegmentation.listFormat.GENERAL;
			if(item.size()>2 && !item.get(2).contains("M")) {
				dataFormat = SpecSegmentation.listFormat.COMPACTED;
			}
			// add component in pair
			while(!item.isEmpty()) {
				if(dataFormat == SpecSegmentation.listFormat.GENERAL) {
					specSeg.addComponent(new Pair<String, Float>(item.get(0), Float.parseFloat(item.get(1))));
					item.remove(0);
					item.remove(0);
				} else{
					int count = Integer.parseInt(item.get(2));
					for(int i=0 ; i<count ; i++) {
						String component = item.get(0);
						float length = Float.parseFloat(item.get(1));
						specSeg.addComponent(new Pair<String, Float>(component, length));
					}
					item.remove(0);
					item.remove(0);
					item.remove(0);
				}
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
		return sumList;
	}
	public HashMap<String, ArrayList<ConnectionInfo>> readConnection() {
		HashMap<String, ConnectionInfo> typeHash = readConnectionType();
		HashMap<String, ArrayList<String>> codeHash = readConnectionCode();
		HashMap<String, ArrayList<ConnectionInfo>> CNCConnection 
			= new HashMap<String, ArrayList<ConnectionInfo>>();
		
		for (Entry<String, ArrayList<String>> entry : codeHash.entrySet()) {
//			System.out.println(entry.getKey() + ":" + entry.getValue().toString());
			ArrayList<ConnectionInfo> info = new ArrayList<ConnectionInfo>();
			for(String code : entry.getValue()) {
				if(!info.contains(typeHash.get(code))) {
					info.add(typeHash.get(code));
				}
			}
			CNCConnection.put(entry.getKey(), info);
		}
		return CNCConnection;
	}
	private HashMap<String, ConnectionInfo> readConnectionType() {
		HashMap<String, ConnectionInfo> typeHash 
			= new HashMap<String, ConnectionInfo>();
		
		Sheet sheet = wb.getSheet(variables.CONNECTION_TYPE);
		Iterator<Row> rows = sheet.rowIterator();		
		rows = sheet.rowIterator();
		// omit first menu row
		Iterator<Cell> menuColumns = rows.next().cellIterator();
		// dump first element
		menuColumns.next();
		ArrayList<String> wReference = new ArrayList<String>();
		while(menuColumns.hasNext()) {
			wReference.add(menuColumns.next().getStringCellValue());
		}

		while (rows.hasNext()) {
			Row row = rows.next();
			Iterator<Cell> columns = row.cellIterator();
			String type = columns.next().getStringCellValue();
			String type_domninate = type.split("-")[0];
			ConnectionInfo connectionInfo = new ConnectionInfo(type);
			if(type_domninate.equals("W")) {
				int i = 0;
				while(columns.hasNext()) {
					Cell cell = columns.next();
					if(cell.getCellType() == CellType.STRING) {
						connectionInfo.add(new Pair<Object, Object>( wReference.get(i), 
								cell.getStringCellValue()));
					} else if (cell.getCellType() == CellType.NUMERIC){
						connectionInfo.add(new Pair<Object, Object>(wReference.get(i), 
								cell.getNumericCellValue()));
					}
					i++;
				}
			}
			else {
				if(rows.hasNext()) {
					Row nextRow = rows.next();
					Iterator<Cell> nextColumn = nextRow.cellIterator();
					// dump first blank
					nextColumn.next();
					boolean lastNull = false;
					while(columns.hasNext() && nextColumn.hasNext()) {
						Cell cell = columns.next();
						Cell nextCell = nextColumn.next();
						Object first = new Object();
						Object second = new Object();
						switch(cell.getCellType()) {
						case STRING: 
							first = cell.getStringCellValue();
							break;
						case NUMERIC:
							first = cell.getNumericCellValue();
							break;
						default:
							lastNull = true;
							break;
						}
						switch(nextCell.getCellType()) {
						case STRING: 
							second = nextCell.getStringCellValue();
							break;
						case NUMERIC:
							second = nextCell.getNumericCellValue();
							break;
						default:
							lastNull = true;
							break;
						}
						if(lastNull) {break;}
						connectionInfo.add(new Pair<Object, Object>( first, second));
					}
				}
			}

			typeHash.put(type, connectionInfo);
		}
		return typeHash;
	}
	private HashMap<String, ArrayList<String>> readConnectionCode() {
			HashMap<String, ArrayList<String>> codeHash = new HashMap<String, ArrayList<String>>();
			
		Sheet sheet = wb.getSheet(variables.CONNECTION_CODE);
		Iterator<Row> rows = sheet.rowIterator();
		// omit first menu row
		rows.next();
		
		while (rows.hasNext()) {	
			Iterator<Cell> columns = rows.next().cellIterator();
			
			ArrayList<String> codeList = new ArrayList<String>();
			String component = columns.next().getStringCellValue();
			while(columns.hasNext()) {
				Cell cell = columns.next();
				if(!cell.getStringCellValue().equals(""))
					codeList.add(cell.getStringCellValue());
			}
			// remove last null element
			codeHash.put(component, codeList);
		}
		
		return codeHash;
	}
	public HashMap<String, String> componentToTexture() {
		HashMap<String, String> hash = new HashMap<String, String>();
		Sheet sheet = wb.getSheet(variables.COMPONENTS_SPECIFICATION);
		Iterator<Row> rows = sheet.rowIterator();
		
		Row row;
		Cell cell;
		String component = "";
		String texture = "";
		rows.next();
		while (rows.hasNext()) {	
			row = rows.next();
			cell = row.getCell(0);
			component = cell.getStringCellValue();
			cell = row.getCell(5);
			texture = cell.getStringCellValue();
			hash.put(component, texture);
		}
		return hash;
	}
}
