package com.tidalsolutions.phillife;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.tidalsolutions.phillife.master_activities.Announcement_Navigation;
import com.tidalsolutions.phillife.phillife.R;

/**
 * Created by Jeffrey on 4/8/2016.
 */
public class SplashScreen extends Activity {
    private static int SPLASH_TIME_OUT = 2000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);

        new Handler().postDelayed(new Runnable() {

         /*
          * Showing splash screen with a timer. This will be useful when you
          * want to show case your app logo / company
          */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                Intent i = new Intent(SplashScreen.this, Announcement_Navigation.class);
                startActivity(i);
                finish();

                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);

    }
}
