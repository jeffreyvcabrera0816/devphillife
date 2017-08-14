package com.tidalsolutions.phillife.master_activities;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.WindowManager;

import com.tidalsolutions.phillife.db.DatabaseHandler;
import com.tidalsolutions.phillife.phillife.R;

/**
 * Created by SantusIgnis on 14/07/2016.
 */
public class Agents_My_Reports_Search_Drafts extends AppCompatActivity {



    DatabaseHandler dh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.agents_my_reports_search_drafts);

        //requestWindowFeature(Window.FEATURE_ACTION_BAR);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#1b313a")));
        actionBar.setTitle("Search Reports");
        init_cdv();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

            inflater.inflate(R.menu.menu_main_hidden, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //Write your logic here
                this.finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void init_cdv() {

        dh = new DatabaseHandler(getApplicationContext());
        dh.getReadableDatabase();



    }
}
