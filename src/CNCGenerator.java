import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import resources.CompSummarization;
import resources.ConnectionInfo;
import resources.DataReader;
import resources.ExcelReader;
import resources.ExcelWriter;
import resources.Gatekeeper;
import resources.Pair;
import resources.SpecSegmentation;
import resources.Utils;
import resources.variables;

public class CNCGenerator {

	public static void main(String[] args) {
		try {
			String path = KeyGenerator.class.getProtectionDomain().getCodeSource().getLocation().getPath();
			String decodedPath[] = URLDecoder.decode(path, "UTF-8").split("/");
			int lastToken = decodedPath.length -1;
			String jarName = decodedPath[lastToken].substring(0, decodedPath[lastToken].lastIndexOf('.'));
			Utils.terminal(jarName);
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		DataReader dateReader = new DataReader();
		// required password
		if(variables.REQUIRED_PASSWORD) {
			Gatekeeper gatekeeper = new Gatekeeper();
			Pair<String, String> locker = dateReader.getLocker();
			// check host name and uuid
			if(!gatekeeper.isPassed(locker.getFirst(), locker.getSecond())) {
				System.out.println("Fail to passed verification(驗證失敗)");
				return;
			}
		}
		
		// read spec
		String pathStr = dateReader.toPath(
				variables.PROJECT_ROOT, variables.PROJECT_NAME, variables.PROJECT_NAME + ".xlsx");
		ExcelReader excelReader = new ExcelReader(pathStr);

		// override old database
		if(!excelReader.isSheetExist(variables.PREDICTION) | 
				variables.OVERRIDE_DATABASE_PREDICTION) {
			System.out.println("Override DatabaseGenerator(重新覆蓋 "+ 
				variables.COMPONENTS_SPECIFICATION+" 與 "+variables.PREDICTION+" )");
			DatabaseGenerator databaseGenerator = new DatabaseGenerator();
			databaseGenerator.run();
			excelReader.updateWorkbook();
		}
		else {
			System.out.println("Use original Database and Prediction(使用原本的 "+ 
				variables.COMPONENTS_SPECIFICATION+" 與 "+variables.PREDICTION+" )");
		}
		
		// sum
		ArrayList<CompSummarization> sumData = excelReader.getSummarization();
		ExcelWriter excelWriter = new ExcelWriter(pathStr);
		excelWriter.writeSum(sumData);
		
		// connection
		boolean connectPrompt = true;
		if(excelReader.isSheetExist(variables.CONNECTION_TYPE) && 
				excelReader.isSheetExist(variables.CONNECTION_CODE)) {
			connectPrompt = false;
			HashMap<String, ArrayList<ConnectionInfo>> connectionHash = excelReader.readConnection();
			System.out.println("Reading " + variables.CONNECTION_TYPE + " and " + variables.CONNECTION_CODE+ 
					"(讀取 " + variables.CONNECTION_TYPE + " 和 " + variables.CONNECTION_CODE+" )");
			excelWriter.setConnection(connectionHash);
		}

		// cnc
		LinkedHashMap <SpecSegmentation, Integer> predictionData = excelReader.readPrediction();
		HashMap<String, String> compToTexture = excelReader.componentToTexture();
		excelWriter.writeCNC(predictionData, compToTexture);
		
		if(connectPrompt) {
			System.out.println("Please generate "+ variables.CONNECTION_TYPE + " and " + 
				variables.CONNECTION_CODE + " sheet to continue(請提供 "+ variables.CONNECTION_TYPE + " 和 " + 
				variables.CONNECTION_CODE +" 以繼續工作)\n");
		}
		
		// write out
		excelWriter.save();
		System.out.println("CNCGenerator successes(CNC報表完成)\n");
	}

}
