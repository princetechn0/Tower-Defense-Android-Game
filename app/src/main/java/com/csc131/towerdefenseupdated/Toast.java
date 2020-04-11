package com.csc131.towerdefenseupdated;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

public class Toast {

    Context context;
    Handler h = new Handler(Looper.getMainLooper());

    Toast(Context context) {
        this.context = context;
    }

    // Toast Method handler
    void onScreenMessages(final String prompt) {
        //Let this be the code in your n'th level thread from main UI thread
        h.post(new Runnable() {
            public void run() {
                android.widget.Toast.makeText(context, prompt, android.widget.Toast.LENGTH_SHORT).show();
            }
        });
    }

}
