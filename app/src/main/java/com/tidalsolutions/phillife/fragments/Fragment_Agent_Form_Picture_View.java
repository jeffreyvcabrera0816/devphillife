package com.tidalsolutions.phillife.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.tidalsolutions.phillife.models.Pictures_model;
import com.tidalsolutions.phillife.phillife.R;

import java.io.File;

/**
 * Created by SantusIgnis on 27/09/2016.
 */
public class Fragment_Agent_Form_Picture_View extends Fragment {

    Pictures_model pm;
    ImageView iv_picture;
    TextView tv_test;
    public static Fragment_Agent_Form_Picture_View newInstance(Context c, Pictures_model pm){

        Fragment_Agent_Form_Picture_View fa = new Fragment_Agent_Form_Picture_View();
        Bundle args = new Bundle();
        args.putSerializable("info", pm);
        fa.setArguments(args);

        return fa;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        pm = (Pictures_model) getArguments().getSerializable("info");
        return inflater.inflate(R.layout.fragment_agent_form_picture_view, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //customizedActionBar();
        View view = getView();
        iv_picture = (ImageView) view.findViewById(R.id.iv_picture);

        Glide.with(this)
                .load(new File(pm.getFile_path()))
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(iv_picture);


    }

}
