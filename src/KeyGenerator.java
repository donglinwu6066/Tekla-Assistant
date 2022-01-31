import java.io.BufferedWriter;
import java.io.Console;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Paths;

import resources.Encryption;
import resources.Gatekeeper;
import resources.Utils;
import resources.variables;

public class KeyGenerator {
	public static void main(String argv[]) {
		String jarName = "";
		try {
			String path = KeyGenerator.class.getProtectionDomain().getCodeSource().getLocation().getPath();
			String decodedPath[] = URLDecoder.decode(path, "UTF-8").split("/");
			int lastToken = decodedPath.length -1;
			jarName = decodedPath[lastToken].substring(0, decodedPath[lastToken].lastIndexOf('.'));
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Console console = Utils.getTerminal(jarName);
		
		
        char[] passwordArray = console.readPassword("input(輸入): ");
        String password = new String(passwordArray);
        
        if(!variables.PASSWORD.equals(password)) {
        	boolean exitFlag = true;
        	
        	while(true) {
        		System.out.println("fail(錯誤)");
            	System.out.println("ctrl + c to exit(按control + c 鍵結束程式)\n");
            	char[] pwdTry = console.readPassword("input(輸入): ");
            	String pwd = new String(pwdTry);
            	if(variables.PASSWORD.equals(pwd)){
            		exitFlag = false;
            		break;
            	}
        	}
        	if(exitFlag){
        		System.exit(0);
        	}
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
	public void run (Console console) {
		if(console == null) {
			System.exit(1);
		}
		String jarName = "";
		try {
			String path = KeyGenerator.class.getProtectionDomain().getCodeSource().getLocation().getPath();
			String decodedPath[] = URLDecoder.decode(path, "UTF-8").split("/");
			int lastToken = decodedPath.length -1;
			jarName = decodedPath[lastToken].substring(0, decodedPath[lastToken].lastIndexOf('.'));
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
        char[] passwordArray = console.readPassword("input(輸入): ");
        String password = new String(passwordArray);
        
        if(!variables.PASSWORD.equals(password)) {
        	boolean exitFlag = true;
        	
        	while(true) {
        		System.out.println("fail(錯誤)");
            	System.out.println("ctrl + c to exit(按control + c 鍵結束程式)\n");
            	char[] pwdTry = console.readPassword("input(輸入): ");
            	String pwd = new String(pwdTry);
            	if(variables.PASSWORD.equals(pwd)){
            		exitFlag = false;
            		break;
            	}
        	}
        	if(exitFlag){
        		System.exit(1);
        	}
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
