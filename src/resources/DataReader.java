package resources;

import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;

public class DataReader {
	
	public DataReader() {
		super();
	}
	public ArrayList<Component> ncl(final File folder) {
		ArrayList<File> fileArr = getAllFileInFolder(folder);
		ArrayList<Component> componentList = new ArrayList<Component>();
		
		String component;
		int material;
		float length;
		int count;
		String specification;
		String texture;
		System.out.println("Reading ncl(讀取.ncl)");
		for(final File f : fileArr) {
			//System.out.println(f);
			List<String> lineList;
			try {
				lineList = Files.readAllLines(Paths.get(f.toString()));
				component = lineList.get(4).trim();
				material = Integer.parseInt(component.trim().split("M")[0]);
				length = Float.parseFloat(lineList.get(10).trim());
				count = Integer.parseInt(lineList.get(7).trim());
				specification = lineList.get(8).trim();
				texture = lineList.get(6).trim();

				componentList.add(new Component(component, material, length, count, specification, texture));
				
				//System.out.println(componentList.get(componentList.size() - 1));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		Collections.sort(componentList, new ComponentSort()); 
		return componentList;
	}
	
	public ArrayList<SpecSegmentation> sym(
			final File folder, Hashtable<String, String> hashToSpec, Hashtable<String, Float> hashToLength) {
		ArrayList<File> fileArr = getAllFileInFolder(folder);
		ArrayList<SpecSegmentation> specSegmentationList = new ArrayList<SpecSegmentation>();
		
		String specification = "";
		System.out.println("Reading sym(讀取.sym)");
		
		for(final File f : fileArr) {
			ArrayList<SpecSegmentation> tempList = new ArrayList<SpecSegmentation>();
			try {
				
				BufferedReader br = new BufferedReader(new FileReader(f));
				String line = "";
				// get specification
				while((line = br.readLine())!= null) {
					if(line.charAt(0) == '(') {
						String []tokens = line.split(" ");
						specification = hashToSpec.get(tokens[0].split("M")[0].substring(1));
						br = new BufferedReader(new FileReader(f));
						break;
					}
				}
				
				SpecSegmentation item = null;
				while((line = br.readLine()) != null) {
					String []tokens = line.split(" ");
					if(tokens[0].equals("Encl") && item != null) {
						tempList.add(item); 
						item = null;
					}
					else if(tokens[0].equals("Stock")) {
						item = new SpecSegmentation();
						item.setSpecification(specification);
						item.setLength(Float.parseFloat(tokens[4].substring(0, tokens[4].length() - 1)));
						item.setCount(Integer.parseInt(tokens[6]));
					}
					else if(tokens[0].charAt(0) == '(' ) {
						String componentStr = tokens[0].split("-")[0].substring(1);
						item.addComponent(new Pair<String, Float>(componentStr, Float.valueOf(hashToLength.get(componentStr))));
					}
				}
				br.close();
				if(item != null) {
					tempList.add(item);
					item = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			specSegmentationList.addAll(tempList);
		}
		
		
		return specSegmentationList;
	}
	
	public String toPath(final String... dir) {
		String pathStr = ".\\";
	    for (String i : dir) 
	    {
	    	pathStr += i + "\\";
	    }
	    pathStr = pathStr.substring(0, pathStr.length()-1);
		return pathStr;
	}
	public String toFolder(final String... dir) {
		String pathStr = ".\\";
	    for (String i : dir) 
	    {
	    	pathStr += i + "\\";
	    }
		return pathStr;
	}
	
	public ArrayList<File> getAllFileInFolder(final File folder) {
		ArrayList<File> fileArr = new ArrayList<File>();
	    for (final File fileEntry : folder.listFiles()) {
	        if (fileEntry.isDirectory()) {
	        	fileArr.addAll(getAllFileInFolder(fileEntry));
	        } else {
	        	fileArr.add(fileEntry);
	        }
	    }
	    return fileArr;
	}
	public Pair<String, String> getLocker() {
		String hostName = "";
		String uuid = "";
		
		try(BufferedReader br = new BufferedReader(new FileReader(variables.KEY_FILE_NAME))) {
			hostName = Encryption.decode(br.readLine());
			uuid = Encryption.decode(br.readLine());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return new Pair<String, String>(hostName, uuid);
	}
}
