package com.tidalsolutions.phillife.adapters;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tidalsolutions.phillife.models.Albums_model;
import com.tidalsolutions.phillife.phillife.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Jeffrey on 10/17/2016.
 */

public class Agents_Mailbox_Adapter extends BaseAdapter {

    Context c;
    ArrayList<Albums_model> forms_model;

    public Agents_Mailbox_Adapter(Context c, ArrayList<Albums_model> forms_model) {
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
            v = li.inflate(R.layout.agents_mailbox_row,parent,false);

        }else v=convertView;


        TextView client_name = (TextView) v.findViewById(R.id.tv_client_name);
//        TextView form_id = (TextView) v.findViewById(R.id.tv_form_name);
        TextView date = (TextView) v.findViewById(R.id.tv_date_received);
        TextView note_name = (TextView) v.findViewById(R.id.note);
        TextView note = (TextView) v.findViewById(R.id.tv_note);

        SimpleDateFormat sourceFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat DesiredFormat = new SimpleDateFormat("MMM/dd/yyyy");
        // 'a' for AM/PM
        Date dd1=null;Date dd2=null; Date dd3=null;
        try {
            dd1 = sourceFormat.parse(forms_model.get(position).getDate_created());
            dd2 = sourceFormat.parse(forms_model.get(position).getDate_updated());
        }catch (Exception e){

        }

        String formattedDate1="";// = DesiredFormat.format(dd1.getTime());
        String formattedDate2="";// = DesiredFormat.format(dd2.getTime());
        String formattedDate3="";// = DesiredFormat.format(dd3.getTime());
        try {
            formattedDate1 = DesiredFormat.format(dd1.getTime());
            formattedDate2 = DesiredFormat.format(dd2.getTime());
            formattedDate3 = DesiredFormat.format(dd3.getTime());
        }catch (Exception e){
            formattedDate2 = "N/A";
            formattedDate3 = "N/A";
        }
        note_name.setVisibility(View.GONE);
        note.setVisibility(View.GONE);

        int status = forms_model.get(position).getStatus();
        if(forms_model.get(position)!=null){
            client_name.setText(forms_model.get(position).getClient_name());
//            form_id.setText("Your report (Form ID #"+forms_model.get(position).getForms_hashkey()+")");

            String first;
            String next;
            if(status==1){
                first = "has been received on ";
                next = "<font color='#f49b42'>"+formattedDate1+"</font>";
                date.setText(Html.fromHtml(first + next));

            }
            if(status==2){
                first = "has been ";
                next = "<font color='#299f09'>approved</font>";
                date.setText(Html.fromHtml(first + next));
            }
            if(status==3){
                first = "has been ";
                next = "<font color='#c70707'>rejected</font>";
                date.setText(Html.fromHtml(first + next));
                note_name.setVisibility(View.VISIBLE);
                note.setVisibility(View.VISIBLE);
                note.setText(forms_model.get(position).getNote());
            }
        }

        return v;
    }
}
