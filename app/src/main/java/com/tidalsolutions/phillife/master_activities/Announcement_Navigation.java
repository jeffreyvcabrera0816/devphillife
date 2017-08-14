package com.tidalsolutions.phillife.master_activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;

import com.tidalsolutions.phillife.adapters.Announcement_Menu_Adapter;
import com.tidalsolutions.phillife.fragments.Fragment_Announcements;
import com.tidalsolutions.phillife.models.SimpleImageTextMenuModel;
import com.tidalsolutions.phillife.phillife.R;
import com.tidalsolutions.phillife.utils.CONSTANT;
import com.tidalsolutions.phillife.utils.SharedPrefManager;
import com.tidalsolutions.phillife.utils.Toaster;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by SantusIgnis on 23/06/2016.
 */
public class Announcement_Navigation extends AppCompatActivity implements Serializable {

    DrawerLayout dl;
    ActionBarDrawerToggle drawerListener;
    ListView lv;
    Announcement_Menu_Adapter ama;
    ArrayList<SimpleImageTextMenuModel> menuEntries= new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.announcement_navigation);


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        init();
        set_behaviours();

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

    public void set_behaviours(){

        drawerListener = new ActionBarDrawerToggle(this, dl, R.string.app_name, R.string.logo);

        dl.setDrawerListener(drawerListener);

        if(getSupportActionBar() != null) {


             getSupportActionBar().setHomeButtonEnabled(true);
            //getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffff")));
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFFAFBF1")));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setIcon(R.drawable.announcement_border);


            getSupportActionBar().setDisplayUseLogoEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);


        }

        customizedActionBar();
    }
    int backpressed=0;
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if(backpressed==1){
            finish();

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }
    void customizedActionBar(){
        setRequestedOrientation(
                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ActionBar ab = ((AppCompatActivity) this)
                .getSupportActionBar();
        ab.setDisplayShowCustomEnabled(true);
        ab.setDisplayShowTitleEnabled(false);

        Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha );
        upArrow.setColorFilter(getResources().getColor(R.color.darkadobe), PorterDuff.Mode.SRC_ATOP);

        ab.setHomeAsUpIndicator(upArrow);
        ab.setIcon(R.drawable.ic_action_add);
        LayoutInflater inflator = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflator.inflate(R.layout.toolbar_announcement_main, null);
        ab.setCustomView(v);
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

        switch(item.getItemId())
        {
            case R.id.gago:
                String check = new SharedPrefManager(this).userGet().getSession_cookie();

                    finish();
                    Intent i = new Intent(this, Portal_Navigation.class);

                    startActivity(i);

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
        menuEntries.add(new SimpleImageTextMenuModel("announcement", R.drawable.input_icn_email));
        menuEntries.add(new SimpleImageTextMenuModel("help", R.drawable.input_icn_mobile));

        ama = new Announcement_Menu_Adapter(this, menuEntries);
        lv.setAdapter(ama);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    fragmentTransaction(CONSTANT.FRAGMENT_ANNOUNCEMENTS);
                }

            }
        });
        fragmentTransaction(CONSTANT.FRAGMENT_ANNOUNCEMENTS);
      /*  lv.performItemClick(
                lv.getAdapter().getView(0, null, null),
                0,
                lv.getAdapter().getItemId(0));*/
    }

    void fragmentTransaction(int mode){


        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        dl.closeDrawers();
        Fragment frag=null;

        //mode 9001 and 9002 are for jurisprudence
        //8000 for viewing main article
        //3000 notes 3002highlights
        if(mode== CONSTANT.FRAGMENT_ANNOUNCEMENTS) {
            frag = new Fragment_Announcements().newInstance(this);
           /* frag = new Fragment_JurisprudenceBabies();
            ft.replace(R.id.main, frag);
            ft.commit();*/
        }

        ft.replace(R.id.main, frag);
        ft.commit();
    }



}
