package de.geosearchef.rtsIO.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Geosearchef on 23.05.2017.
 */
public class SpamPrevention {

    private static final long SPAM_CACHE_CLEAR_INTERVAL = 300 * 1000;//5 minutes
    private static final long MAX_REQUESTS_PER_INTERVAL = 1000;//TODO

    private static final Logger logger = LoggerFactory.getLogger(SpamPrevention.class);

    private static Map<String, Integer> attempts = new HashMap<String, Integer>();

    /**
     * used for preventing spam, counts IP on call
     * @return false if the user sent more then MAX_REQUESTS_PER_INTERVAL since the last chache clear, else false
     */
    public static boolean isValid(String ip) {
        synchronized (attempts) {
            int old = attempts.containsKey(ip) ? attempts.get(ip) : 0;
            attempts.put(ip, old + 1);
            return old < MAX_REQUESTS_PER_INTERVAL;
        }
    }

    static {
        //Thread for clearing cache every SPAM_CACHE_CLEAR_INTERVAL ms
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(SPAM_CACHE_CLEAR_INTERVAL);
                } catch (InterruptedException e) {
                    logger.error("Cache clear thread crashed!", e);
                    System.exit(-1);
                }

                synchronized (attempts) {
                    attempts.clear();
                }
            }
        }).start();
    }

}
