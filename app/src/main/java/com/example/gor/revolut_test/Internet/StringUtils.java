package com.example.gor.revolut_test.Internet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Gor on 25.07.2017.
 */

class StringUtils {
    public static String readInputStream(InputStream is) throws IOException {
        BufferedReader reader = null;
        String resultJson = "";

        StringBuffer buffer = new StringBuffer();

        reader = new BufferedReader(new InputStreamReader(is));

        String line;
        while ((line = reader.readLine()) != null) {
            buffer.append(line);
        }

        resultJson = buffer.toString();
        return resultJson;

    }
}
