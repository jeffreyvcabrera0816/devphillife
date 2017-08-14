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
import android.widget.TextView;

import com.tidalsolutions.phillife.API;
import com.tidalsolutions.phillife.AsyncTaskListener;
import com.tidalsolutions.phillife.models.User_Model;
import com.tidalsolutions.phillife.phillife.R;
import com.tidalsolutions.phillife.utils.MD5;
import com.tidalsolutions.phillife.utils.SharedPrefManager;
import com.tidalsolutions.phillife.utils.Toaster;

import org.json.JSONObject;

import java.net.URLEncoder;

/**
 * Created by SantusIgnis on 24/06/2016.
 */
public class Fragment_Teacher_Edit_Account extends Fragment implements AsyncTaskListener {

    TextView createAccount;
    EditText et_firstname, et_lastname, et_employee, et_region, et_division,et_station, et_mobile, et_email,
    et_password, et_confirm;
    Button b_save;
    public static Fragment_Teacher_Edit_Account newInstance(Context c){

        Fragment_Teacher_Edit_Account fa = new Fragment_Teacher_Edit_Account();

        return fa;
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        return inflater.inflate(R.layout.fragment_teacher_edit_account, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initFTL();
    }

    void initFTL(){

        customizedActionBar();

        et_firstname = (EditText) getActivity().findViewById(R.id.et_firstname);
        et_lastname = (EditText) getActivity().findViewById(R.id.et_lastname);
        et_region = (EditText) getActivity().findViewById(R.id.et_region);
        et_division = (EditText) getActivity().findViewById(R.id.et_division);
        et_employee = (EditText) getActivity().findViewById(R.id.et_employee);
        et_station = (EditText) getActivity().findViewById(R.id.et_station);
        et_mobile = (EditText) getActivity().findViewById(R.id.et_mobile);
        et_email = (EditText) getActivity().findViewById(R.id.et_email);
        et_password = (EditText) getActivity().findViewById(R.id.et_password);
        et_confirm = (EditText) getActivity().findViewById(R.id.et_confirm);
        b_save = (Button) getActivity().findViewById(R.id.b_save);


        User_Model um = new SharedPrefManager(getActivity()).userGet();
        et_firstname.setText(um.getFirstname());
        et_lastname.setText(um.getLastname());
        et_region.setText(um.getRegion());
        et_division.setText(um.getDivision());
        et_employee.setText(um.getEmployee_no());
        et_station.setText(um.getStation_no());
        et_mobile.setText(um.getMobile());
        et_email.setText(um.getEmail());
        et_password.setText("");

        b_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(et_firstname.getText().toString().length()>1&&et_lastname.getText().toString().length()>1
                        &&et_region.getText().toString().length()>1&&et_division.getText().toString().length()>1
                        &&et_employee.getText().toString().length()>5
                        &&et_station.getText().toString().length()>1
                        &&et_mobile.getText().toString().length()>1
                        &&et_email.getText().toString().length()>1
                        &&et_password.getText().toString().length()>1
                        &&et_confirm.getText().toString().length()>1){

                        if(et_password.getText().toString().equalsIgnoreCase(et_confirm.getText().toString()) ){

                            //call api
                            ///update/4/boboxxy/catincox/123123/asdasd/division/station/123/boboxxx@gmail.combs/1a1dc91c907325c69271ddf0c944bc72
                            //phillifedev.tidalsolutions.com.ph/apij/update/boboxxy/catincox/123123/asdasd/division/station/123/boboxxx@gmail.combs/1a1dc91c907325c69271ddf0c944bc72
                           String xurl =  new SharedPrefManager(getActivity()).userGet().getUser_id() + "/" + URLEncoder.encode(et_firstname.getText().toString()) + "/" + URLEncoder.encode(et_lastname.getText().toString()) + "/" + URLEncoder.encode(et_employee.getText().toString()) + "/" + URLEncoder.encode(et_region.getText().toString()) + "/" + URLEncoder.encode(et_division.getText().toString()) + "/" + URLEncoder.encode(et_station.getText().toString()) + "/" + et_mobile.getText().toString() + "/" + URLEncoder.encode(et_email.getText().toString()) + "/" + URLEncoder.encode(MD5.crypt(et_password.getText().toString())) + "/";
                            Log.e("api", ""+xurl);

                            //new API(getActivity(), Fragment_Teacher_Edit_Account.this, false).execute("POST", "apij/update/"+new SharedPrefManager(getActivity()).userGet().getUser_id()+"/"+et_firstname.getText().toString()+"/"+et_lastname.getText().toString()+"/"+et_employee.getText().toString()+"/"+et_region.getText().toString()+"/"+et_division.getText().toString()+"/"+et_station.getText().toString()+"/"+et_mobile.getText().toString()+"/"+et_email.getText().toString()+"/"+ MD5.crypt(et_password.getText().toString()));

                            new API(getActivity(), Fragment_Teacher_Edit_Account.this, false).execute("POST", "apij/update/" + xurl);

                           /* new API(getActivity(), Fragment_Teacher_Edit_Account.this, false).execute("POST", "apij/update/"
                                             +4+
                                            "/"+et_firstname.getText().toString()+
                                            "/"+et_lastname.getText().toString()+
                                            "/"+et_employee.getText().toString()+
                                            "/"+et_region.getText().toString()+
                                            "/"+et_division.getText().toString()+
                                            "/"+et_station.getText().toString()+
                                            "/"+et_mobile.getText().toString()+
                                            "/"+et_email.getText().toString()+
                                            "/" + MD5.crypt(et_password.getText().toString())


                            );*/
                        }




                }
                else if(et_employee.getText().toString().length()<5){
                        new Toaster(getActivity(),"Invalid License number!");
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
        Button b = (Button)v.findViewById(R.id.iv);
        b.setText("EDIT PROFILE");
        ab.setCustomView(v);
    }


    @Override
    public void onTaskComplete(String result) {

        boolean success=false;

        Log.e("update","update "+result);
        try {
            JSONObject jObj = new JSONObject(result);
             success = jObj.getBoolean("success");
        }catch (Exception e){

        }

        if(success){
            Log.e("save data!","saved!");
            SharedPrefManager spm = new SharedPrefManager(getActivity());
            User_Model um = new User_Model();
            um.setEmployee_no(et_employee.getText().toString());
            um.setStation_no(et_station.getText().toString());
            um.setDivision(et_division.getText().toString());
            um.setRegion(et_region.getText().toString());
            um.setEmail(et_email.getText().toString());
            um.setFirstname(et_firstname.getText().toString());
            um.setLastname(et_lastname.getText().toString());
            um.setMobile(et_mobile.getText().toString());
            um.setSession_cookie(spm.userGet().getSession_cookie());
            um.setUser_id(spm.userGet().getUser_id());

            spm.userSaver(um);
        }

    }
}
