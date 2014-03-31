package com.nathaniel.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileManager {
	public int copyToDirectory(String old,String newDir)
	{
		int BUFFER=2048;
		File old_file = new File(old);
		File temp_dir = new File(newDir);
		byte[] data = new byte[BUFFER];
		int read = 0;
		
		if(old_file.isFile() && temp_dir.isDirectory() && temp_dir.canWrite()){
			String file_name = old.substring(old.lastIndexOf("/"), old.length());
			File cp_file = new File(newDir + file_name);

			try {
				BufferedOutputStream o_stream = new BufferedOutputStream(
												new FileOutputStream(cp_file));
				BufferedInputStream i_stream = new BufferedInputStream(
											   new FileInputStream(old_file));
				
				while((read = i_stream.read(data, 0, BUFFER)) != -1)
					o_stream.write(data, 0, read);
				
				o_stream.flush();
				i_stream.close();
				o_stream.close();
				
			} catch (FileNotFoundException e) {
				return -1;
				
			} catch (IOException e) {
				return -1;
			}
			
		}else if(old_file.isDirectory() && temp_dir.isDirectory() && temp_dir.canWrite()) {
			String files[] = old_file.list();
			String dir = newDir + old.substring(old.lastIndexOf("/"), old.length());
			int len = files.length;
			
			if(!new File(dir).mkdir())
				return -1;
			
			for(int i = 0; i < len; i++)
				copyToDirectory(old + "/" + files[i], dir);
			
		} else if(!temp_dir.canWrite())
			return -1;
		
		return 0;
	}
	
	public int renameTarget(String filePath, String newName) {
		File src = new File(filePath);
		String ext = "";
		File dest;
		
		if(src.isFile())
			/*get file extension*/
			ext = filePath.substring(filePath.lastIndexOf("."), filePath.length());
		
		if(newName.length() < 1)
			return -1;
	
		String temp = filePath.substring(0, filePath.lastIndexOf("/"));
		
		dest = new File(temp + "/" + newName + ext);
		if(src.renameTo(dest))
			return 0;
		else
			return -1;
	}
	
	public int deleteTarget(String path) {
		File target = new File(path);
		
		if(target.exists() && target.isFile() && target.canWrite()) {
			target.delete();
			return 0;
		}
		
		else if(target.exists() && target.isDirectory() && target.canRead()) {
			String[] file_list = target.list();
			
			if(file_list != null && file_list.length == 0) {
				target.delete();
				return 0;
				
			} else if(file_list != null && file_list.length > 0) {
				
				for(int i = 0; i < file_list.length; i++) {
					File temp_f = new File(target.getAbsolutePath() + "/" + file_list[i]);

					if(temp_f.isDirectory())
						deleteTarget(temp_f.getAbsolutePath());
					else if(temp_f.isFile())
						temp_f.delete();
				}
			}
			if(target.exists())
				if(target.delete())
					return 0;
		}	
		return -1;
	}
	
	public int createDir(String path, String name) {
		int len = path.length();
		
		if(len < 1 || len < 1)
			return -1;
		
		if(path.charAt(len - 1) != '/')
			path += "/";
		
		if (new File(path+name).mkdir())
			return 0;
		
		return -1;
	}
}
