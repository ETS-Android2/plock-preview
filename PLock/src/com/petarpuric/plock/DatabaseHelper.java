package com.petarpuric.plock;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper{

	
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "passwordsManager";
	private static final String TABLE_PASS = "passwords";
	
	private static final String KEY_ID = "_id";
	private static final String KEY_USE = "use";
	private static final String KEY_PASS = "pass";
	
	
	
	String CREATE_PASS_TABLE = "CREATE TABLE " + TABLE_PASS + "("
			+ KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_USE + " TEXT,"
			+ KEY_PASS + " TEXT" + ")";
	

	 public DatabaseHelper(Context context) {
	        super(context, DATABASE_NAME, null, DATABASE_VERSION);
	    }
	 //create database
	@Override
	public void onCreate(SQLiteDatabase db) {
		
		db.execSQL(CREATE_PASS_TABLE);
		
	}

	
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_PASS);
		
		onCreate(db);
		
	}
	
	//getters specified in order to use DatabaseHelper class with other classes
	
	public String getKeyUse() {
		return KEY_USE;
	}
	
	public String getKeyPass() {
		return KEY_PASS;
	}
	
	public String getKeyId() {
		return KEY_ID;
	}
	
	public String getTable() {
		return TABLE_PASS;
	}

}
