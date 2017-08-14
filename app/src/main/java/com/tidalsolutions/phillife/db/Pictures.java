package com.tidalsolutions.phillife.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.tidalsolutions.phillife.models.Pictures_model;

import java.util.ArrayList;

public class Pictures {

	private DatabaseHandler databaseHandler;

    // SurveyResponse table name
    private static final String TABLE_NAME = "Pictures";//or albums
    private static final String KEY_ID = "_id";
    private static final String KEY_NAME= "name";
	private static final String KEY_DESCRIPTION= "description";
	private static final String KEY_SEQUENCE= "sequence";
	private static final String KEY_FILE_PATH= "file_path";
	private static final String KEY_FORMS_HASHKEY= "forms_hashkey";
	private static final String KEY_DATE= "date";

    private static String create_table = "CREATE TABLE \"pictures\" (\n" +
			"\t`_id`\tINTEGER PRIMARY KEY AUTOINCREMENT,\n" +
			"\t`name`\tTEXT,\n" +
			"\t`description`\tTEXT,\n" +
			"\t`sequence`\tINTEGER,\n" +
			"\t`file_path`\tTEXT,\n" +
			"\t`forms_hash_key`\tTEXT,\n" +
			"\t`date`\tTEXT\n" +
			")";


    public Pictures(DatabaseHandler dbHandler) {
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



	public void updatePicture(Pictures_model pm){

		SQLiteDatabase db = databaseHandler.getWritableDatabase();
		/*SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
		String currentDateandTime = sdf.format(new Date());*/

		ContentValues dataToInsert = new ContentValues();
		dataToInsert.put("name", pm.getName());
		dataToInsert.put("description", pm.getDescription());
		dataToInsert.put("sequence", pm.getSequence());
		dataToInsert.put("file_path", pm.getFile_path());
		dataToInsert.put("forms_hash_key", pm.getForms_hashkey());
		dataToInsert.put("description", pm.getDescription());
		dataToInsert.put("date", pm.getDate());

		String where = "_id=?";
		String[] whereArgs = new String[] {String.valueOf(pm.get_id())};



		db.update(TABLE_NAME, dataToInsert, where, whereArgs);

		db.close();


		System.out.println("updated");

	}


   public void add_picture(Pictures_model rm) {
       SQLiteDatabase db = databaseHandler.getWritableDatabase();
       
       ContentValues values = new ContentValues();
       

       values.put("name", rm.getName());
       values.put("description", rm.getDescription());
	   values.put("sequence", rm.getSequence());
	   values.put("file_path", rm.getFile_path());
	   values.put("forms_hash_key", rm.getForms_hashkey());
	   values.put("description", rm.getDescription());
	   values.put("date", rm.getDate());
       db.insert(TABLE_NAME, null, values);
       db.close(); 
       // Closing database connection    	    	
   }
   
   public int getLastHelpId(){
  	 SQLiteDatabase db = databaseHandler.getReadableDatabase();
  	String sql = "select _id from "+TABLE_NAME+" order by _id desc limit 1";
  	Cursor c = db.rawQuery(sql, null);
  	
  	if(c!=null){
  		if(c.moveToFirst())
  		return c.getInt(0);
  	}
  	
  	
  	
  	return 0;
  }
   
   

    
       public ArrayList<Pictures_model> getPictures(){
    	//Log.e("inside getReadings", "asdasd");
    	ArrayList<Pictures_model> albumList = new ArrayList<Pictures_model>();

    	SQLiteDatabase sql = databaseHandler.getReadableDatabase();
    	String query = "select * from "+TABLE_NAME;
    	Cursor c = sql.rawQuery(query, null);

    	if(c.moveToFirst()){

    		do{
				Pictures_model rd = new Pictures_model();

    			rd.set_id(c.getInt(0));
    			rd.setName(c.getString(1));
    			rd.setDescription(c.getString(2));
				rd.setSequence(c.getInt(3));
				rd.setFile_path(c.getString(4));
    			rd.setForms_hashkey(c.getString(5));
				rd.setDate(c.getString(6));
				albumList.add(rd);
    		}
    		while(c.moveToNext());
    	}


    	return albumList;
    }



	public ArrayList<Pictures_model> getPicturesByHash(String hashkey){
		//Log.e("inside getReadings", "asdasd");
		ArrayList<Pictures_model> albumList = new ArrayList<Pictures_model>();

		SQLiteDatabase sql = databaseHandler.getReadableDatabase();
		String query = "select * from "+TABLE_NAME+" where forms_hash_key ='"+hashkey+"'";
		Cursor c = sql.rawQuery(query, null);

		if(c.moveToFirst()){

			do{
				Pictures_model rd = new Pictures_model();

				rd.set_id(c.getInt(0));
				rd.setName(c.getString(1));
				rd.setDescription(c.getString(2));
				rd.setSequence(c.getInt(3));
				rd.setFile_path(c.getString(4));
				rd.setForms_hashkey(c.getString(5));
				rd.setDate(c.getString(6));
				albumList.add(rd);
			}
			while(c.moveToNext());
		}


		return albumList;
	}
    
    
    
    
    
    
    // Adding new venue
    
}
