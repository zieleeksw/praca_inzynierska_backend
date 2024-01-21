package com.example.praza_inzynierska;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {

    private Utils() {
        throw new IllegalStateException("Utils class");
    }

    public static String getTimeStamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date());
    }
}
