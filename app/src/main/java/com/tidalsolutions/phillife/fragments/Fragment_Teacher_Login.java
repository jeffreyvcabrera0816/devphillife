package com.tidalsolutions.phillife.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.tidalsolutions.phillife.API;
import com.tidalsolutions.phillife.AsyncTaskListener;
import com.tidalsolutions.phillife.dialogs.Forgot_Password_Post;
import com.tidalsolutions.phillife.interfaces.DialogCallBack;
import com.tidalsolutions.phillife.interfaces.PortalCallBack;
import com.tidalsolutions.phillife.master_activities.Teacher_Create_Account;
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
public class Fragment_Teacher_Login extends Fragment implements AsyncTaskListener, DialogCallBack {

    Button login;
    TextView createAccount, forgotPassword;
    EditText username, password;

    public static Fragment_Teacher_Login newInstance(Context c){

        Fragment_Teacher_Login fa = new Fragment_Teacher_Login();

        return fa;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        return inflater.inflate(R.layout.fragment_teacher_login, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initFTL();
    }

    void initFTL(){
        forgotPassword = (TextView) getActivity().findViewById(R.id.tv_forgot_password);
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Forgot_Password_Post.newInstance((DialogCallBack) Fragment_Teacher_Login.this, 0, 0).show(getChildFragmentManager(), null);

            }
        });
        createAccount = (TextView) getActivity().findViewById(R.id.create_account_tv);
        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PortalCallBack pcb = (PortalCallBack) getActivity();
                pcb.callFragment(CONSTANT.FRAGMENT_PORTAL_TO_CREATE_ACCOUNT,0,null,null,null);

                Intent i = new Intent(getActivity(), Teacher_Create_Account.class);
                startActivity(i);
            }
        });

        username = (EditText) getActivity().findViewById(R.id.et_username);
        password = (EditText) getActivity().findViewById(R.id.et_password);

        login = (Button) getActivity().findViewById(R.id.button_login);
        password.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN)
                {
                    switch (keyCode)
                    {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            login.performClick();
                            return true;
                        default:
                            break;
                    }
                }
                return false;

            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* PortalCallBack pcb = (PortalCallBack) getActivity();
                pcb.callFragment(CONSTANT.FRAGMENT_PORTAL_TEACHER_LOGIN_SUCCESS,0,null,null,null);*/
                if((username.getText().toString().length()>4)&&(password.getText().toString().length()>=4)){
                    //Log.e("login","login");
                    new API(getActivity(), Fragment_Teacher_Login.this, false).execute("POST", "api/login/"+username.getText().toString()+"/"+ MD5.crypt(password.getText().toString())+"/");
                    //new API(getActivity(), Fragment_Teacher_Login.this, false).execute("POST", "api/announcements/");
                }


            }
        });

    }


    @Override
    public void onTaskComplete(String result) {

        boolean success=false;
        Log.e("log",""+result);
        new Toaster(getActivity(),"Pleas wait...");
        try {
            JSONObject jObj = new JSONObject(result);
             success = jObj.getBoolean("success");
            String service = jObj.getString("service");
            if(service.equalsIgnoreCase("forgot_password")){
                if (success) {
                    new Toaster(getActivity(),"Check your email!");
                }
                else{
                    new Toaster(getActivity(),"Email is not registered!");
                }
            }
            else{
                    if (success) {
                        new Toaster(getActivity(),"Check your email!");
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

                        PortalCallBack pcb = (PortalCallBack) getActivity();
                        pcb.callFragment(CONSTANT.FRAGMENT_PORTAL_TEACHER_LOGIN_SUCCESS, 0, null, null, null);
                    }
                    else{
                        new Toaster(getActivity(),"Invalid Credentials!");
                    }
            }
        } catch (JSONException e) {
            Log.e("error", "Error parsing data" + e.toString());
        }


      /*  if(success){
            PortalCallBack pcb = (PortalCallBack) getActivity();
            pcb.callFragment(CONSTANT.FRAGMENT_PORTAL_TEACHER_LOGIN_SUCCESS,0,null,null,null);
        }*/
        //Log.e("share test",""+new SharedPrefManager(getActivity()).userGet().getEmail());

    }

    @Override
    public void dialogfunction(int position, int position2, Object data1, Object data2) {
        if(position==CONSTANT.DIALOG_FORGOT_PASSWORD){
            new API(getActivity(), Fragment_Teacher_Login.this, true).execute("POST", "apij/forgot_password/" + URLEncoder.encode((String) data1) + "/");

        }
    }
}
