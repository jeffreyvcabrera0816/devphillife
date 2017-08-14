package com.tidalsolutions.phillife.fragments;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.tidalsolutions.phillife.API;
import com.tidalsolutions.phillife.AsyncTaskListener;
import com.tidalsolutions.phillife.interfaces.PortalCallBack;
import com.tidalsolutions.phillife.models.User_Model;
import com.tidalsolutions.phillife.phillife.R;
import com.tidalsolutions.phillife.utils.CONSTANT;
import com.tidalsolutions.phillife.utils.MD5;
import com.tidalsolutions.phillife.utils.SharedPrefManager;
import com.tidalsolutions.phillife.utils.Toaster;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;

/**
 * Created by SantusIgnis on 24/06/2016.
 */
public class Fragment_Teacher_Create_Account extends Fragment implements AsyncTaskListener {

    TextView createAccount;
    TextView login;
    EditText et_firstname, et_lastname, et_employee, et_region, et_division,et_station, et_mobile, et_email,
            et_password, et_confirm;
    Button save;



    public static Fragment_Teacher_Create_Account newInstance(Context c){

        Fragment_Teacher_Create_Account fa = new Fragment_Teacher_Create_Account();

        return fa;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        return inflater.inflate(R.layout.fragment_teacher_create_account, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initFTL();
    }

    void initFTL(){
        final PortalCallBack pcb = (PortalCallBack)getActivity();
        customizedActionBar();
        login = (TextView) getActivity().findViewById(R.id.login);
        et_firstname = (EditText) getActivity().findViewById(R.id.cet_firstname);
        et_lastname = (EditText) getActivity().findViewById(R.id.cet_lastname);
        et_region = (EditText) getActivity().findViewById(R.id.cet_region);
        et_division = (EditText) getActivity().findViewById(R.id.cet_division);
        et_employee = (EditText) getActivity().findViewById(R.id.cet_employeeno);
        et_station = (EditText) getActivity().findViewById(R.id.cet_station_no);
        et_mobile = (EditText) getActivity().findViewById(R.id.cet_mobile_no);
        et_email = (EditText) getActivity().findViewById(R.id.cet_email_address);
        et_password = (EditText) getActivity().findViewById(R.id.cet_password);
        et_confirm = (EditText) getActivity().findViewById(R.id.cet_confirm_password);
        save = (Button) getActivity().findViewById(R.id.b_save);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pcb.callFragment(CONSTANT.FRAGMENT_LOGIN_CREATE, 0, null, null, null);
            }
        });

        et_confirm.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            save.performClick();
                            return true;
                        default:
                            break;
                    }
                }
                return false;

            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (et_firstname.getText().toString().length() > 1 && et_lastname.getText().toString().length() > 1
                        && et_region.getText().toString().length() > 1 && et_division.getText().toString().length() > 1
                        &&et_employee.getText().toString().length()>5
                        && et_station.getText().toString().length() > 1
                        && et_mobile.getText().toString().length() > 1
                        && et_email.getText().toString().length() > 1
                        && et_password.getText().toString().length() > 1
                        && et_confirm.getText().toString().length() > 1) {


                    if (et_password.getText().toString().equalsIgnoreCase(et_confirm.getText().toString())) {
                        String xurl = "/" + URLEncoder.encode(et_firstname.getText().toString()) + "/" + URLEncoder.encode(et_lastname.getText().toString()) + "/" + URLEncoder.encode(et_employee.getText().toString()) + "/" + URLEncoder.encode(et_region.getText().toString()) + "/" + URLEncoder.encode(et_division.getText().toString()) + "/" + URLEncoder.encode(et_station.getText().toString()) + "/" + et_mobile.getText().toString() + "/" + URLEncoder.encode(et_email.getText().toString()) + "/" + URLEncoder.encode(MD5.crypt(et_password.getText().toString())) + "/";
                        new API(getActivity(), Fragment_Teacher_Create_Account.this, false).execute("POST", "apij/register/" + xurl);
                    }


                }
                else if(et_employee.getText().toString().length()<6){
                    new Toaster(getActivity(),"Invalid License Number");
                }
                else{
                    new Toaster(getActivity(),"All fields should have more than 1 character");
                }

            }//onlick
        });

    }

    void customizedActionBar(){
        getActivity().setRequestedOrientation(
                ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        ActionBar ab = ((AppCompatActivity) getActivity())
                .getSupportActionBar();
        ab.setDisplayShowCustomEnabled(true);
        ab.setDisplayShowTitleEnabled(false);

        Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha );
        upArrow.setColorFilter(getResources().getColor(R.color.darkadobe), PorterDuff.Mode.SRC_ATOP);

        ab.setHomeAsUpIndicator(upArrow);
        ab.setIcon(R.drawable.ic_action_add);
        LayoutInflater inflator = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflator.inflate(R.layout.toolbar_announcement_main_left, null);
        Button b = (Button)v.findViewById(R.id.iv);
        b.setText("Create An Account");
        ab.setCustomView(v);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch(item.getItemId())
        {

            case android.R.id.home:
                Log.e("disabled","disabled");
                break;
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTaskComplete(String result) {
        Log.e("resulter",""+result);

        boolean success=false;
        //Log.e("log",""+result);
        try {
            JSONObject jObj = new JSONObject(result);
            success = jObj.getBoolean("success");


            if (success) {
                User_Model um = new User_Model();

                JSONObject activity_data = jObj.optJSONObject("user_data");




                Integer id = activity_data.optInt("id");
                String email = activity_data.optString("email");
                String firstname = activity_data.optString("first_name");
                String lastname = activity_data.optString("last_name");
                String mobile = activity_data.optString("mobile");
                String image = activity_data.optString("image");
                String region = activity_data.optString("region");
                String division = activity_data.optString("division");
                String station_no = activity_data.optString("station_no");
                String employee_no = activity_data.optString("employee_no");
                String cookie = activity_data.optString("cookie");

                um.setUser_id(id);
                um.setEmail(email);
                um.setFirstname(firstname);
                um.setLastname(lastname);
                um.setMobile(mobile);
                um.setImage_url(image);
                um.setRegion(region);
                um.setDivision(division);
                um.setStation_no(station_no);
                um.setEmployee_no(employee_no);
                um.setSession_cookie(cookie);

                SharedPrefManager sm = new SharedPrefManager(getActivity());
                sm.userSaver(um);



            }
        } catch (JSONException e) {
            Log.e("error", "Error parsing data" + e.toString());
        }


        if(success){
            PortalCallBack pcb = (PortalCallBack) getActivity();
            pcb.callFragment(CONSTANT.FRAGMENT_PORTAL_TEACHER_LOGIN_SUCCESS,0,null,null,null);
        }
        //Log.e("share test",""+new SharedPrefManager(getActivity()).userGet().getEmail());

    }

}
