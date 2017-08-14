package com.tidalsolutions.phillife.master_activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by SantusIgnis on 14/07/2016.
 */
public class Agents_Take_Pictures extends AppCompatActivity implements DialogCallBack
{



    DatabaseHandler dh;
    CameraView cv;
    ImageView camera_icon, iv_preview;
    TextView tv_retake;
    int currentFrame=1;
    int picturesTaken=0;
    TextView tv_next, tv_done;
    String forms_hashkey;
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
                Confirm_Save_Agents_Form.newInstance((DialogCallBack) Agents_Take_Pictures.this, 0, 0).show(getSupportFragmentManager(), null);

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

        Pictures_model pm = new Pictures_model();
        pm.setForms_hashkey(forms_hashkey);
        pm.setSequence(currentFrame);
        pm.setName(updatedImage);
        pm.setFile_path(localStorage);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String currentDateandTime = sdf.format(new Date());
        pm.setDate(currentDateandTime);
        pmList.put(pm.getSequence(), pm);

        loadImage(data);
//        Toast.makeText(this, forms_hashkey, Toast.LENGTH_SHORT).show();

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

        if(position==CONSTANT.DIALOG_AGENT_ACTION_CONFIRM){
            for(int g=0;g<pmList.size();g++){
                dh.pictures.add_picture(pmList.get(g+1));
            }
            Albums_model am = new Albums_model();
            am.setPage_count(pmList.size());
            am.setForm_id(forms_hashkey);
            dh.forms.updateFormPagecount(am);
            finish();
        }
        else if(position==CONSTANT.DIALOG_AGENT_ACTION_REVOKE){
            finish();
        }

        Intent i = new Intent(Agents_Take_Pictures.this, Agents_My_Reports_Drafts.class);
        startActivity(i);

    }

    public Bitmap rotateImage(String bitmapSrc) throws IOException {

        BitmapFactory.Options bounds = new BitmapFactory.Options();
        bounds.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(bitmapSrc, bounds);

        BitmapFactory.Options opts = new BitmapFactory.Options();
        Bitmap bm = BitmapFactory.decodeFile(bitmapSrc, opts);
        ExifInterface exif = new ExifInterface(bitmapSrc);
        String orientString = exif.getAttribute(ExifInterface.TAG_ORIENTATION);
        int orientation = orientString != null ? Integer.parseInt(orientString) :  ExifInterface.ORIENTATION_NORMAL;

        int rotationAngle = 0;
        if (orientation == ExifInterface.ORIENTATION_ROTATE_90) rotationAngle = 90;
        if (orientation == ExifInterface.ORIENTATION_ROTATE_180) rotationAngle = 180;
        if (orientation == ExifInterface.ORIENTATION_ROTATE_270) rotationAngle = 270;

        Matrix matrix = new Matrix();
        matrix.setRotate(rotationAngle, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);
        Bitmap rotatedBitmap = Bitmap.createBitmap(bm, 0, 0, bounds.outWidth, bounds.outHeight, matrix, true);

        return rotatedBitmap;

    }
}
