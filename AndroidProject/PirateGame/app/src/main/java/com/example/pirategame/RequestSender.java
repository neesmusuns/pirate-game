package com.example.pirategame;

import android.graphics.Canvas;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RequestSender {
    private static String url = "http://192.168.87.41:8081";
    private static RequestBody body;
    private static HashMap<String,String> postData;

    public static void sendLogInRequest(EntityManager entityManager, OkHttpClient client, Canvas ctx) {
        postData = new HashMap<>();
        postData.put("IsLoggedIn", "false");
        postData.put("Username", "tester1");
        postData.put("Password", "tester1");

        FormBody.Builder builder = new FormBody.Builder();

        for ( Map.Entry<String, String> entry : postData.entrySet() ) {
            builder.add( entry.getKey(), entry.getValue() );
        }

        body = builder.build();

        String response;
        try {
            response = doPostRequest(url, client);
            JSONObject parsedResponse = (JSONObject) new JSONTokener(response).nextValue();
            entityManager.isLoggedIn = parsedResponse.getBoolean("IsLoggedIn");
            if(entityManager.isLoggedIn){
                entityManager.updateGameState(parsedResponse, ctx);
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    private static String doPostRequest(String url, OkHttpClient client) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public static void sendUpdateRequest(EntityManager entityManager, OkHttpClient client, Canvas ctx) {
        postData = new HashMap<>();
        postData.put("IsLoggedIn", "true");
        postData.put("Keys", Util.collectInput());

        FormBody.Builder builder = new FormBody.Builder();

        for ( Map.Entry<String, String> entry : postData.entrySet() ) {
            builder.add( entry.getKey(), entry.getValue() );
        }

        body = builder.build();
        String response;
        try {
            response = doPostRequest(url, client);
            JSONObject parsedResponse = (JSONObject) new JSONTokener(response).nextValue();
            entityManager.updateGameState(parsedResponse, ctx);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }
}
