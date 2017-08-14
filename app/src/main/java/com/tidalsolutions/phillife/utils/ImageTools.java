package com.tidalsolutions.phillife.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.pkmmte.view.CircularImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by SantusIgnis on 29/07/2016.
 */
public class ImageTools {

    int deviceHeight;
    int deviceWidth;


    public Bitmap decodeFile(File f) {
        try {
            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f), null, o);

            // The new size we want to scale to
            final int REQUIRED_SIZE=100;

            // Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while(o.outWidth / scale / 2 >= REQUIRED_SIZE &&
                    o.outHeight / scale / 2 >= REQUIRED_SIZE) {
                scale *= 2;
            }

            // Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException e) {}
        return null;
    }

    private int[] minimizeImageSize(int height){
        do{
            height-=height/7;
        }
        while(deviceHeight<height);

        height-=height/7;

        int width = (int) ( height/((double)deviceWidth/(double)deviceHeight) );


        return new int[]{width,height};
    }

    private boolean validImageSize(Context c, int _height, int _width){

        DisplayMetrics metrics = new DisplayMetrics();

        WindowManager wm = (WindowManager) c.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(metrics);



        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        deviceHeight=height;
        deviceWidth=width;

        if(_height<height&&_width<width){
            return true;
        }


        return false;
    }


    public void set_imageview_image(Context c, String ato, View v, String location){
        //thumbnail or pictures
        try{
            File sd = c.getDir(location, Context.MODE_PRIVATE);

            File image = new File(sd.getAbsolutePath()+"/"+ato);
            //System.out.println(sd.getAbsolutePath()+"/"+ato);
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            Bitmap bitmap = BitmapFactory.decodeFile(image.getAbsolutePath(),bmOptions);

            if(bitmap!=null){

                if (validImageSize(c, bitmap.getHeight(), bitmap.getWidth())) {
                    bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(),
                            bitmap.getHeight(), true);
                    ((CircularImageView) v).setImageBitmap(bitmap);
                }
                else{

                    int[]dimensions = minimizeImageSize(bitmap.getHeight());
                    bitmap = Bitmap.createScaledBitmap(bitmap, dimensions[0],
                            dimensions[1], true);
                    ((CircularImageView) v).setImageBitmap(bitmap);
                }
            }
            else{
                //v.setBackground(c.getResources().getDrawable(R.drawable.portal_profile_sample));
            }
        }catch (Exception e){}

    }


    public void set_imageview_image_square(Context c, String ato, View v, String location){
        //thumbnail or pictures
        try{
            File sd = c.getDir(location, Context.MODE_PRIVATE);

            File image = new File(sd.getAbsolutePath()+"/"+ato);
            //System.out.println(sd.getAbsolutePath()+"/"+ato);
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            Bitmap bitmap = BitmapFactory.decodeFile(image.getAbsolutePath(),bmOptions);

            Matrix matrix = new Matrix();
            matrix.preRotate(90);

            if(bitmap!=null){

                if (validImageSize(c, bitmap.getHeight(), bitmap.getWidth())) {
                    bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                            bitmap.getHeight(), matrix, true);
                    ((ImageView) v).setImageBitmap(bitmap);
                }
                else{

                    int[]dimensions = minimizeImageSize(bitmap.getHeight());
                    bitmap = Bitmap.createBitmap(bitmap, 0, 0, dimensions[0],
                            dimensions[1], matrix, true);
                    ((ImageView) v).setImageBitmap(bitmap);
                }
            }
            else{
                //v.setBackground(c.getResources().getDrawable(R.drawable.portal_profile_sample));
            }
        }catch (Exception e){}

    }


    public String[] downloadImage(Context c,String urlString,String location){

        String rawUrl=urlString;
        URL url=null;
        HttpURLConnection htrl=null;
        InputStream is=null;
        FileOutputStream os=null;
        File dir = c.getDir(location, Context.MODE_PRIVATE);
        File f=null;
        String[]result = new String[2];
        try {
            url = new URL(rawUrl);
            htrl = (HttpURLConnection) url.openConnection();


            is = htrl.getInputStream();
            f = new File(dir.getAbsolutePath()+"/"+location+"/"+rawUrl.replaceAll(".*/([^/]+)", "$1"));

            if(f.getParentFile()!=null){
                f.getParentFile().mkdir();
            }
            os = new FileOutputStream(f);
            int counter = -1;
            byte[]blob = new byte[2048];
            while((counter = is.read(blob))!=-1){
                os.write(blob, 0, counter);
            }

            result[1]=location+"/"+rawUrl.replaceAll(".*/([^/]+)", "$1");

            result[0]="1";
        } catch (IOException e) {
            result[0]="0";
        }
        finally{
            try {
                if(is!=null&&os!=null){
                    is.close();
                    os.close();
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
        return result;
    }

}
