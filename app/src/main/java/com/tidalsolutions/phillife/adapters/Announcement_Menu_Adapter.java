package com.tidalsolutions.phillife.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tidalsolutions.phillife.models.SimpleImageTextMenuModel;
import com.tidalsolutions.phillife.phillife.R;

import java.util.ArrayList;

/**
 * Created by SantusIgnis on 23/06/2016.
 */
public class Announcement_Menu_Adapter extends BaseAdapter {

    Context c;
    ArrayList<SimpleImageTextMenuModel> menuItems;


    public Announcement_Menu_Adapter(Context c,  ArrayList<SimpleImageTextMenuModel> menuItems) {
        this.c = c;
        this.menuItems = menuItems;
    }

    @Override
    public int getCount() {
        return menuItems.size();
    }

    @Override
    public Object getItem(int position) {
        return menuItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v;
        if(convertView==null) {
            LayoutInflater li = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = li.inflate(R.layout.announcement_menu_adapter,parent,false);
        }
        else v=convertView;
        TextView title = (TextView)v.findViewById(R.id.textView1);
        ImageView iv = (ImageView)v.findViewById(R.id.imageView1);
        title.setText(menuItems.get(position).getText());
        iv.setImageResource(menuItems.get(position).getId());
        return v;
    }
}
