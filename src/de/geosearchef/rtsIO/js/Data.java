package de.geosearchef.rtsIO.js;

import com.google.gson.Gson;
import de.geosearchef.rtsIO.game.Unit;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Data {

    public static final String unitDataFile = "public/play/game/unitData.js";
    public static final String buildingDataFile = "public/play/game/buildingData.js";

    public static final Logger logger = LoggerFactory.getLogger(Data.class);

    public static List<UnitData> unitData = new ArrayList<UnitData>();
    public static List<BuildingData> buildingData = new ArrayList<BuildingData>();

    public static void init() {
        try {
            //TODO: JsonBuilder.create() can read json arrays directily to array
            JSONArray unitDataSimple = readFile(new File(unitDataFile));
            JSONArray buildingDataSimple = readFile(new File(buildingDataFile));

            unitDataSimple.stream().forEachOrdered(o -> unitData.add((UnitData) new Gson().fromJson(((JSONObject)o).toJSONString(), UnitData.class)));
            buildingDataSimple.stream().forEachOrdered(o -> buildingData.add((BuildingData) new Gson().fromJson(((JSONObject)o).toJSONString(), BuildingData.class)));

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

    public static UnitData getUnitData(int unitType) {
        return unitData.get(unitType);
    }
    public static BuildingData getBuildingData(int buildingType) {
        return buildingData.get(buildingType);
    }

    public static boolean hasUnitData(int unitType) {
        return unitType < unitData.size();
    }

    public static boolean hasBuildingData(int buildingType) {
        return buildingType < buildingData.size();
    }
}
