package com.petarpuric.plock;

import java.util.ArrayList;
import java.util.List;

import com.actionbarsherlock.ActionBarSherlock;
import com.actionbarsherlock.view.Menu;



import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ClipData;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.text.ClipboardManager;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.holoeverywhere.app.Activity;
import org.holoeverywhere.widget.Button;
import org.holoeverywhere.widget.CheckBox;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.petarpuric.plock.PasswordsAdapter.ViewHolder;


public class SavedPass extends Activity {
	
	DatabaseHelper dbHelp = new DatabaseHelper(this);
	
	//List of passwords to be stored
	ArrayList<Passwords> pw = null;
	int iID, chkG = 0;
	
	TextView rowView;
	CheckBox rowCheckBox;
	
	private ListView mListView;
	private PasswordsAdapter listAdapter;
	
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
			
			return false;
		}
		
	}

}
