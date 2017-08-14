package com.tidalsolutions.phillife.master_activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.tidalsolutions.phillife.Settings;
import com.tidalsolutions.phillife.phillife.R;
import com.tidalsolutions.phillife.utils.ImagePicker;
import com.tidalsolutions.phillife.utils.SharedPrefManager;
import com.tidalsolutions.phillife.utils.Toaster;

import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;

import cz.msebera.android.httpclient.Header;

/**
 * Created by SantusIgnis on 14/07/2016.
 */
public class Teachers_Upload_Image_View extends AppCompatActivity {



    Button add_thread;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_image_view);

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

        add_thread = (Button) findViewById(R.id.add_photo);
        add_thread.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent chooseImageIntent = ImagePicker.getPickImageIntent(getApplicationContext(),false,"");
                startActivityForResult(chooseImageIntent, 205);


            }
        });

    }
    private Uri outputFileUri;
    Uri initialURI;
    void chooseImage(){

    }

    FileInputStream getSourceStream(Uri u) throws FileNotFoundException {
        FileInputStream out = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            ParcelFileDescriptor parcelFileDescriptor =
                    getApplicationContext().getContentResolver().openFileDescriptor(u, "r");
            FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
            out = new FileInputStream(fileDescriptor);
        } else {
            out = (FileInputStream) getApplication().getContentResolver().openInputStream(u);
        }
        return out;
    }


    private Bitmap decodeFile(InputStream f){
        try {
            //Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(f,null,o);

            //The new size we want to scale to
            final int REQUIRED_SIZE=70;

            //Find the correct scale value. It should be the power of 2.
            int scale=1;
            while(o.outWidth/scale/2>=REQUIRED_SIZE && o.outHeight/scale/2>=REQUIRED_SIZE)
                scale*=2;

            //Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize=scale;
            return BitmapFactory.decodeStream(f, null, o2);
        } catch (Exception e) {}
        return null;
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
       // if (resultCode == RESULT_OK) {
            if (requestCode == 205&&resultCode==RESULT_OK) {
                Log.e("something","something");
                final boolean isCamera;

                if (data == null) {
                    isCamera = true;

                        Log.e("camerax here","camerax here");

                }

                else {
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
                            Log.e("ulolz","ulolz"+imgPath);
                            //get uri


                            FileOutputStream os=null;
                            InputStream stream = getContentResolver().openInputStream(data.getData());




                            //decrease size first
                            bitmap = BitmapFactory.decodeStream(stream);

                            //bitmap = decodeFile(stream);

                            ByteArrayOutputStream bos = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 33 /*ignored for PNG*/, bos);


                            byte[] bitmapdata = bos.toByteArray();
                            ByteArrayInputStream bs = new ByteArrayInputStream(bitmapdata);

                            //decrease
                            int user_id = new SharedPrefManager(getApplicationContext()).userGet().getUser_id();
                            File dir = getApplicationContext().getDir("user_profile", Context.MODE_PRIVATE);
                            updatedImage = "image_"+user_id+"_profile_pic";
                            localStorage = dir.getAbsolutePath()+"/user_profile/"+updatedImage;

                            File f = new File(localStorage);

                            if(f.getParentFile()!=null){
                                f.getParentFile().mkdir();
                            }
                            os = new FileOutputStream(f);

                            int counter = -1;
                            byte[]blob = new byte[2048];
                            while((counter = bs.read(blob))!=-1){
                                os.write(blob, 0, counter);
                            }







                            stream.close();





                            //add_thread.setBackground(new BitmapDrawable(bitmap));
                           /* profile_pic.setImageBitmap(bitmap);
                            profile_pic.setVisibility(View.GONE);*/
                        }

                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }

                    }

                    else
                    {
                        bitmap=(Bitmap) data.getExtras().get("data");
                        //profile_pic.setImageBitmap(bitmap);
                    }

                    //stuff
                }

               /* Uri selectedImageUri;
                if ( isCamera == MediaStore.ACTION_IMAGE_CAPTURE.equals(data.getAction())) {
                    selectedImageUri = data.getData();
                } else {
                    selectedImageUri = data == null ? null : data.getData();
                }*/


                uploadImage(add_thread);
            }

        else if(resultCode==RESULT_CANCELED){

            }

       // }


    }



    String imgPath;
    ProgressDialog pd;
    String encodedString;
    Bitmap bitmap;
    RequestParams params = new RequestParams();
    String localStorage,updatedImage;
    public void uploadImage(View v) {
        // When Image is selected from Gallery
        if (imgPath != null && !imgPath.isEmpty()) {
            // ui_dialog.dismiss();
            pd = new ProgressDialog(getApplicationContext());
            pd.setMessage("Processing Request");
//            prgDialog.setMessage("Converting Image to Binary Data");
            //pd.show();
            // Convert image to String using Base64
            encodeImagetoString();
            // When Image is not selected from Gallery
        } else {
            Toast.makeText(getApplicationContext(), "You must select image from gallery before you try to upload", Toast.LENGTH_LONG).show();
        }
    }


    public void encodeImagetoString() {
        new AsyncTask<Void, Void, String>() {

            protected void onPreExecute() {

            };

            @Override
            protected String doInBackground(Void... params) {
                try {
                    BitmapFactory.Options options = null;
                    options = new BitmapFactory.Options();
                    options.inSampleSize = 2;
                    bitmap = BitmapFactory.decodeFile(imgPath, options);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    // Must compress the Image to reduce image size to make upload easy
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 35, stream);
                    byte[] byte_arr = stream.toByteArray();
                    // Encode Image to String
                    encodedString = Base64.encodeToString(byte_arr, 0);
                }catch (Exception e){
                    Log.e("asdasd","asdasd"+e);
                }
                return "";
            }

            @Override
            protected void onPostExecute(String msg) {
                Log.e("Processing Request", "Processing Request"+msg);
                pd.setMessage("Calling Upload");
                // Put converted Image string into Async Http Post param
                params.put("image", encodedString);
                params.put("filename", "profile_pic");
                // Trigger Image upload
                triggerImageUpload();
            }
        }.execute(null, null, null);
    }


    public void triggerImageUpload() {
        makeHTTPCall();
    }

    public void makeHTTPCall() {
        pd.setMessage("Uploading");
//        prgDialog.setMessage("Invoking PHP");
        AsyncHttpClient client = new AsyncHttpClient();
        // Don't forget to change the IP address to your LAN address. Port no as well.
        client.post(Settings.base_url + "apij/upload_image/users/"+new SharedPrefManager(getApplicationContext()).userGet().getSession_cookie()+"/",
                params, new AsyncHttpResponseHandler() {
                    // When the response returned by REST has Http
                    // response code '200'
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        //succesfuly uploaded
                        try {
                            String str = new String(responseBody, "UTF-8");
                            Log.e("error",""+str);
                            checkResponse(str);
                        }catch (Exception e){}
                        Log.e("result", "result" + statusCode);
                    }



                    // When the response returned by REST has Http
                    // response code other than '200' such as '404',
                    // '500' or '403' etc
                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        // Hide Progress Dialog
                        pd.hide();
                        // When Http response code is '404'
                        if (statusCode == 404) {
                            Toast.makeText(getApplicationContext(), "Requested resource not found. Please try again.", Toast.LENGTH_LONG).show();
                        }
                        // When Http response code is '500'
                        else if (statusCode == 500) {
                            Toast.makeText(getApplicationContext(), "Something went wrong at server. Please try again.", Toast.LENGTH_LONG).show();
                        }
                        // When Http response code other than 404, 500
                        else {
//                            Toast.makeText( getActivity().getApplicationContext(), "Error Occured \n Most Common Error: \n1. Device not connected to Internet\n2. Web App is not deployed in App server\n3. App server is not running\n HTTP Status code : "+ statusCode, Toast.LENGTH_LONG).show();
                            Toast.makeText(getApplicationContext(), "Connection Timeout. Please try again.", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    //image


    void checkResponse(String result){
        try {
            JSONObject jObj = new JSONObject(result);
            String service = jObj.getString("service");
            boolean success = jObj.getBoolean("success");

            if(success){
                new SharedPrefManager(getApplicationContext()).setPicture(localStorage);
                new SharedPrefManager(getApplicationContext()).updatePicture(updatedImage);
                finish();
            }
            else new Toaster(getApplicationContext(),"Upload Image Failed!");
        }catch (Exception e){

        }
    }


   /* @Override
    public void onTaskComplete(String result) {

        Log.e("result", "result" + result);


        try {
            JSONObject jObj = new JSONObject(result);
            String service = jObj.getString("service");
            boolean success = jObj.getBoolean("success");

            if(success){

                finish();
            }

        }catch (Exception e){

        }


    }*/
}
