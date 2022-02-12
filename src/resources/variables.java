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
    public static String CNC_TABLE_NAME;
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
    public static String SERIAL_NUMBER;
    public static String MATERIAL_LENGTH;
    public static String STRUCTURAL_MEMBER;
    public static String WIDTH;
    public static String FILE_STR;
    public static String GIRDER_HEIGHT;
    public static String PRO;
    public static String GIRDER_THICKNESS;
    public static String NO;
    public static String STEP;
    public static String POSITION;
    public static String LEFT_GIRDER;
    public static String WEB_GIRDER;
    public static String RIGHT_GIRDER;
    public static String MATERIAL_COUNT;
    public static String CHECK;
    public static String USE_LENGTH;
    public static String TABULATOR;
    public static String AUDITOR;
    public static String TABULATING_DATE;
    public static String RELEASING_DATE;
    public static String CONNECTION_TYPE;
    public static String CONNECTION_CODE;
    static {
        // program define variables
        PASSWORD = "atp29563t9906";
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
            CNC_TABLE_NAME = (String) variablesObject.get("CNC_TABLE_NAME");
            SERIAL_NUMBER = (String) variablesObject.get("SERIAL_NUMBER");
            MATERIAL_LENGTH = (String) variablesObject.get("MATERIAL_LENGTH");
            STRUCTURAL_MEMBER = (String) variablesObject.get("STRUCTURAL_MEMBER");
            WIDTH = (String) variablesObject.get("WIDTH");
            FILE_STR = (String) variablesObject.get("FILE_STR");
            GIRDER_HEIGHT = (String) variablesObject.get("GIRDER_HEIGHT");
            PRO = (String) variablesObject.get("PRO");
            GIRDER_THICKNESS = (String) variablesObject.get("GIRDER_THICKNESS");
            NO = (String) variablesObject.get("NO");
            STEP = (String) variablesObject.get("STEP");
            POSITION = (String) variablesObject.get("POSITION");
            LEFT_GIRDER = (String) variablesObject.get("LEFT_GIRDER");
            WEB_GIRDER = (String) variablesObject.get("WEB_GIRDER");
            RIGHT_GIRDER = (String) variablesObject.get("RIGHT_GIRDER");
            MATERIAL_COUNT = (String) variablesObject.get("MATERIAL_COUNT");
            CHECK = (String) variablesObject.get("CHECK");
            USE_LENGTH = (String) variablesObject.get("USE_LENGTH");
            TABULATOR = (String) variablesObject.get("TABULATOR");
            AUDITOR = (String) variablesObject.get("AUDITOR");
            TABULATING_DATE = (String) variablesObject.get("TABULATING_DATE");
            RELEASING_DATE = (String) variablesObject.get("RELEASING_DATE");
            CONNECTION_TYPE = (String) variablesObject.get("CONNECTION_TYPE");
            CONNECTION_CODE = (String) variablesObject.get("CONNECTION_CODE");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
