package com.example.hp.assistant;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.telephony.SmsManager;
import android.widget.Toast;

public class MessagingActivity {

    String phno=null;
    Context cont;

    public MessagingActivity(Context con,String text)
    {
        cont=con;
        phno=ContactFinder.getAllContacts(con,text);
    }

    public boolean sendMessage() {
        try {
            if (phno == null) {
                return false;
            } else {
                Toast.makeText(cont, phno, Toast.LENGTH_SHORT).show();

                SmsManager smsmanager = SmsManager.getDefault();
                smsmanager.sendTextMessage(phno, null, "Call me !!!", null, null);
                return true;
            }
        }
        catch (Exception e)
        {
            Toast.makeText(cont, "MessgAct Ecxp", Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}
