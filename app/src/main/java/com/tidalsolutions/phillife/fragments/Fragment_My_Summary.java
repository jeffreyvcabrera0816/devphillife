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
import android.widget.Toast;

import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.tidalsolutions.phillife.API_TEST;
import com.tidalsolutions.phillife.AsyncTaskListener;
import com.tidalsolutions.phillife.adapters.Agents_Mailbox_Adapter;
import com.tidalsolutions.phillife.models.Albums_model;
import com.tidalsolutions.phillife.phillife.R;
import com.tidalsolutions.phillife.utils.SharedPrefManager;
import com.tidalsolutions.phillife.utils.Toaster;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by SantusIgnis on 23/06/2016.
 */
public class Fragment_My_Summary extends Fragment implements AsyncTaskListener {

    View view;
    private SwipeRefreshLayout swipeRefreshLayout;



    public static Fragment_My_Summary newInstance(Context c){

        Fragment_My_Summary fa = new Fragment_My_Summary();

        return fa;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_my_summary, container, false);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        init_fragment_summary();

    }



    void init_fragment_summary(){
        //customizedActionBar();

        swipeRefreshLayout = (SwipeRefreshLayout) getActivity().findViewById(R.id.swipeActivitySUMMARY);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            Context c = getActivity();

            @Override
            public void onRefresh() {
                Context c = getActivity();
                String params = ""+new SharedPrefManager(getActivity()).agentGet().get_id();
                new API_TEST(getActivity(), Fragment_My_Summary.this, false).execute("POST", "apij/summary/"+params);
            }
        });

        String params = ""+new SharedPrefManager(getActivity()).agentGet().get_id();
        new API_TEST(getActivity(), Fragment_My_Summary.this, false).execute("POST", "apij/summary/" + params);

    }



    void customizedActionBar(){
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
        b.setText("My Summary");
        ab.setCustomView(v);
    }


    @Override
    public void onTaskComplete(String result) {
//        Log.e("result1",""+result);

        ArrayList<Albums_model> sampleModel = new ArrayList<>();
        try {
            JSONObject jObj = new JSONObject(result);
            boolean success = jObj.getBoolean("success");

            if (swipeRefreshLayout.isRefreshing()) {
                swipeRefreshLayout.setRefreshing(false);
            }
            if (success) {
                TextView one_month_rejected_count, three_month_rejected_count, year_rejected_count, one_month_approved_count,
                        three_month_approved_count, year_approved_count, one_month_total_count, three_month_total_count, year_total_count;

                one_month_rejected_count = (TextView) getActivity().findViewById(R.id.one_month_rejected_count);
                three_month_rejected_count = (TextView) getActivity().findViewById(R.id.three_month_rejected_count);
                year_rejected_count = (TextView) getActivity().findViewById(R.id.year_rejected_count);

                one_month_approved_count = (TextView) getActivity().findViewById(R.id.one_month_approved_count);
                three_month_approved_count = (TextView) getActivity().findViewById(R.id.three_month_approved_count);
                year_approved_count = (TextView) getActivity().findViewById(R.id.year_approved_count);
//
                one_month_total_count = (TextView) view.findViewById(R.id.one_month_total_count);
                three_month_total_count = (TextView) view.findViewById(R.id.three_month_total_count);
                year_total_count = (TextView) view.findViewById(R.id.year_total_count);
//
//                JSONArray activities_data = jObj.optJSONArray("data");
                JSONObject a = jObj.getJSONObject("data");

                one_month_total_count.setText(a.optString("one_month_total_count"));
                three_month_total_count.setText(a.optString("three_month_total_count"));
                year_total_count.setText(a.optString("year_total_count"));
                one_month_rejected_count.setText(a.optString("one_month_rejected_count"));
                three_month_rejected_count.setText(a.optString("three_month_rejected_count"));
                year_rejected_count.setText(a.optString("year_rejected_count"));
                one_month_approved_count.setText(a.optString("one_month_approved_count"));
                three_month_approved_count.setText(a.optString("three_month_approved_count"));
                year_approved_count.setText(a.optString("year_approved_count"));

//                String tc = activity_data.optString("total_count");
//                String ac = activity_data.optString("approved_count");
//                String rc = activity_data.optString("rejected_count");

//                total_count.setText(tc);
//                total_approved.setText(ac);
//                total_rejected.setText(rc);
//                JSONArray a = activities_data.getJSONArray(0);
//                String aa = a.getString('total_count');
//                String total_count_st, total_approved_st, total_rejected_st;

//                JSONObject a = activities_data.getJSONObject("total_count");
//                  Log.e("result1",""+activities_data);
//                Toast.makeText(getActivity(), activities_data.toString(), Toast.LENGTH_SHORT).show();



            }

//            ama = new Agents_Mailbox_Adapter(getActivity(),sampleModel);
//            lv = (ListView) getActivity().findViewById(R.id.listViewMAILBOX);
//            lv.setAdapter(ama);



            //lv.setSelection(cla.getCount() - 1);


        } catch (JSONException e) {
            Log.e("error", "Error parsing data" + e.toString());
        }
        Log.e("error", "error");
    }
}
