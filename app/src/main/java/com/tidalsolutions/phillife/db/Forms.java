package com.tidalsolutions.phillife.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.tidalsolutions.phillife.models.Albums_model;

import java.util.ArrayList;

public class Forms {

	private DatabaseHandler databaseHandler;

    // SurveyResponse table name
    private static final String TABLE_NAME = "forms";
    private static final String KEY_ID = "_id";
    private static final String KEY_NAME= "name";
	private static final String KEY_DESCRIPTION= "description";
	private static final String KEY_CLIENT_ID= "client_id";
	private static final String KEY_AGENT_ID= "agent_id";
	private static final String KEY_DATE_CREATED= "date_created";
	private static final String KEY_HASH_KEY= "hash_key";

	private static final String KEY_DATE_SUBMITTED= "date_submitted";
	private static final String KEY_DATE_REJECTED= "date_rejected";
	private static final String KEY_DATE_APPROVED= "date_approved";

	private static final String KEY_REJECTED_DESCRIPTION= "rejected_description";
	private static final String KEY_IS_DRAFT= "is_draft";
	private static final String KEY_IS_PAGE_COUNT= "page_count";


    private static String create_table = "CREATE TABLE \"forms\" (\n" +
			"\t`_id`\tINTEGER PRIMARY KEY AUTOINCREMENT,\n" +
			"\t`forms_hash_key`\tTEXT,\n" +
			"\t`name`\tTEXT,\n" +
			"\t`description`\tTEXT,\n" +
			"\t`client_name`\tTEXT,\n" +
			"\t`agent_id`\tINTEGER,\n" +
			"\t`date_created`\tTEXT,\n" +
			"\t`date_submitted`\tTEXT,\n" +
			"\t`date_rejected`\tTEXT,\n" +
			"\t`date_approved`\tTEXT,\n" +
			"\t`rejected_description`\tTEXT,\n" +
			"\t`page_count`\tINTEGER,\n" +
			"\t`is_draft`\tINTEGER,\n" +
			"\t`status`\tINTEGER,\n" +
			"\t`note`\tTEXT\n" +
			")";


    public Forms(DatabaseHandler dbHandler) {
    	databaseHandler = dbHandler;
    }
    
    public static void create(SQLiteDatabase sqLiteDatabase) {
    	try {
			sqLiteDatabase.execSQL(create_table);
		} catch (SQLException e) {
			Log.e("error in", ""+e);
		}   	
    }
    
    public static void drop(SQLiteDatabase sqLiteDatabase) {
    	try {
			sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		} catch (SQLException e) {
			Log.e("error in", ""+e);
		}    	
    }


	public void deleteAll(){

		SQLiteDatabase db = databaseHandler.getWritableDatabase();

		String query = "delete from "+TABLE_NAME;
		Cursor c = db.rawQuery(query, null);

	}
    


   public void add_form(Albums_model rm) {
	   Log.e("saving stuff here","add form");
       SQLiteDatabase db = databaseHandler.getWritableDatabase();
       
       ContentValues values = new ContentValues();
       

       values.put("name", rm.getName());
       values.put("description", rm.getDescription());
	   values.put("forms_hash_key", rm.getForm_id());
       values.put("client_name", rm.getClient_name());
       values.put("agent_id",rm.getAgent_id());
	   values.put("date_created",rm.getDate_created());
	   values.put("date_submitted",rm.getDate_submitted());
	   values.put("date_rejected",rm.getDate_rejected());
	   values.put("date_approved",rm.getDate_approved());
	   values.put("rejected_description",rm.getRejected_description());
	   values.put("is_draft",rm.getIs_draft());
	   values.put("page_count",rm.getPage_count());
	   values.put("status",rm.getStatus());
	   values.put("note",rm.getNote());
       
       db.insert(TABLE_NAME, null, values);
       db.close(); 
       // Closing database connection    	    	
   }



	public void updateForm(Albums_model rm){

		SQLiteDatabase db = databaseHandler.getWritableDatabase();
		/*SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
		String currentDateandTime = sdf.format(new Date());*/

		ContentValues values = new ContentValues();

		values.put("name", rm.getName());
		values.put("description", rm.getDescription());
		values.put("forms_hash_key", rm.getForm_id());
		values.put("client_name", rm.getClient_name());
		values.put("agent_id",rm.getAgent_id());
		values.put("date_created",rm.getDate_created());
		values.put("date_submitted",rm.getDate_submitted());
		values.put("date_rejected",rm.getDate_rejected());
		values.put("date_approved",rm.getDate_approved());
		values.put("rejected_description",rm.getRejected_description());
		values.put("is_draft",rm.getIs_draft());
		values.put("page_count",rm.getPage_count());
		values.put("status",rm.getStatus());
		values.put("note",rm.getNote());

		String where = "forms_hash_key=?";
//		String[] whereArgs = new String[] {String.valueOf(rm.getForms_hashkey())};
		String[] whereArgs = new String[] {rm.getForm_id()};

		db.update(TABLE_NAME, values, where, whereArgs);

		db.close();

		System.out.println("updated");

	}

	public void updateFormPagecount(Albums_model rm){

		SQLiteDatabase db = databaseHandler.getWritableDatabase();
		/*SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
		String currentDateandTime = sdf.format(new Date());*/

		ContentValues values = new ContentValues();


		values.put("page_count",rm.getPage_count());

		String where = "forms_hash_key=?";
		String[] whereArgs = new String[] {String.valueOf(rm.getForm_id())};



		db.update(TABLE_NAME, values, where, whereArgs);

		db.close();


		System.out.println("updated");

	}

	public void updateFormDraft(Albums_model rm){

		SQLiteDatabase db = databaseHandler.getWritableDatabase();
		/*SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
		String currentDateandTime = sdf.format(new Date());*/

		ContentValues values = new ContentValues();


		values.put("is_draft",0);

		String where = "forms_hash_key=?";
		String[] whereArgs = new String[] {String.valueOf(rm.getForm_id())};



		db.update(TABLE_NAME, values, where, whereArgs);

		db.close();


		System.out.println("updated");

	}

   public int getLastFormId(){
  	 SQLiteDatabase db = databaseHandler.getReadableDatabase();
  	String sql = "select _id from "+TABLE_NAME+" order by _id desc limit 1";
  	Cursor c = db.rawQuery(sql, null);
  	
  	if(c!=null){
  		if(c.moveToFirst())
  		return c.getInt(0);
  	}
  	
  	
  	
  	return 0;
  }
   
   

    
       public ArrayList<Albums_model> getForms(int isDraft){
    	//if drafts or all
    	ArrayList<Albums_model> albumList = new ArrayList<Albums_model>();

    	SQLiteDatabase sql = databaseHandler.getReadableDatabase();
    	String query;

		   if(isDraft==0){
			   query = "select * from "+TABLE_NAME;
		   }
		   else{
			   query = "select * from "+TABLE_NAME+" where is_draft = 1";
		   }


    	Cursor c = sql.rawQuery(query, null);

    	if(c.moveToFirst()){

    		do{
				Albums_model rd = new Albums_model();

    			rd.set_id(c.getInt(0));
				rd.setForm_id(c.getString(1));

    			rd.setName(c.getString(2));
    			rd.setDescription(c.getString(3));
				rd.setClient_name(c.getString(4));
				rd.setAgent_id(c.getInt(5));
				rd.setDate_created(c.getString(6));

				rd.setDate_submitted(c.getString(7));
				rd.setDate_rejected(c.getString(8));
				rd.setDate_approved(c.getString(9));
				rd.setRejected_description(c.getString(10));
				rd.setPage_count(c.getInt(11));
				rd.setIs_draft(c.getInt(12));
				rd.setStatus(c.getInt(13));
				rd.setNote(c.getString(14));

				albumList.add(rd);
    		}
    		while(c.moveToNext());
    	}


    	return albumList;
    }

	public Albums_model getFormByHashKey(String hashkey){
		//if drafts or all
		Albums_model rd = new Albums_model();

		SQLiteDatabase sql = databaseHandler.getReadableDatabase();
		String query;


		query = "select * from "+TABLE_NAME+" where forms_hash_key ='"+hashkey+"'";



		Cursor c = sql.rawQuery(query, null);

		if(c.moveToFirst()){

			do{


				rd.set_id(c.getInt(0));
				rd.setForm_id(c.getString(1));

				rd.setName(c.getString(2));
				rd.setDescription(c.getString(3));
				rd.setClient_name(c.getString(4));
				rd.setAgent_id(c.getInt(5));
				rd.setDate_created(c.getString(6));

				rd.setDate_submitted(c.getString(7));
				rd.setDate_rejected(c.getString(8));
				rd.setDate_approved(c.getString(9));
				rd.setRejected_description(c.getString(10));
				rd.setPage_count(c.getInt(11));
				rd.setIs_draft(c.getInt(12));
				rd.setStatus(c.getInt(13));
				rd.setNote(c.getString(14));

				return rd;
			}
			while(c.moveToNext());
		}


		return rd;
	}
    
    
    
    
    
    
    
    
    // Adding new venue
    
}
