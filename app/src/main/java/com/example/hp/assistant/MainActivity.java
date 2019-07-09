package com.example.hp.assistant;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.media.AudioManager;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;


public class MainActivity extends ActionBarActivity implements
        TextToSpeech.OnInitListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    TextToSpeech tts;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    String text = "";
    String timetext = "";
    String messagetext = "";
    Button btn;
    GoogleApiClient googleApiClient;

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

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.SET_ALARM) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.SET_ALARM,
                    Manifest.permission.READ_CONTACTS,
                    Manifest.permission.CALL_PHONE,
                    Manifest.permission.SEND_SMS,
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA}, 101);
        } else {
            buildApiClient();
        }

    }

    public void buildApiClient() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();
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
                        Log.e("COMMAND", text);
                        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();

                        if (FA.match("call", text) || FA.match("connect me to", text) || FA.match("connect to", text)) {
                            CallingActivity cl = new CallingActivity(this, text);
                            if (cl.makeCall()) {
                                Toast.makeText(this, "Calling...!!!", Toast.LENGTH_SHORT).show();
                            } else
                                Toast.makeText(this, "Please try again ...!!!", Toast.LENGTH_SHORT).show();
                        } else if (FA.match("torch", text) || FA.match("flash", text)) {
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
                        } else if (FA.match("alarm", text) || FA.match("wake", text)) {
                            AlarmActivity al = new AlarmActivity(this, text);
                            if (al.setAlarm()) {
                                Toast.makeText(this, "Setting alarm...!!!", Toast.LENGTH_SHORT).show();
                            } else
                                Toast.makeText(this, "Please try again ...!!!", Toast.LENGTH_SHORT).show();
                        } else if (FA.match("play", text) || FA.match("listen", text)) {
                            MediaPlayerActivity mpa = new MediaPlayerActivity(this, text);
                            if (mpa.playMedia()) {
                                Toast.makeText(this, "Playing media...!!!", Toast.LENGTH_SHORT).show();
                            } else
                                Toast.makeText(this, "Please try again ...!!!", Toast.LENGTH_SHORT).show();
                        } else if (FA.match("news", text) || FA.match("headlines", text)) {
                            NewsActivity newsActivity = new NewsActivity(this, text, tts);
                        } else if (FA.match("cinemas", text) || FA.match("cinema", text) || FA.match("movies", text)) {
                            NearbyCinemasActivity cinemasActivity = new NearbyCinemasActivity(this, text);
                            cinemasActivity.findCinemas(tts);
                        } else if (FA.match("weather", text)) {
                            WeatherActivity weatherActivity = new WeatherActivity(this, text);
                            weatherActivity.getWeatherDetails(tts);
                        } else if (FA.match("reminder", text) || FA.match("memo", text)) {
                            HashMap<String, String> myHashAlarm = new HashMap<String, String>();
                            myHashAlarm.put(TextToSpeech.Engine.KEY_PARAM_STREAM, String.valueOf(AudioManager.STREAM_ALARM));
                            myHashAlarm.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "TIME");
                            tts.speak("Please tell me the time", TextToSpeech.QUEUE_ADD, myHashAlarm);
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Could not recognise " + text, Toast.LENGTH_SHORT).show();
                        text = "";
                    }

                    break;
                }
                case 201: {
                    if (resultCode == RESULT_OK && null != data) {
                        ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                        timetext = result.get(0);
                        timetext = timetext.toLowerCase();
                        Log.e("TIMETEXT", timetext);
                        Toast.makeText(this, timetext, Toast.LENGTH_SHORT).show();

                        HashMap<String, String> myHashAlarm = new HashMap<String, String>();
                        myHashAlarm.put(TextToSpeech.Engine.KEY_PARAM_STREAM, String.valueOf(AudioManager.STREAM_ALARM));
                        myHashAlarm.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "MESSAGE");
                        tts.speak("Please tell me the message", TextToSpeech.QUEUE_ADD, myHashAlarm);
                    }
                    break;
                }
                case 202: {
                    if (resultCode == RESULT_OK && null != data) {
                        ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                        messagetext = result.get(0);
                        Toast.makeText(this, messagetext, Toast.LENGTH_SHORT).show();

                        AlarmActivity alarmActivity = new AlarmActivity(MainActivity.this, timetext);
                        if (alarmActivity.setReminder(messagetext)) {
                            Toast.makeText(this, "Setting reminder...!!!", Toast.LENGTH_SHORT).show();
                        } else
                            Toast.makeText(this, "Please try again ...!!!", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }

            }
        } catch (Exception e) {
            Log.e("EXCC", e.getMessage());
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = tts.setLanguage(Locale.US);
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Toast.makeText(this, "Some problem occurred, try again later...!!!", Toast.LENGTH_SHORT).show();
                finish();
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
                        if (utteranceId.equals("TIME"))
                            promptSpeechInput(201);
                        else if (utteranceId.equals("MESSAGE"))
                            promptSpeechInput(202);
                    }

                    @Override
                    public void onError(String utteranceId) {
                        Log.e("TTS", "errer");

                    }
                });
//                promptSpeechInput();
            }
        } else {
            Toast.makeText(this, "Some problem occurred, try again later...!!!", Toast.LENGTH_SHORT).show();
            finish();
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        int i;
        for (i = 0; i < grantResults.length; i++) {
            if (grantResults[i] == PackageManager.PERMISSION_DENIED)
                break;
        }

        if (i != grantResults.length)
            Toast.makeText(this, "Permissions denied, app may not work properly", Toast.LENGTH_SHORT).show();
        else
            buildApiClient();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest,
                new com.google.android.gms.location.LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        SharedPreferences sp = getSharedPreferences("assistant", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("latitude", "" + location.getLatitude());
                        editor.putString("longitude", "" + location.getLongitude());
                        editor.apply();
                    }
                });
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

}
