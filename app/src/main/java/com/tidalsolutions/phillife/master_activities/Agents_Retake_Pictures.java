package com.tidalsolutions.phillife.master_activities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import com.tidalsolutions.phillife.models.Pictures_model;
import com.tidalsolutions.phillife.phillife.R;
import com.tidalsolutions.phillife.utils.CONSTANT;
import com.tidalsolutions.phillife.utils.SharedPrefManager;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by SantusIgnis on 14/07/2016.
 */
public class Agents_Retake_Pictures extends AppCompatActivity implements DialogCallBack
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
    HashMap<Integer,Pictures_model> pmList = new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.agents_take_pictures);
        Bundle extras = getIntent().getExtras();
        forms_hashkey = extras.getString("forms_hashkey");
        pm = (Pictures_model) extras.getSerializable("picture_info");
        //new Toaster(getApplicationContext(),forms_hashkey);

        currentFrame=pm.getSequence();
        picturesTaken=pm.getSequence();

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
        dh.getReadableDatabase();

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
                Confirm_Save_Agents_Form.newInstance((DialogCallBack) Agents_Retake_Pictures.this, 0, 0).show(getSupportFragmentManager(), null);

            }
        });
        tv_retake = (TextView) findViewById(R.id.tv_camera_retake);

        tv_retake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
//                if (android.os.Build.VERSION.SDK_INT <= 23) {
//                    // only for gingerbread and newer versions
//                    Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
//
//                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                    rotateImage(90,bmp).compress(Bitmap.CompressFormat.PNG, 100, stream);
//                    byte[] flippedImageByteArray = stream.toByteArray();
//                    saveToLocal(flippedImageByteArray);
//                }
//                else
                    saveToLocal(data);


            }
        });


        //load image to be updated

        Glide.with(this)
                .load(new File(pm.getFile_path()))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(iv_preview);

    }


    void saveToLocal(byte[] data){





        int user_id = new SharedPrefManager(getApplicationContext()).userGet().getUser_id();
        File dir = getApplicationContext().getDir("agent_data", Context.MODE_PRIVATE);
        String updatedImage = pm.getSequence()+".jpg";
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

        pm.setForms_hashkey(forms_hashkey);
        pm.setSequence(pm.getSequence());
        pm.setName(updatedImage);
        pm.setFile_path(localStorage);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
        String currentDateandTime = sdf.format(new Date());
        pm.setDate(currentDateandTime);




        loadImage(data);

    }


    void loadImage(byte[]data){

        if(currentFrame-1==picturesTaken){
            tv_next.setVisibility(View.INVISIBLE);
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

            dh.pictures.updatePicture(pm);

            finish();
        }
        else if(position==CONSTANT.DIALOG_AGENT_ACTION_REVOKE){
            finish();
        }
    }

    public Bitmap rotateImage(int angle, Bitmap bitmapSrc) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(bitmapSrc, 0, 0,
                bitmapSrc.getWidth(), bitmapSrc.getHeight(), matrix, true);
    }

}
