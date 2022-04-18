package resources;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.EmptyFileException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelBase {
	public String fileName;
	public Workbook wb;

	public ExcelBase(String fileName) {
		this.fileName = fileName;
		setWorkbook(this.fileName);
	}

	public void updateWorkbook() {
		setWorkbook(this.fileName);
	}

	private void setWorkbook(String path) {
		if (path == null) {
			System.out.println("path is null(路徑位置為空)");
			return;
		}
		int count = 0;
		int maxTries = 3;
		while(true) {
			try {
				String extString = path.substring(path.lastIndexOf("."));
				InputStream is = new FileInputStream(path);
	
				if (".xls".equals(extString) && is != null) {
					wb = new HSSFWorkbook(is);
				} else if (".xlsx".equals(extString) && is != null) {
					wb = new XSSFWorkbook(is);
				} else {
					System.out.println("\"" + path + "\" is a wrong extension(錯誤的副檔名)");
					is.close();
					return;
				}
				break;
			} catch (IOException | EmptyFileException e) {
				if (++count == maxTries) {
					wb = new XSSFWorkbook();
					FileOutputStream fileOut;
					try {
						fileOut = new FileOutputStream(fileName);
						wb.write(fileOut);
						fileOut.close();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					System.out.println("\"" + path + "\" is not exist(不存在)");
					System.out.println("Create(創建) " + fileName);
					break;
				}
			} 
		}
	}

	public boolean isSheetExist(String sheetname) {
		for (Sheet sheet : wb) {
			if (sheetname.equals(sheet.getSheetName()))
				return true;
		}
		return false;
	}

}
