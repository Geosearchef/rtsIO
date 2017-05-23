package de.geosearchef.rtsIO;

public class IDFactory {
    private static int currentPlayerID;


    public static synchronized int generatePlayerID() {
        return currentPlayerID++;
    }

}