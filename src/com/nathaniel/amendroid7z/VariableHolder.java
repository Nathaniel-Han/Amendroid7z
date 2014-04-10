package com.nathaniel.amendroid7z;

import java.util.List;
import java.util.Map;

import com.nathaniel.util.CheckMap;
import com.nathaniel.util.FileManager;
import com.nathaniel.util.Unzip_type;

import android.view.Menu;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public final class VariableHolder {
	public static String src = "";
	public static String now_path = "";
	public static boolean First = true;
	public static String operation;
	public static Map<String, CheckMap> check_state;
	
	public static List<String>file_path=null;
	public static List<String>file_name=null;
	public static TextView show_path=null;
	public static boolean check_box;
	public static String back_path;
	
	public static FileManager fileManager=new FileManager();
	public static Unzip_type unzip_type=new Unzip_type();
	
	
	
	public static String Unzip_file_path;
	public static String Unzip_file_name;
	
	//for Settings
	public static int sort;
	public static boolean ascend;
	public static boolean hidden;
	public static int size;
	public static boolean search;
	public static Map<String, Integer> position;
	public static final int SETTING_REQ = 0x0d;
	public static final String PREFS_NAME = "ManagerPrefsFile";
	public static final String PREFS_SIZE = "size";
	public static final String PREFS_SEARCH = "search";
	public static final String PREFS_HIDDEN = "hidden";
	public static final String PREFS_SORT = "sort";
	public static final String PREFS_ASCEND = "ascend";
	
	public static String _line = "";
	public static String line = "joke";
	
	public static ImageButton compression=null;
	public static ImageButton create=null;
	public static ImageButton cancle=null;
	public static EditText editText=null;//for lots of dialogs
	
	//Constants 
	//for OnCreateDialog
	public static final int COM_LIST = 0x00;
	public static final int COM_SEARCH = 0x01;
	public static final int COM_SEARCH_INPUT = 0x02;
	public static final int COM_RETURN = 0x03;
	
	//for BackgroundWork
	public static final int SEARCH_TYPE = 0x10;
	public static final int ZIP_PASSWORD_TYPE = 0x11;
	public static final int UNZIP_TYPE = 0x12;
	public static final int LIST_TYPE = 0x13;
	public static final int COPY_TYPE = 0x14;
	public static final int MOVE_TYPE = 0x15;
	public static final int DELETE_TYPE = 0x16;
	//for Menu
	public static final int ITEM0=Menu.FIRST;//系统值  
	public static final int ITEM1=Menu.FIRST+1;  
	public static final int ITEM2=Menu.FIRST+2;  
	public static final int ITEM3=Menu.FIRST+3;
	public static final int ITEM4=Menu.FIRST+4;	
	
}


