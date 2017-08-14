package com.tidalsolutions.phillife.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.pkmmte.view.CircularImageView;
import com.tidalsolutions.phillife.API;
import com.tidalsolutions.phillife.AsyncTaskListener;
import com.tidalsolutions.phillife.master_activities.Announcement_Navigation;
import com.tidalsolutions.phillife.master_activities.Teachers_Upload_Image_View;
import com.tidalsolutions.phillife.models.User_Model;
import com.tidalsolutions.phillife.phillife.R;
import com.tidalsolutions.phillife.utils.ImageTools;
import com.tidalsolutions.phillife.utils.SharedPrefManager;

import org.json.JSONObject;

import java.io.File;
import java.net.URLEncoder;

/**
 * Created by SantusIgnis on 24/06/2016.
 */
public class Fragment_Portal_Profile_View extends Fragment implements AsyncTaskListener {

    CircularImageView profile_pic;
    TextView createAccount;
    ImageView input_icn_employee, input_icn_user, input_icn_location, input_icn_location2, input_icn_location3, input_icn_mobile, input_icn_email, input_icn_password;
    TextView fullname, employeeno, region, division, stationno, mobileno, email, password;
    Button button_logout;
    public static Fragment_Portal_Profile_View newInstance(Context c){

        Fragment_Portal_Profile_View fa = new Fragment_Portal_Profile_View();

        return fa;
    }


    @Override
    public void onResume() {
        super.onResume();
        applyPic();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        return inflater.inflate(R.layout.fragment_portal_profile_view, container, false);
    }

    void applyPic(){
        SharedPrefManager spm = new SharedPrefManager(getActivity());
        if(spm.getPicture().length()>3){
            ImageTools it = new ImageTools();
            it.set_imageview_image(getActivity(),"user_profile/image_"+spm.userGet().getUser_id()+"_profile_pic",profile_pic,"user_profile");
        }

    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initFTL();
        applyPic();
    }

    void goToUploadActivity(){
        Intent i = new Intent(getActivity(),Teachers_Upload_Image_View.class);
        startActivity(i);
    }

    void initFTL(){
        //customizedActionBar();
        imageTinting();

        profile_pic  = (CircularImageView) getActivity().findViewById(R.id.profile_pic);
        profile_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                goToUploadActivity();



            }
        });
        button_logout = (Button) getActivity().findViewById(R.id.button_logout);
        fullname = (TextView)getActivity().findViewById(R.id.tv_fullname);
        employeeno = (TextView)getActivity().findViewById(R.id.tv_empno);
        region = (TextView) getActivity().findViewById(R.id.tv_region);
        division = (TextView)getActivity().findViewById(R.id.tv_division);
        stationno = (TextView)getActivity().findViewById(R.id.tv_station_no);
        mobileno = (TextView)getActivity().findViewById(R.id.tv_mobile);
        email = (TextView) getActivity().findViewById(R.id.tv_email_address);

        User_Model um = new SharedPrefManager(getActivity()).userGet();

        fullname.setText(um.getFirstname()+" "+um.getLastname());
        employeeno.setText(um.getEmployee_no());
        region.setText(um.getRegion());
        division.setText(um.getDivision());
        stationno.setText(um.getStation_no());
        mobileno.setText(um.getMobile());
        email.setText(um.getEmail());

        button_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new API(getActivity(), Fragment_Portal_Profile_View.this, true).execute("POST", "apij/logout/" + URLEncoder.encode(new SharedPrefManager(getActivity()).userGet().getSession_cookie()) + "/");

            }
        });

    }


    void imageTinting(){


        input_icn_user = (ImageView) getActivity().findViewById(R.id.input_icn_user);
        input_icn_employee = (ImageView) getActivity().findViewById(R.id.input_icn_employee);
        input_icn_location = (ImageView) getActivity().findViewById(R.id.input_icn_location);
        input_icn_location2 = (ImageView) getActivity().findViewById(R.id.input_icn_location2);
        input_icn_location3 = (ImageView) getActivity().findViewById(R.id.input_icn_location3);
        input_icn_email = (ImageView) getActivity().findViewById(R.id.input_icn_email);
        input_icn_mobile = (ImageView) getActivity().findViewById(R.id.input_icn_mobile);
        input_icn_password = (ImageView) getActivity().findViewById(R.id.input_icn_password);




        Drawable icon1 = getResources().getDrawable(R.drawable.input_icn_user);
        icon1.setColorFilter(Color.parseColor("#FF57A301"), PorterDuff.Mode.SRC_ATOP);
        input_icn_user.setImageDrawable(icon1);

        Drawable icon2 = getResources().getDrawable(R.drawable.input_icn_employee);
        icon2.setColorFilter(Color.parseColor("#FF57A301"), PorterDuff.Mode.SRC_ATOP);
        input_icn_employee.setImageDrawable(icon2);

        Drawable icon3 = getResources().getDrawable(R.drawable.input_icn_location);
        icon3.setColorFilter(Color.parseColor("#FF57A301"), PorterDuff.Mode.SRC_ATOP);
        input_icn_location.setImageDrawable(icon1);
        input_icn_location.setImageDrawable(icon2);
        input_icn_location.setImageDrawable(icon3);

        Drawable icon4 = getResources().getDrawable(R.drawable.input_icn_email);
        icon4.setColorFilter(Color.parseColor("#FF57A301"), PorterDuff.Mode.SRC_ATOP);
        input_icn_email.setImageDrawable(icon4);

        Drawable icon5 = getResources().getDrawable(R.drawable.input_icn_mobile);
        icon5.setColorFilter(Color.parseColor("#FF57A301"), PorterDuff.Mode.SRC_ATOP);
        input_icn_mobile.setImageDrawable(icon4);


        Drawable icon6= getResources().getDrawable(R.drawable.input_icn_password);
        icon6.setColorFilter(Color.parseColor("#FF57A301"), PorterDuff.Mode.SRC_ATOP);
        input_icn_password.setImageDrawable(icon6);

        Drawable icon7= getResources().getDrawable(R.drawable.input_icn_mobile);
        icon7.setColorFilter(Color.parseColor("#FF57A301"), PorterDuff.Mode.SRC_ATOP);
        input_icn_mobile.setImageDrawable(icon7);



    }



    void customizedActionBar(){
        getActivity().setRequestedOrientation(
                ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        ActionBar ab = ((AppCompatActivity) getActivity())
                .getSupportActionBar();
        ab.setDisplayShowCustomEnabled(true);
        ab.setDisplayShowTitleEnabled(false);
        //(R.drawable.abc_ic_ab_back_mtrl_am_alpha
        Drawable upArrow = getResources().getDrawable(R.drawable.icn_mainbar_side_menu);
        //upArrow.setColorFilter(getResources().getColor(R.color.darkadobe), PorterDuff.Mode.SRC_ATOP);

        ab.setHomeAsUpIndicator(upArrow);
        ab.setIcon(R.drawable.ic_action_add);
        LayoutInflater inflator = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflator.inflate(R.layout.toolbar_announcement_main_create, null);
        TextView b = (TextView)v.findViewById(R.id.tv);
        b.setText("My Profile");
        ab.setCustomView(v);
    }

   /* String imgPath;
    ProgressDialog pd;
    String encodedString;
    Bitmap bitmap;
    RequestParams params = new RequestParams();





    public void uploadImage(View v) {
        // When Image is selected from Gallery
        if (imgPath != null && !imgPath.isEmpty()) {
           // ui_dialog.dismiss();
            pd.setMessage("Processing Request");
//            prgDialog.setMessage("Converting Image to Binary Data");
            pd.show();
            // Convert image to String using Base64
            encodeImagetoString();
            // When Image is not selected from Gallery
        } else {
            Toast.makeText(getActivity().getApplicationContext(), "You must select image from gallery before you try to upload", Toast.LENGTH_LONG).show();
        }
    }


    public void encodeImagetoString() {
        new AsyncTask<Void, Void, String>() {

            protected void onPreExecute() {

            };

            @Override
            protected String doInBackground(Void... params) {
                BitmapFactory.Options options = null;
                options = new BitmapFactory.Options();
                options.inSampleSize = 2;
                bitmap = BitmapFactory.decodeFile(imgPath, options);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                // Must compress the Image to reduce image size to make upload easy
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byte_arr = stream.toByteArray();
                // Encode Image to String
                encodedString = Base64.encodeToString(byte_arr, 0);
                return "";
            }

            @Override
            protected void onPostExecute(String msg) {
                pd.setMessage("Calling Upload");
                // Put converted Image string into Async Http Post param
                params.put("image", encodedString);
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
        client.post(Settings.base_url + "api/upload_image/users",
                params, new AsyncHttpResponseHandler() {
                    // When the response returned by REST has Http
                    // response code '200'
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        //succesfuly uploaded
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
                            Toast.makeText(getActivity().getApplicationContext(), "Requested resource not found. Please try again.", Toast.LENGTH_LONG).show();
                        }
                        // When Http response code is '500'
                        else if (statusCode == 500) {
                            Toast.makeText(getActivity().getApplicationContext(), "Something went wrong at server. Please try again.", Toast.LENGTH_LONG).show();
                        }
                        // When Http response code other than 404, 500
                        else {
//                            Toast.makeText( getActivity().getApplicationContext(), "Error Occured \n Most Common Error: \n1. Device not connected to Internet\n2. Web App is not deployed in App server\n3. App server is not running\n HTTP Status code : "+ statusCode, Toast.LENGTH_LONG).show();
                            Toast.makeText(getActivity().getApplicationContext(), "Connection Timeout. Please try again.", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    //image
*/
    @Override
    public void onTaskComplete(String result) {
        boolean success=false;
        Log.e("log", "" + result);
        try {
            JSONObject jObj = new JSONObject(result);
            success = jObj.getBoolean("success");


            if (success) {
                //delete caches
                File sd = getActivity().getDir("user_profile", Context.MODE_PRIVATE);
                File dir = new File(sd.getAbsolutePath() + "/user_profile/");
                if (dir.isDirectory())
                {
                    String[] children = dir.list();
                    for (int i = 0; i < children.length; i++)
                    {
                        new File(dir, children[i]).delete();
                    }
                }
                clearApplicationData();
                new SharedPrefManager(getActivity()).clearSave();
                getActivity().finish();

                Intent i = new Intent(getActivity(), Announcement_Navigation.class);
                startActivity(i);
            }
        }
        catch (Exception e){

        }
    }


    public void clearApplicationData()
    {
        File cache = getActivity().getCacheDir();
        File appDir = new File(cache.getParent());
        if (appDir.exists()) {
            String[] children = appDir.list();
            for (String s : children) {
                if (!s.equals("lib")) {
                    deleteDir(new File(appDir, s));Log.i("TAG", "**************** File /data/data/APP_PACKAGE/" + s + " DELETED *******************");
                }
            }
        }
    }

    public static boolean deleteDir(File dir)
    {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }

}
