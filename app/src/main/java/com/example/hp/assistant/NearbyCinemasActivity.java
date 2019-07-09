package com.example.hp.assistant;

import android.content.Context;
import android.content.SharedPreferences;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by HP on 24-04-2017.
 */

public class NearbyCinemasActivity {

    Context context;
    String text;
    ArrayList<String> cinemas;

    public NearbyCinemasActivity(Context context, String text) {
        this.context = context;
        this.text = text;
    }

    public void findCinemas(final TextToSpeech textToSpeech)
    {
        SharedPreferences sp=context.getSharedPreferences("assistant",Context.MODE_PRIVATE);
        String url="https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="+sp.getString("latitude","")+
                ","+sp.getString("longitude","")+"&radius=2000&keyword=cinemas&key=AIzaSyBGhnpH9VGMRvq7opbr9bX0ZgnEvirMf1U";
        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try
                {
                    Log.e("cinemasRES",response);
                    cinemas=new ArrayList<>();
                    JSONObject jsonObject=new JSONObject(response);
                        Log.e("cinemasERR","OK");
                        JSONArray jsonArray=jsonObject.getJSONArray("results");
                        for(int i=0;i<jsonArray.length();i++)
                        {
                            cinemas.add(jsonArray.getJSONObject(i).getString("name"));
                            Log.e("cinemasEXCC",cinemas.get(i));
                        }
                        readcinemas(textToSpeech);
                }
                catch (Exception e)
                {
                    Log.e("cinemasEXCC",e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("cinemasERR",error.getMessage());
            }
        });
        VolleySingleton.getInstance(context).addToRequestQueue(stringRequest);
    }

    public void readcinemas(TextToSpeech textToSpeech)
    {
        Toast.makeText(context, "Playing media...!!!", Toast.LENGTH_SHORT).show();
        if(cinemas.size()!=0)
        {
            for(int i=0;i<cinemas.size()/2;i++)
            {
                textToSpeech.speak(cinemas.get(i),TextToSpeech.QUEUE_ADD,null);
            }
        }
        else
        {
            textToSpeech.speak("Sorry, no cinemas available",TextToSpeech.QUEUE_FLUSH,null);
        }
    }

}
