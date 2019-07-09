package com.example.hp.assistant;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.util.Log;

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

public class NewsActivity {

    Context context;
    String text;
    ArrayList<String> news;


    public NewsActivity(Context context, String text, final TextToSpeech textToSpeech) {
        this.context = context;
        this.text = text;

        String url="https://newsapi.org/v1/articles?source=the-times-of-india&sortBy=top&apiKey=e3913fed50e943c6a8ee638cb22db5cb";

        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try
                {
                    Log.e("NEWSRES",response);
                    news=new ArrayList<>();
                    JSONObject jsonObject=new JSONObject(response);
                    if(jsonObject.getString("status").equals("ok"))
                    {
                        JSONArray jsonArray=jsonObject.getJSONArray("articles");
                        for(int i=0;i<jsonArray.length();i++)
                        {
                            news.add(jsonArray.getJSONObject(i).getString("title"));
                        }
                        readNews(textToSpeech);
                    }
                }
                catch (Exception e)
                {
                    Log.e("NEWSEXCC",e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("NEWSERR",error.getMessage());
            }
        });
        VolleySingleton.getInstance(context).addToRequestQueue(stringRequest);
    }

    public void readNews(TextToSpeech textToSpeech)
    {
        if(news.size()!=0)
        {
            for(int i=0;i<news.size();i++)
            {
                textToSpeech.speak(news.get(i),TextToSpeech.QUEUE_ADD,null);
            }
        }
        else
        {
            textToSpeech.speak("Sorry, no news available",TextToSpeech.QUEUE_FLUSH,null);
        }
    }
}
