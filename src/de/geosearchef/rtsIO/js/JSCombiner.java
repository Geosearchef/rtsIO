package de.geosearchef.rtsIO.js;

import de.geosearchef.rtsIO.Main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class JSCombiner {

    public static final String[] files = {"controller.js",
            "view.js",
            "websocket.js",
            "input.js",
            "util/vector.js",
            "game/data.js",
            "game/unitData.js",
            "game/buildingData.js",
            "game/projectileData.js",
            "rendering/renderGui.js",
            "rendering/renderUnits.js",
            "rendering/renderBuildings.js",
            "rendering/renderMap.js",
            "rendering/renderGems.js",
            "gui/button.js",
            "util/latency.js",
            "rendering/renderProjectiles.js"
    };

    private static String combineJS() throws FileNotFoundException {
        StringBuilder s = new StringBuilder();
        StringBuilder consts = new StringBuilder();
        for(String fileName : files) {
            Scanner scan = new Scanner(new File("public/play/" + fileName));
            while(scan.hasNext()) {
                String nextLine = scan.nextLine();
                if(nextLine.startsWith("const"))
                    consts.append(nextLine.split("////")[0] + "\n");
                else
                    s.append(nextLine.split("////")[0] + "\n");
            }
            scan.close();
        }

        return consts.toString() + "\n" + s.toString();
    }

    public static String cached = null;
    public static String getCombinedJS() throws FileNotFoundException {
        if(!Main.PRODUCTION || cached == null)
            cached = combineJS();
        return cached;
    }
}
