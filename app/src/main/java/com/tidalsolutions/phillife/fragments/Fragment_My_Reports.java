package com.tidalsolutions.phillife.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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
import com.tidalsolutions.phillife.API_TEST;
import com.tidalsolutions.phillife.AsyncTaskListener;
import com.tidalsolutions.phillife.adapters.Agents_Reports_New_Adapter;
import com.tidalsolutions.phillife.master_activities.Agents_Form_New;
import com.tidalsolutions.phillife.master_activities.Agents_Form_Update;
import com.tidalsolutions.phillife.master_activities.Agents_Form_Update_Mailbox;
import com.tidalsolutions.phillife.master_activities.Agents_Form_View_Reports;
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
public class Fragment_My_Reports extends Fragment implements AsyncTaskListener {



    FloatingActionButton fla;
    ListView lv;
    private SwipyRefreshLayout swipeRefreshLayout;
    Agents_Reports_New_Adapter arna;
    public static Fragment_My_Reports newInstance(Context c){

        Fragment_My_Reports fa = new Fragment_My_Reports();

        return fa;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        return inflater.inflate(R.layout.fragment_my_reports, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        init_fragment_my_reports();

    }



    void init_fragment_my_reports(){
        //customizedActionBar();


        fla = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), Agents_Form_New.class);
                startActivity(i);
            }
        });

        swipeRefreshLayout = (SwipyRefreshLayout) getActivity().findViewById(R.id.swipeActivityFMR);

        swipeRefreshLayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {
                Context c = getActivity();
                String params = ""+new SharedPrefManager(getActivity()).agentGet().get_id();
                new API_TEST(getActivity(), Fragment_My_Reports.this, false).execute("POST", "apij/reports/"+params);
            }
        });
        String params = ""+new SharedPrefManager(getActivity()).agentGet().get_id();
        new API_TEST(getActivity(), Fragment_My_Reports.this, false).execute("POST", "apij/reports/" + params);

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
        b.setText("My Reports");
        ab.setCustomView(v);
    }


    @Override
    public void onTaskComplete(String result) {
        Log.e("result",""+result);

         final ArrayList<Albums_model> sampleModel = new ArrayList<>();
        try {
            JSONObject jObj = new JSONObject(result);
            boolean success = jObj.getBoolean("success");

            if (swipeRefreshLayout.isRefreshing()) {
                swipeRefreshLayout.setRefreshing(false);
            }
            if (success) {
                JSONArray activities_data = jObj.optJSONArray("user_data");
                for(int x=0; x<activities_data.length();x++){
                    JSONObject a = activities_data.getJSONObject(x);

                    Albums_model cma1 = new Albums_model();
//                    new Toaster(getActivity(),a.getString("form_hashkey"));
                    cma1.setForm_id(a.getString("form_hashkey"));
                    cma1.setClient_name(a.getString("client_name"));
                    cma1.setStatus(a.getInt("status"));
                    cma1.setNote(a.getString("note"));
                    cma1.setDate_created(a.getString("date_created"));
                    cma1.setDate_updated(a.getString("date_updated"));
//                    cma1.setDate_approved(a.getString("date_updated"));
//                    cma1.setDate_rejected(a.getString("date_updated"));
                    //downloadProfilePicture(cma1.getPoster_image(), cma1.getThread_user_id());
                    sampleModel.add(cma1);

                }


            }

            arna = new Agents_Reports_New_Adapter(getActivity(),sampleModel);
            lv = (ListView) getActivity().findViewById(R.id.listViewFMR);
            lv.setAdapter(arna);

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    Intent i = new Intent(getActivity(), Agents_Form_View_Reports.class);
                    i.putExtra("form", sampleModel.get(position));
                    startActivity(i);

                }
            });

            //lv.setSelection(cla.getCount() - 1);


        } catch (JSONException e) {
            Log.e("error", "Error parsing data" + e.toString());
        }
//        Log.e("error", "error" + sampleModel.get(0).getClient_name());
    }
}
