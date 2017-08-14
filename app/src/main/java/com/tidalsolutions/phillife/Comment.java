package com.tidalsolutions.phillife;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.tidalsolutions.phillife.phillife.R;

import java.io.ByteArrayOutputStream;

public class Comment extends Activity {
    String filename = "";
    String filePath = "";
    String encodedString;
    private static final int RESULT_LOAD_IMAGE = 1;
    ImageView image_container;
    Button image_upload_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        image_container = (ImageView) findViewById(R.id.image_container);
        image_upload_btn = (Button) findViewById(R.id.image_upload_btn);

        image_upload_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
            }
        });

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null) {

            Uri selectedImage = data.getData();
            image_container.setImageURI(selectedImage);

            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            filePath = cursor.getString(columnIndex);
            cursor.close();

            // Get the Image's file name
            String fileNameSegments[] = filePath.split("/");
            filename = fileNameSegments[fileNameSegments.length - 1];
            encodeImagetoString();

//            Toast.makeText(RegisterActivity.this, filePath, Toast.LENGTH_SHORT).show();
        }
    }

    public void encodeImagetoString() {
        new AsyncTask<Void, Void, String>() {

            protected void onPreExecute() {

            };

            @Override
            protected String doInBackground(Void... params) {
                BitmapFactory.Options options = null;
                Bitmap bitmap;
                options = new BitmapFactory.Options();
                options.inSampleSize = 2;

                bitmap = BitmapFactory.decodeFile(filePath, options);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                // Must compress the Image to reduce image size to make upload easy
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] byte_arr = stream.toByteArray();
                // Encode Image to String
                encodedString = Base64.encodeToString(byte_arr, 0);
                return "";
            }

            @Override
            protected void onPostExecute(String msg) {

            }
        }.execute(null, null, null);
    }
}
