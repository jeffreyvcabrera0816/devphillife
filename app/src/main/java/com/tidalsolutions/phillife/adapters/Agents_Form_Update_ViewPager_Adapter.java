package com.tidalsolutions.phillife.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.tidalsolutions.phillife.fragments.Fragment_Agent_Form_Picture_View;
import com.tidalsolutions.phillife.models.Pictures_model;

import java.util.ArrayList;

/**
 * Created by SantusIgnis on 27/09/2016.
 */
public class Agents_Form_Update_ViewPager_Adapter extends FragmentStatePagerAdapter {

    Context c;
    ArrayList<Pictures_model> pm;

    public Agents_Form_Update_ViewPager_Adapter(FragmentManager fm, Context c, ArrayList<Pictures_model> pm) {
        super(fm);
        this.c = c;
        this.pm = pm;
    }

    @Override
    public int getItemPosition(Object object) {
        // TODO Auto-generated method stub

        return POSITION_NONE;
    }

    @Override
    public Fragment getItem(int position) {

        return Fragment_Agent_Form_Picture_View.newInstance(c, pm.get(position));

    }

    @Override
    public int getCount() {
        return pm.size();
    }
}
