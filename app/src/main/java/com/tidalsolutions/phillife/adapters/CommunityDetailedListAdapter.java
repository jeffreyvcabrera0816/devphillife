package com.tidalsolutions.phillife.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.pkmmte.view.CircularImageView;
import com.tidalsolutions.phillife.Settings;
import com.tidalsolutions.phillife.interfaces.DialogCallBack;
import com.tidalsolutions.phillife.models.Community_Model;
import com.tidalsolutions.phillife.phillife.R;
import com.tidalsolutions.phillife.utils.CONSTANT;
import com.tidalsolutions.phillife.utils.SharedPrefManager;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by SantusIgnis on 12/07/2016.
 */
public class CommunityDetailedListAdapter extends BaseAdapter {

    Context c;
    ArrayList<Community_Model> cm;
    DialogCallBack dcb;
    AQuery aq;
    public CommunityDetailedListAdapter(Context c, ArrayList<Community_Model> cm) {
        this.c = c;
        this.cm = cm;
        this.dcb=(DialogCallBack)c;
        //Collections.reverse(cm);
    }

    public CommunityDetailedListAdapter(DialogCallBack dcb, Context c, ArrayList<Community_Model> cm) {
        this.c = c;
        this.cm = cm;
        this.dcb=dcb;
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
    public View getView(final int position, View convertView, ViewGroup parent) {

        View v;
        //DIALOG_SHOW_REPORT_COMMENT_SELF
        if(convertView==null){
            LayoutInflater li = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = li.inflate(R.layout.community_comment_row,parent,false);
            this.aq = new AQuery(v);
        }
        else{
            v=convertView;
        }
        this.aq = new AQuery(v);

        TextView tv_name = (TextView) v.findViewById(R.id.tv_name);
        TextView tv_date = (TextView) v.findViewById(R.id.tv_date);
        TextView tv_comment = (TextView) v.findViewById(R.id.tv_comment);
        ImageView overflow = (ImageView) v.findViewById(R.id.iv_overflow);
        CircularImageView forumImage = (CircularImageView) v.findViewById(R.id.forumImage);
        RelativeLayout rl_frame = (RelativeLayout) v.findViewById(R.id.rl_frame);
        ImageView iv_photo = (ImageView) v.findViewById(R.id.iv_comment);

        tv_name.setText(cm.get(position).getUsername());
        tv_date.setText(cm.get(position).getDate());
        tv_comment.setText(cm.get(position).getDetail());

        //assign image
        SharedPrefManager spm = new SharedPrefManager(c);
        File sd = c.getDir("user_profile", Context.MODE_PRIVATE);

        if(cm.get(position).getPost_image().length()>1){
                rl_frame.setVisibility(View.VISIBLE);
            String imgaq = Settings.base_url + "/assets/images/threads/"+cm.get(position).getPost_image();
            Log.e("comment image","comment image"+cm.get(position).getPost_image());
            aq.id(R.id.iv_comment).image(imgaq, false, true);
        }else{
            rl_frame.setVisibility(View.GONE);
        }

        if((cm.get(position).getComment_id()==spm.userGet().getUser_id())&&cm.get(position).getPoster_image().length()>3&&!cm.get(position).getPoster_image().equalsIgnoreCase("")) {
            forumImage.setVisibility(View.VISIBLE);


            try {
                aq.id(R.id.forumImage).image(new File(sd.getAbsolutePath() + "/user_profile/" + spm.userGet().getImage_url()), 50).enabled(true);

               //or
               /* final Uri uri = Uri.parse(sd.getAbsolutePath() + "/user_profile/" + spm.userGet().getImage_url());
                final String path = uri.getPath();
                final Drawable d = Drawable.createFromPath(path);
                forumImage.setImageDrawable(d);*/
            }catch (Exception e){
                //Log.e("error:"+sd.getAbsolutePath() + "/user_profile/" + spm.userGet().getImage_url(),"error"+e);
                aq.id(R.id.forumImage).image(R.drawable.portal_profile_sample).enabled(true);
            }
        }else{
            forumImage.setVisibility(View.VISIBLE);
            try {
                if (cm.get(position).getPoster_image().length() > 3 && !cm.get(position).getPoster_image().equalsIgnoreCase("")) {
                    //aq.id(R.id.forumImage).image(Settings.image_url + cm.get(position).getPoster_image(), true, false).enabled(true);
                    aq.id(R.id.forumImage).image(new File(sd.getAbsolutePath() + "/user_profile/" + cm.get(position).getPoster_image()), 50).enabled(true);

                    //or
                   /* final Uri uri = Uri.parse(sd.getAbsolutePath() + "/user_profile/" + cm.get(position).getPoster_image());
                    final String path = uri.getPath();
                    final Drawable d = Drawable.createFromPath(path);
                    forumImage.setImageDrawable(d);*/
                }
                else aq.id(R.id.forumImage).image(R.drawable.portal_profile_sample).enabled(true);
            }catch (Exception e){
                aq.id(R.id.forumImage).image(new File(sd.getAbsolutePath() + "/user_profile/" + cm.get(position).getPoster_image()), 50).enabled(true);
                Log.e("", "" + e);
            }

        }



        //assign image
        overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (new SharedPrefManager(c).userGet().getUser_id()==cm.get(position).getComment_id()) {
                    dcb.dialogfunction(CONSTANT.DIALOG_SHOW_REPORT_COMMENT_SELF, cm.get(position).getId(), null, null);
                } else
                    dcb.dialogfunction(CONSTANT.DIALOG_SHOW_REPORT_COMMENT, cm.get(position).getId(), null, null);
            }
        });



        /*LayoutInflater lix = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View header = lix.inflate(R.layout.community_comment_row,parent,false);

        TextView zname = (TextView)header.findViewById(R.id.tv_name);
        TextView zdate = (TextView)header.findViewById(R.id.tv_date);
        TextView ztitle = (TextView)header.findViewById(R.id.tv_forum_title);
        TextView zdetail = (TextView)header.findViewById(R.id.tv_forum_detail);

        zname.setText("dsf");
        zdate.setText("asda");
        ztitle.setText("asda");
        zdetail.setText("asd");

        rl_frame.addView(header);*/

        return v;
    }
}
