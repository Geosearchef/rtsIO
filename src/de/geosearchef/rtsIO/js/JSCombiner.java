package de.geosearchef.rtsIO.js;

import de.geosearchef.rtsIO.Main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class JSCombiner {

    public static final String[] files = {"controller.js", "view.js", "websocket.js", "input.js", "util/vector.js"};

    private static String combineJS() throws FileNotFoundException {
        StringBuilder s = new StringBuilder();
        for(String fileName : files) {
            Scanner scan = new Scanner(new File("public/play/" + fileName));
            while(scan.hasNext()) {
                s.append(scan.nextLine() + "\n");
            }
            scan.close();
        }
        return s.toString();
    }

    public static String cached = null;
    public static String getCombinedJS() throws FileNotFoundException {
        if(!Main.PRODUCTION || cached == null)
            cached = combineJS();
        return cached;
    }
}
