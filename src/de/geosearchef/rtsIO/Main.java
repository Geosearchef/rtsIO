package de.geosearchef.rtsIO;

import de.geosearchef.rtsIO.api.Api;
import websocket.WebSocket;

import static spark.Spark.*;

public class Main {

    public static void main(String args[]) {

        staticFiles.externalLocation("public/");

        WebSocket.init();
        Api.init();

    }

}
