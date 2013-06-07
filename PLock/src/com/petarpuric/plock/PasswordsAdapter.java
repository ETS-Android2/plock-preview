package com.petarpuric.plock;

import java.util.ArrayList;

import org.holoeverywhere.widget.CheckBox;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class PasswordsAdapter extends ArrayAdapter<Passwords>{

	private ArrayList<Passwords> pw;
	private Context c;
	
	public PasswordsAdapter(Context c, int textViewResourceId, ArrayList<Passwords> pw) {
		super(c, textViewResourceId, pw);
		this.c = c;
		this.pw = pw;
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		final ViewHolder holder;
		if (view==null) {
			
			LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.listrow, null);
			
			holder = new ViewHolder();
			holder.checkBox = (CheckBox) view.findViewById(R.id.rowCheckBox);
			holder.textView = (TextView) view.findViewById(R.id.rowListView);
			
			view.setTag(holder);
		} else {
            holder = (ViewHolder) view.getTag();
		}
		
	
		 Passwords pass = pw.get(position);
		if (pass!=null) {
			
		holder.textView.setText(pass.getUse());
		}
		
		return view;
	}
	
	class ViewHolder {
		CheckBox checkBox;
		TextView textView;
	}
}
