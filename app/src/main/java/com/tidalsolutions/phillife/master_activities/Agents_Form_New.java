package com.tidalsolutions.phillife.master_activities;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tidalsolutions.phillife.db.DatabaseHandler;
import com.tidalsolutions.phillife.models.Albums_model;
import com.tidalsolutions.phillife.phillife.R;
import com.tidalsolutions.phillife.utils.MD5;
import com.tidalsolutions.phillife.utils.SharedPrefManager;
import com.tidalsolutions.phillife.utils.Toaster;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by SantusIgnis on 14/07/2016.
 */
public class Agents_Form_New extends AppCompatActivity {


    ImageView iv_camera_icon;
    //CameraView cv;
    DatabaseHandler dh;
    EditText  tv_client_name;
    TextView form_id, tv_agent_name, tv_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.agents_form_new);

        //requestWindowFeature(Window.FEATURE_ACTION_BAR);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#1b313a")));
        actionBar.setTitle("Client Form (New)");

        ActivityCompat.requestPermissions(Agents_Form_New.this,
                new String[]{Manifest.permission.CAMERA},
                4289);

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

            inflater.inflate(R.menu.menu_main_hidden, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void init_cdv() {

        dh = new DatabaseHandler(getApplicationContext());
        dh.getReadableDatabase();

        int last_form_id = dh.forms.getLastFormId();
        String cookie = new SharedPrefManager(getApplicationContext()).agentGet().getCookie();
        Integer user_id = new SharedPrefManager(Agents_Form_New.this).agentGet().get_id();
        String agent_code = new SharedPrefManager(Agents_Form_New.this).agentGet().getAgent_code();
        String random =  MD5.crypt("" + System.currentTimeMillis());
        final String final_form_id = user_id+agent_code+cookie.substring(0,15)+last_form_id;
        Log.d("form id: ", final_form_id);

        tv_client_name = (EditText) findViewById(R.id.et_client_name);
        tv_agent_name = (TextView) findViewById(R.id.tv_agent_name);
        String username = ""+new SharedPrefManager(Agents_Form_New.this).agentGet().getUsername();
        tv_agent_name.setText(username);

        iv_camera_icon = (ImageView) findViewById(R.id.iv_camera_icon);
        iv_camera_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tv_client_name.getText().toString().length()<=4){
                    new Toaster(getApplicationContext(),"Client name too short!");
                }
                else {
                    Integer user_id = new SharedPrefManager(Agents_Form_New.this).agentGet().get_id();
                    Albums_model am = new Albums_model();

                    am.setAgent_id(user_id);
                    am.setForm_id(final_form_id);
                    am.setIs_draft(1);
                    am.setClient_name(tv_client_name.getText().toString());
                    am.setDescription("FORM CREATED BY AGENT ID 1");
                    am.setDate_created(getCurDate());

                    dh.forms.add_form(am);
                    new Toaster(getApplicationContext(), "New Form Saved!");

                    Intent i = new Intent(getApplicationContext(), Agents_Take_Pictures.class);
                    i.putExtra("forms_hashkey", final_form_id);

                    startActivity(i);
                    finish();
                }

            }
        });


    }

    public String getCurDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
        String currentDateandTime = sdf.format(new Date());
        return currentDateandTime;
    }
}
