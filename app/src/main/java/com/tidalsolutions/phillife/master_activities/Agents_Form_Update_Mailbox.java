package com.tidalsolutions.phillife.master_activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tidalsolutions.phillife.API_TEST;
import com.tidalsolutions.phillife.AsyncTaskListener;
import com.tidalsolutions.phillife.Settings;
import com.tidalsolutions.phillife.adapters.Agents_Form_Update_ViewPager_Adapter;
import com.tidalsolutions.phillife.db.DatabaseHandler;
import com.tidalsolutions.phillife.interfaces.SpecialCallBack;
import com.tidalsolutions.phillife.models.Albums_model;
import com.tidalsolutions.phillife.models.Pictures_model;
import com.tidalsolutions.phillife.phillife.R;
import com.tidalsolutions.phillife.utils.ImageUniversalUploader;
import com.tidalsolutions.phillife.utils.SharedPrefManager;
import com.tidalsolutions.phillife.utils.Toaster;

import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Jeffrey on 10/21/2016.
 */

public class Agents_Form_Update_Mailbox extends AppCompatActivity implements AsyncTaskListener, SpecialCallBack {

    //CameraView cv;
    DatabaseHandler dh;
    EditText tv_client_name;
    TextView form_id, tv_agent_name, tv_date;
    RelativeLayout rl_save, rl_submit, rl_camera, rl_add;
    ViewPager vps;
    Albums_model am; ArrayList<Pictures_model> pm;
    Agents_Form_Update_ViewPager_Adapter afyvpa;
    ProgressDialog pd;
    //page count to verify
    int num_of_images_to_upload=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.agents_form_update_mailbox);

        //requestWindowFeature(Window.FEATURE_ACTION_BAR);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#1b313a")));
        actionBar.setTitle("Client Form (Update)");
        Bundle extras = getIntent().getExtras();
        am = (Albums_model) extras.getSerializable("form");
        init_cdv();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.menu_main_hidden, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
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
    public void init_cdv() {

        dh = new DatabaseHandler(getApplicationContext());
        dh.getReadableDatabase();
        //cv = (CameraView) findViewById(R.id.camera);

//        am = dh.forms.getFormByHashKey(am.getForms_hashkey());

        pd = new ProgressDialog(this);
//        form_id = (TextView) findViewById(R.id.tv_form_id);
        tv_client_name = (EditText) findViewById(R.id.et_client_name);
        tv_agent_name = (TextView) findViewById(R.id.tv_agent_name);
//        tv_date = (TextView) findViewById(R.id.tv_date);



//        form_id.setText(am.getForms_hashkey());
        tv_client_name.setText(am.getClient_name());
        tv_agent_name.setText("agent"+am.getAgent_id());
//        tv_date.setText(am.getDate_created());



        pm= new ArrayList<>();
        pm = dh.pictures.getPicturesByHash(am.getForm_id());
//        new Toaster(getApplicationContext(),""+pm.size());
        afyvpa = new Agents_Form_Update_ViewPager_Adapter(getSupportFragmentManager(),getApplicationContext(), pm);
        vps = (ViewPager) findViewById(R.id.vp_image);
        vps.setAdapter(afyvpa);

        rl_camera = (RelativeLayout) findViewById(R.id.rl_camera);
        rl_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Agents_Form_Update_Mailbox.this, Agents_Retake_Pictures.class);
                if (pm.size() == 0) {
                    new Toaster(getApplicationContext(), "There's no image to update!");
                } else {
                    i.putExtra("picture_info", pm.get(vps.getCurrentItem()));
                    i.putExtra("forms_hashkey", am.getForm_id());
//                    new Toaster(getApplicationContext(), "" + vps.getCurrentItem());
                    startActivity(i);
                }


            }
        });

        rl_add = (RelativeLayout) findViewById(R.id.rl_add);
        rl_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Agents_Form_Update_Mailbox.this,Agents_Add_Pictures.class);

                if(pm.size()==0) {
                    i.putExtra("picture_info", new Pictures_model());
                }
                else{
                    i.putExtra("picture_info", pm.get(vps.getCurrentItem()));
                }



                i.putExtra("forms_hashkey",am.getForm_id());
                i.putExtra("last_sequence",pm.size());
//                new Toaster(getApplicationContext(),"current "+vps.getCurrentItem());
                startActivity(i);
            }
        });


//        rl_save = (RelativeLayout) findViewById(R.id.rl_save);
//        rl_save.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Albums_model am = new Albums_model();
//                am.setAgent_id(new SharedPrefManager(getApplicationContext()).agentGet().get_id());
//                am.setForms_hashkey(form_id.getText().toString());
//                am.setIs_draft(1);
//                am.setClient_name(tv_client_name.getText().toString());
//                am.setDescription("FORM CREATED BY AGENT");
//                am.setDate_created(tv_date.getText().toString());
//
//                dh.forms.updateForm(am);
//                new Toaster(getApplicationContext()," Form Updated!");
//                init_cdv();
//            }
//        });


        rl_submit = (RelativeLayout) findViewById(R.id.rl_submit_form);
        rl_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  String toPass="";
                try {
                    toPass = URLEncoder.encode(tv_client_name.getText().toString(), "UTF-8") + "/" + "" + form_id.getText().toString() + "/" + "description" + "/" + form_id.getText().toString() + "/" + "" + pm.size() + "/" + new SharedPrefManager(getApplicationContext()).agentGet().get_id() + "/";
                }catch (Exception e){

                }
                    new API_TEST(Agents_Form_Update.this, Agents_Form_Update.this, false).execute("POST", "apij/create_form/"+toPass);
               */ //new API(Agents_Form_Update.this, Agents_Form_Update.this, false).execute("POST", "apij/upload_image_agent_files/");


                //for individual pictures
                for(int gz=0;gz<pm.size();gz++) {

                    String api = Settings.base_url_test + "apij/upload_image_agent_files/files/";
                    HashMap<String,String> additional = new HashMap<String, String>();
                    additional.put("filename",pm.get(gz).getName());
                    additional.put("album",pm.get(gz).getForms_hashkey());

                    new ImageUniversalUploader(getApplicationContext(), Agents_Form_Update_Mailbox.this, pm.get(gz).getFile_path(), api, pd, "uploading",additional).uploadImage();


                }

            }

        });



    }

    @Override
    public void onTaskComplete(String result) {
        Log.e("result","resulter "+result);
        //new Toaster(getApplicationContext(),result);

        try {


            JSONObject jObj = new JSONObject(result);
            String service = jObj.getString("service");
            boolean success = jObj.getBoolean("success");

            if (success) {
                if(service.equalsIgnoreCase("update_form")){

                    finish();

                }
            }
        }catch (Exception e){

        }
    }

    int pictureCounter=0;
    @Override
    public void specialCallBack(int position, Object data1) {
        Log.e("special ",":: "+(String)data1);
        //new Toaster(getApplicationContext(),""+(String)data1);
        String result = (String)data1;
        //new Toaster(getApplicationContext(),"...");
        try {


            JSONObject jObj = new JSONObject(result);
            String service = jObj.getString("service");
            boolean success = jObj.getBoolean("success");

            if(success){


                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {

                    }
                }, 2000);
                if(service.equalsIgnoreCase("upload_form_image")){
                    pictureCounter++;
                    if(pictureCounter==pm.size()){
                        new Toaster(getApplicationContext(),"All uploaded!");
                        new Toaster(getApplicationContext(),"Form sent!");
                        dh.forms.updateFormDraft(am);


                        String toPass="";
                        try {
                            toPass = URLEncoder.encode(tv_client_name.getText().toString(), "UTF-8") + "/" + "" + am.getForm_id() + "/" + "description" + "/" + am.getForm_id() + "/" + "" + pm.size() + "/" + new SharedPrefManager(getApplicationContext()).agentGet().get_id() + "/"+am.get_id()+"/";
                        }catch (Exception e){

                        }
                        new API_TEST(Agents_Form_Update_Mailbox.this, Agents_Form_Update_Mailbox.this, false).execute("POST", "apij/update_form/1/" + am.get_id()+"/"+pm.size());
                    }
                }
            }else{
                new Toaster(getApplicationContext(),"error occured! please try again");

            }
        }catch (Exception e){

        }

    }
}