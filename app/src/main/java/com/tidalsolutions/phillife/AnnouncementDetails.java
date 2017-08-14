package com.tidalsolutions.phillife;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.tidalsolutions.phillife.phillife.R;

public class AnnouncementDetails extends AppCompatActivity {
    private AQuery aq;
    String sdate_created, stitle, scontent, sprofile_name, sprofile_image, simage;
    Integer id;
    TextView date_created, title, content, profile_name;
    ImageView image, profile_image;
    RelativeLayout rootLayout;
    Typeface Boldface, Lightface, Regularface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.announcement_details);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Boldface = Typeface.createFromAsset(AnnouncementDetails.this.getAssets(), "Montserrat-Bold.otf");
        Lightface = Typeface.createFromAsset(AnnouncementDetails.this.getAssets(), "Montserrat-Light.otf");
        Regularface = Typeface.createFromAsset(AnnouncementDetails.this.getAssets(), "Montserrat-Regular.otf");

        id = getIntent().getExtras().getInt("id");
        sdate_created = getIntent().getExtras().getString("date_created");
        stitle = getIntent().getExtras().getString("title");
        simage = getIntent().getExtras().getString("image");
        scontent = getIntent().getExtras().getString("content");
        sprofile_name = getIntent().getExtras().getString("profile_name");
        sprofile_image = getIntent().getExtras().getString("profile_image");

        rootLayout = (RelativeLayout) findViewById(R.id.root_layout);
        title = (TextView) findViewById(R.id.title);
        content = (TextView )findViewById(R.id.content);
        image = (ImageView) findViewById(R.id.image);
        profile_image = (ImageView) findViewById(R.id.profile_image);
        date_created = (TextView) findViewById(R.id.date_created);
        profile_name = (TextView) findViewById(R.id.profile_name);

        this.aq = new AQuery(AnnouncementDetails.this);

        String profile_img = Settings.base_url + "/assets/images/profiles/" + sprofile_image;
        aq.id(profile_image).image(profile_img, false, true);

        String imgaq = Settings.base_url + "/assets/images/announcements/" + simage;
        aq.id(image).image(imgaq, false, true);

        title.setTypeface(Regularface);
        profile_name.setTypeface(Regularface);
        content.setTypeface(Lightface);
        date_created.setTypeface(Lightface);

        title.setText(stitle);
        content.setText(scontent);
        date_created.setText(sdate_created);
        profile_name.setText(sprofile_name);
    }

        @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_share, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so longf
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        } else if (id == R.id.share) {
            String share = sprofile_name + "\n" + sdate_created + "\n\n" + stitle + "\n" + scontent;

            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "\n\n");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, share);
            startActivity(Intent.createChooser(sharingIntent, "Share"));
        }

        return super.onOptionsItemSelected(item);
    }
}
