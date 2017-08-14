package com.tidalsolutions.phillife.adapters;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.pkmmte.view.CircularImageView;
import com.tidalsolutions.phillife.Settings;
import com.tidalsolutions.phillife.models.Community_Model;
import com.tidalsolutions.phillife.phillife.R;
import com.tidalsolutions.phillife.utils.ImageTools;
import com.tidalsolutions.phillife.utils.SharedPrefManager;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by SantusIgnis on 12/07/2016.
 */
public class CommunityListAdapter extends BaseAdapter {

    Context c;
    ArrayList<Community_Model> cm;
    private AQuery aq;
    public CommunityListAdapter(Context c, ArrayList<Community_Model> cm) {
        this.c = c;
        this.cm = cm;
        //Collections.reverse(cm);
    }

    @Override
    public int getCount() {
        return cm.size();
    }

    @Override
    public Object getItem(int position) {

        return cm.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }




    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v;

        if(convertView==null){

            LayoutInflater li = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = li.inflate(R.layout.community_preview_row,parent,false);
            this.aq = new AQuery(v);
        }
        else{
            v=convertView;
        }
        this.aq = new AQuery(v);


        TextView tv_title = (TextView) v.findViewById(R.id.tv_schedule);
        TextView tv_subject = (TextView) v.findViewById(R.id.tv_title);
        TextView tv_by = (TextView) v.findViewById(R.id.tv_by);
        TextView tv_comment_count= (TextView) v.findViewById(R.id.tv_number_of_comments);
        ImageView with_image = (ImageView)v.findViewById(R.id.ix);
        CircularImageView iv = (CircularImageView)v.findViewById(R.id.forumImage);

        tv_title.setText(cm.get(position).getDate());
        tv_subject.setText(cm.get(position).getTitle());
        tv_by.setText(cm.get(position).getUsername());
        tv_comment_count.setText("" + cm.get(position).getNotifications());



        if(cm.get(position).getPost_image().length()>0){
                with_image.setVisibility(View.VISIBLE);
        }else  with_image.setVisibility(View.GONE);


     /*   SharedPrefManager spm = new SharedPrefManager(c);
        File sd = c.getDir("user_profile", Context.MODE_PRIVATE);
        //Log.e("check","check"+position);
        if((cm.get(position).getThread_user_id()==spm.userGet().getUser_id())&&cm.get(position).getPoster_image().length()>3&&!cm.get(position).getPoster_image().equalsIgnoreCase("")) {

            //it.set_imageview_image(c, "user_profile/" + spm.userGet().getUser_id() + "_profile_pic", iv, "user_profile");
            //Log.e("error:"+cm.get(position).getPoster_image(),"error");
            try {
                aq.id(R.id.forumImage).image(new File(sd.getAbsolutePath() + "/user_profile/" + spm.userGet().getImage_url()), 50).enabled(true);
            }catch (Exception e){
                Log.e("error:"+sd.getAbsolutePath() + "/user_profile/" + spm.userGet().getImage_url(),"error"+e);
            }
        }

        else{

            try {
                if (cm.get(position).getPoster_image().length() > 3 && !cm.get(position).getPoster_image().equalsIgnoreCase("")) {
                    //aq.id(R.id.forumImage).image(Settings.image_url + cm.get(position).getPoster_image(), true, false).enabled(true);


                    aq.id(R.id.forumImage).image(new File(sd.getAbsolutePath() + "/user_profile/" + cm.get(position).getPoster_image()), 50).enabled(true);
                    //Log.e("loading preview images", "" + sd.getAbsolutePath() + "/user_profile/" + cm.get(position).getPoster_image());
                } else aq.id(R.id.forumImage).image(R.drawable.portal_profile_sample).enabled(true);
            }catch (Exception e){Log.e("",""+e);}
        }
*/

        SharedPrefManager spm = new SharedPrefManager(c);
        File sd = c.getDir("user_profile", Context.MODE_PRIVATE);
        ImageTools it = new ImageTools();
        //if thread user is same as user logged in
        if((cm.get(position).getThread_user_id()==spm.userGet().getUser_id())&&cm.get(position).getPoster_image().length()>3&&!cm.get(position).getPoster_image().equalsIgnoreCase("")) {
            iv.setVisibility(View.VISIBLE);

            //Log.e("thread user: "+cm.get(position).getThread_user_id(),"user: "+spm.userGet().getUser_id());
            try {
                final Uri uri = Uri.parse(sd.getAbsolutePath() + "/user_profile/" + spm.userGet().getImage_url());
                final String path = uri.getPath();
                final Drawable d = Drawable.createFromPath(path);
                iv.setImageDrawable(d);
            }catch (Exception e){
                //Log.e("error:" + sd.getAbsolutePath() + "/user_profile/" + spm.userGet().getImage_url(), "error" + e);
                //aq.id(R.id.forumImage).image(R.drawable.portal_profile_sample).enabled(true);
                //aq.id(R.id.forumImage).image(new File(sd.getAbsolutePath() + "/user_profile/" + spm.userGet().getImage_url()), 50);
                try {
                    aq.id(R.id.forumImage).image(Settings.image_url + cm.get(position).getPoster_image(), true, false).enabled(true);
                }catch (Exception e2){}
            }
        }else{
            iv.setVisibility(View.VISIBLE);
            try {
                if (cm.get(position).getPoster_image().length() > 3 && !cm.get(position).getPoster_image().equalsIgnoreCase("")) {
                    //aq.id(R.id.forumImage).image(Settings.image_url + cm.get(position).getPoster_image(), true, false).enabled(true);

                    //aq.id(R.id.forumImage).image(new File(sd.getAbsolutePath() + "/user_profile/" + cm.get(position).getPoster_image()), 50);
                   /* final Uri uri = Uri.parse(sd.getAbsolutePath() + "/user_profile/" + cm.get(position).getPoster_image());
                    final String path = uri.getPath();
                    final Drawable d = Drawable.createFromPath(path);*/

                    iv.setImageDrawable(new BitmapDrawable(c.getResources(),it.decodeFile(new File(sd.getAbsolutePath() + "/user_profile/" + cm.get(position).getPoster_image()))));
                }
                else{
                    aq.id(R.id.forumImage).image(new File(sd.getAbsolutePath() + "/user_profile/" + cm.get(position).getPoster_image()), 50);

                }
            }catch (Exception e){
                try{
                aq.id(R.id.forumImage).image(Settings.image_url + cm.get(position).getPoster_image(), true, false).enabled(true);
                }catch (Exception ex){}
                //Log.e("", "" + e);
            }
        }




        return v;
    }
}
