package com.tidalsolutions.phillife.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.widget.Toast;

import com.tidalsolutions.phillife.fragments.Fragment_Agent_Kept_Logged;
import com.tidalsolutions.phillife.fragments.Fragment_Agent_Login;
import com.tidalsolutions.phillife.fragments.Fragment_Teacher_Login;
import com.tidalsolutions.phillife.interfaces.PortalCallBack;
import com.tidalsolutions.phillife.utils.CONSTANT;
import com.tidalsolutions.phillife.utils.SharedPrefManager;

/**
 * Created by SantusIgnis on 24/06/2016.
 */
public class LoginViewPagerAdapter extends FragmentStatePagerAdapter {



    Context c;
    Fragment f =null;
    int position;

    public LoginViewPagerAdapter(Context c, FragmentManager fm, int position) {
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
            return Fragment_Teacher_Login.newInstance(c);
        }
        else if(position==1){

             SharedPrefManager sm = new SharedPrefManager(c);
//             Log.e("monkey", "monkey: " + sm.agentGet().getLogged());
//             Toast.makeText(c, ((str) sm.agentGet().getLogged()), Toast.LENGTH_SHORT).show();
             if (sm.agentGet().getLogged()) {
                 return Fragment_Agent_Kept_Logged.newInstance(c);
             } else {
                 return Fragment_Agent_Login.newInstance(c);
             }

        }

        return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if(position==0){
            return "TEACHERS";
        }
        else if(position==1){
            return "AGENTS";
        }
        return "";
    }

    @Override
    public int getCount() {


        return 2;
    }
}
