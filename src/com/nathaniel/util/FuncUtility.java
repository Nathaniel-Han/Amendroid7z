package com.nathaniel.util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.nathaniel.amendroid7z.PrAdapter;
import com.nathaniel.amendroid7z.R;
import com.nathaniel.amendroid7z.VariableHolder;
import com.nathaniel.amendroid7z.R.layout;


import android.app.ListActivity;
import android.util.Log;
import android.view.View;

public final class FuncUtility {
	public static String searchResult = "";

	private static DataInputStream Terminal(String command) throws Exception {
		Process process;
		process = Runtime.getRuntime().exec("su");

		OutputStream outstream = process.getOutputStream();
		DataOutputStream DOPS = new DataOutputStream(outstream);
		InputStream instream = process.getInputStream();
		DataInputStream DIPS = new DataInputStream(instream);
		String temp = command + "\n";

		DOPS.writeBytes(temp);
		DOPS.flush();
		DOPS.writeBytes("exit\n");
		DOPS.flush();
		process.waitFor();
		return DIPS;
	}

	public static void findTerminal(String search) throws Exception {
		String tmp;
		searchResult = "";
		String searchCommand;

		searchCommand = "find " + VariableHolder.now_path + " -name " + search;
		DataInputStream stream = Terminal(searchCommand);
		while ((tmp = stream.readLine()) != null) {
			searchResult += tmp;
			searchResult += "\n";
		}
	}

	public static void Clear_map() {
		VariableHolder.check_state.clear();
		Set entries = VariableHolder.check_state.entrySet();
		if (entries != null) {
			Iterator iterator = entries.iterator();
			while (iterator.hasNext()) {
				Map.Entry<String, CheckMap> entry = (Map.Entry<String, CheckMap>) iterator
						.next();
				String key = entry.getKey();
				CheckMap value = entry.getValue();
			}
		}
	}

	public static void show_tools() {
		VariableHolder.create.setVisibility(View.VISIBLE);
		VariableHolder.cancle.setVisibility(View.VISIBLE);
		VariableHolder.compression.setVisibility(View.INVISIBLE);
	}

	public static void hid_tools() {
		VariableHolder.create.setVisibility(View.INVISIBLE);
		VariableHolder.cancle.setVisibility(View.INVISIBLE);
		VariableHolder.compression.setVisibility(View.VISIBLE);
	}

	// 获取文件信息
	public static void GetFile(ListActivity activity, String home_root,
			boolean show_checkbox) {
		VariableHolder.file_path = new ArrayList<String>();
		VariableHolder.file_name = new ArrayList<String>();
		File file = new File(home_root);
		// VariableHolder.show_path=(TextView)findViewById(R.id.show_path);
		VariableHolder.show_path.setText(home_root);
		VariableHolder.check_box = show_checkbox;

		VariableHolder.back_path = file.getParent();
		VariableHolder.now_path = home_root;

		File[] files = file.listFiles();
		Arrays.sort(files, new Comparator<File>() {
			public int compare(File f1, File f2) {
				int result;

				if (VariableHolder.sort == 1) {
					if (f1.isDirectory() && f2.isDirectory())
						result = f1.getName().compareToIgnoreCase(f2.getName());
					else if (f1.isFile() && f2.isFile()) {
						String fName1 = f1.getName();
						String end1 = fName1.substring(
								fName1.lastIndexOf(".") + 1, fName1.length());
						String fName2 = f2.getName();
						String end2 = fName2.substring(
								fName2.lastIndexOf(".") + 1, fName2.length());
						result = end1.compareToIgnoreCase(end2);
					} else if (f1.isDirectory() && f2.isFile())
						result = -1;
					else
						result = 1;

				} else if (VariableHolder.sort == 2) {
					if (f1.isDirectory() && f2.isDirectory())
						result = f1.getName().compareToIgnoreCase(f2.getName());
					else if (f1.isFile() && f2.isFile()) {
						if (f1.length() - f2.length() > 0)
							result = 1;
						else if (f1.length() - f2.length() == 0)
							result = 0;
						else
							result = -1;
					} else if (f1.isDirectory() && f2.isFile())
						result = -1;
					else
						result = 1;

				} else if (VariableHolder.sort == 3) {
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
					result = format.format(new Date(f1.lastModified()))
							.compareToIgnoreCase(
									format.format(new Date(f2.lastModified())));
				} else
					result = (f1.getName()).compareToIgnoreCase(f2.getName());

				if (VariableHolder.ascend)
					result = -result;

				return result;
			}
		});

		for (int i = 0; i < files.length; i++) {
			if (!VariableHolder.hidden) {
				String fName = files[i].getName();
				String true_name = fName.substring(fName.lastIndexOf("/") + 1,
						fName.length());
				if (true_name.charAt(0) != '.') {
					VariableHolder.file_name.add(files[i].getName());
					VariableHolder.file_path.add(files[i].getPath());
				}
			} else {
				VariableHolder.file_name.add(files[i].getName());
				VariableHolder.file_path.add(files[i].getPath());
			}
		}

		if (VariableHolder.size == 0)
			activity.setListAdapter(new PrAdapter(activity,
					VariableHolder.file_name, VariableHolder.file_path,
					show_checkbox, R.layout.rowmin));
		else if (VariableHolder.size == 1)
			activity.setListAdapter(new PrAdapter(activity,
					VariableHolder.file_name, VariableHolder.file_path,
					show_checkbox, R.layout.row));
		else
			activity.setListAdapter(new PrAdapter(activity,
					VariableHolder.file_name, VariableHolder.file_path,
					show_checkbox, R.layout.rowlar));

		if (VariableHolder.position.containsKey(VariableHolder.back_path)) {
			activity.setSelection(VariableHolder.position
					.get(VariableHolder.back_path));
		}
	}

	// =====================获取文件信息

	public static void listFunc() {
		VariableHolder._line = "";

		try {
			RandomAccessFile in = new RandomAccessFile(
					"/data/data/com.nathaniel.amendroid7z/LOG", "rw");
			Log.i("here", "line");
			Log.i("size", String.valueOf(in.length()));
			while ((VariableHolder.line = in.readLine()) != null) {
				Log.i("line", VariableHolder.line);
				VariableHolder.line += "\n";
				VariableHolder._line += VariableHolder.line;
			}
			
			in.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 获取文件类型
	public static String GetType(File f) {
		String type = "";
		String fName = f.getName();

		String end = fName
				.substring(fName.lastIndexOf(".") + 1, fName.length());

		if (end.equals("m4a") || end.equals("mp3") || end.equals("mid")
				|| end.equals("xmf") || end.equals("ogg") || end.equals("wav")) {
			type = "audio/*";
		} else if (end.equals("3gp") || end.equals("mp4")) {
			type = "video/*";
		} else if (end.equals("jpg") || end.equals("gif") || end.equals("png")
				|| end.equals("jpeg") || end.equals("bmp")) {
			type = "image/*";
		} else if (end.equals("pdf")){
			type = "application/pdf";
		}else if (end.equals("txt")){
			type = "text/plain";
		}else if (end.equals("doc") || end.equals("docx")){
			type = "application/msword";
		}else if(end.equals("ppt") || end.equals("pptx")){
			type = "application/vnd.ms-powerpoint";
		}else if (end.equals("xls") || end.equals("xlsx")){
			type = "application/vnd.ms-excel";
		}
		else if (end.equals("apk")) {
			type = "application/vnd.android.package-archive";
		} else {
			type = "*/*";
		}
		return type;
	}

	// =========================================获取文件类型

	public static String zipCommand(String name, String password, String type) {
		String command = "7z a " + VariableHolder.now_path + "/" + name + "."
				+ type;
		if (!password.equalsIgnoreCase("")) {
			command = "7z a -p{" + password + "} " + VariableHolder.now_path
					+ "/" + name + "." + type;
		}
		Set entries = VariableHolder.check_state.entrySet();
		if (entries != null) {
			Iterator iterator = entries.iterator();
			while (iterator.hasNext()) {
				Map.Entry<String, CheckMap> entry = (Map.Entry<String, CheckMap>) iterator
						.next();
				String key = entry.getKey();
				CheckMap value = entry.getValue();
				if (value.check_state) {
					command = command + " " + value.file_path;
				}
			}
		}
		return command;
	}

	public static String unzipCommand(String src, String des, String password) {
		String command;
		File file = new File(src);
		String name = file.getName();
		String type = name.substring(name.lastIndexOf(".") + 1, name.length());
		if (!password.equalsIgnoreCase("")) {
			command = "7za x -p{" + password + "} " + src + " -o" + des;
		} else {
			command = "7za x " + src + " -o" + des;
		}
		return command;
	}
}
