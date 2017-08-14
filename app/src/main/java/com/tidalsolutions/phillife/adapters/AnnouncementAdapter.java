package com.tidalsolutions.phillife.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.tidalsolutions.phillife.AnnouncementDetails;
import com.tidalsolutions.phillife.phillife.R;
import com.tidalsolutions.phillife.Settings;

/**
 * Created by Jeffrey on 4/8/2016.
 */
public class AnnouncementAdapter extends android.widget.BaseAdapter {
    Animation animation;
    private AQuery aq;
    private Context context;
    private Integer[] id;
    private String[] title, content, image, date_created, profile_image, profile_name;
    private LayoutInflater inflater;
    Typeface Boldface, Lightface, Regularface;

    public AnnouncementAdapter(Context context, Integer[] id, String[] title, String[] content, String[] image, String[] date_created, String[] profile_image, String[] profile_name) {
        this.context = context;
        this.id = id;
        this.title = title;
        this.content = content;
        this.image = image;
        this.date_created = date_created;
        this.profile_image = profile_image;
        this.profile_name = profile_name;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        Boldface = Typeface.createFromAsset(context.getAssets(), "Montserrat-Bold.otf");
        Lightface = Typeface.createFromAsset(context.getAssets(), "Montserrat-Light.otf");
        Regularface = Typeface.createFromAsset(context.getAssets(), "Montserrat-Regular.otf");
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    @Override
    public int getItemViewType(int position) {
        if (image[position].equals("")) {
            return 0;
        } else if (title[position].equals("") && content[position].equals("")) {
            return 2;
        } else {
            return 1;
        }
    }

    @Override
    public int getCount() {
        return title.length;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    class ViewHolder {
        RelativeLayout rootLayout;
        TextView title, content, date_created, profile_name, read_more;
        ImageView image, profile_image;

        ViewHolder(View view) {

            rootLayout = (RelativeLayout) view.findViewById(R.id.root_layout);
            title = (TextView)view.findViewById(R.id.title);
            content = (TextView)view.findViewById(R.id.content);
            image = (ImageView) view.findViewById(R.id.image);
            profile_image = (ImageView) view.findViewById(R.id.profile_image);
            date_created = (TextView)view.findViewById(R.id.date_created);
            profile_name = (TextView)view.findViewById(R.id.profile_name);
            read_more = (TextView)view.findViewById(R.id.read_more);
        }
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Integer type = getItemViewType(position);
        final ViewHolder holder;
        this.aq = new AQuery(convertView);

        if (convertView == null) {

            if (type == 0) {
                convertView = inflater.inflate(R.layout.announcement_row_no_image, parent, false);
            } else if (type == 2) {
                convertView = inflater.inflate(R.layout.announcement_row_image_only, parent, false);
            } else {
                convertView = inflater.inflate(R.layout.announcement_row, parent, false);
            }

            holder = new ViewHolder(convertView);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (type == 2) {
            String imgaq = Settings.base_url + "/assets/images/announcements/" + image[position];
            aq.id(holder.image).image(imgaq, false, true);
        } else {
            animation = AnimationUtils.loadAnimation(context, R.anim.read_more);
            String prof_img = Settings.base_url + "/assets/images/profiles/" + profile_image[position];
            aq.id(holder.profile_image).image(prof_img, false, true);

            holder.profile_name.setText(profile_name[position]);
            holder.title.setTypeface(Regularface);
            holder.profile_name.setTypeface(Regularface);
            holder.content.setTypeface(Lightface);
            holder.date_created.setTypeface(Lightface);
            holder.read_more.setTypeface(Lightface);

            if (type == 1) {
                String imgaq = Settings.base_url + "/assets/images/announcements/" + image[position];
                aq.id(holder.image).image(imgaq, false, true);
            }
//            String content_sub = content[position];
//            if (content[position].length() > 100) {
//                content_sub = content[position].substring(0, 100) + "..";
//                holder.read_more.setVisibility(convertView.VISIBLE);
//                StringTokenizer tokens = new StringTokenizer(content[position], ".");
//            }

            String[] contents_arr = content[position].split("\\.\\s");

            if (contents_arr.length > 1) {

            }

            String content_sub = contents_arr[0]+".";

            holder.title.setText(title[position]);
            holder.content.setText(content_sub);
            holder.date_created.setText(date_created[position]);

            holder.read_more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.read_more.startAnimation(animation);

                    Intent intent = new Intent(context, AnnouncementDetails.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("id", id[position]);
                    bundle.putString("date_created", date_created[position]);
                    bundle.putString("title", title[position]);
                    bundle.putString("image", image[position]);
                    bundle.putString("content", content[position]);
                    bundle.putString("profile_name", profile_name[position]);
                    bundle.putString("profile_image", profile_image[position]);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            });

        }

        return convertView;
    }
}
