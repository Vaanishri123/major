package com.example.hp.assistant;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

public class CallingActivity {

    String phno=null;
    Context cont;

    public CallingActivity(Context con,String text)
    {
        cont=con;
        phno=ContactFinder.getAllContacts(con,text);
    }

    public boolean makeCall()
    {
        try
        {
        if(phno==null)
            return false;
        else {
            Toast.makeText(cont, phno, Toast.LENGTH_SHORT).show();

            Intent in = new Intent(Intent.ACTION_CALL);
            in.setData(Uri.parse("tel:"+phno));
            cont.startActivity(in);
            return true;
        }

        }
        catch (Exception e)
        {
            Toast.makeText(cont, "Call ActEcxp", Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}
