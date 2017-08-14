package com.tidalsolutions.phillife.fragments;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tidalsolutions.phillife.adapters.AgentViewPagerAdapter;
import com.tidalsolutions.phillife.interfaces.GenericCallBack;
import com.tidalsolutions.phillife.models.Agent_Model;
import com.tidalsolutions.phillife.phillife.R;
import com.tidalsolutions.phillife.utils.CONSTANT;
import com.tidalsolutions.phillife.utils.SharedPrefManager;

/**
 * Created by SantusIgnis on 24/06/2016.
 */
public class Fragment_Agent_View_Pager extends Fragment {

    ViewPager mPager;
    AgentViewPagerAdapter la;
    android.app.ActionBar ab;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    SharedPrefManager um;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        View v =inflater.inflate(R.layout.fragment_profile_view_pager, container, false);

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //downloadProfilePicture();
        //Toast.makeText(getActivity(),"asdasd",Toast.LENGTH_LONG).show();
        um = new SharedPrefManager(getActivity());
        initFLVP();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    /*void downloadProfilePicture(){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
        ImageTools it = new ImageTools();
        SharedPrefManager spm = new SharedPrefManager(getActivity());
        String[]as=it.downloadImage(getActivity(), Settings.image_url + spm.userGet().getImage_url(), "user_profile");
        if(as!=null){
            File dir = getActivity().getDir("user_profile", Context.MODE_PRIVATE);
            String localStorage = dir.getAbsolutePath()+"/user_profile/"+spm.userGet().getImage_url();

            new SharedPrefManager(getActivity()).setPicture(localStorage);
        }
        Log.e("user pic " + spm.userGet().getImage_url(), "user pic " + Settings.image_url + spm.userGet().getImage_url());

    }*/
   /* @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment f = la.getItem(mPager.getCurrentItem());
        f.onActivityResult(requestCode, resultCode, data);
        Log.e("hule!","hule"+requestCode);
    }*/

    public static Fragment_Agent_View_Pager newInstance(){
        Fragment_Agent_View_Pager frv = new Fragment_Agent_View_Pager();
        Bundle b = new Bundle();

       /* b.putInt("book_id", book_id);
        b.putInt("article_pos", article_pos);
        b.putInt("book_pos", book_pos);
        b.putInt("article_source", article_source);
        b.putString("description", description);

        frv.setArguments(b);
        Log.e("sequence tracker", "" + article_pos);*/



        return frv;
    }




    void customizedActionBar(String title){
        getActivity().setRequestedOrientation(
                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ActionBar ab = ((AppCompatActivity) getActivity())
                .getSupportActionBar();
        ab.setDisplayShowCustomEnabled(true);
        ab.setDisplayShowTitleEnabled(false);
        //(R.drawable.abc_ic_ab_back_mtrl_am_alpha
        Drawable upArrow = getResources().getDrawable(R.drawable.icn_mainbar_side_menu);
        //upArrow.setColorFilter(getResources().getColor(R.color.darkadobe), PorterDuff.Mode.SRC_ATOP);

        ab.setHomeAsUpIndicator(upArrow);
        /* ab.setIcon(R.drawable.ic_action_add);*/
        LayoutInflater inflator = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflator.inflate(R.layout.toolbar_announcement_main_create, null);
        TextView b = (TextView)v.findViewById(R.id.tv);
        b.setText(title);
        ab.setCustomView(v);
    }

    void initFLVP(){
        //db init

       /* DatabaseHandler db = new DatabaseHandler(getActivity());
        db.initializeDatabase();*/

       /* ActionBar ab = ((AppCompatActivity) getActivity())
                .getSupportActionBar();*/

        //ab.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);



        //ab.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        la = new AgentViewPagerAdapter(getActivity(),getChildFragmentManager(),0);

        mPager = (ViewPager) getView().findViewById(R.id.articles_view_pager);
        mPager.setAdapter(la);

        //mPager.setOffscreenPageLimit(0);

        tabLayout = (TabLayout) getView().findViewById(R.id.tabs);

        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                final GenericCallBack gcb = (GenericCallBack)getActivity();
                tabLayout.setupWithViewPager(mPager);
                for (int g = 0; g < tabLayout.getTabCount(); g++) {

                    if (um.agentGet().get_agent_type() == 1) {
                        if (g == 0) {
                            customizedActionBar("Announcements");
                            gcb.genericCall(CONSTANT.FRAGMENT_PORTAL_MY_SUMMARY_MENU,0,null,null,null);
                            tabLayout.getTabAt(g).setIcon(R.drawable.icn_tab_announcements_selected);


                        } else if (g == 1) {
                            tabLayout.getTabAt(g).setIcon(R.drawable.icn_tab_mysummary_active);

                        } else if (g == 2) {
                            tabLayout.getTabAt(g).setIcon(R.drawable.icn_tab_myreports_active);

                        } else if (g == 3) {
                            tabLayout.getTabAt(g).setIcon(R.drawable.icn_tab_mymailbox_active);

                        } else if (g == 4) {
                            tabLayout.getTabAt(g).setIcon(R.drawable.icn_tab_drafts_active);

                        } else{
                            tabLayout.getTabAt(g).setIcon(R.drawable.icn_tab_profile_active);
                        }
                    } else {
                        if (g == 0) {
                            customizedActionBar("Announcements");
                            gcb.genericCall(CONSTANT.FRAGMENT_PORTAL_MY_SUMMARY_MENU,0,null,null,null);
                            tabLayout.getTabAt(g).setIcon(R.drawable.icn_tab_announcements_selected);
                        }  else{
                            tabLayout.getTabAt(g).setIcon(R.drawable.icn_tab_profile_active);
                        }
                    }


                }

                tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {

                        if (um.agentGet().get_agent_type() == 1) {
                            if(tab.getPosition()==0){
                                customizedActionBar("Announcements");

                                gcb.genericCall(CONSTANT.FRAGMENT_PORTAL_MY_SUMMARY_MENU,0,null,null,null);
                                tabLayout.getTabAt(0).setIcon(R.drawable.icn_tab_announcements_selected);
                                tabLayout.getTabAt(1).setIcon(R.drawable.icn_tab_mysummary_active);
                                tabLayout.getTabAt(2).setIcon(R.drawable.icn_tab_myreports_active);
                                tabLayout.getTabAt(3).setIcon(R.drawable.icn_tab_mymailbox_active);
                                tabLayout.getTabAt(4).setIcon(R.drawable.icn_tab_drafts_active);
                                tabLayout.getTabAt(5).setIcon(R.drawable.icn_tab_profile_active);
                                mPager.setCurrentItem(0);

                            }
                            if(tab.getPosition()==1){
                                customizedActionBar("My Summary");

                                gcb.genericCall(CONSTANT.FRAGMENT_PORTAL_MY_REPORTS_MENU,0,null,null,null);
                                tabLayout.getTabAt(0).setIcon(R.drawable.icn_tab_announcements_active);
                                tabLayout.getTabAt(1).setIcon(R.drawable.icn_tab_mysummary_selected);
                                tabLayout.getTabAt(2).setIcon(R.drawable.icn_tab_myreports_active);
                                tabLayout.getTabAt(3).setIcon(R.drawable.icn_tab_mymailbox_active);
                                tabLayout.getTabAt(4).setIcon(R.drawable.icn_tab_drafts_active);
                                tabLayout.getTabAt(5).setIcon(R.drawable.icn_tab_profile_active);
                                mPager.setCurrentItem(1);
                            }
                            if(tab.getPosition()==2){
                                customizedActionBar("My Reports");

                                gcb.genericCall(CONSTANT.FRAGMENT_PORTAL_MAILBOX_MENU,0,null,null,null);
                                tabLayout.getTabAt(0).setIcon(R.drawable.icn_tab_announcements_active);
                                tabLayout.getTabAt(1).setIcon(R.drawable.icn_tab_mysummary_active);
                                tabLayout.getTabAt(2).setIcon(R.drawable.icn_tab_myreports_selected);
                                tabLayout.getTabAt(3).setIcon(R.drawable.icn_tab_mymailbox_active);
                                tabLayout.getTabAt(4).setIcon(R.drawable.icn_tab_drafts_active);
                                tabLayout.getTabAt(5).setIcon(R.drawable.icn_tab_profile_active);
                                mPager.setCurrentItem(2);
                            }
                            if(tab.getPosition()==3){
                                customizedActionBar("Mailbox");

                                gcb.genericCall(CONSTANT.FRAGMENT_PORTAL_GENERIC_PROFILE_MENU,0,null,null,null);
                                tabLayout.getTabAt(0).setIcon(R.drawable.icn_tab_announcements_active);
                                tabLayout.getTabAt(1).setIcon(R.drawable.icn_tab_mysummary_active);
                                tabLayout.getTabAt(2).setIcon(R.drawable.icn_tab_myreports_active);
                                tabLayout.getTabAt(3).setIcon(R.drawable.icn_tab_mymailbox_selected);
                                tabLayout.getTabAt(4).setIcon(R.drawable.icn_tab_drafts_active);
                                tabLayout.getTabAt(5).setIcon(R.drawable.icn_tab_profile_active);
                                mPager.setCurrentItem(3);
                            }
                            if(tab.getPosition()==4){
                                customizedActionBar("Draft Reports");

                                gcb.genericCall(CONSTANT.FRAGMENT_PORTAL_GENERIC_PROFILE_MENU,0,null,null,null);
                                tabLayout.getTabAt(0).setIcon(R.drawable.icn_tab_announcements_active);
                                tabLayout.getTabAt(1).setIcon(R.drawable.icn_tab_mysummary_active);
                                tabLayout.getTabAt(2).setIcon(R.drawable.icn_tab_myreports_active);
                                tabLayout.getTabAt(3).setIcon(R.drawable.icn_tab_mymailbox_active);
                                tabLayout.getTabAt(4).setIcon(R.drawable.icn_tab_drafts_selected);
                                tabLayout.getTabAt(5).setIcon(R.drawable.icn_tab_profile_active);
                                mPager.setCurrentItem(4);
                            }

                            if(tab.getPosition()==5){
                                customizedActionBar("Profile");

                                gcb.genericCall(CONSTANT.FRAGMENT_PORTAL_GENERIC_PROFILE_MENU,0,null,null,null);
                                tabLayout.getTabAt(0).setIcon(R.drawable.icn_tab_announcements_active);
                                tabLayout.getTabAt(1).setIcon(R.drawable.icn_tab_mysummary_active);
                                tabLayout.getTabAt(2).setIcon(R.drawable.icn_tab_myreports_active);
                                tabLayout.getTabAt(3).setIcon(R.drawable.icn_tab_mymailbox_active);
                                tabLayout.getTabAt(4).setIcon(R.drawable.icn_tab_drafts_active);
                                tabLayout.getTabAt(5).setIcon(R.drawable.icn_tab_profile_selected);
                                mPager.setCurrentItem(5);
                            }
                        } else {
                            if(tab.getPosition()==0){
                                customizedActionBar("Announcements");

                                gcb.genericCall(CONSTANT.FRAGMENT_PORTAL_MY_SUMMARY_MENU,0,null,null,null);
                                tabLayout.getTabAt(0).setIcon(R.drawable.icn_tab_announcements_selected);
                                tabLayout.getTabAt(1).setIcon(R.drawable.icn_tab_profile_active);
                                mPager.setCurrentItem(0);

                            }

                            if(tab.getPosition()==1){
                                customizedActionBar("Profile");

                                gcb.genericCall(CONSTANT.FRAGMENT_PORTAL_GENERIC_PROFILE_MENU,0,null,null,null);
                                tabLayout.getTabAt(0).setIcon(R.drawable.icn_tab_announcements_active);
                                tabLayout.getTabAt(1).setIcon(R.drawable.icn_tab_profile_selected);
                                mPager.setCurrentItem(1);
                            }
                        }

                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {

                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {

                    }
                });
            }
        });

    }

}
