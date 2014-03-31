package com.nathaniel.amendroid7z;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;

public class Settings extends Activity{
	private boolean mSizeChanged = false;
	private boolean mSearchChanged = false;
	private boolean mHiddenChanged = false;
	private boolean mSortChanged = false;
	private boolean mAscendChanged = false;
	
	private int size_state;
	private boolean search_state;
	private boolean hidden_state;
	private int sort_state;
	private boolean ascend_state;
	private Intent is = new Intent();
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);
		
		Intent i = getIntent();
		size_state = i.getExtras().getInt("SIZE");
		search_state = i.getExtras().getBoolean("SEARCH");
		hidden_state = i.getExtras().getBoolean("HIDDEN");
		sort_state = i.getExtras().getInt("SORT");
		ascend_state = i.getExtras().getBoolean("ASCEND");
		final CheckBox search_bx = (CheckBox)findViewById(R.id.setting_search_box);
		final CheckBox hidden_bx = (CheckBox)findViewById(R.id.setting_hidden);
		final CheckBox ascend_bx = (CheckBox)findViewById(R.id.setting_ascend_box);
		final ImageButton size_bt = (ImageButton)findViewById(R.id.setting_text_color_button);
		final ImageButton sort_bt = (ImageButton)findViewById(R.id.settings_sort_button);
		
		search_bx.setChecked(search_state);
		hidden_bx.setChecked(hidden_state);
		ascend_bx.setChecked(ascend_state);
		
		size_bt.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				AlertDialog.Builder builder = new AlertDialog.Builder(Settings.this);
				CharSequence[] options = {"小", "中", "大"};
				
				builder.setTitle("改变文本大小");
				builder.setIcon(R.drawable.size);
				builder.setSingleChoiceItems(options, size_state, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int index) {
						switch(index) {
							case 0:
								size_state = 0;
								is.putExtra("SIZE", size_state);
								mSizeChanged = true;
								
								break;
							case 1:
								size_state = 1;
								is.putExtra("SIZE", size_state);
								mSizeChanged = true;
								
								break;
							case 2:
								size_state = 2;
								is.putExtra("SIZE", size_state);
								mSizeChanged = true;
								
								break;
								}
					}
				});
				
				builder.create().show();
			}
		});
		
		search_bx.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				search_state = isChecked;
				
				is.putExtra("SEARCH", search_state);
				mSearchChanged = true;
			}
		});
		
		hidden_bx.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				hidden_state = isChecked;
				
				is.putExtra("HIDDEN", hidden_state);
				mHiddenChanged = true;
			}
		});
		
		
		sort_bt.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				AlertDialog.Builder builder = new AlertDialog.Builder(Settings.this);
    			CharSequence[] options = {"字母", "类型","大小", "日期"};
    			
    			builder.setTitle("Sort by...");
    			builder.setIcon(R.drawable.sort);
    			builder.setSingleChoiceItems(options, sort_state, new DialogInterface.OnClickListener() {					
					@Override
					public void onClick(DialogInterface dialog, int index) {
						switch(index) {
						case 0:
							sort_state = 0;
							mSortChanged = true;
							is.putExtra("SORT", sort_state);
							break;
							
						case 1:
							sort_state = 1;
							mSortChanged = true;
							is.putExtra("SORT", sort_state);
							break;
							
						case 2:
							sort_state = 2;
							mSortChanged = true;
							is.putExtra("SORT", sort_state);
							break;
						case 3:
							sort_state = 3;
							mSortChanged = true;
							is.putExtra("SORT", sort_state);
							break;
							
						}
					}
				});
    			
    			builder.create().show();
			}
		});
		
		ascend_bx.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				ascend_state = isChecked;
				
				is.putExtra("ASCEND", ascend_state);
				mAscendChanged = true;
			}
		});
		
		
		
	}
	
	
	@Override
	protected void onResume(){
		super.onResume();
		
		if(!mSizeChanged)
			is.putExtra("SIZE", size_state);
		
		if(!mSearchChanged)
			is.putExtra("HIDDEN", search_state);
		
		if(!mHiddenChanged)
			is.putExtra("HIDDEN", hidden_state);
		
		if(!mSortChanged)
			is.putExtra("SORT", sort_state);
		
		if(!mAscendChanged)
			is.putExtra("ASCEND", ascend_state);
		
		setResult(RESULT_CANCELED, is);
	}
	
}