package com.mapboxweather.kamleshsahu.mapboxdemo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;

public class DisplayError {
  static AlertDialog.Builder bld;
   static void displayError(Context context,String title, String msg){
//        if(!AlreadyGotError) {
//            AlreadyGotError=true;
            int maxLength = (msg.length() < 40) ? msg.length() : 40;
            msg = msg.substring(0, maxLength);

            if(bld==null){
            bld = new AlertDialog.Builder(context);
            bld.setMessage(msg);
            bld.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //System.out.println(" display error dialog dismiss");
                    dialog.dismiss();
                    bld = null;

                }
            });
            bld.setTitle(title);

            Log.d("TAG", "Showing alert dialog: " + msg);
            Dialog dialog = bld.create();

            dialog.show();
        }
    };
}
