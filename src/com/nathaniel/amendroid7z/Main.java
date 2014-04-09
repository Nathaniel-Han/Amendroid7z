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
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
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
	public DialogFactory dialog_factory = new DialogFactory(Main.this);
	
	
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
        
        //VariableHolder.dialog_factory = new DialogFactory(Main.this);
        
  
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
        	dialog_factory.InitialHandleDialog();
        }

    }
       
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case VariableHolder.COM_LIST:
			return dialog_factory.listDialog(VariableHolder._line);
		case VariableHolder.COM_SEARCH:
			try {
				return dialog_factory.searchDialog(FuncUtility.searchResult);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		case VariableHolder.COM_SEARCH_INPUT:
			return dialog_factory.SearchInputDialog();
		case VariableHolder.COM_RETURN:
			return dialog_factory.returnDialog();
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
    		dialog_factory.SimpleDialog("Message", "没有权限!");
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
				dialog_factory.zipDialog(null);
			}
			else {
				if (VariableHolder.operation.equalsIgnoreCase("unzip")) {
					dialog_factory.input_password_unzipDialog(VariableHolder.Unzip_file_path, VariableHolder.now_path);
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
				dialog_factory.FolderHandleDialog(position2);
			}
			else{
				String type=name.substring(name.lastIndexOf(".")+1,name.length());
				// TODO Auto-generated method stub
				// handle single zipped file
				if(VariableHolder.unzip_type.can_unzip(type))
				{
					dialog_factory.ZipFileHandleDialog(position2);
				}
				// handle single non-zipped file
				else 
				{
					dialog_factory.NonZipFileDialog(position2);
				}
			}}
			else {
				dialog_factory.SimpleDialog("Message", "没有权限!");
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
		 dialog_factory.newfile_dialog();
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
	
	public class DialogFactory {
		private ListActivity activity;

		public DialogFactory(ListActivity activity) {
			this.activity = activity;
		}

		public void SimpleDialog(String title,String message) {
			AlertDialog.Builder builder = new AlertDialog.Builder(activity);

			builder.setMessage(message);
			builder.setTitle(title);
			builder.setPositiveButton("OK",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub

						}
					});
			builder.create().show();
		}
		
		// For Debug
		public void SimpleDialog(String title,Exception exception) {
			Throwable ex = exception;
			if(exception.getCause() != null){
				ex = exception.getCause();
			}
			SimpleDialog(title,ex.getMessage());
		}
		
		public void InitialHandleDialog() {
			AlertDialog.Builder builder = new AlertDialog.Builder(activity);
			builder.setTitle("未发现SD卡！")
					.setPositiveButton("OK", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							FuncUtility.GetFile(activity, "/", false);
						}
					}).show();
		}
		
		public void FolderHandleDialog(final int position2){
			AlertDialog.Builder builder=new AlertDialog.Builder(activity);
			builder.setTitle("请选择操作");
			builder.setItems(VariableHolder.operte2,new DialogInterface.OnClickListener(){
				public void onClick(DialogInterface dialog,int item){
					if (item==0) {
						String filename = new String(VariableHolder.file_name.get(position2));
						VariableHolder.check_state.put(VariableHolder.file_path.get(position2),new CheckMap(!VariableHolder.check_state.get(VariableHolder.file_path.get(position2)).check_state,VariableHolder.check_state.get((VariableHolder.file_path.get(position2))).file_path));
						zipDialog(filename);
						}
					if (item==1) {
						VariableHolder.src=VariableHolder.file_path.get(position2);
						delete_dialog();							
					}
					if (item==2) {
						VariableHolder.operation="move";
						VariableHolder.src=VariableHolder.file_path.get(position2);
						FuncUtility.show_tools();
						
					}
					if (item==3) {
						VariableHolder.operation="copy";
						VariableHolder.src=VariableHolder.file_path.get(position2);
						FuncUtility.show_tools();
					}
					if (item==4) {
						VariableHolder.src=VariableHolder.file_path.get(position2);
						rename_dialog();
						}
					}
			});
			AlertDialog alert=builder.create();
			alert.show();
		}
		
		public void ZipFileHandleDialog(final int position2){
			AlertDialog.Builder builder=new AlertDialog.Builder(activity);
			builder.setTitle("请选择操作");
			builder.setItems(VariableHolder.operte,new DialogInterface.OnClickListener(){
				public void onClick(DialogInterface dialog,int item){
					if (item==0) {
						VariableHolder.operation="unzip";
						VariableHolder.Unzip_file_path=VariableHolder.file_path.get(position2);
						String fname = VariableHolder.file_name.get(position2);
						VariableHolder.Unzip_file_name=fname.substring(0,fname.lastIndexOf("."));
						FuncUtility.show_tools();
					}
					if (item==1) {
						VariableHolder.src=VariableHolder.file_path.get(position2);
						dialog_factory.delete_dialog();
						}
					if (item==2) {
						VariableHolder.operation="move";
						VariableHolder.src=VariableHolder.file_path.get(position2);
						FuncUtility.show_tools();
						}
					if (item==3) {
						VariableHolder.operation="copy";
						VariableHolder.src=VariableHolder.file_path.get(position2);
						FuncUtility.show_tools();
					}
					if (item==4) {
						VariableHolder.src=VariableHolder.file_path.get(position2);
						dialog_factory.rename_dialog();
							}
					}
			});
			AlertDialog alert=builder.create();
			alert.show();
		}
		
		public void NonZipFileDialog(final int position2){
			AlertDialog.Builder builder=new AlertDialog.Builder(activity);
			builder.setTitle("请选择操作");
			builder.setItems(VariableHolder.operte3,new DialogInterface.OnClickListener(){
				public void onClick(DialogInterface dialog,int item){
					if(item==0)  {
						String fullfilename = new String(VariableHolder.file_name.get(position2));
	                    String filename = fullfilename.substring(0,fullfilename.lastIndexOf("."));
	                    VariableHolder.check_state.put(VariableHolder.file_path.get(position2),new CheckMap(!VariableHolder.check_state.get(VariableHolder.file_path.get(position2)).check_state,VariableHolder.check_state.get((VariableHolder.file_path.get(position2))).file_path));
	                    dialog_factory.zipDialog(filename);
					}
					if (item==1) {
						VariableHolder.src=VariableHolder.file_path.get(position2);
						dialog_factory.delete_dialog();
						}
					if (item==2) {
						VariableHolder.operation="move";
						VariableHolder.src=VariableHolder.file_path.get(position2);
						FuncUtility.show_tools();
					}
					if(item==3){
					VariableHolder.operation="copy";
					VariableHolder.src=VariableHolder.file_path.get(position2);
					FuncUtility.show_tools();
						}
					if (item==4) {
						VariableHolder.src=VariableHolder.file_path.get(position2);
						dialog_factory.rename_dialog();
						}
					}
			});
			AlertDialog alert=builder.create();
			alert.show();
		}

		public Dialog listDialog(String str) {
			LayoutInflater inflater = LayoutInflater.from(activity);
			View textEntryView = inflater.inflate(R.layout.dialog, null);

			// regex
			Pattern pattern = Pattern
					.compile("(Solid = ((\\+)|(-))\n)|(Block(s)? = \\d+\n)|(CRC = [\\dA-Z]+\n)|"
							+ "(Attributes = .R..A\n)|((Physical |Headers |Packed )?Size = (\\d+)?\n)");
			Matcher matcher = pattern.matcher(str);

			TextView tv;
			// **
			tv = (TextView) textEntryView.findViewById(R.id.dialog_tv);
			// **
			TextPaint tp = tv.getPaint();
			tp.setFakeBoldText(true);
			tv.setText(matcher.replaceAll(""));
			tv.setMovementMethod(ScrollingMovementMethod.getInstance());

			AlertDialog.Builder builder = new AlertDialog.Builder(activity);
			builder.setTitle("压缩信息");
			DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					activity.removeDialog(VariableHolder.COM_LIST);
				}
			};
			builder.setPositiveButton("返回", listener).setView(textEntryView)
					.setCancelable(false);

			return builder.create();
		}

		public Dialog searchDialog(String str) {
			LayoutInflater inflater = LayoutInflater.from(activity);
			View textEntryView = inflater.inflate(R.layout.dialog, null);

			TextView tv;
			tv = (TextView) textEntryView.findViewById(R.id.dialog_tv);
			tv.setText(str);
			tv.setMovementMethod(ScrollingMovementMethod.getInstance());

			AlertDialog.Builder builder = new AlertDialog.Builder(activity);
			builder.setTitle("Result");

			DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					activity.removeDialog(VariableHolder.COM_SEARCH);
				}
			};
			builder.setPositiveButton("返回", listener).setView(textEntryView)
					.setCancelable(false);

			return builder.create();
		}

		public Dialog SearchInputDialog() {
			LayoutInflater inflater = LayoutInflater.from(activity);
			View textEntryView = inflater.inflate(R.layout.input_layout, null);

			ImageView searchIcon = (ImageView) textEntryView
					.findViewById(R.id.input_icon);
			searchIcon.setImageResource(R.drawable.search);
			TextView search_label = (TextView) textEntryView
					.findViewById(R.id.input_label);
			search_label.setText("查找文件(支持部分通配符)");
			final EditText search_input = (EditText) textEntryView
					.findViewById(R.id.input_inputText);

			AlertDialog.Builder builder = new AlertDialog.Builder(activity);
			builder.setTitle("搜索");

			DialogInterface.OnClickListener listener1 = new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					String temp = search_input.getText().toString();
					new BackgroundWork(VariableHolder.SEARCH_TYPE, activity)
							.execute(temp);
				}
			};
			DialogInterface.OnClickListener listener2 = new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					activity.removeDialog(VariableHolder.COM_SEARCH_INPUT);
				}
			};
			builder.setPositiveButton("search", listener1)
					.setNegativeButton("cancel", listener2).setView(textEntryView)
					.setCancelable(false);

			return builder.create();

		}

		// 处理键盘返回按钮事件
		public Dialog returnDialog() {
			AlertDialog.Builder builder = new AlertDialog.Builder(activity);

			builder.setTitle("确认退出吗？")
					.setPositiveButton("OK", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							activity.finish();
							int nPid = android.os.Process.myPid();
							android.os.Process.killProcess(nPid);
						}
					})
					.setNegativeButton("Cancel",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface arg0, int arg1) {
									activity.removeDialog(VariableHolder.COM_RETURN);
								}
							}).setCancelable(false);
			return builder.create();
		}

		// =========================处理键盘返回按钮事件
		
		public void newfile_dialog(){
		    LayoutInflater factory = LayoutInflater.from(activity);
	    	View textEntryView = factory.inflate(R.layout.rename, null);
	    	VariableHolder.editText=(EditText)textEntryView.findViewById(R.id.edittext);
	    	VariableHolder.editText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
	    	VariableHolder.editText.setText(null);
			AlertDialog.Builder builder=new AlertDialog.Builder(activity);
			builder.setTitle("请输入文件夹名称").setView(textEntryView)
			.setPositiveButton("OK",new DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface arg0, int arg1) {		
				 VariableHolder.fileManager.createDir(VariableHolder.now_path,VariableHolder.editText.getText().toString());
				 FuncUtility.GetFile(activity, VariableHolder.now_path, false);
			}})
			.setNegativeButton("cancel",new DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
				}
			}).show();
		}
		
		public void delete_dialog() {
			AlertDialog.Builder builder = new AlertDialog.Builder(activity);
			builder.setTitle("确认删除吗？")
					.setPositiveButton("OK", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							new BackgroundWork(VariableHolder.DELETE_TYPE, activity)
									.execute("");
						}
					})
					.setNegativeButton("Cancel",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface arg0, int arg1) {
								}
							}).show();
		}

		public void rename_dialog() {
			LayoutInflater factory = LayoutInflater.from(activity);
			View textEntryView = factory.inflate(R.layout.rename, null);
			VariableHolder.editText = (EditText) textEntryView
					.findViewById(R.id.edittext);
			VariableHolder.editText
					.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
			VariableHolder.editText.setText(null);
			AlertDialog.Builder builder = new AlertDialog.Builder(activity);
			builder.setTitle("请输入新文件名")
					.setView(textEntryView)
					.setPositiveButton("OK", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							VariableHolder.fileManager.renameTarget(
									VariableHolder.src, VariableHolder.editText
											.getText().toString());
							FuncUtility.GetFile(activity, VariableHolder.now_path,
									VariableHolder.check_box);
						}
					})
					.setNegativeButton("cancel",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface arg0, int arg1) {
								}
							}).show();
		}

		public void zipDialog(String filename) {
			AlertDialog.Builder builder = new AlertDialog.Builder(activity);
			builder.setTitle("压缩参数设置");
			LayoutInflater inflater = activity.getLayoutInflater();
			View view = inflater.inflate(R.layout.create7z, null);

			ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity,
					android.R.layout.simple_spinner_item);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

			adapter.add("7z");
			adapter.add("ZIP");
			adapter.add("TAR");
			adapter.add("WIM");
			adapter.add("RAR");
			final Spinner spinner = (Spinner) view.findViewById(R.id.select_type);

			spinner.setAdapter(adapter);

			spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
				public void onItemSelected(AdapterView<?> parent, View view,
						int position, long id) {
					Spinner spinner = (Spinner) parent;
				}

				public void onNothingSelected(AdapterView<?> parent) {
				}

			});

			final EditText doc_name = (EditText) view.findViewById(R.id.doc_name);
			if (filename == null)
				doc_name.setHint("请输入文件名");
			else
				doc_name.setText(filename);

			CheckBox checkBox = (CheckBox) view.findViewById(R.id.input_password);
			final EditText doc_password = (EditText) view
					.findViewById(R.id.doc_password);
			doc_password.setVisibility(View.INVISIBLE);
			doc_password.setHint("请输入密码");

			doc_password.setText(null);

			checkBox.setOnClickListener(new CheckBox.OnClickListener() {
				boolean check = false;

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					check = !check;
					if (check == false) {
						doc_password.setVisibility(View.INVISIBLE);
					} else {
						doc_password.setVisibility(View.VISIBLE);
					}
					doc_password.setText(null);
				}
			});

			builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					// TODO Auto-generated method stub
					String type = null;
					switch (spinner.getSelectedItemPosition()) {
					case 0:
						type = "7z";
						break;
					case 1:
						type = "zip";
						break;
					case 2:
						type = "tar";
						break;
					case 3:
						type = "wim";
						break;
					case 4:
						type = "rar";
						break;
					}

					// 处理重名问题
					String fname = doc_name.getText() + "." + type;
					File file = new File(VariableHolder.now_path);
					File[] files = file.listFiles();
					for (File f : files) {
						if (f.getName().equals(fname)) {
							Toast.makeText(activity, "无法创建文件", Toast.LENGTH_LONG)
									.show();
							FuncUtility.Clear_map();
							FuncUtility.hid_tools();
							FuncUtility.GetFile(activity, VariableHolder.now_path,
									false);
							return;
						}
					}

					final String command = FuncUtility.zipCommand(doc_name
							.getText().toString(), doc_password.getText()
							.toString(), type);
					new BackgroundWork(VariableHolder.ZIP_PASSWORD_TYPE, activity)
							.execute(command);

				}
			}).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					// TODO Auto-generated method stub
					FuncUtility.Clear_map();
					FuncUtility.GetFile(activity, VariableHolder.now_path, false);
					FuncUtility.hid_tools();
				}
			}).setCancelable(false);

			builder.setView(view);

			AlertDialog alert = builder.create();
			alert.show();
		}

		public void input_password_unzipDialog(final String src, final String des) {
			LayoutInflater factory = LayoutInflater.from(activity);

			View textEntryView = factory.inflate(R.layout.password, null);
			VariableHolder.editText = (EditText) textEntryView
					.findViewById(R.id.edittext);
			VariableHolder.editText
					.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
			VariableHolder.editText.setText(null);
			CheckBox checkBox = (CheckBox) textEntryView
					.findViewById(R.id.input_password2);
			VariableHolder.editText.setVisibility(View.INVISIBLE);
			checkBox.setOnClickListener(new CheckBox.OnClickListener() {
				boolean check = false;

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					check = !check;
					if (check == false) {
						VariableHolder.editText.setVisibility(View.INVISIBLE);
					} else {
						VariableHolder.editText.setVisibility(View.VISIBLE);
					}
					VariableHolder.editText.setText(null);
				}
			});
			AlertDialog.Builder builder = new AlertDialog.Builder(activity);
			builder.setTitle("解压").setView(textEntryView)
					.setPositiveButton("OK", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							// TODO Auto-generated method stub

							File file = new File(VariableHolder.now_path);
							File[] files = file.listFiles();
							for (File f : files) {
								if (f.getName().equals(
										VariableHolder.Unzip_file_name)) {
									Toast.makeText(activity, "无法创建文件",
											Toast.LENGTH_LONG).show();
									return;
								}

							}
							VariableHolder.fileManager.createDir(
									VariableHolder.now_path,
									VariableHolder.Unzip_file_name);

							final String command = FuncUtility.unzipCommand(src,
									des + "/" + VariableHolder.Unzip_file_name,
									VariableHolder.editText.getText().toString());
							new BackgroundWork(VariableHolder.UNZIP_TYPE, activity)
									.execute(command);
						}
					});

			AlertDialog alert = builder.create();

			alert.show();
		}
	}

	
	

	// Our NDK Interface ===========================================
	public native static int doeverything(String command);

	static {
		System.loadLibrary("my7zip");
	}
	// ===============================================================
	
	// MutiThread Class
	public class BackgroundWork extends AsyncTask<String, Void, String> {
		private ProgressDialog pr_dialog;
		private ListActivity activity;
		private int type;

		public BackgroundWork(int type, ListActivity activity) {
			this.type = type;
			this.activity = activity;
		}

		private ProgressDialog MakePd(String mes) {
			return ProgressDialog.show(activity, "提示", mes + ",请稍后...", true, true);
		}

		@Override
		protected void onPreExecute() {
			switch (type) {
			case VariableHolder.SEARCH_TYPE:
				pr_dialog = MakePd("正在查找");
				break;
			case VariableHolder.ZIP_PASSWORD_TYPE:
				pr_dialog = MakePd("正在压缩");
				break;
			case VariableHolder.UNZIP_TYPE:
				pr_dialog = MakePd("正在解压");
				break;
			case VariableHolder.LIST_TYPE:
				pr_dialog = MakePd("");
				break;
			case VariableHolder.COPY_TYPE:
				pr_dialog = MakePd("正在复制");
				break;
			case VariableHolder.MOVE_TYPE:
				pr_dialog = MakePd("正在移动");
				break;
			case VariableHolder.DELETE_TYPE:
				pr_dialog = MakePd("正在删除");
				break;
			}
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			switch (type) {
			case VariableHolder.SEARCH_TYPE:
				try {
					FuncUtility.findTerminal(params[0]);
					return null;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			case VariableHolder.ZIP_PASSWORD_TYPE:
			case VariableHolder.UNZIP_TYPE:
				Main.doeverything(params[0]);
				return null;
			case VariableHolder.LIST_TYPE: {
				Main.doeverything(params[0]);
				// This is a trick*****************
				if (VariableHolder.First)
					Main.doeverything(params[0]);
				// ********************************
				return null;
			}
			case VariableHolder.COPY_TYPE: {
				VariableHolder.fileManager.copyToDirectory(VariableHolder.src, VariableHolder.now_path);
				return null;
			}
			case VariableHolder.MOVE_TYPE: {
				VariableHolder.fileManager.copyToDirectory(VariableHolder.src, VariableHolder.now_path);
				VariableHolder.fileManager.deleteTarget(VariableHolder.src);
				return null;
			}
			case VariableHolder.DELETE_TYPE: {
				VariableHolder.fileManager.deleteTarget(VariableHolder.src);
				return null;
			}

			}
			return null;
		}

		@Override
		protected void onPostExecute(String str) {
			switch (type) {
			case VariableHolder.SEARCH_TYPE:
				if (FuncUtility.searchResult.equals("")) {
					Toast.makeText(activity, "找不到文件", Toast.LENGTH_LONG).show();
					pr_dialog.dismiss();
				} else {
					activity.showDialog(VariableHolder.COM_SEARCH);
					pr_dialog.dismiss();
				}
				break;
			case VariableHolder.ZIP_PASSWORD_TYPE:
				FuncUtility.Clear_map();
				FuncUtility.hid_tools();
				FuncUtility.GetFile(activity, VariableHolder.now_path, false);
				pr_dialog.dismiss();
				break;
			case VariableHolder.UNZIP_TYPE:
				FuncUtility.GetFile(activity, VariableHolder.now_path, false);
				FuncUtility.hid_tools();
				pr_dialog.dismiss();
				break;
			case VariableHolder.LIST_TYPE:
				FuncUtility.listFunc();
				if (VariableHolder._line.equals("")) {
					Toast.makeText(activity, "请再试一次", Toast.LENGTH_SHORT).show();
					pr_dialog.dismiss();
				} else {
					activity.showDialog(VariableHolder.COM_LIST);
					pr_dialog.dismiss();
				}
				break;
			case VariableHolder.COPY_TYPE:
			case VariableHolder.MOVE_TYPE:
				FuncUtility.GetFile(activity, VariableHolder.now_path, VariableHolder.check_box);
				FuncUtility.hid_tools();
				pr_dialog.dismiss();
				break;
			case VariableHolder.DELETE_TYPE:
				FuncUtility.GetFile(activity, VariableHolder.now_path, VariableHolder.check_box);
				pr_dialog.dismiss();
				break;

			}
		}
	}
	

}