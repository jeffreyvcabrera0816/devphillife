package com.tidalsolutions.phillife.db;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DatabaseHandler extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;
  
    // Database Name
    private static final String DATABASE_NAME = "PHILLIFE_DB";

    private String DATABASE_PATH = "/data/data/com.tidalsolutions.phillife/databases/";
    private final Context mContext;
    private boolean mCreateDatabase = false;
    private boolean mUpgradeDatabase = false;

         
    //public Dictionary_searches dictionary_searches;
    public Forms forms;

    public Pictures pictures;
    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        
        mContext = context;

        forms = new Forms(this);

        pictures = new Pictures(this);
    }

	@Override
	public void onCreate(SQLiteDatabase db) {
		
		mCreateDatabase = true;
		// Create tables


        Forms.create(db);

        Pictures.create(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
		mUpgradeDatabase = true;
		// Drop older tables if existing

        Forms.drop(db);

        Pictures.drop(db);
		// Create tables again
        onCreate(db);
	}

    public void resetTable(){
        SQLiteDatabase db = this.getWritableDatabase();
        onUpgrade(db,DATABASE_VERSION,DATABASE_VERSION+1);
        //new SharedPrefManager(mContext).deleteUsers();
    }

	public void initializeDatabase() {
		//initializeDatabase(DATABASE_PATH);
		initializeDatabase(DATABASE_PATH);
	}
	
    public void initializeDatabase(String path) {
    	Log.e("init DB", "initialized!");
        DATABASE_PATH = path;
        getWritableDatabase();
        
        if(mUpgradeDatabase) {
            mContext.deleteDatabase(DATABASE_NAME);
        }
        
        if(mCreateDatabase || mUpgradeDatabase) {
            try {
                copyDatabase();
                Log.e("Copy: ", "Done.");
            } catch (IOException e) {
                throw new Error("Error copying database: " + e);
            }
        }
    }
	 
    private void copyDatabase() throws IOException {
        close();
        
        InputStream input = mContext.getAssets().open("databases/" + DATABASE_NAME);
        
        String outFileName = DATABASE_PATH + DATABASE_NAME;
        
        OutputStream output = new FileOutputStream(outFileName);
        
        // Transfer bytes from the input file to the output file
        byte[] buffer = new byte[1024];
        int length;
        
        while ((length = input.read(buffer)) > 0) {
            output.write(buffer, 0, length);
        }
          
        output.flush();
        output.close();
        input.close();
 
        getWritableDatabase().close();
    }
    
    
}

