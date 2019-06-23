package com.example.hp.assistant;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

public class NewAppActivity {

    Context cont;
    String PackageName=null;

    public NewAppActivity(Context con,String text)
    {
        try {
            cont = con;
            final PackageManager packageManager = cont.getPackageManager();
            List<ApplicationInfo> installedApplications = packageManager.getInstalledApplications(PackageManager.GET_META_DATA);

            for (ApplicationInfo appInfo : installedApplications) {
                if (KMP.match(appInfo.loadLabel(packageManager).toString().toLowerCase(), text.toLowerCase())) {
                    PackageName = appInfo.packageName;
                    break;
                }
                // Toast.makeText(cont,appInfo.packageName+"->"+appInfo.loadLabel(packageManager),Toast.LENGTH_SHORT).show();
                // Log.d("OUTPUT", "Package name : " + appInfo.packageName);
                //Log.d("OUTPUT", "Name: " + appInfo.loadLabel(packageManager));
            }
        }
        catch (Exception e)
        {
            Toast.makeText(cont, "AppACt1 Ecxp", Toast.LENGTH_SHORT).show();
        }

    }

    public boolean openActivty() {
        try {
            PackageManager manager = cont.getPackageManager();
            try {
                Intent i = manager.getLaunchIntentForPackage(PackageName);
                if (i == null) {
                    return false;
                    //throw new PackageManager.NameNotFoundException();
                }
                i.addCategory(Intent.CATEGORY_LAUNCHER);
                cont.startActivity(i);
                return true;
            } catch (Exception e) {
                return false;
            }

        }
        catch (Exception e)
        {
            Toast.makeText(cont, "AppAct2 Ecxp", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

}
