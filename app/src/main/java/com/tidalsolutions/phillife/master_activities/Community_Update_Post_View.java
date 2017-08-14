package com.tidalsolutions.phillife.master_activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.pkmmte.view.CircularImageView;
import com.tidalsolutions.phillife.API;
import com.tidalsolutions.phillife.AsyncTaskListener;
import com.tidalsolutions.phillife.phillife.R;
import com.tidalsolutions.phillife.utils.SharedPrefManager;

import org.json.JSONObject;

import java.io.File;
import java.net.URLEncoder;

/**
 * Created by SantusIgnis on 14/07/2016.
 */
public class Community_Update_Post_View extends AppCompatActivity implements AsyncTaskListener {


    TextView tv_post_name, tv_post_date, tv_post_title, tv_post_detail;
    EditText et_title, et_content;
    Button add_thread;
    String thread_id, thread_title, thread_detail, thread_date;
    CircularImageView civ;
    AQuery aq;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.community_add_post_view);

        //requestWindowFeature(Window.FEATURE_ACTION_BAR);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#163632")));
        actionBar.setTitle("");

        Intent ii = getIntent();
        thread_id = ii.getStringExtra("thread_id");
        thread_title = ii.getStringExtra("thread_title");
        thread_detail = ii.getStringExtra("thread_detail");
        thread_date = ii.getStringExtra("thread_date");
        init_cdv();
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
    public void init_cdv(){

        aq = new AQuery(getApplicationContext());

        civ  = (CircularImageView) findViewById(R.id.forumImage);


        SharedPrefManager spm = new SharedPrefManager(getApplicationContext());
        File sd = getApplicationContext().getDir("user_profile", Context.MODE_PRIVATE);
        try {
            aq.id(civ).image(new File(sd.getAbsolutePath() + "/user_profile/" + spm.userGet().getImage_url()), 50).enabled(true);
        }catch (Exception e){
            aq.id(R.id.forumImage).image(R.drawable.portal_profile_sample).enabled(true);
        }

        et_title = (EditText) findViewById(R.id.et_title);
        et_content = (EditText) findViewById(R.id.et_content);
        add_thread = (Button) findViewById(R.id.add_thread);
        tv_post_date = (TextView) findViewById(R.id.tv_date);

        et_title.setText(thread_title);
        et_content.setText(thread_detail);
        tv_post_date.setText(thread_date);

        et_title.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            et_content.setNextFocusDownId(R.id.et_content);
                            return true;
                        default:
                            break;
                    }
                }
                return false;

            }
        });


        et_content.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            add_thread.performClick();
                            return true;
                        default:
                            break;
                    }
                }
                return false;

            }
        });

        add_thread.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(et_title.getText().toString().length()>1&&et_content.getText().toString().length()>1){
                    try {
                        new API(getApplicationContext(), Community_Update_Post_View.this, true).execute("POST", "apij/update_thread/" + URLEncoder.encode(new
                                SharedPrefManager(getApplicationContext()).userGet().getSession_cookie()) + "/"+thread_id+"/" + URLEncoder.encode(et_title.getText().toString(), "UTF-8") + "/" + URLEncoder.encode(et_content.getText().toString(), "UTF-8"));
                    }catch (Exception e){}
                }
                //Log.e("session request",""+new SharedPrefManager(getApplicationContext()).userGet().getSession_cookie());

            }
        });

    }

    @Override
    public void onTaskComplete(String result) {

        Log.e("result", "result" + result);


        try {
            JSONObject jObj = new JSONObject(result);
            String service = jObj.getString("service");
            boolean success = jObj.getBoolean("success");

            if(success){
                et_title.setText("");
                et_content.setText("");
                finish();
            }

        }catch (Exception e){

        }


    }
}
