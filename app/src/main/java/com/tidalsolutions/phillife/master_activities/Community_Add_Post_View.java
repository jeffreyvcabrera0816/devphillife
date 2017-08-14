package com.tidalsolutions.phillife.master_activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.pkmmte.view.CircularImageView;
import com.tidalsolutions.phillife.AsyncTaskListener;
import com.tidalsolutions.phillife.Settings;
import com.tidalsolutions.phillife.interfaces.SpecialCallBack;
import com.tidalsolutions.phillife.phillife.R;
import com.tidalsolutions.phillife.utils.ImagePicker;
import com.tidalsolutions.phillife.utils.ImageTools;
import com.tidalsolutions.phillife.utils.ImageUniversalUploader;
import com.tidalsolutions.phillife.utils.SharedPrefManager;
import com.tidalsolutions.phillife.utils.Toaster;

import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.concurrent.Semaphore;

/**
 * Created by SantusIgnis on 14/07/2016.
 */
public class Community_Add_Post_View extends AppCompatActivity implements AsyncTaskListener, SpecialCallBack {


    TextView tv_post_name, tv_post_date, tv_post_title, tv_post_detail;
    EditText et_title, et_content;
    Button add_thread;
    CircularImageView civ;
    AQuery aq;
    RelativeLayout rl;
    ImageView upload_pic, preview_image, close_image;
    String imageFilePath;
    ProgressDialog pd;
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
        pd = new ProgressDialog(this);
        civ  = (CircularImageView) findViewById(R.id.forumImage);
        upload_pic = (ImageView) findViewById(R.id.upload_pic);
        preview_image = (ImageView) findViewById(R.id.preview_image);
        close_image = (ImageView) findViewById(R.id.close_image);
        rl = (RelativeLayout)findViewById(R.id.rl4);
        SharedPrefManager spm = new SharedPrefManager(getApplicationContext());
        File sd = getApplicationContext().getDir("user_profile", Context.MODE_PRIVATE);
        try {
            aq.id(civ).image(new File(sd.getAbsolutePath() + "/user_profile/" + spm.userGet().getImage_url()), 50).enabled(true);
        }catch (Exception e){
            aq.id(R.id.forumImage).image(R.drawable.portal_profile_sample).enabled(true);
        }


        et_title = (EditText) findViewById(R.id.et_title);
        et_content = (EditText) findViewById(R.id.et_content);


        et_title.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN)
                {
                    switch (keyCode)
                    {
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
                if (event.getAction() == KeyEvent.ACTION_DOWN)
                {
                    switch (keyCode)
                    {
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

        upload_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent chooseImageIntent = ImagePicker.getPickImageIntent(getApplicationContext(), false, "");

                startActivityForResult(chooseImageIntent, 205);
            }
        });

        close_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preview_image.setVisibility(View.GONE);
                close_image.setVisibility(View.GONE);
                rl.setVisibility(View.GONE);
                imgPath="";
            }
        });

        add_thread = (Button) findViewById(R.id.add_thread);
        add_thread.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if((et_title.getText().toString().length()>1&&et_content.getText().toString().length()>1)||imgPath!=null){
                    //Log.e("add tjread","add thread");
                    try {
                        /*new API(getApplicationContext(), Community_Add_Post_View.this, true).execute("POST", "apij/add_thread/" + URLEncoder.encode(new
                                SharedPrefManager(getApplicationContext()).userGet().getSession_cookie()) + "/" + URLEncoder.encode(et_title.getText().toString(), "UTF-8") + "/" + URLEncoder.encode(et_content.getText().toString(), "UTF-8"));*/
                        runDialog();
                        String api = Settings.base_url+"apij/upload_image_thread/threads/" + URLEncoder.encode(new
                        SharedPrefManager(getApplicationContext()).userGet().getSession_cookie()) + "/" + URLEncoder.encode(et_title.getText().toString()+" ", "UTF-8") + "/" + URLEncoder.encode(et_content.getText().toString()+" ", "UTF-8");

                        new ImageUniversalUploader(getApplicationContext(), Community_Add_Post_View.this, imageFilePath, api,pd,"uploading").uploadImage();
                        add_thread.setEnabled(false);
                        //finish();
                    }catch (Exception e){
                        add_thread.setEnabled(true);
                    }
                }
                //Log.e("session request",""+new SharedPrefManager(getApplicationContext()).userGet().getSession_cookie());

            }
        });

        add_thread.setOnKeyListener(new View.OnKeyListener() {
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

    }



    void runDialog(){

        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd = ProgressDialog.show(this, "Waiting...", "Please wait...");
    }

    String imgPath;
    Bitmap bitmap;
    String localStorage;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // if (resultCode == RESULT_OK) {
        if (requestCode == 205&&resultCode==RESULT_OK) {
            Log.e("something","something");
            final boolean isCamera;

            if (data == null) {
                isCamera = true;

            }

            else {
                Log.e("not null","not null");
                final String action = data.getAction();
                if (action == null) {
                    isCamera = false;
                } else {
                    isCamera = action.equals(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                }
                //stuff
                if(data.getData()!=null) {
                    try
                    {
                        if (bitmap != null)
                        {
                            bitmap.recycle();
                        }

                        //get uri path
                        Uri selectedImageUri = data.getData();
                        imgPath = getRealPathFromURI(selectedImageUri);


                        Semaphore mutex = new Semaphore(1);
                        //save to templocal
                        try {
                            FileOutputStream os = null;
                            InputStream stream = getContentResolver().openInputStream(data.getData());

                            //decrease size first
                            bitmap = BitmapFactory.decodeStream(stream);

                            //bitmap = decodeFile(stream);

                            ByteArrayOutputStream bos = new ByteArrayOutputStream();

                            bitmap.compress(Bitmap.CompressFormat.JPEG, 50 /*ignored for PNG*/, bos);


                            byte[] bitmapdata = bos.toByteArray();
                            ByteArrayInputStream bs = new ByteArrayInputStream(bitmapdata);

                            //decrease
                            int user_id = new SharedPrefManager(getApplicationContext()).userGet().getUser_id();
                            //File dir = getApplicationContext().getDir("user_profile", Context.MODE_PRIVATE);
                            File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),"");
                            String updatedImage = "temp" + System.currentTimeMillis() + "_thread_pic.jpg";
                             localStorage = dir.getAbsolutePath() + "/Phillife/" + updatedImage;
                            Log.e("local","local "+localStorage);

                            File f = new File(localStorage);

                            if (f.getParentFile() != null) {
                                f.getParentFile().mkdir();
                            }
                            os = new FileOutputStream(f);

                            int counter = -1;
                            byte[] blob = new byte[2048];
                            while ((counter = bs.read(blob)) != -1) {
                                os.write(blob, 0, counter);
                            }

                            stream.close();
                            mutex.acquire();
                        }catch (Exception e){}
                        mutex.release();
                        //save








                    }

                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                }

                else
                {
                    //bitmap=(Bitmap) data.getExtras().get("data");

                }


            }

            preview_image.setVisibility(View.VISIBLE);
            close_image.setVisibility(View.VISIBLE);
            rl.setVisibility(View.VISIBLE);
            bitmap.recycle();


            final Uri uri = Uri.parse(localStorage);
            final String path = uri.getPath();
            imageFilePath = path;
            ImageTools it = new ImageTools();
            preview_image.setImageDrawable(new BitmapDrawable(getResources(),it.decodeFile(new File(localStorage))));


            //uploadImage(add_thread);
        }

        else if(resultCode==RESULT_CANCELED){

        }



        //aq.id(R.id.preview_image).image(new File(localStorage),50);


      /* final Uri uri = Uri.parse(localStorage);
        final String path = uri.getPath();
        imageFilePath = path;
        final Drawable d = Drawable.createFromPath(path);
        preview_image.setImageDrawable(d);*/







    }


    public String getRealPathFromURI(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        @SuppressWarnings("deprecation")
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
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

    @Override
    public void specialCallBack(int position, Object data1) {
        Log.e("special callback "+position,"sepcai calback"+(String)data1);
        String result = (String)data1;
        //new Toaster(getApplicationContext(),"...");
        try {


            JSONObject jObj = new JSONObject(result);
            String service = jObj.getString("service");
            boolean success = jObj.getBoolean("success");

            if(success){
                new Toaster(getApplicationContext(),"Thread posted!");

                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        finish();
                    }
                }, 2000);


            }else{
                new Toaster(getApplicationContext(),"error occured! please try again");
                add_thread.setEnabled(true);
            }
        }catch (Exception e){

        }

    }
}
