package com.marinovskiy.ekreativetestproject.managers;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;

import java.util.concurrent.TimeUnit;

public class Utils {

    public static boolean hasInternet(@NonNull Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public static String getDuration(String youtubeDuration) {
        String time = youtubeDuration.substring(2);
        long longDuration = 0L;
        Object[][] indexes = new Object[][]{{"H", 3600}, {"M", 60}, {"S", 1}};
        for (Object[] indexObj : indexes) {
            int index = time.indexOf((String) indexObj[0]);
            if (index != -1) {
                String value = time.substring(0, index);
                longDuration += Integer.parseInt(value) * (int) indexObj[1] * 1000;
                time = time.substring(value.length() + 1);
            }
        }
        String duration = "";
        long hours = TimeUnit.MILLISECONDS.toHours(longDuration);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(longDuration) -
                TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(longDuration));
        long seconds = TimeUnit.MILLISECONDS.toSeconds(longDuration) -
                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(longDuration));
        if (hours != 0) {
            duration = duration + TimeUnit.MILLISECONDS.toHours(longDuration) + ":";
        }
        if (seconds <= 9) {
            duration = duration + minutes + ":0" + seconds;
        } else {
            duration = duration + minutes + ":" + seconds;
        }
        return duration;
    }

}