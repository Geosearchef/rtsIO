package de.geosearchef.rtsIO.js;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Data {

    public static final String unitDataFile = "public/play/game/unitData.js";
    public static final String buildingDataFile = "public/play/game/buildingData.js";

    public static final Logger logger = LoggerFactory.getLogger(Data.class);

    public static JSONArray unitData;
    public static JSONArray buildingData;

    public static void init() {
        try {
            unitData = readFile(new File(unitDataFile));
            buildingData = readFile(new File(buildingDataFile));
        } catch(Exception e) {
            logger.error("Could not read unit/building data from JS.", e);
            System.exit(10);
        }
    }

    private static JSONArray readFile(File file) throws FileNotFoundException, ParseException {
        Scanner scan = new Scanner(file);
        StringBuilder json = new StringBuilder("{\"data\": ");

        json.append(scan.nextLine().split(" = ")[1]);
        while(scan.hasNextLine()) {
            json.append(scan.nextLine());
            json.append(" ");
        }
        json.append("}");
        return (JSONArray) ((JSONObject)new JSONParser().parse(json.toString().replace(";", ""))).get("data");
    }

    public static JSONObject getUnitData(int unitType) {
        return (JSONObject) unitData.get(unitType);
    }
    public static JSONObject getBuildingData(int buildingType) {
        return (JSONObject) buildingData.get(buildingType);
    }
}
