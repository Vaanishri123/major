package com.example.hp.assistant;

import android.content.Context;
import android.content.SharedPreferences;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

/**
 * Created by HP on 24-04-2017.
 */

public class WeatherActivity {

    private static final String TAG = WeatherActivity.class.getSimpleName();
    Context context;
    String text;

    public WeatherActivity(Context context, String text) {
        this.context = context;
        this.text = text;
        Log.i(TAG, text);
    }

    public void getWeatherDetails(final TextToSpeech textToSpeech) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("assistant", Context.MODE_PRIVATE);
        String lat = sharedPreferences.getString("latitude", "");
        String lon = sharedPreferences.getString("longitude", "");

        if (lat.equals("") || lon.equals("")) {
            Log.i(TAG, "lat lon null");
        } else {
            String url = "http://api.openweathermap.org/data/2.5/weather?lat=" + lat + "&lon=" + lon + "&appid=7d5c737478a1e2a7d96b0b016c9d8614";

            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        Log.i(TAG, response);
                        JSONObject jsonObject = new JSONObject(response);

                        if (jsonObject.getString("cod").equals("200")) {
                            textToSpeech.speak("Weather is " + jsonObject.getJSONArray("weather").getJSONObject(0).getString("description"), TextToSpeech.QUEUE_ADD, null);
                            textToSpeech.speak("Temperature is " + jsonObject.getJSONObject("main").getString("temp"), TextToSpeech.QUEUE_ADD, null);
                            textToSpeech.speak("Pressure is " + jsonObject.getJSONObject("main").getString("pressure"), TextToSpeech.QUEUE_ADD, null);
                            textToSpeech.speak("Humidity is " + jsonObject.getJSONObject("main").getString("humidity"), TextToSpeech.QUEUE_ADD, null);
                            textToSpeech.speak("Wind is " + jsonObject.getJSONObject("wind").getString("speed"), TextToSpeech.QUEUE_ADD, null);
                        } else {
                            Log.e(TAG, "Not 200");
                        }
                    } catch (Exception e) {
                        Log.e(TAG, e.getMessage());
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("WEATHRERR", error.getMessage());
                }
            });

            VolleySingleton.getInstance(context).addToRequestQueue(stringRequest);
        }
    }
}
