package com.tidalsolutions.phillife.master_activities;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.cameraview.CameraView;
import com.tidalsolutions.phillife.db.DatabaseHandler;
import com.tidalsolutions.phillife.dialogs.Confirm_Save_Agents_Form;
import com.tidalsolutions.phillife.interfaces.DialogCallBack;
import com.tidalsolutions.phillife.models.Albums_model;
import com.tidalsolutions.phillife.models.Pictures_model;
import com.tidalsolutions.phillife.phillife.R;
import com.tidalsolutions.phillife.utils.CONSTANT;
import com.tidalsolutions.phillife.utils.SharedPrefManager;
import com.tidalsolutions.phillife.utils.Toaster;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by SantusIgnis on 14/07/2016.
 */
public class Agents_Add_Pictures extends AppCompatActivity implements DialogCallBack
{



    DatabaseHandler dh;
    CameraView cv;
    ImageView camera_icon, iv_preview;
    TextView tv_retake;
    int currentFrame=1;
    int picturesTaken=0;
    TextView tv_next, tv_done;
    String forms_hashkey;
    Pictures_model pm;
    int last_sequence;
    ArrayList<Pictures_model> pmList;// = new HashMap<Integer,Pictures_model>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.agents_take_pictures);
        Bundle extras = getIntent().getExtras();
        pmList = new ArrayList<Pictures_model>();
        forms_hashkey = extras.getString("forms_hashkey");
        pm = (Pictures_model) extras.getSerializable("picture_info");
        last_sequence=extras.getInt("last_sequence");
        //new Toaster(getApplicationContext(),forms_hashkey);

        currentFrame=last_sequence+1;
        picturesTaken=last_sequence;

        init_cdv();
    }

    @Override
    protected void onResume() {
        super.onResume();
        cv.setBackgroundColor(Color.parseColor("#222222"));

        cv.start();
    }

    @Override
    protected void onPause() {
        cv.stop();
        super.onPause();

    }

    public void init_cdv() {

        dh = new DatabaseHandler(getApplicationContext());
        dh.getWritableDatabase();

        cv = (CameraView) findViewById(R.id.camera);
        camera_icon = (ImageView) findViewById(R.id.iv_camera_icon);
        iv_preview = (ImageView) findViewById(R.id.iv_preview);
        tv_next = (TextView) findViewById(R.id.tv_next);
        tv_next.setVisibility(View.INVISIBLE);



        tv_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picturesTaken += 1;
                currentFrame += 1;
                cv.setVisibility(View.VISIBLE);
                camera_icon.setVisibility(View.VISIBLE);
                tv_retake.setVisibility(View.INVISIBLE);
                iv_preview.setVisibility(View.GONE);
                tv_next.setVisibility(View.INVISIBLE);
            }
        });

        tv_done = (TextView) findViewById(R.id.tv_done);
        tv_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Confirm_Save_Agents_Form.newInstance((DialogCallBack) Agents_Add_Pictures.this, 0, 0).show(getSupportFragmentManager(), null);

            }
        });
        tv_retake = (TextView) findViewById(R.id.tv_camera_retake);

        tv_retake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pmList.remove(pmList.size()-1);
                camera_icon.setVisibility(View.VISIBLE);
                tv_retake.setVisibility(View.INVISIBLE);
                iv_preview.setVisibility(View.GONE);
                cv.setVisibility(View.VISIBLE);
            }
        });
        camera_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cv.takePicture();
            }
        });


        cv.addCallback(new CameraView.Callback() {
            @Override
            public void onPictureTaken(CameraView cameraView, byte[] data) {
                super.onPictureTaken(cameraView, data);

                saveToLocal(data);


            }
        });




    }


    void saveToLocal(byte[] data){





        int user_id = new SharedPrefManager(getApplicationContext()).userGet().getUser_id();
        File dir = getApplicationContext().getDir("agent_data", Context.MODE_PRIVATE);
        String updatedImage = currentFrame+".jpg";
        String localStorage = dir.getAbsolutePath()+"/agent_data/"+forms_hashkey+"/"+updatedImage;
        FileOutputStream os=null;
        ByteArrayInputStream bs = new ByteArrayInputStream(data);

        try {
            File f = new File(localStorage);
            File dummy = new File(dir.getAbsolutePath()+"/agent_data/"+forms_hashkey+"/");
            if (!dummy.exists()) {
                dummy.mkdirs();
            }

            if (f.getParentFile() != null) {
                f.getParentFile().mkdir();
            }
            os = new FileOutputStream(f);

            int counter = -1;
            byte[] blob = new byte[2048];
            while ((counter = bs.read(blob)) != -1) {
                os.write(blob, 0, counter);
            }

        }
        catch (Exception e){

        }

        //saving to db
        Pictures_model pmx = new Pictures_model();
        pmx.setForms_hashkey(forms_hashkey);
        pmx.setSequence(currentFrame);
        Log.e("sequence?",": "+pmx.getSequence());
        pmx.setName(updatedImage);
        pmx.setFile_path(localStorage);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
        String currentDateandTime = sdf.format(new Date());
        pmx.setDate(currentDateandTime);

        pmList.add(pmx);
        //pmList.add(pm);

        new Toaster(getApplicationContext()," "+pmList.size());

        loadImage(data);

    }


    void loadImage(byte[]data){

        if(currentFrame-1==picturesTaken){
            tv_next.setVisibility(View.VISIBLE);
        }else tv_next.setVisibility(View.INVISIBLE);

        cv.setVisibility(View.GONE);
        camera_icon.setVisibility(View.INVISIBLE);
        tv_retake.setVisibility(View.VISIBLE);
        iv_preview.setVisibility(View.VISIBLE);



        Glide.with(this)
                .load(data)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(iv_preview);


        cv.setVisibility(View.GONE);
        iv_preview.setVisibility(View.VISIBLE);



    }

    @Override
    public void dialogfunction(int position, int position2, Object data1, Object data2) {
        if(position== CONSTANT.DIALOG_AGENT_ACTION_CONFIRM){

            for(int g=0;g<pmList.size();g++){
                dh.pictures.add_picture(pmList.get(g));
                Log.e("asdasd "+g,"xxx"+pmList.get(g).getSequence());
            }
            Albums_model am = new Albums_model();
            am.setPage_count(currentFrame);
            am.setForm_id(forms_hashkey);
            dh.forms.updateFormPagecount(am);
            finish();

        }
        else if(position==CONSTANT.DIALOG_AGENT_ACTION_REVOKE){
            finish();
        }
    }
}
