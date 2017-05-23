import api.Api;

import static spark.Spark.*;

/**
 * Created by Geosearchef on 22.05.2017.
 */
public class Main {

    public static void main(String args[]) {

        staticFiles.externalLocation("public/");

        Api.init();

    }

}
