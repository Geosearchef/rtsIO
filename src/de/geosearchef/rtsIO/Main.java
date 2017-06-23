package de.geosearchef.rtsIO;

import de.geosearchef.rtsIO.api.Api;
import de.geosearchef.rtsIO.game.Updater;
import de.geosearchef.rtsIO.js.JSCombiner;
import de.geosearchef.rtsIO.websocket.WebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

import static spark.Spark.*;

public class Main {

    public static boolean PRODUCTION = true;
    public static final int PORT = 4567;

    public static void main(String args[]) {
        if(args.length > 0 && Objects.equals(args[0], "debug"))
            PRODUCTION = false;

        //Configure slf4j to use stdout instead of stderr, TODO: does not work
        System.setProperty("org.slf4j.simpleLogger.logFile", "System.out");
        Logger logger = LoggerFactory.getLogger("rtsIO");
        if(!PRODUCTION)
            logger.info("DEBUG MODE ACTIVATED");


        port(PORT);
        //Set location of external, static data (html, js)
        staticFiles.externalLocation("public/");

        WebSocket.init();
        Api.init();

        //JS file is automatically combined server sided
        get("/play/gameScript.js", (req, res) -> JSCombiner.getCombinedJS());

        Updater.init();
    }
}
