package com.tidalsolutions.phillife.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.widget.Toast;

import com.tidalsolutions.phillife.fragments.Fragment_Agent_Announcements;
import com.tidalsolutions.phillife.fragments.Fragment_Agent_Profile_View;
import com.tidalsolutions.phillife.fragments.Fragment_Announcements;
import com.tidalsolutions.phillife.fragments.Fragment_Draft_Reports;
import com.tidalsolutions.phillife.fragments.Fragment_Mailbox;
import com.tidalsolutions.phillife.fragments.Fragment_My_Reports;
import com.tidalsolutions.phillife.fragments.Fragment_My_Summary;
import com.tidalsolutions.phillife.models.Agent_Model;
import com.tidalsolutions.phillife.utils.SharedPrefManager;

/**
 * Created by SantusIgnis on 24/06/2016.
 */
public class AgentViewPagerAdapter extends FragmentStatePagerAdapter {


    SharedPrefManager um;
    Context c;
    Fragment f =null;
    int position;

    public AgentViewPagerAdapter(Context c, FragmentManager fm, int position) {
        super(fm);
        um = new SharedPrefManager(c);
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
//        String ss = um.get_agent_type()
//        Toast.makeText(c, um.get_agent_type(), Toast.LENGTH_LONG).show();
        if (um.agentGet().get_agent_type() == 1) {
            if(position==0){
                return Fragment_Agent_Announcements.newInstance(c);
            }
            else if(position==1){
                return Fragment_My_Summary.newInstance(c);
            }
            else if(position==2){
                return Fragment_My_Reports.newInstance(c);
            }
            else if(position==3){
                return Fragment_Mailbox.newInstance(c);
            }
            else if(position==4){
                return new Fragment_Draft_Reports();
            }
            else if(position==5){
                return new Fragment_Agent_Profile_View();
            }
            return null;
        } else {
            if(position==0){
                return Fragment_Agent_Announcements.newInstance(c);
            }
            else if(position==1){
                return new Fragment_Agent_Profile_View();
            }
            return null;
        }


    }

    @Override
    public CharSequence getPageTitle(int position) {

        return "";
    }

    @Override
    public int getCount() {

        if (um.agentGet().get_agent_type() == 1) {
            return 6;
        } else {
            return 2;
        }


    }
}
