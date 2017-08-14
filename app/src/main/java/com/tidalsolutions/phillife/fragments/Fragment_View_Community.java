package com.tidalsolutions.phillife.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.tidalsolutions.phillife.API;
import com.tidalsolutions.phillife.AsyncTaskListener;
import com.tidalsolutions.phillife.Settings;
import com.tidalsolutions.phillife.adapters.CommunityListAdapter;
import com.tidalsolutions.phillife.master_activities.Community_Add_Post_View;
import com.tidalsolutions.phillife.master_activities.Community_Detailed_View;
import com.tidalsolutions.phillife.models.Community_Model;
import com.tidalsolutions.phillife.phillife.R;
import com.tidalsolutions.phillife.utils.DownloadThumbnails;
import com.tidalsolutions.phillife.utils.ImageTools;
import com.tidalsolutions.phillife.utils.SharedPrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

/**
 * Created by SantusIgnis on 24/06/2016.
 */
public class Fragment_View_Community extends Fragment  implements AsyncTaskListener {


    CommunityListAdapter cla;
    ListView lv;
    private SwipyRefreshLayout swipeRefreshLayout;
    FloatingActionButton fla;
    ProgressDialog pd;
    int page;
    public static Fragment_View_Community newInstance(Context c){

        Fragment_View_Community fa = new Fragment_View_Community();

        return fa;
    }

    @Override
    public void onResume() {
        super.onResume();
        initFVC();
//        cla.notifyDataSetChanged();
        Log.e("fragment resumed","resumed like hot");
    }

    @Override
    public void onTaskComplete(String result) {
        Log.e("result", "result" + result.toString());
        final ArrayList<Community_Model>sampleModel = new ArrayList<>();
        try {
            JSONObject jObj = new JSONObject(result);
            boolean success = jObj.getBoolean("success");

            if (swipeRefreshLayout.isRefreshing()) {
                swipeRefreshLayout.setRefreshing(false);
            }
            if (success) {
                JSONArray activities_data = jObj.optJSONArray("community_preview");
                for(int x=0; x<activities_data.length();x++){
                    JSONObject a = activities_data.getJSONObject(x);

                    Community_Model cma1 = new Community_Model();
                    cma1.setDate(a.getString("date_created"));
                    cma1.setTitle(a.getString("title"));
                    cma1.setId(a.getInt("id"));
                    cma1.setPost_image(a.getString("image"));
                    cma1.setUsername(a.getString("first_name") + " " + a.getString("last_name"));
                    cma1.setNotifications(a.getInt("comment_count"));
                    cma1.setThread_user_id(a.getInt("user_id"));
                    cma1.setPoster_image(a.getString("poster_image"));

                    //downloadProfilePicture(cma1.getPoster_image(), cma1.getThread_user_id());
                    sampleModel.add(cma1);

                }


            }

        } catch (JSONException e) {
            Log.e("error", "Error parsing data" + e.toString());
        }

        //downloadProfilePicture("image_18_profile_pic",18);
         Semaphore mutex = new Semaphore(1);

        try {

            DownloadThumbnails sc = new DownloadThumbnails(getActivity(), getActivity(), pd, sampleModel);
            sc.execute();
            mutex.acquire();
        }catch (Exception e){

        }
        mutex.release();

        cla = new CommunityListAdapter(getActivity(),sampleModel);

        ListView lv = (ListView) getActivity().findViewById(R.id.listViewFVC);
        cla.notifyDataSetChanged();
        lv.setAdapter(cla);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getActivity(), Community_Detailed_View.class);
                i.putExtra("thread_id", "" + sampleModel.get(position).getId());
                i.putExtra("thread_user_id", "" + sampleModel.get(position).getThread_user_id());
                startActivity(i);
            }
        });
       /* lv.setSelection(cla.getCount() - 1);*/
        lv.setSelection(cla.getCount() - 1);
    }

    void downloadProfilePicture(String image_path, int comment_user_id){

        SharedPrefManager spm = new SharedPrefManager(getActivity());

        if(spm.getUserThumbnail(comment_user_id).length()<1){
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            ImageTools it = new ImageTools();

            String[] as = it.downloadImage(getActivity(), Settings.image_url + image_path, "user_profile");

            if (as != null) {
                File dir = getActivity().getDir("user_profile", Context.MODE_PRIVATE);
                String localStorage = dir.getAbsolutePath() + "/user_profile/" + image_path;

                new SharedPrefManager(getActivity()).setUserThumbnail(comment_user_id);
            }
        }
        //Log.e("user pic " + spm.userGet().getImage_url(), "user pic " + Settings.image_url + image_path);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        return inflater.inflate(R.layout.fragment_view_community, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //customizedActionBar();
        initFVC();


    }

    void initFVC(){

        page = 5;
        pd = new ProgressDialog(getActivity());
        fla = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), Community_Add_Post_View.class);
                startActivity(i);
            }
        });

        Log.e("session", "session " + new SharedPrefManager(getActivity()).userGet().getSession_cookie());
        swipeRefreshLayout = (SwipyRefreshLayout) getActivity().findViewById(R.id.swipeActivityFVC);

        swipeRefreshLayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {
                Context c = getActivity();
                page+=5;
                new API(getActivity(), Fragment_View_Community.this, true).execute("POST", "apij/community_preview_paginated/" + URLEncoder.encode(new
                        SharedPrefManager(getActivity()).userGet().getSession_cookie()) + "/"+page);
            }
        });



        new API(getActivity(), Fragment_View_Community.this, true).execute("POST", "apij/community_preview_paginated/" + URLEncoder.encode(new
                SharedPrefManager(getActivity()).userGet().getSession_cookie()) + "/"+page);


    }





    void customizedActionBar(String title){
        getActivity().setRequestedOrientation(
                ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
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

}
