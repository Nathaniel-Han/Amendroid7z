package com.nathaniel.amendroid7z;

import com.nathaniel.util.FuncUtility;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;


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
