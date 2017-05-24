package de.geosearchef.rtsIO.json;

import com.google.gson.Gson;

/**
 * super class for all messages
 * is automatically serialized/converted to JSON via reflection based on field names
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
