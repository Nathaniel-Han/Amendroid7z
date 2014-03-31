package com.nathaniel.amendroid7z;


import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.nathaniel.util.CheckMap;
import com.nathaniel.util.FuncUtility;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.InputType;
import android.text.TextPaint;
import android.text.method.ScrollingMovementMethod;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

public class Main extends ListActivity implements OnClickListener,OnItemLongClickListener{
    /** Called when the activity is first created. */
	private String root="/sdcard/";
	
	private ImageButton back=null;
	private ImageButton home=null;
		
	
    //for Settings
	private SharedPreferences mSettings;
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        mSettings = getSharedPreferences(VariableHolder.PREFS_NAME, 0);
        VariableHolder.size = mSettings.getInt(VariableHolder.PREFS_SIZE, 1);
        VariableHolder.search = mSettings.getBoolean(VariableHolder.PREFS_SEARCH, false);
        VariableHolder.hidden = mSettings.getBoolean(VariableHolder.PREFS_HIDDEN, false);
        VariableHolder.sort = mSettings.getInt(VariableHolder.PREFS_SORT, 0);
        VariableHolder.show_path=(TextView)findViewById(R.id.show_path);

        back=(ImageButton)findViewById(R.id.back_button);
        back.setOnClickListener(this);
        home=(ImageButton)findViewById(R.id.home_button);
        home.setOnClickListener(this);
        VariableHolder.compression=(ImageButton)findViewById(R.id.compression_button);
        VariableHolder.compression.setOnClickListener(this);
        VariableHolder.create=(ImageButton)findViewById(R.id.create_button);
        VariableHolder.create.setOnClickListener(this);
        VariableHolder.cancle=(ImageButton)findViewById(R.id.cancle_button);
        VariableHolder.cancle.setOnClickListener(this);
        
        getListView().setOnItemLongClickListener(this);
        VariableHolder.check_state=new HashMap<String, CheckMap>();
        VariableHolder.position=new HashMap<String, Integer>();
        
        VariableHolder.dialog_factory = new DialogFactory(Main.this);
        
  
     //record position
        getListView().setOnScrollListener(new OnScrollListener() {
         	 
         	@Override
         	public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {
         		VariableHolder.position.put(VariableHolder.back_path, Integer.valueOf(getListView().getFirstVisiblePosition()));
         	}
          
         	@Override
         	public void onScrollStateChanged(AbsListView view, int scrollState) {
         		if(scrollState==OnScrollListener.SCROLL_STATE_IDLE){
                  	}
         	}
         });
      
        String status=Environment.getExternalStorageState();
        if(status.equals(Environment.MEDIA_MOUNTED))
        {
        FuncUtility.GetFile(Main.this, root, false); 
        }
        else{
        	VariableHolder.dialog_factory.InitialHandleDialog();
        }

    }
       
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case VariableHolder.COM_LIST:
			return VariableHolder.dialog_factory.listDialog(VariableHolder._line);
		case VariableHolder.COM_SEARCH:
			try {
				return VariableHolder.dialog_factory.searchDialog(FuncUtility.searchResult);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		case VariableHolder.COM_SEARCH_INPUT:
			return VariableHolder.dialog_factory.SearchInputDialog();
		case VariableHolder.COM_RETURN:
			return VariableHolder.dialog_factory.returnDialog();
		}
		return null;
	}

    
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	if(keyCode == KeyEvent.KEYCODE_SEARCH) {
    		if(VariableHolder.search)
    		     showDialog(VariableHolder.COM_SEARCH_INPUT);
    		return false;
    	}	
    	else  
    		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) { 
            showDialog(VariableHolder.COM_RETURN);
            return false; 
        } 
        return false; 
    }
  
   

  //点击文件或文件夹引发的事件
    protected void onListItemClick(ListView l,View v,int position,long id)
    {
       	File file=new File(VariableHolder.file_path.get(position));
    	if(file.canRead())
    	{
    		if (file.isDirectory())
    		{
    			FuncUtility.GetFile(Main.this,VariableHolder.file_path.get(position),VariableHolder.check_box);
    		}
    		else
    		{
    		    String fName=file.getName();
    		      String end=fName.substring(fName.lastIndexOf(".")+1,
    		              fName.length());
 
    		     
    		      if(VariableHolder.unzip_type.can_unzip(end))
    		      {
    		    	String command = "7za l -slt " + VariableHolder.now_path  + "/" +fName; 
    		    	new BackgroundWork(VariableHolder.LIST_TYPE, Main.this).execute(command);
    		       }
    		      else
    		    	  openFile(file);
    		}
    	}
    	else
    	{
    		VariableHolder.dialog_factory.SimpleDialog("Message", "没有权限!");
    	}
    }
  //=============================点击文件或文件夹引发的事件

	@Override
	public void onClick(View id) {
		// TODO Auto-generated method stub
		switch (id.getId()) {
		case R.id.back_button:

			if (!VariableHolder.now_path.equalsIgnoreCase("/")) {
				FuncUtility.GetFile(Main.this,VariableHolder.back_path,VariableHolder.check_box);
			}
			else FuncUtility.GetFile(Main.this,VariableHolder.now_path, VariableHolder.check_box);			
			break;
		case R.id.home_button:
			FuncUtility.GetFile(Main.this,root,VariableHolder.check_box);
			break;
		case R.id.compression_button:				
			FuncUtility.show_tools();
			FuncUtility.GetFile(Main.this,VariableHolder.now_path,true);
			break;
		case R.id.create_button:
			if (VariableHolder.check_box) {
				VariableHolder.dialog_factory.zipDialog(null);
			}
			else {
				if (VariableHolder.operation.equalsIgnoreCase("unzip")) {
					VariableHolder.dialog_factory.input_password_unzipDialog(VariableHolder.Unzip_file_path, VariableHolder.now_path);
					FuncUtility.GetFile(Main.this,VariableHolder.now_path, false);
				    FuncUtility.hid_tools();
				}
				if (VariableHolder.operation.equalsIgnoreCase("copy")) {
					new BackgroundWork(VariableHolder.COPY_TYPE, Main.this).execute("");
				}
				if (VariableHolder.operation.equalsIgnoreCase("move")) {
					new BackgroundWork(VariableHolder.MOVE_TYPE, Main.this).execute("");
				}
			}
		
			break;
		case R.id.cancle_button:
			FuncUtility.Clear_map();
			FuncUtility.hid_tools();
			FuncUtility.GetFile(Main.this,VariableHolder.now_path,false);
		}
		
	}

	@Override
	public boolean onItemLongClick(AdapterView parent, View view,int position,
			long id) {
		final int position2=position;
		String name=VariableHolder.file_name.get(position);
		File file=new File(VariableHolder.file_path.get(position));
		if (file.canRead()) {
			if (file.isDirectory()) {
				// TODO Auto-generated method stub
				VariableHolder.dialog_factory.FolderHandleDialog(position2);
			}
			else{
				String type=name.substring(name.lastIndexOf(".")+1,name.length());
				// TODO Auto-generated method stub
				// handle single zipped file
				if(VariableHolder.unzip_type.can_unzip(type))
				{
					VariableHolder.dialog_factory.ZipFileHandleDialog(position2);
				}
				// handle single non-zipped file
				else 
				{
					VariableHolder.dialog_factory.NonZipFileDialog(position2);
				}
			}}
			else {
				VariableHolder.dialog_factory.SimpleDialog("Message", "没有权限!");
		}
		return true;
	}	
	 
	 @Override
	 protected void onActivityResult(int requestCode, int resultCode, Intent data){
		 super.onActivityResult(requestCode, resultCode, data);
		 
		 SharedPreferences.Editor editor = mSettings.edit();
		 if(requestCode == VariableHolder.SETTING_REQ && resultCode == RESULT_CANCELED){
			 VariableHolder.size = data.getIntExtra("SIZE", 1);
			 VariableHolder.search = data.getBooleanExtra("SEARCH", false);
			 VariableHolder.hidden = data.getBooleanExtra("HIDDEN", false);
			 VariableHolder.sort = data.getIntExtra("SORT", 0);
			 VariableHolder.ascend = data.getBooleanExtra("ASCEND", false);
			 
			 editor.putInt(VariableHolder.PREFS_SIZE, VariableHolder.size);
			 editor.putBoolean(VariableHolder.PREFS_SEARCH, VariableHolder.search);
			 editor.putBoolean(VariableHolder.PREFS_HIDDEN, VariableHolder.hidden);
			 editor.putInt(VariableHolder.PREFS_SORT, VariableHolder.sort);
			 editor.putBoolean(VariableHolder.PREFS_ASCEND, VariableHolder.ascend);
			 editor.commit();
			 
			 FuncUtility.GetFile(Main.this,VariableHolder.now_path, VariableHolder.check_box);
		 }
		 
	 }
	 
	 public boolean onCreateOptionsMenu(Menu menu)
	 {
		 menu.add(0,VariableHolder.ITEM0,0,"压缩").setIcon(R.drawable.add7z);
		 menu.add(0,VariableHolder.ITEM1,0,"搜索").setIcon(R.drawable.search);
		 menu.add(0,VariableHolder.ITEM2,0,"新建").setIcon(R.drawable.newfolder);
		 menu.add(0,VariableHolder.ITEM3,0,"设置").setIcon(R.drawable.setting);
		 menu.add(0,VariableHolder.ITEM4,0,"退出").setIcon(R.drawable.logout);
		 return true;
	 }
	 
	 @Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		 switch (item.getItemId()) {
			case VariableHolder.ITEM0:
				MenuItem0();
				break;
			case VariableHolder.ITEM1:
				MenuItem1();
				break;
			case VariableHolder.ITEM2:
				MenuItem2();
				break;
			case VariableHolder.ITEM3:
				MenuItem3();
				break;
			case VariableHolder.ITEM4:
				MenuItem4();
				break;
			}
		return super.onOptionsItemSelected(item);
	}
	 public void MenuItem0(){  
		 FuncUtility.show_tools();
		 FuncUtility.GetFile(Main.this,VariableHolder.now_path, true);
	}  
	 public void MenuItem1(){
		 if(VariableHolder.search)
		    showDialog(VariableHolder.COM_SEARCH_INPUT);
		 else {
			 Toast.makeText(Main.this, "还未开启搜索功能" , Toast.LENGTH_LONG).show();
		}
	}
	 public void MenuItem2(){  
		 VariableHolder.dialog_factory.newfile_dialog();
	}
	 public void MenuItem3(){
		 Intent settings_int = new Intent(this, Settings.class);
		 settings_int.putExtra("SIZE", mSettings.getInt(VariableHolder.PREFS_SIZE, 1));
		 settings_int.putExtra("SEARCH", mSettings.getBoolean(VariableHolder.PREFS_SEARCH, false));
		 settings_int.putExtra("HIDDEN", mSettings.getBoolean(VariableHolder.PREFS_HIDDEN, false));
		 settings_int.putExtra("SORT", mSettings.getInt(VariableHolder.PREFS_SORT, 0));
		 settings_int.putExtra("ASCEND", mSettings.getBoolean(VariableHolder.PREFS_ASCEND, false));
		 
		 startActivityForResult(settings_int, VariableHolder.SETTING_REQ);
	 }
	 public void MenuItem4(){
		 Main.this.finish();
		 int nPid=android.os.Process.myPid();
		 android.os.Process.killProcess(nPid);
	}	 
	 
	// 打开文件或服务
	private void openFile(File f) {
		String type = FuncUtility.GetType(f);
		{
			Intent intent = new Intent();
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.setAction(android.content.Intent.ACTION_VIEW);

			intent.setDataAndType(Uri.fromFile(f), type);
			
			try {
				startActivity(intent);
			} catch(ActivityNotFoundException e) {
				intent.setType("*/*");
				startActivity(intent);
			}
			
		}
	}
	//==============打开文件或服务

	// Our NDK Interface ===========================================
	public native static int doeverything(String command);

	static {
		System.loadLibrary("my7zip");
	}
	// ===============================================================

}