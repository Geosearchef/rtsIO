package de.geosearchef.rtsIO;

import de.geosearchef.rtsIO.api.Api;
import websocket.WebSocket;

import static spark.Spark.*;

public class Main {

    public static final int PORT = 4567;

    public static void main(String args[]) {

        System.setProperty("org.slf4j.simpleLogger.logFile", "System.out");

        port(PORT);
        staticFiles.externalLocation("public/");

        WebSocket.init();
        Api.init();

    }

}
