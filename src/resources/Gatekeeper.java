package resources;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.lang.Runtime;

public class Gatekeeper {
	private String hostName;
	private String uuid;

	public Gatekeeper(){
		// get host name
		try {
			InetAddress ip = InetAddress.getLocalHost();
			this.hostName = ip.getHostName();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// get uuid, windows only
		String OS = System.getProperty("os.name").toLowerCase();
	    if (OS.indexOf("win") >= 0) {
	        StringBuffer output = new StringBuffer();
	        Process process;
	        String[] cmd = {"wmic", "csproduct", "get", "UUID"};  
	        try {
	            process = Runtime.getRuntime().exec(cmd);
	            process.waitFor();
	            BufferedReader reader = new BufferedReader
	            		(new InputStreamReader(process.getInputStream()));
	            String line = "";
	            while ((line = reader.readLine()) != null) {
	                output.append(line + "\n");
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        this.uuid = output.toString().substring(4).replace("\n", "")
	        		.replace(" ", "");
	    }
	}
	
	public boolean isPassed(String hostName, String uuid) {
		return (this.hostName.equals(hostName) && this.uuid.equals(uuid));
	}
	public String getHostName() {
		return hostName;
	}
	public String getuuid() {
		return uuid;
	}
	
}
