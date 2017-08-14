package com.tidalsolutions.phillife.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tidalsolutions.phillife.models.Albums_model;
import com.tidalsolutions.phillife.phillife.R;

import java.util.ArrayList;

/**
 * Created by SantusIgnis on 22/09/2016.
 */
public class Agents_Reports_Drafts_Adapter extends BaseAdapter {

    Context c;
    ArrayList<Albums_model> forms_model;

    public Agents_Reports_Drafts_Adapter(Context c, ArrayList<Albums_model> forms_model) {
        this.c = c;
        this.forms_model = forms_model;
    }

    @Override
    public int getCount() {
        return forms_model.size();
    }

    @Override
    public Object getItem(int position) {
        return forms_model.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v;
        if(convertView==null){

            LayoutInflater li = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = li.inflate(R.layout.agents_draft_row,parent,false);

        }else v=convertView;


        TextView client_name = (TextView) v.findViewById(R.id.tv_name);
//        TextView form_id = (TextView) v.findViewById(R.id.tv_form_id);
        TextView date = (TextView) v.findViewById(R.id.tv_date);

        if(forms_model.get(position)!=null){
                client_name.setText(forms_model.get(position).getClient_name());
//                form_id.setText("Form ID #"+forms_model.get(position).getForms_hashkey());
                date.setText(forms_model.get(position).getDate_created());
        }

        return v;
    }
}
