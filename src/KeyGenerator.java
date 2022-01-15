import java.io.BufferedWriter;
import java.io.Console;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import resources.Encryption;
import resources.Gatekeeper;
import resources.Utils;
import resources.variables;

public class KeyGenerator {
	public static void main(String argv[]) {
		Console console = Utils.getTerminal("KeyGenerator");
		
        char[] passwordArray = console.readPassword("input(輸入): ");
        String password = new String(passwordArray);
        
        if(!variables.PASSWORD.equals(password)) {
        	System.out.println("no(錯誤)");
        	System.exit(0);
        }
		try {
			Files.deleteIfExists(Paths.get(variables.KEY_FILE_NAME));
			File outputFile = new File(variables.KEY_FILE_NAME);
			outputFile.createNewFile();
			// Creating binary file
	        FileOutputStream fout=new FileOutputStream(outputFile);
	        BufferedWriter writer = new BufferedWriter(new FileWriter(variables.KEY_FILE_NAME));
	        Gatekeeper gatekeeper = new Gatekeeper();
	        
	        writer.write(Encryption.encode(gatekeeper.getHostName())+'\n');
	        writer.write(Encryption.encode(gatekeeper.getuuid())+'\n');
	        writer.close();
	        
	        fout.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
        System.out.println("KeyGenerator successes(KeyGenerator完成)\n");
	}
}
