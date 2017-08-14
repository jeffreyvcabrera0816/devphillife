package com.tidalsolutions.phillife;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import com.tidalsolutions.phillife.adapters.AnnouncementAdapter;
import com.tidalsolutions.phillife.phillife.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AsyncTaskListener, SwipeRefreshLayout.OnRefreshListener {
    private SwipeRefreshLayout swipeRefreshLayout;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
//        getSupportActionBar().setDisplayUseLogoEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        getSupportActionBar().setLogo(R.mipmap.logo_teachers_journal);

        listView = (ListView) findViewById(R.id.listView);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeActivity);
        swipeRefreshLayout.setOnRefreshListener(this);
        new API(MainActivity.this, MainActivity.this, false).execute("POST", "api/announcements/");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.gago) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTaskComplete(String result) {
        try {
            JSONObject jObj = new JSONObject(result);
            boolean success = jObj.getBoolean("success");

            if (swipeRefreshLayout.isRefreshing()) {
                swipeRefreshLayout.setRefreshing(false);
            }
            if (success) {
                List<Integer> id_list = new ArrayList<Integer>();
                List<String> title_list = new ArrayList<String>();
                List<String> content_list = new ArrayList<String>();
                List<String> image_list = new ArrayList<String>();
                List<String> date_list = new ArrayList<String>();
                List<String> profile_image_list = new ArrayList<String>();
                List<String> profile_name_list = new ArrayList<String>();

                JSONArray activities_data = jObj.optJSONArray("data");

                for (int i = 0; i < activities_data.length(); i++) {
                    JSONObject activity_data = activities_data.getJSONObject(i);
                    Integer id = activity_data.optInt("id");
                    String title = activity_data.optString("title");
                    String content = activity_data.optString("content");
                    String image = activity_data.optString("image");
                    String date_created = activity_data.optString("date_created");
                    String profile_image = activity_data.optString("profile_image");
                    String profile_name = activity_data.optString("profile_name");

                    id_list.add(id);
                    title_list.add(title);
                    content_list.add(content);
                    image_list.add(image);
                    date_list.add(date_created);
                    profile_name_list.add(profile_name);
                    profile_image_list.add(profile_image);
                }

                Integer id[] = id_list.toArray(new Integer[id_list.size()]);
                String title[] = title_list.toArray(new String[title_list.size()]);
                String content[] = content_list.toArray(new String[content_list.size()]);
                String image[] = image_list.toArray(new String[image_list.size()]);
                String date_created[] = date_list.toArray(new String[date_list.size()]);
                String profile_image[] = profile_image_list.toArray(new String[profile_image_list.size()]);
                String profile_name[] = profile_name_list.toArray(new String[profile_name_list.size()]);

                AnnouncementAdapter announcementAdapter = new AnnouncementAdapter(MainActivity.this ,id, title, content, image, date_created, profile_image, profile_name);
                listView.setAdapter(announcementAdapter);
            }
        } catch (JSONException e) {
            Log.e("error", "Error parsing data" + e.toString());
        }
    }

    @Override
    public void onRefresh() {
        new API(MainActivity.this, MainActivity.this, true).execute("POST", "api/announcements/");
    }
}
