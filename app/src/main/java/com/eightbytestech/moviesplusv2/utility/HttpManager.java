package com.eightbytestech.moviesplusv2.utility;

import java.io.IOException;

/**
 * Created by vishalsaxena on 23/03/17.
 */

public class HttpManager {
    private static final String DEBUG_TAG = "HttpManager";

    public static boolean checkConnection() {
        /*
        a la http://stackoverflow.com/a/27312494/3818437
         */
        Runtime runtime = Runtime.getRuntime();
        try {

            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int exitValue = ipProcess.waitFor();
            return (exitValue == 0);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }
}
