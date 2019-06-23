package com.example.hp.assistant;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Toast;

public class ContactFinder {

    public static String getAllContacts(Context con, String txt) {
        try {
            ContentResolver cr = con.getContentResolver();
            Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

            if (cur.getCount() > 0) {

                cur.moveToFirst();
                do {
                    String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                    String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                    if (Integer.parseInt(cur.getString(cur.getColumnIndex(
                            ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                        Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);

                        if (pCur.moveToFirst()) {
                            do {
                                String phoneNo = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                //Toast.makeText(con, "Name: " + name + ", Phone No: " + phoneNo, Toast.LENGTH_SHORT).show();

                                String firstname[] = name.split(" ");

                                Log.e("CONTACT", name.toLowerCase());
                                if (FA.match(name.toLowerCase(), txt)) {
                                    Toast.makeText(con, firstname[0], Toast.LENGTH_SHORT).show();
                                    return phoneNo;
                                }
                            } while (pCur.moveToNext());
                        }
                        pCur.close();
                    }
                } while (cur.moveToNext());
            }
            return null;
        } catch (Exception e) {
            Log.e("CONTEXC", e.getMessage());
            Toast.makeText(con, "Cont Find Ecxp", Toast.LENGTH_SHORT).show();
            return null;
        }
    }

}
