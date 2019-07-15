package com.orientalmusic.music;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

/**
 * Created by kameloov on 1/13/2018.
 */

public class Social {

    public static boolean sendEmail(Context context, String address, String title, String content) {
        boolean success = false;
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL, new String[]{address});
        i.putExtra(Intent.EXTRA_SUBJECT, title);
        i.putExtra(Intent.EXTRA_TEXT, content);
        try {
            context.startActivity(Intent.createChooser(i, "Send mail..."));
            success = true;
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(context, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
        return success;
    }

    public static void call(Context context, String number) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + number));
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        context.startActivity(callIntent);
    }

    public static void youtube(Context context, String url) {
        Intent intent = null;
        try {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setPackage("com.google.android.youtube");
            intent.setData(Uri.parse(url));
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            context.startActivity(intent);
        }
    }

    public static void facebook(Context context, String url) {
        Uri uri = Uri.parse(url);
        try {
            PackageManager pm = context.getPackageManager();
            ApplicationInfo applicationInfo = pm.getApplicationInfo("com.facebook.katana", 0);
            if (applicationInfo.enabled) {
                // http://stackoverflow.com/a/24547437/1048340
                uri = Uri.parse("fb://facewebmodal/f?href=" + url);
            }
        } catch (PackageManager.NameNotFoundException ignored) {
        }
        context.startActivity(new Intent(Intent.ACTION_VIEW, uri));
    }

    public static void browser(Context context , String url){
        Uri uri = Uri.parse(url);
        Intent i = new Intent(Intent.ACTION_VIEW,uri);
        context.startActivity(i);
    }
}
