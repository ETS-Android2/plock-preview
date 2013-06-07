package com.petarpuric.plock;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SaveDialog extends FragmentActivity {
	
	
	//SaveDialog class created for when the user wants to save the password.
	
	
	Dialog dialog;
	EditText edtUse, edtPassToSave;
	TextView tvChkSaveErr;
	DatabaseHelper db;
	long nE;
	Context qCon;
	
	public Dialog createDialog(Context mContext, String password) {
		qCon = mContext;
		//using the database to connect in order to write to the database
		db = new DatabaseHelper(mContext);
	
		dialog = new Dialog(mContext);
		dialog.setContentView(R.layout.dialog);
		
		//specify Layout Parameters for the save dialog. Otherwise the Layout shrinks.
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
		lp.copyFrom(dialog.getWindow().getAttributes());
		lp.width = WindowManager.LayoutParams.FILL_PARENT;
		lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
		
		edtUse = (EditText) dialog.findViewById(R.id.edtUse);
	    edtPassToSave = (EditText) dialog.findViewById(R.id.edtPassToSave);
	    Button btnSaveToDB = (Button) dialog.findViewById(R.id.btnSaveToDB);
	    Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);
	    
	    tvChkSaveErr = (TextView) dialog.findViewById(R.id.tvChkSaveErr);
	   
	    
	    edtPassToSave.setText(password);
	    
	    btnSaveToDB.setOnClickListener(new ClickListener());
	    btnCancel.setOnClickListener(new ClickListener());
		
		dialog.setTitle("Save Password");
		
		
		
	
	dialog.show();
	dialog.getWindow().setAttributes(lp);
	return dialog;
	
	
}

	private void limitReached() {
		AlertDialog.Builder limitDialog = new AlertDialog.Builder(qCon);
		
		limitDialog.setTitle("Limit Reached");
		
		limitDialog.setMessage("You have reached your password storage limit." +
				"\nPlease upgrade to the PRO version");
		
		limitDialog.setPositiveButton("Upgrade!", new DialogInterface.OnClickListener() {
			
			
			public void onClick(DialogInterface dialog, int which) {
				
				//Uri marketUri = Uri.parse("market://details?id=" + packageName);
				//Intent marketIntent = new Intent(Intent.ACTION_VIEW, marketUri);
				//startActivity(marketIntent);
				
			}
		});
		
		limitDialog.setNegativeButton("Okay", new DialogInterface.OnClickListener() {
			
			
			public void onClick(DialogInterface dialog, int which) {
				
				dialog.dismiss();
			}
		});
		
		limitDialog.show();
		
	}
	
private void saveToDB() {
		//Specify writeable database
		SQLiteDatabase dbSave = db.getWritableDatabase();
		//Values to be inserted
		ContentValues values = new ContentValues();
		
		nE = DatabaseUtils.queryNumEntries(dbSave, "passwords");
		
		if (nE >= 7) {
			//notify user limit of password has been reached.
			dialog.dismiss();
			limitReached();
		} else {
		
		//get the Key name for each value, and get value from each text box
		values.put(db.getKeyUse().toString(), edtUse.getText().toString());
		values.put(db.getKeyPass().toString(), edtPassToSave.getText().toString());
		
		//insert into table & close
		dbSave.insert(db.getTable(), "_id", values);
		dbSave.close();
		//closes the dialog after saving is done.
		dialog.dismiss();
		}
	}

private void checkIfEmpty() throws GenException {
	//if the Use Textbox is empty throws an exception
	if (edtUse.getText().length() <= 0) {
		throw new GenException("2");
		
		
	}
	//if the Password Textbox is empty throws an exception
	else if (edtPassToSave.getText().length() <= 0) {
		throw new GenException("3");
	}
	else {}
	
}


class ClickListener implements View.OnClickListener {

	
	public void onClick(View v) {
		switch(v.getId()) {
		
		case R.id.btnSaveToDB:
			//validation before saving
			try {
			checkIfEmpty();
			saveToDB();
			} catch (GenException e) {
				
				tvChkSaveErr.setText(e.badSave());
				
			
			}
			break;
		case R.id.btnCancel:
			dialog.dismiss();
			break;
		}
		
	}

	

	
}


	
}
