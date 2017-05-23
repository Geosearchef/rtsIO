package de.geosearchef.rtsIO.api;

import de.geosearchef.rtsIO.game.Game;

import static spark.Spark.*;

public class Api {



    public static void init() {

        //Spam prevention via before?

        get("/start", (req, res) -> {
            String username = req.queryParams("username");
            if(username != null && !username.equals("") && !Game.isUsernameInUse(username)) {
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
