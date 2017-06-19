package de.geosearchef.rtsIO.js;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class JSCombiner {

    public static final String[] files = {"view.js"};

    //TODO: cache in production
    public static String getCombinedJS() throws FileNotFoundException {
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

}
