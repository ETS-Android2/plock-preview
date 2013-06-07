package com.petarpuric.plock;

//import org.holoeverywhere.preference.DialogPreference;
//import org.holoeverywhere.widget.EditText;

//import com.actionbarsherlock.internal.view.
import org.holoeverywhere.widget.Button;

import android.preference.DialogPreference;
import android.preference.PreferenceManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;

import android.widget.EditText;
import android.widget.Toast;

public class CompoundEditText extends DialogPreference{
	
	private EditText edtOldPin, edtNewPin, edtSetPin;
	String oldPin, newPin;
	int length = 0, minLength = 4;
	Context mContext = null;
	
	String checkThis = "", oldCode ="", newCode="";

	SharedPreferences prefs;
	


		public CompoundEditText(Context context, AttributeSet attrs) {
			super(context, attrs);
			mContext = context;
		
			prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
			if(!prefs.getBoolean("setup", false)) {
				setDialogLayoutResource(R.layout.dialogprefs_initialchange);
			} else 
			setDialogLayoutResource(R.layout.dialogprefs_change);
		}
		
	
	
		protected void onBindDialogView(View view) {
			
			
			if (!prefs.getBoolean("setup", false)) {
				
				edtSetPin = (EditText) view.findViewById(R.id.prefs_setpass);
				
				edtSetPin.addTextChangedListener(new TextWatcher() {

					@Override
					public void afterTextChanged(Editable s) {
						
						newCode = edtSetPin.getText().toString();
						
					}

					@Override
					public void beforeTextChanged(CharSequence arg0, int arg1,
							int arg2, int arg3) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void onTextChanged(CharSequence arg0, int arg1,
							int arg2, int arg3) {
						// TODO Auto-generated method stub
						
					}
					
				});
				
			} else {
			
			
			edtOldPin = (EditText) view.findViewById(R.id.prefs_change_oldpass);
			edtNewPin = (EditText) view.findViewById(R.id.prefs_change_newpass);
			
			
			edtOldPin.addTextChangedListener(new TextWatcher() {

				@Override
				public void afterTextChanged(Editable s) {
					
					oldCode = edtOldPin.getText().toString();
					
					if (s.length() == length && oldCode.equals(checkThis)) {
						edtNewPin.setEnabled(true);
						edtOldPin.setEnabled(false);
					}
				}

				@Override
				public void beforeTextChanged(CharSequence s, int start,
						int count, int after) {
					loadPrefs();
					length = checkThis.length();
					
				}

				

				@Override
				public void onTextChanged(CharSequence s, int start,
						int before, int count) {
					
					
				}
				
			});
			
			edtNewPin.addTextChangedListener(new TextWatcher() {

				String msg = "";
				@Override
				public void afterTextChanged(Editable s) {
					
					newCode = edtNewPin.getText().toString();
					
					if (newCode.length() < minLength) {
						msg = "Pin too short!";
						
					} else {
						msg = "Pin OK!";
						
					}
					
				}

				@Override
				public void beforeTextChanged(CharSequence s, int start,
						int count, int after) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void onTextChanged(CharSequence s, int start,
						int before, int count) {
					// TODO Auto-generated method stub
					
				}
				
			});
			}
			
			
			super.onBindDialogView(view);
		}
		
		private void loadPrefs() {
			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
			checkThis = prefs.getString("passcode", "code");	
			
		}
		
		private void savePrefs(String code) {
			
			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
			SharedPreferences.Editor editor = prefs.edit();
			
			editor.putString("passcode", code);
			editor.putBoolean("setup", true);
			editor.commit();

		}
		
		public void onClick(DialogInterface dialog, int which) {
			switch(which) {
			case DialogInterface.BUTTON_POSITIVE:
				savePrefs(newCode);
				break;
				
			}
		
		}
}
