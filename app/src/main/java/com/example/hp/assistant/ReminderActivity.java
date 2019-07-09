package com.example.hp.assistant;

import android.app.Activity;
import android.app.AlarmManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by HP on 25-04-2017.
 */

public class ReminderActivity {

    private AlarmManager am;
    Context cont;
    int option;
    Calendar cal;
    int hr=0,min=0;
    TextToSpeech textToSpeech;
    private final int REQ_CODE_SPEECH_INPUT = 100;

    public ReminderActivity(Context cont, String text, int option, TextToSpeech textToSpeech) {
        this.cont = cont;
        this.option = option;
        this.textToSpeech = textToSpeech;

        askForTime();
        promptSpeechInput();
    }

    private void askForTime() {
        textToSpeech.speak("Please tell me the time",TextToSpeech.QUEUE_ADD,null);
    }

    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak");
        try {
            ((Activity)cont).startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(cont, "Not Supported", Toast.LENGTH_SHORT).show();
        }
    }

}
