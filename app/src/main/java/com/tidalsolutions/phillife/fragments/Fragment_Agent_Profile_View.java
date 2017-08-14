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
import android.widget.Toast;

import com.pkmmte.view.CircularImageView;
import com.tidalsolutions.phillife.API;
import com.tidalsolutions.phillife.AsyncTaskListener;
import com.tidalsolutions.phillife.master_activities.Announcement_Navigation;
import com.tidalsolutions.phillife.master_activities.Teachers_Upload_Image_View;
import com.tidalsolutions.phillife.models.Agent_Model;
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
public class Fragment_Agent_Profile_View extends Fragment implements AsyncTaskListener {

    CircularImageView profile_pic;
    TextView createAccount;
    ImageView input_icn_employee, input_icn_user, input_icn_location, input_icn_location2, input_icn_location3, input_icn_mobile, input_icn_email, input_icn_password;
    TextView fullname, employeeno, region, division, stationno, mobileno, email, password;
    Button button_logout;
    public static Fragment_Agent_Profile_View newInstance(Context c){

        Fragment_Agent_Profile_View fa = new Fragment_Agent_Profile_View();

        return fa;
    }


    @Override
    public void onResume() {
        super.onResume();
//        applyPic();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        return inflater.inflate(R.layout.fragment_agent_profile_view, container, false);
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
//        applyPic();
    }

    void goToUploadActivity(){
        Intent i = new Intent(getActivity(),Teachers_Upload_Image_View.class);
        startActivity(i);
    }

    void initFTL(){
        //customizedActionBar();
        imageTinting();

        button_logout = (Button) getActivity().findViewById(R.id.button_logout);
        fullname = (TextView)getActivity().findViewById(R.id.tv_fullname);
        email = (TextView) getActivity().findViewById(R.id.tv_email_address);

        Agent_Model um = new SharedPrefManager(getActivity()).agentGet();

        fullname.setText(um.getUsername());
        email.setText(um.getEmail());

        button_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                new API(getActivity(), Fragment_Agent_Profile_View.this, true).execute("POST", "apij/logout/" + URLEncoder.encode(new SharedPrefManager(getActivity()).userGet().getSession_cookie()) + "/");
//                clearApplicationData();
                new SharedPrefManager(getActivity()).clearAgent();
                getActivity().finish();

                Intent i = new Intent(getActivity(), Announcement_Navigation.class);
                startActivity(i);
            }
        });

    }


    void imageTinting(){


        input_icn_user = (ImageView) getActivity().findViewById(R.id.input_icn_user);
        input_icn_email = (ImageView) getActivity().findViewById(R.id.input_icn_email);




        Drawable icon1 = getResources().getDrawable(R.drawable.input_icn_user);
        icon1.setColorFilter(Color.parseColor("#FF57A301"), PorterDuff.Mode.SRC_ATOP);
        input_icn_user.setImageDrawable(icon1);

        Drawable icon4 = getResources().getDrawable(R.drawable.input_icn_email);
        icon4.setColorFilter(Color.parseColor("#FF57A301"), PorterDuff.Mode.SRC_ATOP);
        input_icn_email.setImageDrawable(icon4);

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
