package com.tidalsolutions.phillife;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.tidalsolutions.phillife.adapters.ForumAdapter;
import com.tidalsolutions.phillife.phillife.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Community extends AppCompatActivity implements AsyncTaskListener{
    private ListView listView;
    View view;
    RelativeLayout rootLayout;
    private SwipeRefreshLayout swipeRefreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listView = (ListView) findViewById(R.id.community_list);

        new API(Community.this, Community.this, false).execute("POST", "api/community");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Community.this, Comment.class);
                startActivity(intent);
            }
        });
    }

    public void onTaskComplete(String result) {
        try {
            JSONObject jObj = new JSONObject(result);
            boolean success = jObj.getBoolean("success");

            if (success) {

                List<String> threadsTitles = new ArrayList<String>();
                List<String> threadsDetails = new ArrayList<String>();
                List<String> threadsImages = new ArrayList<String>();
                List<Integer> threadsIDs = new ArrayList<Integer>();
                List<Integer> threadsLikes = new ArrayList<Integer>();
                List<String> threadsDateCreated = new ArrayList<String>();
                List<String> usersFirstname = new ArrayList<String>();
                List<String> usersLastname = new ArrayList<String>();
                List<Integer> usersReplies = new ArrayList<Integer>();

                JSONArray threads_data = jObj.optJSONArray("threads_data");

                for (int i = 0; i < threads_data.length(); i++) {
                    JSONObject thread_data = threads_data.getJSONObject(i);
                    String thread_title = thread_data.optString("title");
                    String thread_detail = thread_data.optString("contents");
                    String thread_image = thread_data.optString("user_image");
                    String first_name = thread_data.optString("user_first_name");
                    String last_name = thread_data.optString("user_last_name");
                    String date_created = thread_data.optString("date_created");
                    Integer replies = thread_data.optInt("replies");
                    Integer thread_id = thread_data.optInt("id");
                    Integer thread_likes = thread_data.optInt("likes");

                    threadsTitles.add(thread_title);
                    threadsDetails.add(thread_detail);
                    threadsImages.add(thread_image);
                    usersFirstname.add(first_name);
                    usersLastname.add(last_name);
                    threadsDateCreated.add(date_created);
                    usersReplies.add(replies);
                    threadsIDs.add(thread_id);
                    threadsLikes.add(thread_likes);
                }

                String threadTitle[] = threadsTitles.toArray(new String[threadsTitles.size()]);
                String threadDetail[] = threadsDetails.toArray(new String[threadsDetails.size()]);
                String threadImage[] = threadsImages.toArray(new String[threadsImages.size()]);
                String userFirstname[] = usersFirstname.toArray(new String[usersFirstname.size()]);
                String userLastname[] = usersLastname.toArray(new String[usersLastname.size()]);
                String threadDateCreated[] = threadsDateCreated.toArray(new String[threadsDateCreated.size()]);
                Integer userReplies[] = usersReplies.toArray(new Integer[usersReplies.size()]);
                Integer threadID[] = threadsIDs.toArray(new Integer[threadsIDs.size()]);
                Integer threadLike[] = threadsLikes.toArray(new Integer[threadsLikes.size()]);

                ForumAdapter forumAdapter = new ForumAdapter(Community.this, threadTitle, threadDetail, threadImage, userFirstname, userLastname, threadDateCreated, userReplies, threadID, threadLike);
                listView.setAdapter(forumAdapter);
            }
        } catch (JSONException e) {
            Log.e("error", "Error parsing data" + e.toString());
        }
    }

}
