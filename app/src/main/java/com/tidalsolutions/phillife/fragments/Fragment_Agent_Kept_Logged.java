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

import com.tidalsolutions.phillife.API_TEST;
import com.tidalsolutions.phillife.AsyncTaskListener;
import com.tidalsolutions.phillife.interfaces.PortalCallBack;
import com.tidalsolutions.phillife.models.Agent_Model;
import com.tidalsolutions.phillife.phillife.R;
import com.tidalsolutions.phillife.utils.CONSTANT;
import com.tidalsolutions.phillife.utils.MD5;
import com.tidalsolutions.phillife.utils.SharedPrefManager;
import com.tidalsolutions.phillife.utils.Toaster;

import org.json.JSONObject;

/**
 * Created by SantusIgnis on 24/06/2016.
 */
public class Fragment_Agent_Kept_Logged extends Fragment {

    Button login;
    EditText et_email, et_agent_code, et_password;
    public static Fragment_Agent_Kept_Logged newInstance(Context c){

        Fragment_Agent_Kept_Logged fa = new Fragment_Agent_Kept_Logged();

        return fa;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        return inflater.inflate(R.layout.fragment_agent_kept_logged, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //customizedActionBar();

        login = (Button) getActivity().findViewById(R.id.login_agent);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PortalCallBack pcb = (PortalCallBack) getActivity();
                pcb.callFragment(CONSTANT.FRAGMENT_PORTAL_AGENT_LOGIN_SUCCESS, 0, null, null, null);
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

}
