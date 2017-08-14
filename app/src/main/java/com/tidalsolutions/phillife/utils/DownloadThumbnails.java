package com.tidalsolutions.phillife.utils;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.tidalsolutions.phillife.Settings;
import com.tidalsolutions.phillife.models.Community_Model;

import java.io.File;
import java.util.ArrayList;

public class DownloadThumbnails extends AsyncTask<Void, Void, Void> {
	Context c;

	ProgressDialog pd;
	Activity a;
	String url;
	ArrayList<Community_Model> cm;

	public DownloadThumbnails(Activity a, Context c, ProgressDialog pd, ArrayList<Community_Model> cm) {
		this.c=c;
		this.cm=cm;

		this.pd = pd;
		this.a=a;
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();

		pd.setMessage("Please wait...");
		pd.setTitle("Loading");
		pd.setCancelable(false);
		pd.show();
	}

	@Override
	protected void onProgressUpdate(Void... values) {
		super.onProgressUpdate(values);
	}

	@SuppressWarnings("deprecation")
	@Override
	protected Void doInBackground(Void... params) {
		// TODO Auto-generated method stub

		//json check
		Log.e("async", "async called");
		for(int i=0;i<cm.size();i++) {
				//Log.e("async","async"+cm.get(i).getThread_user_id());
				SharedPrefManager spm = new SharedPrefManager(c);
				int data = 0;

				if(cm.get(i).getComment_id()>0){
						data=cm.get(i).getComment_id();
				}else data=cm.get(i).getThread_user_id();


			if (spm.getUserThumbnail(data).length() < 1) {

					ImageTools it = new ImageTools();

					String[] as = it.downloadImage(c, Settings.image_url + cm.get(i).getPoster_image(), "user_profile");

					if (as != null) {
						File dir = c.getDir("user_profile", Context.MODE_PRIVATE);
						String localStorage = dir.getAbsolutePath() + "/user_profile/" + cm.get(i).getPoster_image();

						new SharedPrefManager(c).setUserThumbnail(data);
					}
				}
		}



		/*ServiceHandler shReadings = new ServiceHandler();
		ServiceHandler shBooks = new ServiceHandler();
		ServiceHandler shArticles = new ServiceHandler();

		//article_last_id
		List<NameValuePair> book_param = new ArrayList<NameValuePair>();

	*//*	book_param.add(new BasicNameValuePair("product_code", ""+product_code));
		book_param.add(new BasicNameValuePair("session_cookie", "" + session_cookie));
		book_param.add(new BasicNameValuePair("hash_key", "" + hashkey));*//*

		String jsonStr = shReadings.makeServiceCall(urls[0], ServiceHandler.POST, book_param);


		Log.e("jsonstr"," "+jsonStr);



		if(jsonStr!=null){
			try {
				JSONObject $json = new JSONObject(jsonStr);
				int success = $json.getInt("success");
				int updates  = $json.getInt("updates");
				if(success==1){

				}
				else{
					return null;
				}
			}
			catch (Exception e){}
		}
*/
		//json check




		return null;
	}

	@Override
	protected void onPostExecute(Void result) {

		super.onPostExecute(result);

		/*dh.close();*/
		if(pd.isShowing())pd.dismiss();

	}



}
