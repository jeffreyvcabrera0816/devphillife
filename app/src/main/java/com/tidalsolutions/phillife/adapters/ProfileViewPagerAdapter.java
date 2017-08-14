package com.tidalsolutions.phillife.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.tidalsolutions.phillife.fragments.Fragment_Announcements;
import com.tidalsolutions.phillife.fragments.Fragment_Portal_Profile_View;
import com.tidalsolutions.phillife.fragments.Fragment_View_Community;

/**
 * Created by SantusIgnis on 24/06/2016.
 */
public class ProfileViewPagerAdapter extends FragmentStatePagerAdapter {



    Context c;
    Fragment f =null;
    int position;

    public ProfileViewPagerAdapter(Context c, FragmentManager fm, int position) {
        super(fm);

        this.position = position;
        this.c=c;
		/*if(am.size()>0&&position!=-1) {
			if(am.get(0).getDescription().equalsIgnoreCase("jurisprudence")||am.get(0).getDescription().equalsIgnoreCase("statutes")) {
				Articles_model retained = am.get(position);
				am.clear();
				am.add(retained);
			}
		}*/
    }

    @Override
    public Fragment getItem(int position) {

        if(position==0){

            return Fragment_Announcements.newInstance(c);
        }
        else if(position==1){
            return Fragment_View_Community.newInstance(c);
        }
        else if(position==2){
            return Fragment_Portal_Profile_View.newInstance(c);
        }
        return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        return "";
    }

    @Override
    public int getCount() {


        return 3;
    }
}
