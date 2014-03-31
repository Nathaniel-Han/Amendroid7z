package com.nathaniel.amendroid7z;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.nathaniel.util.CheckMap;
import com.nathaniel.util.FuncUtility;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.text.InputType;
import android.text.TextPaint;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

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
					VariableHolder.dialog_factory.delete_dialog();
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
					VariableHolder.dialog_factory.rename_dialog();
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
                    VariableHolder.dialog_factory.zipDialog(filename);
				}
				if (item==1) {
					VariableHolder.src=VariableHolder.file_path.get(position2);
					VariableHolder.dialog_factory.delete_dialog();
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
					VariableHolder.dialog_factory.rename_dialog();
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
