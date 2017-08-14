package com.tidalsolutions.phillife.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by SantusIgnis on 21/07/2016.
 */
public class Toaster {

    Context c;
    String message;

    public Toaster(Context c, String message) {
        this.c = c;
        this.message = message;
        Toast.makeText(c, message, Toast.LENGTH_SHORT).show();
    }
}
