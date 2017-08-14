package com.tidalsolutions.phillife.db;

public class Clients {

	/*private DatabaseHandler databaseHandler;

    // SurveyResponse table name
    private static final String TABLE_NAME = "clients";//or albums
    private static final String KEY_ID = "_id";
    private static final String KEY_NAME= "name";
	private static final String KEY_CLIENT_HASHKEY= "client_hashkey";
	private static final String KEY_DATE= "client_date";

    private static String create_table = "CREATE TABLE \"clients\" (\n" +
			"\t`_id`\tINTEGER PRIMARY KEY AUTOINCREMENT,\n" +
			"\t`client_name`\tTEXT,\n" +
			"\t`client_hashkey`\tTEXT,\n" +
			"\t`date`\tTEXT\n" +
			")";


    public Clients(DatabaseHandler dbHandler) {
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

	public void loadDummy(){



		for(int g=0;g<10;g++){
			Clients_model cm = new Clients_model();
			cm.setClient_hash_key(g+"D5K4D"+g);
			cm.setClient_name("Name "+g);
			cm.setDate("05-15-2016");
			add_client(cm);
		}


	}

	public void deleteAll(){

		SQLiteDatabase db = databaseHandler.getWritableDatabase();

		String query = "delete from "+TABLE_NAME;
		Cursor c = db.rawQuery(query, null);

	}
    


   public void add_client(Clients_model rm) {
       SQLiteDatabase db = databaseHandler.getWritableDatabase();
       
       ContentValues values = new ContentValues();
       
       //values.put("_id", rm.get_id());
       values.put("client_name", rm.getClient_name());
       values.put("client_hashkey", rm.getClient_hash_key());
	   values.put("date", rm.getDate());

       db.insert(TABLE_NAME, null, values);
       db.close(); 
       // Closing database connection    	    	
   }
   
   public int getLastClientId(){
  	 SQLiteDatabase db = databaseHandler.getReadableDatabase();
  	String sql = "select _id from "+TABLE_NAME+" order by _id desc limit 1";
  	Cursor c = db.rawQuery(sql, null);
  	
  	if(c!=null){
  		if(c.moveToFirst())
  		return c.getInt(0);
  	}
  	
  	
  	
  	return 0;
  }
   


    
       public ArrayList<Clients_model> getClients(){
    	//Log.e("inside getReadings", "asdasd");
    	ArrayList<Clients_model> albumList = new ArrayList<Clients_model>();

    	SQLiteDatabase sql = databaseHandler.getReadableDatabase();
    	String query = "select * from "+TABLE_NAME;
    	Cursor c = sql.rawQuery(query, null);

    	if(c.moveToFirst()){

    		do{
				Clients_model rd = new Clients_model();

    			rd.set_id(c.getInt(0));
    			rd.setClient_name(c.getString(1));
    			rd.setClient_hash_key(c.getString(2));
				rd.setDate(c.getString(3));

				albumList.add(rd);
    		}
    		while(c.moveToNext());
    	}


    	return albumList;
    }




    
    
    */
    
    
    
    // Adding new venue
    
}
