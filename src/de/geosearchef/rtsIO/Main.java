package de.geosearchef.rtsIO;

import de.geosearchef.rtsIO.api.Api;
import de.geosearchef.rtsIO.websocket.WebSocket;

import static spark.Spark.*;

public class Main {

    public static final int PORT = 4567;

    public static void main(String args[]) {

        //Configure slf4j to use stdout instead of stderr, TODO: does not work
        System.setProperty("org.slf4j.simpleLogger.logFile", "System.out");

        port(PORT);
        //Set location of external, static data (html, js)
        staticFiles.externalLocation("public/");

        WebSocket.init();
        Api.init();

    }

}
