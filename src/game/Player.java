package game;

import lombok.Getter;

/**
 * Created by Geosearchef on 23.05.2017.
 */
public class Player {

    private static final int CONNECT_TIME_OUT = 10000;//name reserve time

    @Getter
    private long loginTime;

    public Player() {
        loginTime = System.currentTimeMillis();
    }

}
