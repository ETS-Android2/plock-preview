package com.petarpuric.plock;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import com.actionbarsherlock.ActionBarSherlock;
import com.actionbarsherlock.view.Menu;



import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;

import android.util.Log;

import android.view.View;

import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

import android.widget.ListView;

import android.widget.TextView;
import android.widget.Toast;

import org.holoeverywhere.app.Activity;

import org.holoeverywhere.widget.CheckBox;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.exception.DropboxException;
import com.dropbox.client2.exception.DropboxUnlinkedException;
import com.dropbox.client2.session.AccessTokenPair;
import com.dropbox.client2.session.AppKeyPair;
import com.dropbox.client2.session.Session.AccessType;




public class SavedPass extends Activity {
	//DROPBOX DISABLED IN THIS VERSION
	//private static final String APP_KEY = "e4gpjbzj6m1vt0n";
	//private static final String APP_SECRET = "eczxaa61dl8euf9";
	//private static final AccessType ACCESS_TYPE = AccessType.APP_FOLDER;
	
	static final int NEWUPLOAD = 0;
	DropboxAPI<AndroidAuthSession> mApi;
	DatabaseHelper dbHelp = new DatabaseHelper(this);
	
	//List of passwords to be stored
	ArrayList<Passwords> pw = null;
	int iID, chkG = 0;
	
	TextView rowView;
	CheckBox rowCheckBox;
	
	private ListView mListView;
	private PasswordsAdapter listAdapter;
	private DropboxAPI<AndroidAuthSession> mDBApi;
	
	MenuItem checkAll, delete;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listview);
		
		
		
		mListView = (ListView) findViewById(R.id.mListView);
		
		//pw is assigned a list of all the passwords in the DB.
		 pw = getPasswords();

		 listAdapter = new PasswordsAdapter(this, R.layout.listrow, pw);
	
		 mListView.setAdapter(listAdapter);
		 mListView.setOnItemClickListener(new itemListener());
		 mListView.setOnItemLongClickListener(new copyListener());
		 
		
		 
		 ActionBar ab = getSupportActionBar();
			
			ab.setDisplayHomeAsUpEnabled(true);
			ab.setIcon(R.drawable.ic_action_home);
			ab.setDisplayShowTitleEnabled(false);
			
			AppKeyPair appKeys = new AppKeyPair(APP_KEY, APP_SECRET);
			AndroidAuthSession session = new AndroidAuthSession(appKeys, ACCESS_TYPE);
			mDBApi = new DropboxAPI<AndroidAuthSession>(session);
		 }
	
	protected void onResume() {
	    super.onResume();

	    if (mDBApi.getSession().authenticationSuccessful()) {
	        try {
	            
	            mDBApi.getSession().finishAuthentication();

	            AccessTokenPair tokens = mDBApi.getSession().getAccessTokenPair();
	            
	            
	    	    
	            
	        } catch (IllegalStateException e) {
	            Log.i("DbAuthLog", "Error authenticating", e);
	        }
	    }
	    
	    
	    
	}
	
	
	 

	public boolean onCreateOptionsMenu(Menu menu) {
		
		MenuInflater mInflater = getSupportMenuInflater();
		mInflater.inflate(R.menu.saved_menu, menu);
		
		checkAll = menu.findItem(R.id.menu_checkall);
		delete = menu.findItem(R.id.menu_discard);
		
		return true;
	}
	
	
	public boolean onOptionsItemSelected(MenuItem item) {
	
		switch(item.getItemId()) {
		case android.R.id.home:
			SavedPass.super.onBackPressed();
			break;
		
		case R.id.sync:
			syncDropBox(pw);
			break;
			
		case R.id.menu_edit:
			toggleEditOptions();
			break;
			
		case R.id.menu_checkall:
			checkAllBoxes();
			break;
			
		case R.id.menu_discard:
			deleteSelected();
			break;
		}
		
		return true;
	}
	
	private void syncDropBox(ArrayList<Passwords> pw) {
		
	
		try {
			FileOutputStream f = openFileOutput("001.txt", Context.MODE_PRIVATE);
			OutputStreamWriter osw = new OutputStreamWriter(f);
			
			
			for (Passwords passwords : pw) {
				osw.write("\n" + passwords.getUse() + "-" + passwords.getPass());
				
			}
			
			
			osw.flush();
			osw.close();
			
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		mDBApi.getSession().startAuthentication(SavedPass.this);
		
		
		
		
		
		
	}
	
	
	private boolean deleteSelected() {
		SQLiteDatabase db = dbHelp.getWritableDatabase();
		
		List<Passwords> chkG =  new ArrayList<Passwords>();
		List<Integer> listG = new ArrayList<Integer>();
		
		boolean deleteItem = false;
		for (int i=0; i < mListView.getCount(); i++) {
			rowCheckBox =	(CheckBox) mListView.getChildAt(i).findViewById(R.id.rowCheckBox);
			
			if (rowCheckBox.isChecked()) {	
				chkG.add(listAdapter.getItem(i));
				listG.add(pw.get(i).getHId());
			}
		}
		
		for (int j=0; j<chkG.size(); j++) {
			deleteItem = db.delete(dbHelp.getTable(), dbHelp.getKeyId() + "=" + listG.get(j) , null) > 0;
			listAdapter.remove(chkG.get(j));
		}
		
		db.close();
		
		return deleteItem;
	}



	private void checkAllBoxes() {
		
		if (chkG == 1) {
			for (int i=0; i < mListView.getCount(); i++) {
				rowCheckBox =	(CheckBox) mListView.getChildAt(i).findViewById(R.id.rowCheckBox);
				
				rowCheckBox.setChecked(false);
				
			
					}
			chkG = 0;
		}
		else {

		for (int i=0; i < mListView.getCount(); i++) {
			rowCheckBox =	(CheckBox) mListView.getChildAt(i).findViewById(R.id.rowCheckBox);
			
			rowCheckBox.setChecked(true);
			
		
				}
		chkG = 1;
		
		}
	}



	private void toggleEditOptions() {
		
		if (!checkAll.isVisible()) {
			checkAll.setVisible(true);
			delete.setVisible(true);
			for (int i=0; i < mListView.getCount(); i++) {
		rowCheckBox =	(CheckBox) mListView.getChildAt(i).findViewById(R.id.rowCheckBox);
		rowCheckBox.setVisibility(View.VISIBLE);
			}
		} else {
			checkAll.setVisible(false);
			delete.setVisible(false);
			for (int i=0; i < mListView.getCount(); i++) {
			rowCheckBox =	(CheckBox) mListView.getChildAt(i).findViewById(R.id.rowCheckBox);
			rowCheckBox.setVisibility(View.GONE);
			}
		}
		
	}



	private ArrayList<Passwords> getPasswords() {
		ArrayList<Passwords> passwordList = new ArrayList<Passwords>();
		//Query to select all passwords from the DB
		String selectQuery = "SELECT * FROM " + dbHelp.getTable();
		SQLiteDatabase db = dbHelp.getWritableDatabase();
		
	//	db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" + dbHelp.getTable() + "'");
		
		Cursor cursor = db.rawQuery(selectQuery, null);
		iID = 1;
		//Retrieve each password and add all its information to passwordList
		if (cursor.moveToFirst()) {
			do {
				Passwords password = new Passwords();
				
				password.setHId(Integer.parseInt(cursor.getString(0)));
				password.setId(iID);
				//password.setId(Integer.parseInt(cursor.getString(0)));
				password.setUse(cursor.getString(1));
				password.setPass(cursor.getString(2));
				
				passwordList.add(password);
				iID++;
			} while (cursor.moveToNext());
		}
		db.close();
		cursor.close();
		return passwordList;
		
	}
	
	private String getPassword(int pos) {
		
		return pw.get(pos).getPass();
	}
	
	private String getUse(int pos) {
		return pw.get(pos).getUse();
	}
	
		

	
	
	
	class itemListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int pos,
				long id) {
			TextView tx =(TextView)view.findViewById(R.id.rowListView);
			
			if (tx.getText().equals(getPassword(pos))) {
				tx.setText(getUse(pos));
			} else 
			tx.setText(getPassword(pos));
			
		}
		
		
	}
	
	class copyListener implements OnItemLongClickListener {

		@TargetApi(11)
		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View view,
				int pos, long id) {
			
			int sdk = android.os.Build.VERSION.SDK_INT;
			if (sdk < android.os.Build.VERSION_CODES.HONEYCOMB) {
				android.text.ClipboardManager cp = (android.text.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
				cp.setText(getPassword(pos));
			} else {
				android.content.ClipboardManager cp = (android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
				ClipData clip = android.content.ClipData.newPlainText("", getPassword(pos));
				cp.setPrimaryClip(clip);
			}
			Toast.makeText(getApplicationContext(), "Password copied!", Toast.LENGTH_SHORT).show();
			return false;
		}
		
	}

}
