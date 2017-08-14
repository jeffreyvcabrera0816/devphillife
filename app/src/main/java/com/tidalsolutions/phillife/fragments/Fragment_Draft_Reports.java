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
import android.widget.Toast;

import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.tidalsolutions.phillife.API_TEST;
import com.tidalsolutions.phillife.AsyncTaskListener;
import com.tidalsolutions.phillife.adapters.Agents_Reports_Drafts_Adapter;
import com.tidalsolutions.phillife.adapters.Agents_Reports_New_Adapter;
import com.tidalsolutions.phillife.db.DatabaseHandler;
import com.tidalsolutions.phillife.master_activities.Agents_Form_New;
import com.tidalsolutions.phillife.master_activities.Agents_Form_Update;
import com.tidalsolutions.phillife.models.Albums_model;
import com.tidalsolutions.phillife.phillife.R;
import com.tidalsolutions.phillife.utils.SharedPrefManager;
import com.tidalsolutions.phillife.utils.Toaster;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Jeffrey on 10/16/2016.
 */

public class Fragment_Draft_Reports extends Fragment {

    Agents_Reports_Drafts_Adapter amrd;
    ListView lv_drafts;
    ArrayList<Albums_model> forms;
    DatabaseHandler dh;
    FloatingActionButton fla;
    ListView lv;
    private SwipyRefreshLayout swipeRefreshLayout;
    Agents_Reports_New_Adapter arna;
//    public static Fragment_My_Reports newInstance(Context c){
//
//        Fragment_My_Reports fa = new Fragment_Draft_Reports();
//
//        return fa;
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        return inflater.inflate(R.layout.agents_my_reports_drafts, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        arna.notifyDataSetChanged();
        init_cdv();

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
        b.setText("Draft Reports");
        ab.setCustomView(v);
    }

    public void init_cdv() {

        dh = new DatabaseHandler(getActivity());
        dh.getReadableDatabase();

        forms = dh.forms.getForms(1);

        lv_drafts = (ListView) getActivity().findViewById(R.id.lv_report_drafts);
        amrd = new Agents_Reports_Drafts_Adapter(getActivity(),forms);
        lv_drafts.setAdapter(amrd);
        amrd.notifyDataSetChanged();

        fla = (FloatingActionButton) getActivity().findViewById(R.id.fab1);
        fla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), Agents_Form_New.class);
                startActivity(i);
            }
        });

        lv_drafts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getActivity(),Agents_Form_Update.class);
                i.putExtra("form", forms.get(position));
                startActivity(i);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        init_cdv();
    }
}
