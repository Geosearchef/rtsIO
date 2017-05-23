package de.geosearchef.rtsIO.json;

import com.google.gson.Gson;

/**
 * Created by Geosearchef on 23.05.2017.
 */
public abstract class Message {
    public static final Gson gson = new Gson();

    @Override
    public String toString() {
        return gson.toJson(this);
    }

    public String toJson() {
        return gson.toJson(this);
    }
}
