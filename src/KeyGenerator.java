import java.awt.GraphicsEnvironment;
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
import resources.variables;

public class KeyGenerator {
	public static void main(String argv[]) {
		Console console = System.console();
        if(console == null && !GraphicsEnvironment.isHeadless()){
            String filename = KeyGenerator.class.getProtectionDomain().getCodeSource().getLocation().toString().substring(6);
            try {
				Runtime.getRuntime().exec(new String[]{"cmd","/c","start","cmd","/k","java -jar \"" + filename + "\""});
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        
        char[] passwordArray = console.readPassword("input: ");
        String password = new String(passwordArray);
        
        if(!variables.PASSWORD.equals(password)) {
        	System.out.println("no");
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
		
        System.out.println("success");
	}
}
