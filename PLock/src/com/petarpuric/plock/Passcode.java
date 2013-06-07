package com.petarpuric.plock;


//import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
//import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.holoeverywhere.app.Activity;

import org.holoeverywhere.widget.Button;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;


public class Passcode extends Activity {

	Button btnNums1, btnNums2, btnNums3, btnNums4, btnNums5, btnNums6, btnNums7, btnNums8, btnNums9, btnNums0, btnSetPassCode;
	ImageButton btnDel, btnCheck;
	TextView tvSetPass, tvEnterCode;
	
	String code = "", checkThis = "";
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.passcode);
		
		
		btnNums1 = (Button) findViewById(R.id.btnNums1);
		btnNums2 = (Button) findViewById(R.id.btnNums2);
		btnNums3 = (Button) findViewById(R.id.btnNums3);
		btnNums4 = (Button) findViewById(R.id.btnNums4);
		btnNums5 = (Button) findViewById(R.id.btnNums5);
		btnNums6 = (Button) findViewById(R.id.btnNums6);
		btnNums7 = (Button) findViewById(R.id.btnNums7);
		btnNums8 = (Button) findViewById(R.id.btnNums8);
		btnNums9 = (Button) findViewById(R.id.btnNums9);
		btnNums0 = (Button) findViewById(R.id.btnNums0);
		
		tvSetPass = (TextView) findViewById(R.id.tvLRPassCode);
		tvEnterCode = (TextView) findViewById(R.id.tvEnterCode);
		
		btnDel = (ImageButton) findViewById(R.id.btnDel);
		btnCheck = (ImageButton) findViewById(R.id.btnCheck);
		
		btnSetPassCode = (Button) findViewById(R.id.btnSetPassCode);
		
		
		btnNums1.setOnClickListener(new ClickListener());
		btnNums2.setOnClickListener(new ClickListener());
		btnNums3.setOnClickListener(new ClickListener());
		btnNums4.setOnClickListener(new ClickListener());
		btnNums5.setOnClickListener(new ClickListener());
		btnNums6.setOnClickListener(new ClickListener());
		btnNums7.setOnClickListener(new ClickListener());
		btnNums8.setOnClickListener(new ClickListener());
		btnNums9.setOnClickListener(new ClickListener());
		btnNums0.setOnClickListener(new ClickListener());
		
		btnDel.setOnClickListener(new ClickListener());
		btnCheck.setOnClickListener(new ClickListener());
		
		btnSetPassCode.setOnClickListener(new ClickListener());
		
		runCodeSetup();
		
		
	}
	
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getSupportMenuInflater().inflate(R.menu.pc_menu, menu);
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch(item.getItemId()) {
		case R.id.pc_menu_settings:
			setPCSettings();
		}
		
		return true;
	}





	private void setPCSettings() {
		
		//Intent passCode = new Intent(PGen.this, Passcode.class);
		//startActivity(passCode);
		
		Intent passSettings = new Intent(Passcode.this, PCSettings.class);
		startActivity(passSettings);
		
	}

	private void runCodeSetup() {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		
		if(!prefs.getBoolean("setup", false)) {
			//code here
			
		//	View btnSetPassCode = findViewById(R.id.btnSetPassCode);
		//	btnSetPassCode.setVisibility(View.VISIBLE);
		//	View tvSetPass = findViewById(R.id.tvLRPassCode);
		//	tvSetPass.setVisibility(View.VISIBLE);
			
		}
		
	}
	
	private void savePrefs() {
		
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		SharedPreferences.Editor editor = prefs.edit();
		
		editor.putString("passcode", code);
		editor.putBoolean("setup", true);
		editor.commit();

	}
	
	private void loadPrefs() {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		checkThis = prefs.getString("passcode", "code");	
	}
	
	private void checkCode() {
		
		if (code.equals(checkThis)) {
			tvEnterCode.setText("GRANTED");
			tvEnterCode.setTextColor(getResources().getColor(R.color.green));
			
			Intent viewSaved = new Intent(Passcode.this, SavedPass.class);
			startActivity(viewSaved);
			
					} else {
			tvEnterCode.setText("DENIED");
			code = "";
			tvSetPass.setText(code);
		}
	}
	
	class ClickListener implements View.OnClickListener {

		
		public void onClick(View v) {
			switch(v.getId()) {
			case R.id.btnNums1:
				code += "1";	
				addStar();
				break;
			case R.id.btnNums2:
				code += "2";
				addStar();
				break;
			case R.id.btnNums3:
				code += "3";
				addStar();
				break;
			case R.id.btnNums4:
				code += "4";
				addStar();
				break;
			case R.id.btnNums5:
				code += "5";
				addStar();
				break;
			case R.id.btnNums6:
				code += "6";
				addStar();
				break;
			case R.id.btnNums7:
				code += "7";
				addStar();
				break;
			case R.id.btnNums8:
				code += "8";
				addStar();
				break;
			case R.id.btnNums9:
				code += "9";
				addStar();
				break;
			case R.id.btnNums0:
				code += "0";
				addStar();
				break;
			case R.id.btnDel:
				code = "";
				tvEnterCode.setText(code);
				break;
			case R.id.btnCheck:
				loadPrefs();
				checkCode();
				break;
			case R.id.btnSetPassCode:
				savePrefs();
				break;
			
			}
			
			tvSetPass.setText(code);	
			
		}

		private void addStar() {
			
			String currentText = tvEnterCode.getText().toString();
						
			if (currentText.contains("*")) {
				currentText += "*";
				tvEnterCode.setText(currentText);
			} else 
			tvEnterCode.setText("*");
			
		}

		
		
	}
	
}
