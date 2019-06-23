package com.example.hp.assistant;

import android.content.Context;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

public class MediaPlayerActivity {

    Context cont;
    String songname=null,songpath=null;

    public MediaPlayerActivity(Context con,String text)
    {
        try {
            cont = con;
            String songn=null;
            String songp=null;
            Uri allsongsuri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";
            Cursor cursor = cont.getContentResolver().query(allsongsuri, null, selection, null, null);

            text=text.toLowerCase();

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {
                        songn = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                        songp = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));

                        songn=songn.toLowerCase();
                        Log.e("SONG",songn);

                        if (KMP.match(songn, text)) {
                            songname=songn;
                            songpath=songp;
                            break;
                        }
                    } while (cursor.moveToNext());

                }
                cursor.close();
            }
        }
        catch (Exception e)
        {
            Toast.makeText(cont, "MusicAcr 1 Ecxp:"+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public boolean playMedia()
    {
        try {
            if(songpath==null)
            {
                return false;
            }
            else {
                Toast.makeText(cont, "playing " + songname, Toast.LENGTH_SHORT).show();
                MediaPlayer mpintro = MediaPlayer.create(cont, Uri.parse(songpath));
                mpintro.start();
                return true;
            }
        }
        catch (Exception e)
        {
            Log.e("SONGEXC",e.getMessage());
            Toast.makeText(cont, "Music Act2 Ecxp", Toast.LENGTH_SHORT).show();
            return false;
        }

    }




}
