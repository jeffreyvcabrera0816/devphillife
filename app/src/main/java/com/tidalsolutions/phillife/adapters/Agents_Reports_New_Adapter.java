package com.tidalsolutions.phillife.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.tidalsolutions.phillife.models.Albums_model;
import com.tidalsolutions.phillife.phillife.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

/**
 * Created by SantusIgnis on 22/09/2016.
 */
public class Agents_Reports_New_Adapter extends BaseAdapter {

    Context c;
    ArrayList<Albums_model> forms_model;

    public Agents_Reports_New_Adapter(Context c, ArrayList<Albums_model> forms_model) {
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
            v = li.inflate(R.layout.agents_reports_row,parent,false);

        }else v=convertView;


        TextView client_name = (TextView) v.findViewById(R.id.tv_name);
//        TextView form_id = (TextView) v.findViewById(R.id.tv_form_id);
        TextView date1 = (TextView) v.findViewById(R.id.tv_date);
        TextView date2 = (TextView) v.findViewById(R.id.tv_date_submitted);
        TextView date3 = (TextView) v.findViewById(R.id.tv_date_rejected);


        SimpleDateFormat sourceFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat DesiredFormat = new SimpleDateFormat("MMM/dd/yyyy");
        // 'a' for AM/PM
        Date dd1=null;Date dd2=null; Date dd3=null; Date dd4 = null;

        try {
            dd1 = sourceFormat.parse(forms_model.get(position).getDate_created());
            dd2 = sourceFormat.parse(forms_model.get(position).getDate_updated());
            dd3 = sourceFormat.parse(forms_model.get(position).getDate_updated());
            dd4 = sourceFormat.parse(forms_model.get(position).getDate_updated());
        }catch (Exception e){

        }

        String formattedDate1;// = DesiredFormat.format(dd1.getTime());
        String formattedDate2;// = DesiredFormat.format(dd2.getTime());
        String formattedDate3;// = DesiredFormat.format(dd3.getTime());
        String formattedDate4;// = DesiredFormat.format(dd4.getTime());
        try {
            formattedDate1 = DesiredFormat.format(dd1.getTime());
            formattedDate2 = DesiredFormat.format(dd2.getTime());
            formattedDate3 = DesiredFormat.format(dd3.getTime());
            formattedDate4 = DesiredFormat.format(dd4.getTime());
        }catch (Exception e){
            formattedDate1 = DesiredFormat.format(dd1.getTime());
            formattedDate2 = DesiredFormat.format(dd2.getTime());
            formattedDate3 = DesiredFormat.format(dd3.getTime());
            formattedDate4 = DesiredFormat.format(dd4.getTime());
        }

        int status = forms_model.get(position).getStatus();
        if(forms_model.get(position)!=null){
                client_name.setText(forms_model.get(position).getClient_name());
//                form_id.setText("Form ID #"+forms_model.get(position).getForms_hashkey());

                date1.setText(formattedDate1);
//                date2.setText(formattedDate2);
//                date3.setText(formattedDate4);
//            Toast.makeText(c, forms_model.get(position).getDate_updated(), Toast.LENGTH_LONG).show();
                if(status==1){
                    date2.setText("N/A");
                    date3.setText("N/A");
                }
                if(status==2){
                    date2.setText(formattedDate4);
                    date3.setText("N/A");
                }
                if(status==3){
                    date2.setText("N/A");
                    date3.setText(formattedDate4);
                }


        }

        return v;
    }
}
