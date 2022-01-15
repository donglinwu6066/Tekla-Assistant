package resources;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public final class variables {
    public static String COMPANY_NAME;
    public static String PROJECT_NAME;
    public static String PASSWORD;
    public static boolean REQUIRED_PASSWORD;
    public static boolean OVERRIDE_DATABASE_PREDICTION;
    public static String KEY_FILE_NAME;
    public static String PROJECT_ROOT;
    public static String NCL_ROOT;
    public static String SYM_ROOT;
    public static String COMPONENTS;
    public static String MATERIAL;
    public static String COMPONENTS_SPECIFICATION;
    public static String LENGTH;
    public static String COUNT;
    public static String SPECIFICATION;
    public static String TEXTURE;
    public static String PREDICTION;
    public static String SPECIFICATION_LENGTH;
    public static String REMAINING;
    public static String WEIGHT;
    public static String SUMMARIZATION;
    static {
        // program define variables
        PASSWORD = "password";
        REQUIRED_PASSWORD = true;

        // JSON parser object to parse read file
        JSONParser jsonParser = new JSONParser();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream("variables.json"), "UTF-8"))) {
            // Read JSON file
            Object obj = jsonParser.parse(reader);
            JSONObject variablesObject = (JSONObject) obj;
            COMPANY_NAME = (String) variablesObject.get("COMPANY_NAME");
            PROJECT_NAME = (String) variablesObject.get("PROJECT_NAME");
            KEY_FILE_NAME = (String) variablesObject.get("KEY_FILE_NAME");
            OVERRIDE_DATABASE_PREDICTION = (boolean) variablesObject.get("OVERRIDE_DATABASE_PREDICTION");
            PROJECT_ROOT = (String) variablesObject.get("PROJECT_ROOT");
            NCL_ROOT = (String) variablesObject.get("NCL_ROOT");
            SYM_ROOT = (String) variablesObject.get("SYM_ROOT");
            COMPONENTS = (String) variablesObject.get("COMPONENTS");
            MATERIAL = (String) variablesObject.get("MATERIAL");
            COMPONENTS_SPECIFICATION = (String) variablesObject.get("COMPONENTS_SPECIFICATION");
            LENGTH = (String) variablesObject.get("LENGTH");
            COUNT = (String) variablesObject.get("COUNT");
            SPECIFICATION = (String) variablesObject.get("SPECIFICATION");
            TEXTURE = (String) variablesObject.get("TEXTURE");
            PREDICTION = (String) variablesObject.get("PREDICTION");
            SPECIFICATION_LENGTH = (String) variablesObject.get("SPECIFICATION_LENGTH");
            REMAINING = (String) variablesObject.get("REMAINING");
            WEIGHT = (String) variablesObject.get("WEIGHT");
            SUMMARIZATION = (String) variablesObject.get("SUMMARIZATION");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
