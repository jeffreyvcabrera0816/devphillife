package com.tidalsolutions.phillife.master_activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.tidalsolutions.phillife.adapters.Agents_Reports_Drafts_Adapter;
import com.tidalsolutions.phillife.db.DatabaseHandler;
import com.tidalsolutions.phillife.models.Albums_model;
import com.tidalsolutions.phillife.phillife.R;

import java.util.ArrayList;

/**
 * Created by SantusIgnis on 14/07/2016.
 */
public class Agents_My_Reports_Drafts extends AppCompatActivity {


    ListView lv_drafts;
    Button add_thread;
    Agents_Reports_Drafts_Adapter amrd;
    DatabaseHandler dh;
    ArrayList<Albums_model> forms;
    FloatingActionButton fla;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.agents_my_reports_drafts_activity);

        //requestWindowFeature(Window.FEATURE_ACTION_BAR);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#1b313a")));
        actionBar.setTitle("Draft Reports");
        init_cdv();
    }

    @Override
    protected void onResume() {
        super.onResume();
        init_cdv();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

            inflater.inflate(R.menu.menu_my_report_draft, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //Write your logic here
                this.finish();
                return true;
            case R.id.reload:
                //Write your logic here
                this.finish();
                return true;
//            case R.id.search:
//                //Write your logic here
//                this.finish();
//                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void init_cdv() {

        dh = new DatabaseHandler(getApplicationContext());
        dh.getReadableDatabase();

        forms = dh.forms.getForms(1);

        lv_drafts = (ListView) findViewById(R.id.lv_report_drafts);
        amrd = new Agents_Reports_Drafts_Adapter(getApplicationContext(),forms);
        lv_drafts.setAdapter(amrd);

//        fla = (FloatingActionButton) findViewById(R.id.fab);
//        fla.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(getApplicationContext(), Agents_Form_New.class);
//                startActivity(i);
//            }
//        });
        lv_drafts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getApplicationContext(),Agents_Form_Update.class);
                i.putExtra("form", forms.get(position));
                startActivity(i);
            }
        });
    }
}
