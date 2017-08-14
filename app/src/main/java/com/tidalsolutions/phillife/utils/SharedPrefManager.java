package com.tidalsolutions.phillife.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.tidalsolutions.phillife.models.Agent_Model;
import com.tidalsolutions.phillife.models.User_Model;

/**
 * Created by SantusIgnis on 29/01/2016.
 */
public class SharedPrefManager {

    Context c;
    public SharedPrefManager(Context c){
        this.c=c;
    }

    public void userSaver(int id, String email, String first_name, String last_name, String mobile, String image, String region, String division, String station_no, String employee_no, String cookie){

        SharedPreferences s = c.getSharedPreferences("users",Context.MODE_PRIVATE);
        SharedPreferences.Editor x = s.edit();
        x.putInt("user_id", id);
        x.putString("email", email);
        x.putString("first_name",first_name );
        x.putString("last_name", last_name);
        x.putString("mobile", mobile);
        x.putString("image", image);
        x.putString("region", region);
        x.putString("division", division);
        x.putString("station_no", station_no);
        x.putString("employee_no", employee_no);
        x.putString("session_cookie", cookie);
        x.commit();
        Log.e("pref saved!", "saved");

    }


    public void agentSaver(int id, String email,  String username, String employee_no, String agent_code, String mobile, String location, String last_login, String date_registered, String cookie){

        SharedPreferences s = c.getSharedPreferences("agents",Context.MODE_PRIVATE);
        SharedPreferences.Editor x = s.edit();
        x.putInt("id", id);
        x.putString("email", email);
        x.putString("username",username );
        x.putString("employee_no", employee_no);
        x.putString("agent_code", agent_code);
        x.putString("mobile", mobile);
        x.putString("location", location);
        x.putString("last_login", last_login);
        x.putString("date_registered", date_registered);
        x.putString("cookie", cookie);
//        x.putBoolean("isLogged", )
        x.commit();
        Log.e("pref saved!", "saved");

    }

    public void agentSaverModel(Agent_Model u){
        SharedPreferences s = c.getSharedPreferences("agents",Context.MODE_PRIVATE);
        SharedPreferences.Editor x = s.edit();

        x.putInt("agent_type", u.get_agent_type());
        x.putInt("id", u.get_id());
        x.putString("email", u.getEmail());
        x.putString("username",u.getUsername() );
        x.putString("employee_no", u.getEmployee_no());
        x.putString("agent_code", u.getAgent_code());
        x.putString("mobile", u.getMobile());
        x.putString("location", u.getLocation());
        x.putString("last_login", u.getLast_login());
        x.putString("date_registered", u.getDate_registered());
        x.putString("cookie", u.getCookie());
        x.putBoolean("isLogged", u.getLogged());
        x.commit();


    }

    public Agent_Model agentGet(){
        Agent_Model um = new Agent_Model();
        SharedPreferences s = c.getSharedPreferences("agents", Context.MODE_PRIVATE);

        um.set_agent_type(s.getInt("agent_type", 0));
        um.set_id(s.getInt("id", -5000));
        um.setEmail(s.getString("email", ""));
        um.setUsername(s.getString("username", ""));
        um.setEmployee_no(s.getString("employee_no", ""));

        um.setAgent_code(s.getString("agent_code", ""));
        um.setMobile(s.getString("mobile", ""));

        um.setLocation(s.getString("location", ""));
        um.setLast_login(s.getString("last_login", ""));
        um.setDate_registered(s.getString("date_registered", ""));
        um.setCookie(s.getString("cookie", ""));
        um.setLogged(s.getBoolean("isLogged", false));
        return um;
    }

    public void clearSave(){
        SharedPreferences s = c.getSharedPreferences("users",Context.MODE_PRIVATE);
        SharedPreferences.Editor x = s.edit();
        x.clear();
        x.commit();

    }

    public void clearAgent(){
        SharedPreferences s = c.getSharedPreferences("agents",Context.MODE_PRIVATE);
        SharedPreferences.Editor x = s.edit();
        x.clear();
        x.commit();

    }

    public void userSaver(User_Model u){
        SharedPreferences s = c.getSharedPreferences("users",Context.MODE_PRIVATE);
        SharedPreferences.Editor x = s.edit();

        x.putInt("user_id", u.getUser_id());
        x.putString("email", u.getEmail());
        x.putString("first_name", u.getFirstname());
        x.putString("last_name", u.getLastname());
        x.putString("mobile", u.getMobile());
        x.putString("image", u.getImage_url());
        x.putString("region", u.getRegion());
        x.putString("division", u.getDivision());
        x.putString("station_no", u.getStation_no());
        x.putString("employee_no", u.getEmployee_no());
        x.putString("session_cookie", u.getSession_cookie());
        x.commit();


    }

    public User_Model userGet(){
       User_Model um = new User_Model();
        SharedPreferences s = c.getSharedPreferences("users", Context.MODE_PRIVATE);
        um.setUser_id(s.getInt("user_id", -5000));
        um.setEmail(s.getString("email", ""));
        um.setFirstname(s.getString("first_name", ""));
        um.setLastname(s.getString("last_name", ""));
        um.setMobile(s.getString("mobile", ""));
        um.setImage_url(s.getString("image", ""));
        um.setRegion(s.getString("region", ""));
        um.setDivision(s.getString("division", ""));
        um.setStation_no(s.getString("station_no", ""));
        um.setEmployee_no(s.getString("employee_no", ""));
        um.setSession_cookie(s.getString("session_cookie", ""));
        return um;
    }




    public void setUserThumbnail(int id){
        SharedPreferences s = c.getSharedPreferences("users",Context.MODE_PRIVATE);
        SharedPreferences.Editor x = s.edit();

        x.putString("pic_" + id, "pic_" + id);
        x.commit();
        Log.e("pref saved!", "saved");
    }
    public String getUserThumbnail(int id){

        SharedPreferences s = c.getSharedPreferences("users",Context.MODE_PRIVATE);
        SharedPreferences.Editor x = s.edit();

        return s.getString("pic_"+id,"");

    }
    public void updatePicture(String profile_pic){

        SharedPreferences s = c.getSharedPreferences("users",Context.MODE_PRIVATE);
        SharedPreferences.Editor x = s.edit();

        x.putString("image", profile_pic);
        x.commit();
        Log.e("pref saved!", "saved");

    }
    public void setPicture(String profile_pic){

        SharedPreferences s = c.getSharedPreferences("users",Context.MODE_PRIVATE);
        SharedPreferences.Editor x = s.edit();

        x.putString("local_picture", profile_pic);
        x.commit();
        Log.e("pref saved!", "saved");

    }
    public void setOnlinePicture(String profile_pic){

        SharedPreferences s = c.getSharedPreferences("users",Context.MODE_PRIVATE);
        SharedPreferences.Editor x = s.edit();

        x.putString("online_picture", profile_pic);
        x.commit();
        Log.e("pref saved!", "saved");

    }
    public String getPicture(){

        SharedPreferences s = c.getSharedPreferences("users",Context.MODE_PRIVATE);
        SharedPreferences.Editor x = s.edit();



        Log.i("annoying local picture", "localpicture " + s.getString("local_picture", ""));
       return s.getString("local_picture","");

    }









}
