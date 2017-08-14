package com.tidalsolutions.phillife.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.tidalsolutions.phillife.interfaces.SpecialCallBack;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

import cz.msebera.android.httpclient.Header;

/**
 * Created by SantusIgnis on 03/08/2016.
 */
public class ImageUniversalUploader {


    Context c;
    String imgPath;
    String api_url;
    ProgressDialog pd;
    String progress_message;
    SpecialCallBack scb;
    HashMap<String,String> additional;
    public ImageUniversalUploader(Context c, String imgPath, String api_url, ProgressDialog pd, String progress_message) {
        this.c = c;
        this.imgPath = imgPath;
        this.api_url = api_url;
        this.pd = pd;
        this.progress_message = progress_message;
        //pd = new ProgressDialog(c);
    }


    public ImageUniversalUploader(Context c,SpecialCallBack scb, String imgPath, String api_url, ProgressDialog pd, String progress_message) {
        this.c = c;
        this.imgPath = imgPath;
        this.api_url = api_url;
        this.pd = pd;
        this.scb=scb;
        this.progress_message = progress_message;
        pd.show();
        //pd = new ProgressDialog(c);
    }

    public ImageUniversalUploader(Context c,SpecialCallBack scb, String imgPath, String api_url, ProgressDialog pd, String progress_message,  HashMap<String,String>additional) {
        this.c = c;
        this.imgPath = imgPath;
        this.api_url = api_url;
        this.pd = pd;
        this.scb=scb;
        this.additional=additional;
        this.progress_message = progress_message;
        pd.show();
        //pd = new ProgressDialog(c);
    }

    String encodedString;

    RequestParams params = new RequestParams();


    int returner=0;


    public String uploadImage() {
        // When Image is selected from Gallery
        if (imgPath != null && !imgPath.isEmpty()) {
            // ui_dialog.dismiss();

            //pd.setMessage(progress_message);
//            prgDialog.setMessage("Converting Image to Binary Data");
            //pd.show();
            // Convert image to String using Base64
            encodeImagetoString();
            // When Image is not selected from Gallery
        } else {
            //Toast.makeText(c, "You must select image from gallery before you try to upload", Toast.LENGTH_LONG).show();
           /* pd = new ProgressDialog(c);
            pd.setMessage(progress_message);
//            prgDialog.setMessage("Converting Image to Binary Data");
            pd = ProgressDialog.show(c, "Waiting...", "Please wait");*/
            // Convert image to String using Base64
            encodeImagetoString();
        }

        return "";
    }

    public void encodeImagetoString() {
        new AsyncTask<Void, Void, String>() {

            protected void onPreExecute() {

            };

            @Override
            protected String doInBackground(Void... params) {
                try {
                    Bitmap bitmap;
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
                    Log.e("error", "error" + e);
                }
                return "";
            }

            @Override
            protected void onPostExecute(String msg) {
                Log.e("Processing Request" + encodedString, "Processing Requestz" + msg);
                //pd.setMessage("Calling Upload");
                // Put converted Image string into Async Http Post param

                    params.put("image", encodedString);
                    params.put("filename", "thread_pic_"+System.currentTimeMillis());
                    if(additional!=null){
                        params.put("filename", additional.get("filename"));
                        params.put("album", additional.get("album"));
                    }
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
        //sample Settings.base_url + "apij/upload_image_thread/users/" + new SharedPrefManager(c).userGet().getSession_cookie() + "/"
        client.post(api_url,
                params, new AsyncHttpResponseHandler() {
                    // When the response returned by REST has Http
                    // response code '200'
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        //succesfuly uploaded
                        try {
                            String str = new String(responseBody, "UTF-8");
                            Log.e("error", "" + str);
                            checkResponse(str);
                        } catch (Exception e) {
                        }
                        Log.e("result", "result" + statusCode);
                    }


                    // When the response returned by REST has Http
                    // response code other than '200' such as '404',
                    // '500' or '403' etc
                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        try {
                            String str = new String(responseBody, "UTF-8");
                            Log.e("error", "" + str);
                            checkResponse(str);
                        } catch (Exception e) {
                        }
                        Log.e("result", "result" + statusCode);
                        // Hide Progress Dialog
                        pd.hide();
                        // When Http response code is '404'
                        if (statusCode == 404) {
                            Toast.makeText(c, "Requested resource not found. Please try again.", Toast.LENGTH_LONG).show();
                        }
                        // When Http response code is '500'
                        else if (statusCode == 500) {
                            Toast.makeText(c, "Something went wrong at server. Please try again.", Toast.LENGTH_LONG).show();
                        }
                        // When Http response code other than 404, 500
                        else {
//                            Toast.makeText( getActivity().getApplicationContext(), "Error Occured \n Most Common Error: \n1. Device not connected to Internet\n2. Web App is not deployed in App server\n3. App server is not running\n HTTP Status code : "+ statusCode, Toast.LENGTH_LONG).show();
                            Toast.makeText(c, "Connection Timeout. Please try again.", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }


    void checkResponse(String result){




            pd.dismiss();
           /* Log.e("check response",""+result);
            new Toaster(c,"sending");
            pd.dismiss();



            try {
                sm.acquire();
                JSONObject jObj = new JSONObject(result);
                String service = jObj.getString("service");
                boolean success = jObj.getBoolean("success");

                if(success){


                }
                else new Toaster(c,"Upload Image Failed!");

            }catch (Exception e){

            }*/




        scb.specialCallBack(1, result);
    }

}
