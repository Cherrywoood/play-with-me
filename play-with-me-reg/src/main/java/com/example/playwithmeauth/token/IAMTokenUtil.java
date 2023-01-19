package com.example.playwithmeauth.token;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

public class IAMTokenUtil {

    public static String getAccessToken(String url) {
        try {
            URL urldemo = new URL(url);
            URLConnection yc = urldemo.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    yc.getInputStream()));
            String inputLine = in.readLine();
            Map token = new ObjectMapper().readValue(inputLine, Map.class);
            in.close();
            return (String) token.get("access_token");
        }catch(Exception e) {
            System.out.println(e);
            throw new RuntimeException(e);
        }
    }
}
