package de.geosearchef.rtsIO.json;

import lombok.Data;

/**
 * Created by Geosearchef on 23.05.2017.
 */

@Data
public class LoginSuccessMessage extends Message {
    private final String type = "loginSuccess";
    private final String username;
    private final int id;
}
