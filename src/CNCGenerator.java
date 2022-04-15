import java.io.Console;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
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

		DataReader dateReader = new DataReader();
		// required password
		if (variables.REQUIRED_PASSWORD) {
			File datFile = new File(variables.KEY_FILE_NAME);
			if (!datFile.exists()) {
				KeyGenerator keyGen = new KeyGenerator();
				keyGen.run(console);
			}
			Gatekeeper gatekeeper = new Gatekeeper();
			Pair<String, String> locker = dateReader.getLocker();
			// check host name and uuid

			if (!gatekeeper.isPassed(locker.getFirst(), locker.getSecond())) {
				System.out.println("Fail to passed verification(驗證失敗)");
				return;
			}
		}

		// read spec
		String pathStr = dateReader.toPath(
				variables.PROJECT_ROOT, variables.PROJECT_NAME, variables.PROJECT_NAME + ".xlsx");
		ExcelReader excelReader = new ExcelReader(pathStr);

		boolean old_wb = true;
		// override old database
		if (!excelReader.isSheetExist(variables.PREDICTION) |
				variables.OVERRIDE_DATABASE_PREDICTION) {
			System.out.println("Override DatabaseGenerator(重新覆蓋 " +
					variables.COMPONENTS_SPECIFICATION + " 與 " + variables.PREDICTION + " )");
			DatabaseGenerator databaseGenerator = new DatabaseGenerator();
			databaseGenerator.run();
			excelReader.updateWorkbook();
			old_wb = false;
		} else {
			System.out.println("Use original Database and Prediction(使用原本的 " +
					variables.COMPONENTS_SPECIFICATION + " 與 " + variables.PREDICTION + " )");
		}

		ExcelWriter excelWriter = new ExcelWriter(pathStr);
		if (old_wb && variables.UPDATE_DATABASE_PREDICTION && !variables.OVERRIDE_DATABASE_PREDICTION) {
			System.out.println("Update DatabaseGenerator with new .ncl(以新.ncl更新 " + variables.PREDICTION + " )");
			DataReader dataReader = new DataReader();
			// ncl
			String nclPath = dataReader.toFolder(variables.PROJECT_ROOT, variables.PROJECT_NAME, variables.NCL_ROOT);
			System.out.println(pathStr);
			File nclFolder = new File(nclPath);
			ArrayList<Component> nclData = dataReader.ncl(nclFolder);

			excelWriter.updateNCL(nclData);
			excelReader.updateWorkbook();
			System.out.println("DatabaseGenerator updated(" + "更新" + variables.PREDICTION + "完成)\n");
		}
		// sum
		ArrayList<CompSummarization> sumData = excelReader.getSummarization();
		excelWriter.writeSum(sumData);

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

		if (connectPrompt) {
			System.out.println("Please generate " + variables.CONNECTION_TYPE + " and " +
					variables.CONNECTION_CODE + " sheet to continue(請提供 " + variables.CONNECTION_TYPE + " 和 " +
					variables.CONNECTION_CODE + " 以繼續工作)\n");
		}

		// write out
		excelWriter.save();
		System.out.println("CNCGenerator successes(CNC報表完成)\n");
	}

}
