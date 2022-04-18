import java.io.Console;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedHashMap;

import resources.CompSummarization;
import resources.Component;
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
		Console console = null;
		try {
			String path = CNCGenerator.class.getProtectionDomain().getCodeSource().getLocation().getPath();
			String decodedPath[] = URLDecoder.decode(path, "UTF-8").split("/");
			int lastToken = decodedPath.length - 1;
			String jarName = decodedPath[lastToken].substring(0, decodedPath[lastToken].lastIndexOf('.'));
			console = Utils.getTerminal(jarName);
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			System.exit(1);
		}

		DataReader dataReader = new DataReader();
		// required password
		if (variables.REQUIRED_PASSWORD) {
			File datFile = new File(variables.KEY_FILE_NAME);
			if (!datFile.exists()) {
				KeyGenerator keyGen = new KeyGenerator();
				keyGen.run(console);
			}
			Gatekeeper gatekeeper = new Gatekeeper();
			Pair<String, String> locker = dataReader.getLocker();
			// check host name and uuid

			if (!gatekeeper.isPassed(locker.getFirst(), locker.getSecond())) {
				System.out.println("Fail to passed verification(驗證失敗)");
				return;
			}
		}
		
		// read spec
		String pathStr = dataReader.toPath(
				variables.PROJECT_ROOT, variables.PROJECT_NAME, variables.PROJECT_NAME + ".xlsx");
		ExcelReader excelReader = new ExcelReader(pathStr);
		ExcelWriter excelWriter = new ExcelWriter(pathStr);
		
		// show running mode
		if(!excelReader.isSheetExist(variables.COMPONENTS_SPECIFICATION) | !excelReader.isSheetExist(variables.PREDICTION)) {
			System.out.println("Running in creating mode(新檔模式)\n");
		}
		else if(variables.OVERRIDE_DATABASE_PREDICTION) {
			excelWriter.saveOld();
			System.out.println("Running in override mode(覆蓋模式)\n");
		}
		else if(variables.UPDATE_DATABASE_PREDICTION) {
			excelWriter.saveOld();
			System.out.println("Running in update mode(更新模式)\n");
		}
		else {
			excelWriter.saveOld();
			System.out.println("Running in CNC mode(cnc報表模式)\n");
		}
		
		// components specification
		ArrayList<Component> nclData =  null;
		if(!excelReader.isSheetExist(variables.COMPONENTS_SPECIFICATION) |
				variables.OVERRIDE_DATABASE_PREDICTION |
				variables.UPDATE_DATABASE_PREDICTION) {
			String nclPath = dataReader.toFolder(variables.PROJECT_ROOT, variables.PROJECT_NAME, variables.NCL_ROOT);
			File nclFolder = new File(nclPath);
			
			nclData = dataReader.ncl(nclFolder);
			excelWriter.writeNCL(nclData);
			System.out.println("Components specification successes("+variables.COMPONENTS_SPECIFICATION+"完成)\n");
		}
		
		// prediction
		if (!excelReader.isSheetExist(variables.PREDICTION) |
				variables.OVERRIDE_DATABASE_PREDICTION) {
			String symPath = dataReader.toFolder(variables.PROJECT_ROOT, variables.PROJECT_NAME, variables.SYM_ROOT);
			File symFolder = new File(symPath);
			
			Hashtable<String, String> hashToSpec = Utils.componentToSpecification(nclData);
			Hashtable<String, Float> hashToLength = Utils.componentToLength(nclData);
			ArrayList<SpecSegmentation> symData = dataReader.sym(symFolder, hashToSpec, hashToLength);
			excelWriter.writeSYM(symData);
			System.out.println("Components specification successes("+variables.PREDICTION+"完成)\n");
		}
		else if(variables.UPDATE_DATABASE_PREDICTION) {
			excelWriter.updateNCL(nclData);
			System.out.println("Components specification and prediction are updated("+variables.COMPONENTS_SPECIFICATION+", "+variables.PREDICTION+" 更新)\n");
		}
		excelReader.updateWorkbook();
		
		// sum
		ArrayList<CompSummarization> sumData = excelReader.getSummarization();
		excelWriter.writeSum(sumData);
		System.out.println("Summarization successes("+variables.SUMMARIZATION+"完成)\n");

		// connection
		boolean connectPrompt = true;
		if (excelReader.isSheetExist(variables.CONNECTION_TYPE) &&
				excelReader.isSheetExist(variables.CONNECTION_CODE)) {
			connectPrompt = false;
			HashMap<String, ArrayList<ConnectionInfo>> connectionHash = excelReader.readConnection();
			System.out.println("Reading " + variables.CONNECTION_TYPE + " and " + variables.CONNECTION_CODE +
					"(讀取 " + variables.CONNECTION_TYPE + " 和 " + variables.CONNECTION_CODE + " )");
			excelWriter.setConnection(connectionHash);
		}

		// cnc
		LinkedHashMap<SpecSegmentation, Integer> predictionData = excelReader.readPrediction();
		HashMap<String, String> compToTexture = excelReader.componentToTexture();
		excelWriter.writeCNC(predictionData, compToTexture);
		System.out.println("CNC successes(CNC 完成)\n");
		
		if (connectPrompt) {
			System.out.println("Please generate " + variables.CONNECTION_TYPE + " and " +
					variables.CONNECTION_CODE + " sheet to continue(請提供 " + variables.CONNECTION_TYPE + " 和 " +
					variables.CONNECTION_CODE + " 以繼續工作)\n");
		}

		// write out
		excelReader.close();
		excelWriter.save();
		System.out.println("CNCGenerator successes(CNC報表完成)\n");
	}

}
