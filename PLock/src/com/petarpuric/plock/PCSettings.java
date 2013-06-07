package com.petarpuric.plock;




import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import com.actionbarsherlock.app.SherlockPreferenceActivity;
import org.holoeverywhere.preference.Preference;


public class PCSettings extends SherlockPreferenceActivity{

	
	
	@SuppressWarnings("deprecation")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.pc_settings);
		

	}
	
	
	
	
}
