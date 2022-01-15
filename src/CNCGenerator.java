import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedHashMap;

import resources.CompSummarization;
import resources.Component;
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
		Utils.terminal("CNCGenerator");

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
		String pathStr = dateReader.toPath(variables.PROJECT_ROOT, variables.PROJECT_NAME, variables.PROJECT_NAME + ".xlsx");
		ExcelReader excelReader = new ExcelReader(pathStr);

		if(!excelReader.isSheetExist(variables.PREDICTION) | variables.OVERRIDE_DATABASE_PREDICTION) {
			System.out.println("Override DatabaseGenerator(重新覆蓋"+ variables.COMPONENTS_SPECIFICATION+"與"+variables.PREDICTION+")");
			DatabaseGenerator databaseGenerator = new DatabaseGenerator();
			databaseGenerator.run();
			excelReader.updateWorkbook();
		}
		else {
			System.out.println("Use original Database and Prediction(使用原本的"+ variables.COMPONENTS_SPECIFICATION+"與"+variables.PREDICTION+")");
		}
		LinkedHashMap <SpecSegmentation, Integer> predictionData = excelReader.readPrediction();
		ArrayList<CompSummarization> sumData = excelReader.getSummarization();
		ExcelWriter excelWriter = new ExcelWriter(pathStr);
		excelWriter.writeSum(sumData);
		//excelWriter.writeSummazation(predictionData);
		
		// ncl
		pathStr = dateReader.toFolder(variables.PROJECT_ROOT, variables.PROJECT_NAME, variables.NCL_ROOT);
		//System.out.println(pathStr);
		File nclFolder = new File(pathStr);
		ArrayList<Component> nclData = dateReader.ncl(nclFolder);
		Hashtable<String, String> hashToSpec = Utils.componentToSpecification(nclData);
		//ArrayList<CompSummarization> compSummarizationList = Utils.summarizePrediction(predictionData);
		// write out
		//ExcelWriter excelWriter = new ExcelWriter(pathStr);
		excelWriter.save();
		System.out.println("CNCGenerator successes(CNC報表完成)\n");
	}

}
