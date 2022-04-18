import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;

import resources.Component;
import resources.DataReader;
import resources.ExcelWriter;
import resources.Gatekeeper;
import resources.Pair;
import resources.SpecSegmentation;
import resources.Utils;
import resources.variables;

public class DatabaseGenerator {
	public static void main(String argv[]) {
		Utils.terminal("DatabaseGenerator");

		DataReader dateReader = new DataReader();
		// required password
		if (variables.REQUIRED_PASSWORD) {
			Gatekeeper gatekeeper = new Gatekeeper();
			Pair<String, String> locker = dateReader.getLocker();
			// check host name and uuid
			if (!gatekeeper.isPassed(locker.getFirst(), locker.getSecond())) {
				System.out.println("Fail to passed verification(驗證失敗)");
				return;
			}
		}
		// ncl
		String pathStr = dateReader.toFolder(variables.PROJECT_ROOT, variables.PROJECT_NAME, variables.NCL_ROOT);
		System.out.println(pathStr);
		File nclFolder = new File(pathStr);
		ArrayList<Component> nclData = dateReader.ncl(nclFolder);

		pathStr = dateReader.toPath(variables.PROJECT_ROOT, variables.PROJECT_NAME, variables.PROJECT_NAME + ".xlsx");
		ExcelWriter excelWriter = new ExcelWriter(pathStr);
		excelWriter.writeNCL(nclData);

		// sym
		pathStr = dateReader.toFolder(variables.PROJECT_ROOT, variables.PROJECT_NAME, variables.SYM_ROOT);
		File symFolder = new File(pathStr);
		Hashtable<String, String> hashToSpec = Utils.componentToSpecification(nclData);
		Hashtable<String, Float> hashToLength = Utils.componentToLength(nclData);

		ArrayList<SpecSegmentation> symData = dateReader.sym(symFolder, hashToSpec, hashToLength);
		excelWriter.writeSYM(symData);

		excelWriter.save();
		System.out.println("DatabaseGenerator successes\n");

	}

	public void run() {
		DataReader dateReader = new DataReader();
		// ncl
		String pathStr = dateReader.toFolder(variables.PROJECT_ROOT, variables.PROJECT_NAME, variables.NCL_ROOT);
		System.out.println(pathStr);
		File nclFolder = new File(pathStr);
		ArrayList<Component> nclData = dateReader.ncl(nclFolder);

		pathStr = dateReader.toPath(variables.PROJECT_ROOT, variables.PROJECT_NAME, variables.PROJECT_NAME + ".xlsx");
		ExcelWriter excelWriter = new ExcelWriter(pathStr);
		excelWriter.writeNCL(nclData);

		// sym
		pathStr = dateReader.toFolder(variables.PROJECT_ROOT, variables.PROJECT_NAME, variables.SYM_ROOT);
		File symFolder = new File(pathStr);
		Hashtable<String, String> hashToSpec = Utils.componentToSpecification(nclData);
		Hashtable<String, Float> hashToLength = Utils.componentToLength(nclData);

		ArrayList<SpecSegmentation> symData = dateReader.sym(symFolder, hashToSpec, hashToLength);
		excelWriter.writeSYM(symData);

		excelWriter.save();
		System.out.println("DatabaseGenerator successes(" + variables.COMPONENTS_SPECIFICATION + "與"
				+ variables.PREDICTION + "報表完成)\n");
	}
	


}
