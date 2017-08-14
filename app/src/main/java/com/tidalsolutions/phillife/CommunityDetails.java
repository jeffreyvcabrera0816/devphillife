package com.tidalsolutions.phillife;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.tidalsolutions.phillife.adapters.ForumDetailsAdapter;
import com.tidalsolutions.phillife.phillife.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jeffrey on 4/26/2016.
 */
public class CommunityDetails extends AppCompatActivity implements AsyncTaskListener{
    private AQuery aq;
    TextView thread_id, detail_title, date_created, contents, username, likes, btnLike;
    EditText commentText;
    Button commentBtn;
    ImageView thread_user_image, btnShare;
    private ListView listview;
    String threadUserImage, threadTitle, threadDetail;
    Integer threadID, total_likes;
    Animation animation, text_animation;
    RelativeLayout rootLayout;
    private SwipeRefreshLayout swipeRefreshLayout;
    Boolean reload = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.community_details);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        listview = (ListView) findViewById(R.id.forum_detail_list);
        commentText = (EditText) findViewById(R.id.comment_et);
        commentBtn = (Button) findViewById(R.id.comment_btn);

        ViewGroup headerView = (ViewGroup)getLayoutInflater().inflate(R.layout.community_details_header, listview,false);
//        rootLayout = (RelativeLayout) headerView.findViewById(R.id.root_layout);

        detail_title = (TextView) headerView.findViewById(R.id.detail_title);
        date_created = (TextView) headerView.findViewById(R.id.date_created);
        contents = (TextView) headerView.findViewById(R.id.contents);
        username = (TextView) headerView.findViewById(R.id.username);
        likes = (TextView) headerView.findViewById(R.id.likes);
        btnLike = (TextView) headerView.findViewById(R.id.btnLike);
        btnShare = (ImageView) headerView.findViewById(R.id.btnShare);
        thread_user_image = (ImageView) headerView.findViewById(R.id.thread_user_image);

//        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);
//        swipeRefreshLayout.setOnRefreshListener(this);

        listview.addHeaderView(headerView);

        threadID = getIntent().getExtras().getInt("threadID");
        total_likes = getIntent().getExtras().getInt("likes");
        threadUserImage = getIntent().getExtras().getString("threadUserImage");
        threadTitle = getIntent().getExtras().getString("threadTitle");
        threadDetail = getIntent().getExtras().getString("threadDetail");

        commentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reload = false;
                String comment = commentText.getText().toString().trim();
                String stringComment;
                try {
                    stringComment = URLEncoder.encode(comment, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    throw new AssertionError("UTF-8 is unknown");
                }

            }
        });


        this.aq = new AQuery(this);
        new API(CommunityDetails.this, CommunityDetails.this, false).execute("POST", "api/community/" + threadID);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so longf
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onTaskComplete(String result) {
        try {
            JSONObject jObj = new JSONObject(result);
            boolean success = jObj.getBoolean("success");

            if (success) {
                JSONObject threadData = jObj.getJSONObject("thread_data");
                Integer id = threadData.getInt("id");
                String title = threadData.getString("title");
                String detail_contents = threadData.getString("contents");
                String detail_username = threadData.getString("user_first_name");
                String dateCreated = threadData.getString("date_created");
                Integer detail_likes = threadData.getInt("likes");
//                String is_liked = threadData.getString("is_liked");

                List<String> comment_usernames_list = new ArrayList<String>();
                List<String> comment_contents_list = new ArrayList<String>();
                List<String> comment_dates_created_list = new ArrayList<String>();
                List<String> user_image_list = new ArrayList<String>();

                JSONArray replies_data = threadData.getJSONArray("replies");

                for (int i = 0; i < replies_data.length(); i++) {
                    JSONObject reply_data = replies_data.getJSONObject(i);
                    String comment_date_created = reply_data.getString("date_created");
                    String comment_username = reply_data.getString("user_first_name");
                    String user_image = reply_data.getString("user_image");
                    String comment_content = reply_data.getString("contents");

                    comment_dates_created_list.add(comment_date_created);
                    comment_usernames_list.add(comment_username);
                    comment_contents_list.add(comment_content);
                    user_image_list.add(user_image);
                }

                String like_append;

                if (detail_likes > 1) {
                    like_append = "Likes";
                } else {
                    like_append = "Like";
                }

                if (!threadUserImage.equals("")) {
                    String imgaq = Settings.base_url + "/assets/images/users/" + threadUserImage;
                    aq.id(R.id.thread_user_image).image(imgaq, false, false);
                } else {
                    thread_user_image.setImageResource(R.mipmap.placeholder_profilephoto);
                }
                detail_title.setText(title);
                date_created.setText(dateCreated);
                contents.setText(detail_contents);
                username.setText(detail_username);
                likes.setText(detail_likes.toString() + " " + like_append);
//                if (is_liked.equals("1")) {
//                        btnLike.setImageResource(R.mipmap.icn512_like_yellow);
//                }

                String comment_date_created_list[] = comment_dates_created_list.toArray(new String[comment_dates_created_list.size()]);
                String comment_username_list[] = comment_usernames_list.toArray(new String[comment_usernames_list.size()]);
                String comment_content_list[] = comment_contents_list.toArray(new String[comment_contents_list.size()]);
                String user_image_arr[] = user_image_list.toArray(new String[user_image_list.size()]);

//                    if (swipeRefreshLayout.isRefreshing()) {
//                        swipeRefreshLayout.setRefreshing(false);
//                    }

                ForumDetailsAdapter forumDetailsAdapter = new ForumDetailsAdapter(CommunityDetails.this, threadID, comment_username_list, comment_content_list, comment_date_created_list, user_image_arr);
                listview.setAdapter(forumDetailsAdapter);
            }
        } catch (JSONException e) {
            Log.e("error", "Error parsing data" + e.toString());
        }
    }

}
