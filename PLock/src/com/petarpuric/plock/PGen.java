package com.petarpuric.plock;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;



import android.os.Bundle;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import org.holoeverywhere.app.Activity;
import org.holoeverywhere.widget.Button;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;



public class PGen extends Activity {

Context mContext = this;
	
	//Button Declarations
	private Button btnGen, btnViewSaved, btnSave;
	private ImageButton btnLengthUp, btnLengthDown;
	public CheckBox chkNumbers, chkSpec, chkUpper, chkLower;
	private TextView tvChkError, tvLenOfP;
	private EditText edtPassword;
    
	
		
	//lower case array
	char[] lC = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i',
			'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
			'u', 'v', 'w', 'x', 'y', 'z'};
	//upper case array
	char[] uC = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I',
			'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T',
			'U', 'V', 'W', 'X', 'Y', 'Z'};
	
	//numbers array
	char[] numbers = {'1', '2', '3', '4', '5', '6', '7', '8', '9'};
	
	//symbols array
	char[] symbols = {'!', '@', '#', '$', '%', '^', '&', '*',
			'(', ')', '-', '=', '+', '?', '[', ']', '~', '<', '>'};
	
	//array of arrays to choose from when randomizing.
	char[][] passwordChars = {lC, uC, numbers, symbols};
	

	
	private int lenOfP = 6, randomArraySelect = 0, selectedArrayLength = 0;
	private char randomCharSelect;
	 String password = "";
	
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        
        
        

        edtPassword = (EditText) findViewById(R.id.edtPassword);
        
        chkNumbers = (CheckBox) findViewById(R.id.chkNumbers);
        chkSpec = (CheckBox) findViewById(R.id.chkSpecChar);
        chkUpper = (CheckBox) findViewById(R.id.chkUpperCase);
        chkLower = (CheckBox) findViewById(R.id.chkLowerCase);
        tvChkError = (TextView) findViewById(R.id.tvChkError);
        
        
        btnLengthUp = (ImageButton) findViewById(R.id.btnLengthUp);
        btnLengthDown = (ImageButton) findViewById(R.id.btnLengthDown);
        
        tvLenOfP = (TextView) findViewById(R.id.tvLenOfP);
        
        tvLenOfP.setText("" + lenOfP);
        
        btnGen = (Button) findViewById(R.id.btnGenerate);
        btnSave = (Button) findViewById(R.id.btnSave);
        btnViewSaved = (Button) findViewById(R.id.btnViewSaved); 
        
        
        btnGen.setOnClickListener(new ClickListener());
        btnLengthUp.setOnClickListener(new ClickListener());
        btnLengthDown.setOnClickListener(new ClickListener()); 
        
        
        btnViewSaved.setOnClickListener(new ClickListener());
        btnSave.setOnClickListener(new ClickListener());
        
        
       
        
        
                }
    
   
//Validation to check if any of the checkboxes are selected otherwise throws an exception.
	public void checkBoxes() throws GenException{
    	
    	if (chkNumbers.isChecked() == false && chkSpec.isChecked() == false
    			&& chkUpper.isChecked() == false && chkLower.isChecked() == false) {
    		
    		throw new GenException("");
    	}
    	else {
    		tvChkError.setText("");
    	}
    	
    }

    //Checks the length specified by the user. Minimum is 6 char, Maximum is 16.
    public void checkLength() {
    	
    	if (lenOfP <= 6) {
    		lenOfP = 6;
    		Toast.makeText(this, "6 characters is the minimum length", Toast.LENGTH_SHORT).show();
    		tvLenOfP.setText("" + lenOfP);
    	}
    	else if (lenOfP > 16) {
    		lenOfP = 16;
    		Toast.makeText(this, "16 characters is the maximum length", Toast.LENGTH_SHORT).show();
    		tvLenOfP.setText("" + lenOfP);
    	}
    	
    }
    
    
    //Clicklistener for all Buttons in the main activity.
    class ClickListener implements View.OnClickListener {
    	
    	public void onClick(View v) {
    		switch(v.getId()) {
    		case R.id.btnGenerate:
    			try {
					checkBoxes();
					genPass();
					//throw exception if options are not selected. 
				} catch (GenException e) {
					
					tvChkError.setText(e.badOPtionChoice());
				}
    			
    			break;
    			//increase length of password
    		case R.id.btnLengthUp:
    			lenOfP++;
    			checkLength();
    			tvLenOfP.setText("" + lenOfP);
    			break;
    			//decrease length of password
    		case R.id.btnLengthDown:
    			lenOfP--;
    			checkLength();
    			tvLenOfP.setText("" + lenOfP);
    			break;
    			//view saved passwords
    		case R.id.btnViewSaved:
    			viewSaved();
    			break;
    			//save password
    		case R.id.btnSave:
    			savePassword();
    			break;
    		}
    	}

	
    	//This is where the magic happens. The password generating.
		private void genPass() {
			
			//first options are checked to see what the user has selected. i.e Lower Case + Numbers etc
			checkOptions();
			
			
			
			Random randArray = new Random();
			Random randChar = new Random();
		
				
			for (int i = 0; i < lenOfP; i++) {
			
				//get size of list
				int x = checkOptions().size();
				//pick random n depending on size
				randomArraySelect = randArray.nextInt(x);
				//use randomArraySelect to choose array
				char[][] a = checkOptions().get(randomArraySelect);
				//assign length of array (**a[0] always needs to be 0**)
				selectedArrayLength = a[0].length;
				//chooses random char from array
				randomCharSelect = a[0][randChar.nextInt(selectedArrayLength)];
		
			//adds randomly selected character from array to the password. Process repeats until lenOfP is matched.
			password += randomCharSelect;
			}
			edtPassword.setText("" + password);
			
			//reset password string before next generation
			password = "";	
		}
		//this method checks for every possiblity of checkboxes selected by the user.
		private List<char[][]> checkOptions() {			
			
			List<char[][]> listTC = new ArrayList<char[][]>();

			
			if (chkNumbers.isChecked()) {
				char[][] numS = {numbers};
				listTC.add(numS);
			}
			
			if (chkSpec.isChecked()) {
				char[][] specS = {symbols};
				listTC.add(specS);
			}
			
			if (chkUpper.isChecked()) {
				char[][] upperS = {uC};
				listTC.add(upperS);
			}
			
			if (chkLower.isChecked()) {
				char[][] lowerS = {lC};
				listTC.add(lowerS);
			}
			
			return listTC;
			
		}

		//Starts a new activity to view saved passwords
		private void viewSaved() {

			Intent passCode = new Intent(PGen.this, Passcode.class);
			startActivity(passCode);
		}
		
		//starts up a dialog to save the password.
		private void savePassword() {
			
		SaveDialog sD = new SaveDialog();
		
		String password = edtPassword.getText().toString();
		
		sD.createDialog(mContext, password);
		
		
		}	
	
		
		
    }
    
    private void about() {
    	
    	
    	LayoutInflater inflater = getLayoutInflater();
    	View dialoglayout = inflater.inflate(R.layout.about, null);
    	
    	
    	AlertDialog.Builder aboutDialog = new AlertDialog.Builder(this);
    	aboutDialog.setTitle("About");
    	
    	aboutDialog.setView(dialoglayout);
    	
    	
    	aboutDialog.setNegativeButton("Close", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				dialog.dismiss();
				
			}
		});
    	
    	aboutDialog.show();
    	
    	
    	
    	
    }
    
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getSupportMenuInflater().inflate(R.menu.activity_pgen, menu);
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch(item.getItemId()) {
		case R.id.sub_upgrade:
			Toast.makeText(getApplicationContext(), "Upgrade!", Toast.LENGTH_SHORT).show();
			break;
		case R.id.sub_about:
			about();
			break;
		case R.id.sub_quit:
			PGen.super.onBackPressed();
			break;
		}
		
		return true;
	}

}
