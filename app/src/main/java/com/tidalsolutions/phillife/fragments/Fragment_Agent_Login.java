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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.tidalsolutions.phillife.API;
import com.tidalsolutions.phillife.API_TEST;
import com.tidalsolutions.phillife.AsyncTaskListener;
import com.tidalsolutions.phillife.interfaces.PortalCallBack;
import com.tidalsolutions.phillife.models.Agent_Model;
import com.tidalsolutions.phillife.phillife.R;
import com.tidalsolutions.phillife.utils.CONSTANT;
import com.tidalsolutions.phillife.utils.MD5;
import com.tidalsolutions.phillife.utils.SharedPrefManager;
import com.tidalsolutions.phillife.utils.Toaster;
import com.tidalsolutions.phillife.utils.Utils;

import org.json.JSONObject;

/**
 * Created by SantusIgnis on 24/06/2016.
 */
public class Fragment_Agent_Login extends Fragment implements AsyncTaskListener {

    Button login;
    EditText et_email, et_agent_code, et_password;
    public static Fragment_Agent_Login newInstance(Context c){

        Fragment_Agent_Login fa = new Fragment_Agent_Login();

        return fa;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        return inflater.inflate(R.layout.fragment_agent_login, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //customizedActionBar();


        et_email = (EditText) getActivity().findViewById(R.id.et_email);
        et_agent_code = (EditText) getActivity().findViewById(R.id.et_agent_code);
        et_password = (EditText) getActivity().findViewById(R.id.et_agent_password);

        login = (Button) getActivity().findViewById(R.id.login_agent);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  new Toaster(getActivity(),"This module is under construction");
                PortalCallBack pcb = (PortalCallBack) getActivity();
                pcb.callFragment(CONSTANT.FRAGMENT_PORTAL_AGENT_LOGIN_SUCCESS, 0, null, null, null);*/

//                new Toaster(getActivity(),"This module password "+et_password.getText().toString());
                if((et_email.getText().toString().length()>=4)&&(et_password.getText().toString().length()>=4)&&(et_agent_code.getText().toString().length()>=4)){
//                    new Toaster(getActivity(),"Logging in");
                    //Log.e("login","login");
                    new API_TEST(getActivity(), Fragment_Agent_Login.this, false).execute("POST", "apij/agent_login/"+et_email.getText().toString()+"/"+ MD5.crypt(et_password.getText().toString())+"/"+et_agent_code.getText().toString()+"/"+ Utils.getMac().toString());
                    //new API(getActivity(), Fragment_Teacher_Login.this, false).execute("POST", "api/announcements/");
                }


            }
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
        ab.setCustomView(v);
    }


    @Override
    public void onTaskComplete(String result) {

        try {
            JSONObject jObj = new JSONObject(result);
            boolean success = jObj.getBoolean("success");

            if (success) {
                //new Toaster(getActivity(),"Check your email!");
                Agent_Model um = new Agent_Model();

                JSONObject activity_data = jObj.optJSONObject("user_data");

                Integer agent_type = activity_data.optInt("agent_type");
                Integer id = activity_data.optInt("id");
                String email = activity_data.optString("email");
                String username = activity_data.optString("username");
                String employee_no = activity_data.optString("employee_no");
                String agent_code = activity_data.optString("agent_code");
                String mobile = activity_data.optString("mobile");
                String location = activity_data.optString("location");
                String last_login = activity_data.optString("last_login");
                String date_registered = activity_data.optString("date_registered");
                String cookie = activity_data.optString("cookie");

                um.set_agent_type(agent_type);
                um.set_id(id);
                um.setEmail(email);
                um.setUsername(username);
                um.setEmployee_no(mobile);
                um.setAgent_code(agent_code);
                um.setMobile(mobile);
                um.setLocation(location);
                um.setLast_login(last_login);
                um.setDate_registered(employee_no);
                um.setCookie(cookie);
                um.setLogged(true);

                SharedPrefManager sm = new SharedPrefManager(getActivity());
                sm.agentSaverModel(um);

                PortalCallBack pcb = (PortalCallBack) getActivity();
                pcb.callFragment(CONSTANT.FRAGMENT_PORTAL_AGENT_LOGIN_SUCCESS, 0, null, null, null);
            }
            else{
                new Toaster(getActivity(),"Invalid Credentials!");
            }
        }
        catch (Exception e){



        }


    }
}
