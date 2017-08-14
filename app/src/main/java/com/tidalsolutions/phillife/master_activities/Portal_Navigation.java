package com.tidalsolutions.phillife.master_activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;

import com.tidalsolutions.phillife.AsyncTaskListener;
import com.tidalsolutions.phillife.adapters.Announcement_Menu_Adapter;
import com.tidalsolutions.phillife.db.DatabaseHandler;
import com.tidalsolutions.phillife.fragments.Fragment_Agent_View_Pager;
import com.tidalsolutions.phillife.fragments.Fragment_Announcements;
import com.tidalsolutions.phillife.fragments.Fragment_Login_View_Pager;
import com.tidalsolutions.phillife.fragments.Fragment_Profile_View_Pager;
import com.tidalsolutions.phillife.fragments.Fragment_Teacher_Create_Account;
import com.tidalsolutions.phillife.fragments.Fragment_Teacher_Edit_Account;
import com.tidalsolutions.phillife.interfaces.DialogCallBack;
import com.tidalsolutions.phillife.interfaces.GenericCallBack;
import com.tidalsolutions.phillife.interfaces.PortalCallBack;
import com.tidalsolutions.phillife.models.SimpleImageTextMenuModel;
import com.tidalsolutions.phillife.phillife.R;
import com.tidalsolutions.phillife.utils.CONSTANT;
import com.tidalsolutions.phillife.utils.SharedPrefManager;
import com.tidalsolutions.phillife.utils.Toaster;

import java.util.ArrayList;

/**
 * Created by SantusIgnis on 23/06/2016.
 */
public class Portal_Navigation extends AppCompatActivity implements PortalCallBack, GenericCallBack, DialogCallBack, AsyncTaskListener {

    DrawerLayout dl;



    ActionBarDrawerToggle drawerListener;
    ListView lv;
    Announcement_Menu_Adapter ama;
    ArrayList<SimpleImageTextMenuModel>menuEntries= new ArrayList<>();
    public int currentFragment = 0;
    public  int genericCall =0;

    @Override
    public void genericCall(int position, int position2, Object data, Object data2, Object data3) {
            if(position==CONSTANT.FRAGMENT_PORTAL_GENERIC_PROFILE_MENU){
                genericCall = CONSTANT.FRAGMENT_PORTAL_GENERIC_PROFILE_MENU;
            }
            else if(position==CONSTANT.FRAGMENT_PORTAL_MY_SUMMARY_MENU){
                genericCall = CONSTANT.FRAGMENT_PORTAL_MY_SUMMARY_MENU;
            }
            else if(position==CONSTANT.FRAGMENT_PORTAL_MY_REPORTS_MENU){
                genericCall = CONSTANT.FRAGMENT_PORTAL_MY_REPORTS_MENU;
            }
            else if(position==CONSTANT.FRAGMENT_PORTAL_MAILBOX_MENU){
                genericCall = CONSTANT.FRAGMENT_PORTAL_MAILBOX_MENU;
            }
           else if(position==CONSTANT.FRAGMENT_PORTAL_NOT_GENERIC_PROFILE_MENU){
                genericCall = CONSTANT.FRAGMENT_PORTAL_NOT_GENERIC_PROFILE_MENU;
            }
            else if (position==CONSTANT.FRAGMENT_PORTAL_DRAFTS_MENU) {
                genericCall = CONSTANT.FRAGMENT_PORTAL_DRAFTS_MENU;
            }

        invalidateOptionsMenu();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //db
        DatabaseHandler db = new DatabaseHandler(this);
        db.initializeDatabase();

        setContentView(R.layout.portal_navigation);
        //downloadProfilePicture();

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        init();
        set_behaviours();

        dl.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                if (currentFragment == CONSTANT.FRAGMENT_PORTAL_EDIT_GENERIC_PROFILE) {
                    dl.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                    onBackPressed();
                    Log.e("opened","drawer portal");
                }
                else{

                }
            }

            @Override
            public void onDrawerClosed(View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });


        //drawerListener = new ActionBarDrawerToggle(this, dl,  getSupportActionBar(), R.string.app_name, R.string.action_settings) {
            Intent i = getIntent();
            int mode = i.getIntExtra("mode",0);
            if(mode==CONSTANT.FRAGMENT_PORTAL_TEACHER_LOGIN_SUCCESS){
                //fragmentTransaction(CONSTANT.FRAGMENT_PORTAL_TEACHER_LOGIN_SUCCESS);
            }
        Log.e("int","inter "+mode);
        }

    //}




    //menu action bar stuff
    //##################################################
    //##################################################
    //##################################################
    //##################################################
    //##################################################
    //##################################################
    //##################################################
    //##################################################
    //##################################################
    //##################################################
    //##################################################
    //##################################################
    //##################################################
    //##################################################
    //##################################################

    public void set_behaviours(){

        drawerListener = new ActionBarDrawerToggle(this, dl, R.string.app_name, R.string.logo);

        dl.setDrawerListener(drawerListener);

        if(getSupportActionBar() != null) {


             getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#f8f8ea")));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setIcon(R.drawable.announcement_border);


            getSupportActionBar().setDisplayUseLogoEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);


        }

        //customizedActionBar();
    }

  /*  @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("portal!", "portal"+requestCode);
        Fragment_Profile_View_Pager f =(Fragment_Profile_View_Pager)getSupportFragmentManager().findFragmentById(R.id.main);
        f.onActivityResult(requestCode, resultCode, data);
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        if(genericCall==CONSTANT.FRAGMENT_PORTAL_GENERIC_PROFILE_MENU){
            inflater.inflate(R.menu.menu_reload, menu);
        }
        else if(genericCall==CONSTANT.FRAGMENT_PORTAL_MY_SUMMARY_MENU){
            inflater.inflate(R.menu.menu_reload, menu);
        }
        else if(genericCall==CONSTANT.FRAGMENT_PORTAL_MY_REPORTS_MENU){
            inflater.inflate(R.menu.menu_my_report, menu);
        }
        else if(genericCall==CONSTANT.FRAGMENT_PORTAL_MAILBOX_MENU){
            inflater.inflate(R.menu.menu_reload, menu);
        }
        else if(genericCall==CONSTANT.FRAGMENT_PORTAL_DRAFTS_MENU) {
            inflater.inflate(R.menu.menu_reload, menu);
        }
        else
        inflater.inflate(R.menu.menu_reload, menu);
        return true;
    }

    @Override
    public void invalidateOptionsMenu() {
        super.invalidateOptionsMenu();
        Log.e("invalidated", "okay");

    }



    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerListener.syncState();
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerListener.onConfigurationChanged(newConfig);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(drawerListener.onOptionsItemSelected(item)){
            return true;
        }
        if(item.getItemId() == android.R.id.home){
            Log.e("edit","edit");
        }
        switch(item.getItemId())
        {
            case R.id.edit:
                Log.e("edit","edit");
                fragmentTransaction(CONSTANT.FRAGMENT_PORTAL_EDIT_GENERIC_PROFILE);
                break;
//            case R.id.drafts:
//               Intent i = new Intent(Portal_Navigation.this,Agents_My_Reports_Drafts.class);
//                startActivity(i);
//                break;
//            case R.id.search:
//                Intent ii = new Intent(Portal_Navigation.this,Agents_My_Reports_Search_Drafts.class);
//                startActivity(ii);
//                break;
            case android.R.id.home:
                Log.e("disabled","disabled");
                break;
        }


        return super.onOptionsItemSelected(item);
    }
    //menu action bar stuff
    //##################################################
    //##################################################
    //##################################################
    //##################################################
    //##################################################
    //##################################################
    //##################################################
    //##################################################
    //##################################################
    //##################################################
    //##################################################
    //##################################################
    //##################################################
    //##################################################
    //##################################################
    //##################################################
    //##################################################
    //##################################################
    //##################################################
    //##################################################


    void init(){

        dl=(DrawerLayout) findViewById(R.id.drawerLayout);
        lv = (ListView) findViewById(R.id.list_view);
        //menuEntries.add(new SimpleImageTextMenuModel("announcement", R.drawable.input_icn_user));
        menuEntries.add(new SimpleImageTextMenuModel("help", R.drawable.input_icn_mobile));
        ama = new Announcement_Menu_Adapter(this, menuEntries);
        lv.setAdapter(ama);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    //fragmentTransaction(CONSTANT.FRAGMENT_ANNOUNCEMENTS);
                    new Toaster(getApplicationContext(),"under construction");
                }

            }
        });
        SharedPrefManager check = new SharedPrefManager(this);

        if (check.agentGet().getLogged()) {
            fragmentTransaction(CONSTANT.FRAGMENT_PORTAL_AGENT_LOGIN_SUCCESS);
        } else if ((check.userGet().getSession_cookie()).equalsIgnoreCase("")||check==null||(check.userGet().getSession_cookie()).length()<5) {
            fragmentTransaction(CONSTANT.FRAGMENT_LOGIN_CREATE);
        } else {
            fragmentTransaction(CONSTANT.FRAGMENT_PORTAL_TEACHER_LOGIN_SUCCESS);
        }

//        if((check.userGet().getSession_cookie()).equalsIgnoreCase("")||check==null||(check.userGet().getSession_cookie()).length()<5 || check.agentGet().getLogged() == false) {
//
//            fragmentTransaction(CONSTANT.FRAGMENT_LOGIN_CREATE);
//        } else if (check.agentGet().getLogged() == true) {
//            fragmentTransaction(CONSTANT.FRAGMENT_PORTAL_AGENT_LOGIN_SUCCESS);
//        } else {
//            fragmentTransaction(CONSTANT.FRAGMENT_PORTAL_TEACHER_LOGIN_SUCCESS);
//        }



    }



    int backpressed=0;
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
      /*  Log.e("on backed pressed",""+currentFragment);
        fragmentTransaction(CONSTANT.FRAGMENT_PORTAL_TEACHER_LOGIN_SUCCESS);*/
        Log.e("on backed pressed", "" + currentFragment);
       if(currentFragment==CONSTANT.FRAGMENT_PORTAL_EDIT_GENERIC_PROFILE) {
           fragmentTransaction(CONSTANT.FRAGMENT_PORTAL_TEACHER_LOGIN_SUCCESS);
       }
       else  if(currentFragment==CONSTANT.FRAGMENT_PORTAL_TO_CREATE_ACCOUNT) {


           fragmentTransaction(CONSTANT.FRAGMENT_LOGIN_CREATE);
       }else{
            if(backpressed==1){
                finish();
                Intent i = new Intent(Portal_Navigation.this,Announcement_Navigation.class);
                startActivity(i);
            }else{
                backpressed=1;
                new Toaster(getApplicationContext(),"Pressed back again to exit");
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        backpressed=0;
                    }
                }, 2000);
            }
       }

    }

    void fragmentTransaction(int mode){

        currentFragment = mode;
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        dl.closeDrawers();
        Fragment frag=null;

        //mode 9001 and 9002 are for jurisprudence
        //8000 for viewing main article
        //3000 notes 3002highlights
        if(mode== CONSTANT.FRAGMENT_ANNOUNCEMENTS) {
            frag = new Fragment_Announcements();
           /* frag = new Fragment_JurisprudenceBabies();
            ft.replace(R.id.main, frag);
            ft.commit();*/

        }
        else if(mode==CONSTANT.FRAGMENT_LOGIN_CREATE){
            frag =  new Fragment_Login_View_Pager();
        }
        else if(mode==CONSTANT.FRAGMENT_PORTAL_TO_CREATE_ACCOUNT){
            frag =  new Fragment_Teacher_Create_Account();
        }
        else if(mode==CONSTANT.FRAGMENT_PORTAL_TEACHER_LOGIN_SUCCESS){
            frag =  new Fragment_Profile_View_Pager();
        }
        else if(mode==CONSTANT.FRAGMENT_PORTAL_AGENT_LOGIN_SUCCESS){
            frag =  new Fragment_Agent_View_Pager();
        }
        else if(mode==CONSTANT.FRAGMENT_PORTAL_EDIT_GENERIC_PROFILE){
            frag =  new Fragment_Teacher_Edit_Account();
        }
        ft.replace(R.id.main, frag);
        ft.commit();
    }

    @Override
    public void dialogfunction(int position, int position2, Object data1, Object data2) {


    }

    @Override
    public void callFragment(int position, int position2,  Object data, Object data2, Object data3) {

        if(position==CONSTANT.FRAGMENT_PORTAL_TO_CREATE_ACCOUNT){
            finish();
            //fragmentTransaction(CONSTANT.FRAGMENT_PORTAL_TO_CREATE_ACCOUNT);
        }
        if(position==CONSTANT.FRAGMENT_PORTAL_TEACHER_LOGIN_SUCCESS){
            fragmentTransaction(CONSTANT.FRAGMENT_PORTAL_TEACHER_LOGIN_SUCCESS);
        }
        if(position==CONSTANT.FRAGMENT_PORTAL_MY_SUMMARY_MENU){
            fragmentTransaction(CONSTANT.FRAGMENT_PORTAL_MY_SUMMARY_MENU);
        }
        if(position==CONSTANT.FRAGMENT_PORTAL_AGENT_LOGIN_SUCCESS){
            fragmentTransaction(CONSTANT.FRAGMENT_PORTAL_AGENT_LOGIN_SUCCESS);
        }
        if(position==CONSTANT.FRAGMENT_LOGIN_CREATE){
            fragmentTransaction(CONSTANT.FRAGMENT_LOGIN_CREATE);
        }
    }

    @Override
    public void onTaskComplete(String result) {

    }
}
