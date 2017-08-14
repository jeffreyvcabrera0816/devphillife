package com.tidalsolutions.phillife.master_activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.pkmmte.view.CircularImageView;
import com.tidalsolutions.phillife.API;
import com.tidalsolutions.phillife.AsyncTaskListener;
import com.tidalsolutions.phillife.Settings;
import com.tidalsolutions.phillife.adapters.CommunityDetailedListAdapter;
import com.tidalsolutions.phillife.dialogs.Dialog_Post;
import com.tidalsolutions.phillife.interfaces.DialogCallBack;
import com.tidalsolutions.phillife.interfaces.SpecialCallBack;
import com.tidalsolutions.phillife.models.Community_Model;
import com.tidalsolutions.phillife.phillife.R;
import com.tidalsolutions.phillife.utils.CONSTANT;
import com.tidalsolutions.phillife.utils.ImagePicker;
import com.tidalsolutions.phillife.utils.ImageTools;
import com.tidalsolutions.phillife.utils.ImageUniversalUploader;
import com.tidalsolutions.phillife.utils.SharedPrefManager;
import com.tidalsolutions.phillife.utils.Toaster;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

/**
 * Created by SantusIgnis on 14/07/2016.
 */
public class Community_Detailed_View extends AppCompatActivity implements AsyncTaskListener, DialogCallBack, SpecialCallBack {

    private SwipeRefreshLayout swipeRefreshLayout;
    ListView lv;
    ArrayList<Community_Model> comments;
    Community_Model post;
    String post_name, post_date, post_title, post_detail;
    //TextView tv_post_name, tv_post_date, tv_post_title, tv_post_detail;
    CommunityDetailedListAdapter cdla;
    EditText et_comment;
    ImageView image_iv;
    String thread_id, thread_user_id;
    String cookie;
    ImageView iv_overflow;
    AQuery aq;
    TextView name;
    TextView date;
    TextView title;
    TextView detail;
    CircularImageView forumImage;
    SharedPrefManager spm;
    ProgressDialog pd;
    RelativeLayout rl_photo;
    ImageView iv_preview;
    String imageFilePath;
    ImageView ivc, iv_comment_preview;
    RelativeLayout rl_frame, rl_close, rl_master;
    int page=5;
    String shareImage;
    //SaveNotes.newInstance((DialogToFragmentCallBack) Fragment_Note.this, 1, s, t, u, id,parent).show(getChildFragmentManager(), null);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.community_detailed_view);
        Intent i = getIntent();
        //requestWindowFeature(Window.FEATURE_ACTION_BAR);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#163632")));
        actionBar.setTitle("");
        thread_id = i.getStringExtra("thread_id");
        thread_user_id = i.getStringExtra("thread_user_id");
        Log.e("thread stuff",""+thread_id);
        cookie= new SharedPrefManager(Community_Detailed_View.this).userGet().getSession_cookie();
        init_cdv();
        initCDV();

    }

    @Override
    protected void onResume() {
        super.onResume();
        initCDV();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

            inflater.inflate(R.menu.menu_share, menu);



        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //Write your logic here
                this.finish();
                return true;
            case R.id.share:
                String shareImageFinal = Settings.base_url + "/assets/images/threads/"+shareImage;
                String share;
                if(shareImage.length()>1||shareImage!=null)
                    share= "View the post written by: "+name.getText().toString()+"\n\n" + title.getText().toString() + "\n" + detail.getText().toString()+"\n \n"+shareImageFinal+"\n\n"+" from http://phillife.com.ph/";
                else
                    share= "View the post written by: "+name.getText().toString()+"\n\n" + title.getText().toString() + "\n" + detail.getText().toString()+"\n \n"+" from http://phillife.com.ph/";

                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "\n\n");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, share);
                startActivity(Intent.createChooser(sharingIntent, "Share"));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


 /*   String share = sprofile_name + "\n" + sdate_created + "\n\n" + stitle + "\n" + scontent;

    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
    sharingIntent.setType("text/plain");
    sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "\n\n");
    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, share);
    startActivity(Intent.createChooser(sharingIntent, "Share"));*/



    void initCDV(){
        pd =new ProgressDialog(this);
        aq = new AQuery(this);
        name = (TextView)findViewById(R.id.tv_name);
        date = (TextView)findViewById(R.id.tv_date);
        title = (TextView)findViewById(R.id.tv_forum_title);
        detail = (TextView)findViewById(R.id.tv_forum_detail);
        iv_overflow = (ImageView) findViewById(R.id.iv_overflow);
        forumImage = (CircularImageView)findViewById(R.id.forumImage);
        rl_photo = (RelativeLayout)findViewById(R.id.rl_thread_photo);
        iv_preview = (ImageView) findViewById(R.id.preview_image);
        iv_comment_preview = (ImageView) findViewById(R.id.iv_preview_);
        ivc = (ImageView) findViewById(R.id.ivc);
        rl_frame = (RelativeLayout)findViewById(R.id.rl_frame_);
        rl_close = (RelativeLayout)findViewById(R.id.rl_close_);
        rl_master = (RelativeLayout)findViewById(R.id.forum_row);
        rl_master.setVisibility(View.GONE);
         spm = new SharedPrefManager(getApplicationContext());
        //put


        rl_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                iv_comment_preview.setVisibility(View.GONE);
                rl_close.setVisibility(View.GONE);
                rl_frame.setVisibility(View.GONE);
                image_iv.setVisibility(View.VISIBLE);
                imgPath=null;
                imageFilePath=null;
            }
        });

        ivc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("clicked!","clicked");
                Intent chooseImageIntent = ImagePicker.getPickImageIntent(getApplicationContext(), false, "");
                startActivityForResult(chooseImageIntent, 205);
            }
        });

        iv_overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int initmode = 0;
                int user_id = new SharedPrefManager(Community_Detailed_View.this).userGet().getUser_id();
                if(user_id==Integer.parseInt(thread_user_id)){
                    initmode= CONSTANT.DIALOG_POST_OWN;
                }
                else{
                    initmode= CONSTANT.DIALOG_POST_OTHERS;
                }
                Dialog_Post.newInstance((DialogCallBack)Community_Detailed_View.this, initmode,0).show(getSupportFragmentManager(), null);

            }
        });
        lv = (ListView) findViewById(R.id.list_view);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeActivity);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {


            @Override
            public void onRefresh() {

                runDialog();
                page += 5;
                new API(Community_Detailed_View.this, Community_Detailed_View.this, true).execute("POST", "apij/community_review_paginated/" + URLEncoder.encode(cookie) + "/" + thread_id + "/" + page);
                new API(Community_Detailed_View.this, Community_Detailed_View.this, true).execute("POST", "apij/community_review_total/" + URLEncoder.encode(cookie) + "/" + thread_id + "/" + page);

               /* Intent i = new Intent(Community_Detailed_View.this,Community_Detailed_View.class);
                i.putExtra("thread_id", thread_id);
                startActivity(i);
                finish();*/
            }
        });
             //runDialog();
            new API(Community_Detailed_View.this, Community_Detailed_View.this, true).execute("POST", "apij/community_review_paginated/" + URLEncoder.encode(cookie) + "/" + thread_id + "/" + page);
             new API(Community_Detailed_View.this, Community_Detailed_View.this, true).execute("POST", "apij/community_review_total/" + URLEncoder.encode(cookie) + "/" + thread_id + "/" + page);

    }

    void runDialog(){

        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd = ProgressDialog.show(this, "Waiting...", "Please wait...");
    }

    public void init_cdv(){

        et_comment = (EditText) findViewById(R.id.et_comment);
        image_iv = (ImageView) findViewById(R.id.image_iv);
        image_iv.setVisibility(View.INVISIBLE);
        image_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if ((et_comment.getText().toString().equalsIgnoreCase("")&&imgPath!=null)||(!et_comment.getText().toString().equalsIgnoreCase("")||imgPath==null)||(!et_comment.getText().toString().equalsIgnoreCase("")||imgPath!=null)) {


                //runDialog();
                try {

                    String api = Settings.base_url + "apij/upload_image_comment/threads/" + URLEncoder.encode(new
                            SharedPrefManager(getApplicationContext()).userGet().getSession_cookie()) + "/" + thread_id + "/" + URLEncoder.encode(et_comment.getText().toString() + " ", "UTF-8");

                    new ImageUniversalUploader(getApplicationContext(), Community_Detailed_View.this, imageFilePath, api, pd, "uploading").uploadImage();

                    //new API(Community_Detailed_View.this, Community_Detailed_View.this, true).execute("POST", "apij/add_comment/" + URLEncoder.encode(cookie) + "/" + thread_id + "/" + URLEncoder.encode(et_comment.getText().toString()));
                    image_iv.setVisibility(View.GONE);
                    rl_frame.setVisibility(View.GONE);
                    rl_close.setVisibility(View.GONE);
                    et_comment.setText("");

                } catch (Exception e) {
                    Log.e("error", "error " + e);
                }


                initCDV();
                //}
            }
        });



        et_comment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (s.length() <= 0&&imgPath==null) {
                    image_iv.setVisibility(View.INVISIBLE);
                }
               else {
                    image_iv.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    @Override
    public void onTaskComplete(String result) {
        Log.e("result", "result" + result);
        if(pd.isShowing()){
            pd.dismiss();
        }


        try {


            JSONObject jObj = new JSONObject(result);
            String service = jObj.getString("service");
            boolean success = jObj.getBoolean("success");
            if (swipeRefreshLayout.isRefreshing()) {
                swipeRefreshLayout.setRefreshing(false);
            }

            if(service.equalsIgnoreCase("add_comment")){
                image_iv.setVisibility(View.VISIBLE);
                if (success) {
                    Semaphore s = new Semaphore(1);
                    try {
                        page += 4;
                        Log.e("comment posted!", "comment posted");
                        et_comment.setText("");
                        s.acquire();
                    }catch (Exception e){}
                    s.release();
                    initCDV();

                }else{
                    Log.e("comment error!","comment error");
                }
            }
            if(service.equalsIgnoreCase("delete_comment")){
                image_iv.setVisibility(View.VISIBLE);
                if (success) {
                    Log.e("comment posted!", "comment posted");
                    et_comment.setText("");

                    initCDV();

                }else{
                    Log.e("comment error!","comment error");
                }
            }
            if(service.equalsIgnoreCase("delete_thread")){
                image_iv.setVisibility(View.VISIBLE);
                if (success) {
                    Log.e("comment posted!", "comment posted");
                    et_comment.setText("");


                    finish();

                }else{
                    Log.e("comment error!","comment error");
                }
            }
            else if(service.equalsIgnoreCase("community_post_paginated_review")){

                comments = new ArrayList<>();
                comments.clear();
                Community_Model master = new Community_Model();
                ArrayList<Community_Model>comments = new ArrayList<>();

                if (success) {
                    JSONArray activities_data = jObj.optJSONArray("community_review");
                    JSONArray comments_data = jObj.optJSONArray("comments");



                    for(int x=0; x<activities_data.length();x++){
                        JSONObject a = activities_data.getJSONObject(x);




                        master.setDate(a.getString("date_created"));
                        master.setTitle(a.getString("title"));
                        master.setDetail(a.getString("contents"));
                        master.setId(a.getInt("id"));
                        master.setPoster_image(a.getString("poster_image"));
                        shareImage = a.getString("image");
                        master.setPost_image(a.getString("image"));
                        master.setThread_user_id(a.getInt("user_id"));
                        master.setUsername(a.getString("first_name") + " " + a.getString("last_name"));

                       /* File sd = getApplicationContext().getDir("user_profile", Context.MODE_PRIVATE);
                        try {

                            if(master.getPost_image().length()>5){
                                iv_preview.setVisibility(View.VISIBLE);
                                rl_photo.setVisibility(View.VISIBLE);

                                String imgaq = Settings.base_url + "/assets/images/threads/"+master.getPost_image();


                                aq.id(iv_preview).image(imgaq,false,true);
                            }

                            if(master.getThread_user_id()==spm.userGet().getUser_id())
                                aq.id(forumImage).image(new File(sd.getAbsolutePath() + "/user_profile/" + spm.userGet().getImage_url()), 50).enabled(true);
                            else {
                                if(master.getPoster_image().length()<3||master.getPoster_image().equalsIgnoreCase(""))
                                    aq.id(R.id.forumImage).image(R.drawable.floating_cross).enabled(true);
                                else {
                                    aq.id(forumImage).image(Settings.image_url + master.getPoster_image(), true, true).enabled(true);
                                }
                            }
                        }catch (Exception e){
                            Log.e("",""+e);
                            String imgaq = Settings.base_url + "/assets/images/users/"+master.getPoster_image();


                            aq.id(iv_preview).image(imgaq, false, true);
                            //aq.id(R.id.forumImage).image(R.drawable.portal_profile_sample).enabled(true);
                        }
*/
                    }




                    if(comments_data!=null) {

                        for (int xx = 0; xx < comments_data.length(); xx++) {
                            JSONObject b = comments_data.getJSONObject(xx);

                            Community_Model comment_model = new Community_Model();
                            comment_model.setId(b.getInt("id"));
                            comment_model.setComment_id(b.getInt("user_id"));
                            comment_model.setDate(b.getString("date_created"));
                            comment_model.setUsername(b.getString("first_name") + " " + b.getString("last_name"));
                            comment_model.setDetail(b.getString("contents"));
                            comment_model.setPost_image(b.getString("image"));
                            comment_model.setPoster_image(b.getString("poster_image"));

                            comments.add(comment_model);

                        }

                        Semaphore mutex = new Semaphore(comments.size());
                        for(int g=0;g<comments.size();g++){
                            //Log.e("cdv"+comments.get(g).getComment_id(), "cdx" +comments.get(g).getPoster_image());
                            downloadProfilePicture(comments.get(g).getPoster_image(), comments.get(g).getComment_id());

                            try{
                                mutex.acquire();
                            }catch (Exception e){}
                        }
                        mutex.release();

                    }

                    master.setCommunity_list(comments);
                    //set to header




                    cdla = new CommunityDetailedListAdapter(this, comments);
                    lv=null;
                    lv = (ListView) findViewById(R.id.list_view);
                    //##########################################################
                    //header experiment

                                        View header = getLayoutInflater().inflate(R.layout.community_detail_row, null);
                                        header.setTag(1);



                                        TextView zname = (TextView)header.findViewById(R.id.tv_name);
                                        TextView zdate = (TextView)header.findViewById(R.id.tv_date);
                                        TextView ztitle = (TextView)header.findViewById(R.id.tv_forum_title);
                                        TextView zdetail = (TextView)header.findViewById(R.id.tv_forum_detail);
                                        CircularImageView forumImage =  (CircularImageView)header.findViewById(R.id.forumImage);
                                        ImageView zoverflow = (ImageView) header.findViewById(R.id.iv_overflow);
                                        ImageView zpostimage = (ImageView) header.findViewById(R.id.preview_image);
                                        RelativeLayout zphoto_frame = (RelativeLayout)findViewById(R.id.rl_thread_photo);
                                        //profilepic loadings
                                        if(master.getPoster_image().length()>3) {

                                            //
                                            if(master.getThread_user_id()==spm.userGet().getUser_id()) {
                                                File sd = getApplicationContext().getDir("user_profile", Context.MODE_PRIVATE);
                                                String imgaq = Settings.base_url + "/assets/images/users/" + master.getPoster_image();
                                                aq.id(forumImage).image(imgaq, false, true);
                                            }
                                            else{
                                                String imgaq = Settings.base_url + "/assets/images/users/"+master.getPoster_image();
                                                aq.id(forumImage).image(imgaq, false, true);
                                            }
                                        }
                                        else {


                                            aq.id(forumImage).image(R.drawable.portal_profile_bg).enabled(true);
                                        }

                                        //thread pic
                                        if(master.getPost_image().length()>5){
                                            iv_preview.setVisibility(View.VISIBLE);
                                            rl_photo.setVisibility(View.VISIBLE);

                                            String imgaq = Settings.base_url + "/assets/images/threads/"+master.getPost_image();


                                            aq.id(zpostimage).image(imgaq, false, true);
                                        }else {
                                            zpostimage.setVisibility(View.GONE);
                                            //aq.id(forumImage).image(R.drawable.portal_profile_sample).enabled(true);
                                        }

                                         zoverflow.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {

                                                int initmode = 0;
                                                int user_id = new SharedPrefManager(Community_Detailed_View.this).userGet().getUser_id();
                                                if(user_id==Integer.parseInt(thread_user_id)){
                                                    initmode= CONSTANT.DIALOG_POST_OWN;
                                                }
                                                else{
                                                    initmode= CONSTANT.DIALOG_POST_OTHERS;
                                                }
                                                Dialog_Post.newInstance((DialogCallBack)Community_Detailed_View.this, initmode,0).show(getSupportFragmentManager(), null);

                                            }
                                        });

                                        zname.setText(master.getUsername());
                                        zdate.setText(master.getDate());
                                        ztitle.setText(master.getTitle());
                                        zdetail.setText(master.getDetail());
                                        //hide texts
                                        if(master.getDetail().length()<=0){
                                            zdetail.setVisibility(View.GONE);
                                        }else zdetail.setVisibility(View.VISIBLE);

                                        if(master.getTitle().length()<=0){
                                            ztitle.setVisibility(View.GONE);
                                        }else ztitle.setVisibility(View.VISIBLE);




                                        View v = lv.findViewWithTag(1);
                                        lv.removeHeaderView(v);
                                        lv.addHeaderView(header);


                    //##########################################################
                    //header

                    name.setText(master.getUsername());
                    date.setText(master.getDate());
                    title.setText(""+master.getTitle()+" ");

                    detail.setText(master.getDetail());

                    if(title.getText().toString().equalsIgnoreCase("")){
                        title.setVisibility(View.GONE);
                    }else title.setVisibility(View.VISIBLE);

                    if(detail.getText().toString().equalsIgnoreCase("")){
                        detail.setVisibility(View.GONE);
                    }else detail.setVisibility(View.VISIBLE);

                    lv.setAdapter(cdla);

                    //set to header
                    lv.setSelection(cdla.getCount() - 1);

                }

            }
            if(service.equalsIgnoreCase("community_post_total_review")){

                if (success) {
                    try {
                        int total = jObj.getInt("total");
                        if (total>page) {
                            View header = getLayoutInflater().inflate(R.layout.community_detail_next_comments, null);

                            header.setTag(2);
                            //forum_image_notification
                            final RelativeLayout rl = (RelativeLayout) header.findViewById(R.id.forum_image_notification);
                            final ImageView plus = (ImageView) header.findViewById(R.id.iv_next);
                            plus.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    rl.setVisibility(View.GONE);

                                    swipeRefreshLayout.post(new Runnable() {
                                        @Override
                                        public void run() {

                                            page+=5;
                                            initCDV();
                                            swipeRefreshLayout.setRefreshing(true);
                                        }
                                    });
                                }
                            });
                            View v = lv.findViewWithTag(2);
                            lv.removeFooterView(v);
                            lv.addFooterView(header);

                        }else{
                            View v = lv.findViewWithTag(2);
                            lv.removeFooterView(v);
                        }
                    }catch (Exception e){}
                }else{
                    Log.e("comment error!","comment error");
                }
            }



           /* ListView lv = (ListView) findViewById(R.id.listViewFVC);
            lv.setAdapter(cla);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent i = new Intent(getActivity(), Community_Detailed_View.class);
                    startActivity(i);
                }
            });*/
            /*lv.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {

                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                    if(firstVisibleItem>0){
                        rl_master.setVisibility(View.GONE);
                    }else rl_master.setVisibility(View.VISIBLE);
                }
            });*/

        } catch (JSONException e) {
            Log.e("error", "Error parsing data" + e.toString());
        }
        //image_iv.setVisibility(View.VISIBLE);

    }



    void downloadProfilePicture(String image_path, int comment_user_id){


        SharedPrefManager spm = new SharedPrefManager(getApplicationContext());

        if(spm.getUserThumbnail(comment_user_id).length()<1){
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            ImageTools it = new ImageTools();

            String[] as = it.downloadImage(getApplicationContext(), Settings.image_url + image_path, "user_profile");

            if (as != null) {
                File dir = getApplicationContext().getDir("user_profile", Context.MODE_PRIVATE);
                String localStorage = dir.getAbsolutePath() + "/user_profile/" + image_path;

                new SharedPrefManager(getApplicationContext()).setUserThumbnail(comment_user_id);
            }
        }
        //Log.e("user pic " + spm.userGet().getImage_url(), "user pic " + Settings.image_url + image_path);

    }


    int comment_id=0;
    @Override
    public void dialogfunction(int position, int position2, Object data1, Object data2) {

        if(position==CONSTANT.DIALOG_ACTION_DELETE){
            new API(Community_Detailed_View.this, Community_Detailed_View.this, true).execute("POST", "apij/delete_thread/" + URLEncoder.encode(cookie) + "/" + thread_id + "/");
         }
        else if(position==CONSTANT.DIALOG_ACTION_REMOVE_COMMENT){
            new API(Community_Detailed_View.this, Community_Detailed_View.this, true).execute("POST", "apij/delete_comment/" + URLEncoder.encode(cookie) + "/" + comment_id + "/");
        }
        else  if(position==CONSTANT.DIALOG_ACTION_EDIT){
            //new API(Community_Detailed_View.this, Community_Detailed_View.this, true).execute("POST", "apij/delete_thread/" + URLEncoder.encode(cookie) + "/" + thread_id + "/");
            Intent i = new Intent(Community_Detailed_View.this,Community_Update_Post_View.class);
            i.putExtra("thread_id",""+thread_id);
            i.putExtra("thread_detail",""+detail.getText().toString());
            i.putExtra("thread_title",""+title.getText().toString());
            i.putExtra("thread_date",""+date.getText().toString());
            startActivity(i);
        }
        else if(position==CONSTANT.DIALOG_ACTION_REPORT){
            new API(Community_Detailed_View.this, Community_Detailed_View.this, true).execute("POST", "apij/add_thread_ban/" + URLEncoder.encode(cookie) + "/" + thread_id + "/"+position2);
            finish();
        }
        else if(position==CONSTANT.DIALOG_SHOW_REPORT_COMMENT){
            Log.e("comment_id",""+comment_id);
            comment_id = position2;
            Dialog_Post.newInstance((DialogCallBack)Community_Detailed_View.this, CONSTANT.DIALOG_SHOW_REPORT_COMMENT,comment_id).show(getSupportFragmentManager(), null);

        }//isabella montana
        else if(position==CONSTANT.DIALOG_SHOW_REPORT_COMMENT_SELF){
            Log.e("comment_id",""+comment_id);
            comment_id = position2;
            Dialog_Post.newInstance((DialogCallBack)Community_Detailed_View.this, CONSTANT.DIALOG_SHOW_REPORT_COMMENT_SELF,comment_id).show(getSupportFragmentManager(), null);

        }//isabella montana
        else if(position==CONSTANT.DIALOG_ACTION_REPORT_COMMENT){
            new API(Community_Detailed_View.this, Community_Detailed_View.this, true).execute("POST", "apij/add_comment_ban/" + URLEncoder.encode(cookie) + "/" + comment_id + "/"+position2);
            initCDV();
        }
    }







    //handling comments


    String imgPath;
    Bitmap bitmap;
    String localStorage;

    public String getRealPathFromURI(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        @SuppressWarnings("deprecation")
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // if (resultCode == RESULT_OK) {
        if (requestCode == 205&&resultCode==RESULT_OK) {
            Log.e("something","something");
            final boolean isCamera;

            if (data == null) {
                isCamera = true;

            }

            else {
                Log.e("not null","not null");
                final String action = data.getAction();
                if (action == null) {
                    isCamera = false;
                } else {
                    isCamera = action.equals(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                }
                //stuff
                if(data.getData()!=null) {
                    try
                    {
                        if (bitmap != null)
                        {
                            bitmap.recycle();
                        }

                        //get uri path
                        Uri selectedImageUri = data.getData();
                        imgPath = getRealPathFromURI(selectedImageUri);


                        Semaphore mutex = new Semaphore(1);
                        //save to templocal
                        try {
                            FileOutputStream os = null;
                            InputStream stream = getContentResolver().openInputStream(data.getData());

                            //decrease size first
                            bitmap = BitmapFactory.decodeStream(stream);

                            //bitmap = decodeFile(stream);

                            ByteArrayOutputStream bos = new ByteArrayOutputStream();

                            bitmap.compress(Bitmap.CompressFormat.JPEG, 50 /*ignored for PNG*/, bos);


                            byte[] bitmapdata = bos.toByteArray();
                            ByteArrayInputStream bs = new ByteArrayInputStream(bitmapdata);

                            //decrease
                            int user_id = new SharedPrefManager(getApplicationContext()).userGet().getUser_id();
                            //File dir = getApplicationContext().getDir("user_profile", Context.MODE_PRIVATE);
                            File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),"");
                            String updatedImage = "temp" + System.currentTimeMillis() + "_thread_pic.jpg";
                            localStorage = dir.getAbsolutePath() + "/Phillife/" + updatedImage;
                            Log.e("local","local "+localStorage);

                            File f = new File(localStorage);

                            if (f.getParentFile() != null) {
                                f.getParentFile().mkdir();
                            }
                            os = new FileOutputStream(f);

                            int counter = -1;
                            byte[] blob = new byte[2048];
                            while ((counter = bs.read(blob)) != -1) {
                                os.write(blob, 0, counter);
                            }

                            stream.close();
                            mutex.acquire();
                        }catch (Exception e){}
                        mutex.release();
                        //save








                    }

                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                }

                else
                {
                    //bitmap=(Bitmap) data.getExtras().get("data");

                }


            }

            iv_comment_preview.setVisibility(View.VISIBLE);
            rl_close.setVisibility(View.VISIBLE);
            rl_frame.setVisibility(View.VISIBLE);
            image_iv.setVisibility(View.VISIBLE);
            bitmap.recycle();


            final Uri uri = Uri.parse(localStorage);
            final String path = uri.getPath();
            imageFilePath = path;
            ImageTools it = new ImageTools();
            iv_comment_preview.setImageDrawable(new BitmapDrawable(getResources(),it.decodeFile(new File(localStorage))));


            //uploadImage(add_thread);
        }

        else if(resultCode==RESULT_CANCELED){

        }



        //aq.id(R.id.preview_image).image(new File(localStorage),50);


      /* final Uri uri = Uri.parse(localStorage);
        final String path = uri.getPath();
        imageFilePath = path;
        final Drawable d = Drawable.createFromPath(path);
        preview_image.setImageDrawable(d);*/







    }

    void close_keyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void specialCallBack(int position, Object data1) {
        Log.e("cdv special callback " + position, "cdv special calback" + (String) data1);
        String result = (String)data1;

        pd.dismiss();

        Semaphore sm = new Semaphore(1);
        try {


            JSONObject jObj = new JSONObject(result);
            String service = jObj.getString("service");
            boolean success = jObj.getBoolean("success");

            if(success){
                new Toaster(this,"Comment sent!");

                et_comment.setText("");
                close_keyboard();

            }else  new Toaster(this,"Comment sending failed!");
            sm.acquire();
        }catch (Exception e){

        }
        sm.release();
        init_cdv();
        initCDV();
        pd.dismiss();
    }
}
