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
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.tidalsolutions.phillife.adapters.LoginViewPagerAdapter;
import com.tidalsolutions.phillife.phillife.R;

/**
 * Created by SantusIgnis on 24/06/2016.
 */
public class Fragment_Login_View_Pager extends Fragment {

    ViewPager mPager;
    LoginViewPagerAdapter la;
    android.app.ActionBar ab;
    private Toolbar toolbar;
    private TabLayout tabLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        View v =inflater.inflate(R.layout.fragment_login_view_pager, container, false);

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Toast.makeText(getActivity(),"asdasd",Toast.LENGTH_LONG).show();

        initFLVP();

    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
        View v = inflator.inflate(R.layout.toolbar_announcement_main_left, null);
        Button b = (Button)v.findViewById(R.id.iv);
        b.setText(title);
        ab.setCustomView(v);
    }

    public static Fragment_Login_View_Pager newInstance(){
        Fragment_Login_View_Pager frv = new Fragment_Login_View_Pager();
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

    void initFLVP(){

       /* ActionBar ab = ((AppCompatActivity) getActivity())
                .getSupportActionBar();*/

        //ab.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);



        //ab.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        la = new LoginViewPagerAdapter(getActivity(),getChildFragmentManager(),0);

        mPager = (ViewPager) getView().findViewById(R.id.articles_view_pager);
        mPager.setAdapter(la);
        //mPager.setOffscreenPageLimit(0);

        tabLayout = (TabLayout) getView().findViewById(R.id.tabs);



        tabLayout.post(new Runnable() {
            @Override
            public void run() {

                tabLayout.setupWithViewPager(mPager);
                for (int g = 0; g < tabLayout.getTabCount(); g++) {

                    if(g==0){
                        customizedActionBar("LOGIN");
                        tabLayout.getTabAt(g).setText("TEACHERS");
                    } else {
                        tabLayout.getTabAt(g).setText("AGENTS");
                        customizedActionBar("LOGIN");
                    }
                    Log.e("tabs", "tabs");

                }

            }
        });



        /*  toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);*/


      /* */
    }

}
