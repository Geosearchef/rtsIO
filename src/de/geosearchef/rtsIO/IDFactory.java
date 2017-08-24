package de.geosearchef.rtsIO;

public class IDFactory {
    private static int currentPlayerID;
    private static int currentUnitID;
    private static int currentGemID;
    private static int currentBuildingID;


    public static synchronized int generatePlayerID() {
        return currentPlayerID++;
    }
    public static synchronized int generateUnitID() {
        return currentUnitID++;
    }
    public static synchronized int generateGemID() {
        return currentGemID++;
    }
    public static synchronized int generateBuildingID() {return currentBuildingID++;}
}