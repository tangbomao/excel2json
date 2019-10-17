package dev.tang.tool.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtils {

    /**
     * Get current time
     */
    public static String getCurrentTime() {
        long now = System.currentTimeMillis();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(new Date(now));
    }
}
