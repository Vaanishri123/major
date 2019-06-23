package com.example.hp.assistant;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;


public class MainActivity extends AppCompatActivity implements
        TextToSpeech.OnInitListener {

    TextToSpeech tts;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    String text = "";
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tts = new TextToSpeech(this, this);
        btn = (Button) findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promptSpeechInput(REQ_CODE_SPEECH_INPUT);
            }
        });
    }

    private void promptSpeechInput(int reqCode) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak");
        try {
            startActivityForResult(intent, reqCode);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(), "Not Supported", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            switch (requestCode) {
                case REQ_CODE_SPEECH_INPUT: {
                    if (resultCode == RESULT_OK && null != data) {
                        ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                        text = result.get(0);
                        text = text.toLowerCase();
                        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();

                        if (FA.match("torch", text) || FA.match("flash", text)) {
                            FlashActivity flashActivity = new FlashActivity(this, text);
                            flashActivity.openOrCloseFlash();
                        } else if (FA.match("message", text) || FA.match("text", text) || FA.match("tell", text) || FA.match("inform", text)) {
                            MessagingActivity ma = new MessagingActivity(this, text);
                            if (ma.sendMessage()) {
                                Toast.makeText(this, "Messaging...!!!", Toast.LENGTH_SHORT).show();
                            } else
                                Toast.makeText(this, "Please try again ...!!!", Toast.LENGTH_SHORT).show();
                        } else if (FA.match("open", text) || FA.match("launch", text)) {
                            NewAppActivity napp = new NewAppActivity(this, text);
                            if (napp.openActivty()) {
                                Toast.makeText(this, "Opening app...!!!", Toast.LENGTH_SHORT).show();
                            } else
                                Toast.makeText(this, "Please try again ...!!!", Toast.LENGTH_SHORT).show();
                        } else if (FA.match("play", text) || FA.match("listen", text)) {
                            MediaPlayerActivity mpa = new MediaPlayerActivity(this, text);
                            if (mpa.playMedia()) {
                                Toast.makeText(this, "Playing media...!!!", Toast.LENGTH_SHORT).show();
                            } else
                                Toast.makeText(this, "Please try again ...!!!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Could not recognise " + text, Toast.LENGTH_SHORT).show();
                        }
                        text = "";
                    }
                    break;
                }
            }
        } catch (Exception e) {
            Log.e("EXCC", e.getMessage());
            Toast.makeText(this, "Some problem occurred, try again later...!!!", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = tts.setLanguage(Locale.US);
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Toast.makeText(this, "Some problem occurred, try again later...!!!", Toast.LENGTH_SHORT).show();
            } else {
                tts.speak("Hey, what can I do for you", TextToSpeech.QUEUE_FLUSH, null);
                tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                    @Override
                    public void onStart(String utteranceId) {
                        Log.e("TTS", utteranceId);
                    }

                    @Override
                    public void onDone(String utteranceId) {
                        Log.e("TTS", "completed " + utteranceId);
                    }

                    @Override
                    public void onError(String utteranceId) {
                        Log.e("TTS", "errer");
                    }
                });
            }
        } else {
            Toast.makeText(this, "Some problem occurred, try again later...!!!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }

}
