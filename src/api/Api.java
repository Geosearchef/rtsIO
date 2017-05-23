package api;

import game.Game;

import static spark.Spark.*;

/**
 * Created by Geosearchef on 22.05.2017.
 */
public class Api {



    public static void init() {

        //Spam prevention via before?

        get("/start", (req, res) -> {
            String username = req.queryParams("username");
            if(username != null && !username.equals("")) {
                if(SpamPrevention.isValid(req.ip())) {
                    Game.logon(username);
                    res.redirect("play/play.html");
                } else {
                    return "Please stop spamming!";
                }
            } else {
                res.redirect("/");
            }
            return null;
        });


    }
}
