package com.tidalsolutions.phillife.fragments;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.tidalsolutions.phillife.API;
import com.tidalsolutions.phillife.API_TEST;
import com.tidalsolutions.phillife.AsyncTaskListener;
import com.tidalsolutions.phillife.adapters.AnnouncementAdapter;
import com.tidalsolutions.phillife.phillife.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SantusIgnis on 23/06/2016.
 */
public class Fragment_Agent_Announcements extends Fragment implements AsyncTaskListener  {


    private SwipeRefreshLayout swipeRefreshLayout;
    ListView listView;



    public static Fragment_Agent_Announcements newInstance(Context c){

        Fragment_Agent_Announcements fa = new Fragment_Agent_Announcements();

        return fa;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        return inflater.inflate(R.layout.fragment_agent_announcement, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        init_fragment_announcements();

    }



    void init_fragment_announcements(){
        //customizedActionBar();
        listView = (ListView) getActivity().findViewById(R.id.listView);
        swipeRefreshLayout = (SwipeRefreshLayout) getActivity().findViewById(R.id.swipeActivity);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            Context c = getActivity();

            @Override
            public void onRefresh() {
                new API_TEST(getActivity(), Fragment_Agent_Announcements.this, true).execute("POST", "apij/agent_announcements/");
            }
        });
        new API_TEST(getActivity(), Fragment_Agent_Announcements.this, false).execute("POST", "apij/agent_announcements/");
        //new API(MainActivity.this, MainActivity.this, false).execute("POST", "api/announcements/");

    }



//    void customizedActionBar(){
//        getActivity().setRequestedOrientation(
//                ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
//        ActionBar ab = ((AppCompatActivity) getActivity())
//                .getSupportActionBar();
//        ab.setDisplayShowCustomEnabled(true);
//        ab.setDisplayShowTitleEnabled(false);
//        //(R.drawable.abc_ic_ab_back_mtrl_am_alpha
//        Drawable upArrow = getResources().getDrawable(R.drawable.icn_mainbar_side_menu);
//        //upArrow.setColorFilter(getResources().getColor(R.color.darkadobe), PorterDuff.Mode.SRC_ATOP);
//
//        ab.setHomeAsUpIndicator(upArrow);
//       /* ab.setIcon(R.drawable.ic_action_add);*/
//        LayoutInflater inflator = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View v = inflator.inflate(R.layout.toolbar_announcement_main_create, null);
//        TextView b = (TextView)v.findViewById(R.id.tv);
//        b.setText("Announcements");
//        ab.setCustomView(v);
//    }

    public void onTaskComplete(String result) {
        try {
            JSONObject jObj = new JSONObject(result);
            boolean success = jObj.getBoolean("success");

            if (swipeRefreshLayout.isRefreshing()) {
                swipeRefreshLayout.setRefreshing(false);
            }
            if (success) {
                List<Integer> id_list = new ArrayList<Integer>();
                List<String> title_list = new ArrayList<String>();
                List<String> content_list = new ArrayList<String>();
                List<String> image_list = new ArrayList<String>();
                List<String> date_list = new ArrayList<String>();
                List<String> profile_image_list = new ArrayList<String>();
                List<String> profile_name_list = new ArrayList<String>();

                JSONArray activities_data = jObj.optJSONArray("data");

                for (int i = 0; i < activities_data.length(); i++) {
                    JSONObject activity_data = activities_data.getJSONObject(i);
                    Integer id = activity_data.optInt("id");
                    String title = activity_data.optString("title");
                    String content = activity_data.optString("content");
                    String image = activity_data.optString("image");
                    String date_created = activity_data.optString("date_created");
                    String profile_image = activity_data.optString("profile_image");
                    String profile_name = activity_data.optString("profile_name");

                    id_list.add(id);
                    title_list.add(title);
                    content_list.add(content);
                    image_list.add(image);
                    date_list.add(date_created);
                    profile_name_list.add(profile_name);
                    profile_image_list.add(profile_image);
                }

                Integer id[] = id_list.toArray(new Integer[id_list.size()]);
                String title[] = title_list.toArray(new String[title_list.size()]);
                String content[] = content_list.toArray(new String[content_list.size()]);
                String image[] = image_list.toArray(new String[image_list.size()]);
                String date_created[] = date_list.toArray(new String[date_list.size()]);
                String profile_image[] = profile_image_list.toArray(new String[profile_image_list.size()]);
                String profile_name[] = profile_name_list.toArray(new String[profile_name_list.size()]);
                if(getActivity()!=null) {
                    AnnouncementAdapter announcementAdapter = new AnnouncementAdapter(getActivity(), id, title, content, image, date_created, profile_image, profile_name);
                    listView.setAdapter(announcementAdapter);
                }
            }
        } catch (JSONException e) {
            Log.e("error", "Error parsing data" + e.toString());
        }
    }



}
