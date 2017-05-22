package api;

import static spark.Spark.*;

/**
 * Created by Geosearchef on 22.05.2017.
 */
public class Api {



    public static void init() {

        get("/hello", (req, res) -> "Hello World!");

    }
}
