package com.nathaniel.amendroid7z;

import java.io.File;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nathaniel.amendroid7z.R;
import com.nathaniel.amendroid7z.R.drawable;
import com.nathaniel.amendroid7z.R.id;
import com.nathaniel.amendroid7z.R.layout;
import com.nathaniel.util.CheckMap;
import com.nathaniel.util.Unzip_type;

import android.R.integer;
import android.R.string;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

public class PrAdapter extends ArrayAdapter<Object>{
	private List<String>file_name=null;
	private List<String>file_path=null;
	private LayoutInflater mInflater;
	private boolean show_checkbox;
	private Bitmap mIcon1;
	private Bitmap mIcon2;
	private Bitmap mIcon3;
	private Bitmap ppt;
	private Bitmap movies;
	private Bitmap music;
	private Bitmap image;
	private Bitmap word;
	private Bitmap pdf;
	private Bitmap excel;
	private Bitmap apk;
	
	private int resourceID;
	//private static final int mResource = R.layout.row;
	
	public PrAdapter(Context context,List<String>item,List<String>path,boolean show_checkbox, int resourceID)
	{
		super(context, resourceID);
		file_name=item;
		file_path=path;
		mInflater = LayoutInflater.from(context);
	    mIcon1 = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.folder);
	    mIcon2 = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.doc);
	    mIcon3 = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.zip);
	    ppt = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.ppt);
	    word = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.word);
	    pdf = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.pdf);
	    music = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.music);
	    movies = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.movies);
	    image = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.image);
	    excel= BitmapFactory.decodeResource(context.getResources(),
                R.drawable.excel);
	    apk = BitmapFactory.decodeResource(context.getResources(),
	    		R.drawable.appicon);
	    
	    this.show_checkbox=show_checkbox;
	    
	    this.resourceID = resourceID;
	    
	    for (int i = 0; i < file_name.size(); i++) {
	    	if (!VariableHolder.check_state.containsKey(file_path.get(i))) {
	    		VariableHolder.check_state.put(file_path.get(i),new CheckMap(false,file_path.get(i)));
			}
		}
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return file_name.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return file_name.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position,View convertView,ViewGroup parent) {
		// TODO Auto-generated method stub
	    Target target;
	    
	    if(convertView == null)
	    {
	      convertView = mInflater.inflate(resourceID, null);
         
	      target= new Target();
	      target.textView= (TextView) convertView.findViewById(R.id.text);
	      target.imageView= (ImageView) convertView.findViewById(R.id.icon);
	      target.checkBox=(CheckBox)convertView.findViewById(R.id.checkbox);
	      target.doc_info=(TextView)convertView.findViewById(R.id.doc_info);
	      convertView.setTag(target);
	    }
	    else
	    {
	      target = (Target) convertView.getTag();
	    }

	    File f=new File(file_path.get(position).toString());
	    target.textView.setText(f.getName());
	    
	    String info="";
	    double size=f.length()/1024.0;
	    SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
	    String last_date=format.format(new Date(f.lastModified()));
	    
	    if (f.canRead()) {
	    	if (f.canWrite()) {
				info="-rw";
				}
	    	else{
			    info="-r";}
	    	}
	    else {info="-w";}
	    
if(!f.isDirectory()){
	 if(size<=1024)
	 {
	    info=String.format("%.2f",size)+"Kb|"+info+"\n"+last_date;
	 }
	 else if(size/1024<=1024){
	    info=String.format("%.2f",size/1024.0)+"Mb|"+info+"\n"+last_date;
	 }
	 else {
	    info=String.format("%.2f",size/(1024.0*1024.0))+"Gb|"+info+"\n"+last_date;
    }
	 }else{
		 String files[]=f.list();
		 info="items:"+tree(f)+"|"+info+"\n"+last_date;
}
	    target.doc_info.setText(info);
	    if(show_checkbox)target.checkBox.setVisibility(View.VISIBLE);
	      else target.checkBox.setVisibility(View.INVISIBLE);
	    
        target.checkBox.setTag(file_path.get(position)); 
        target.checkBox.setChecked(VariableHolder.check_state.get(file_path.get(position)).check_state);
        
        target.checkBox.setOnClickListener(new CheckBox.OnClickListener() { 
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					String list=(String)arg0.getTag();	
					VariableHolder.check_state.put(list,new CheckMap(!VariableHolder.check_state.get(list).check_state,VariableHolder.check_state.get(list).file_path));
				} 
            }); 
	     
	    if(f.isDirectory())
	      {
	        target.imageView.setImageBitmap(mIcon1);
	      }
	      else
	      {
	    	  String type=f.getName().substring(f.getName().lastIndexOf(".")+1,f.getName().length()).toUpperCase();
	    	  Unzip_type unzip_type=new Unzip_type();
	    	  if (unzip_type.can_unzip(type)) {
	    		  target.imageView.setImageBitmap(mIcon3);
			}
	    	  else if (type.equalsIgnoreCase("MP3")||type.equalsIgnoreCase("WAV")) {
	    		  target.imageView.setImageBitmap(music);
			}
	    	  else if(type.equalsIgnoreCase("PDF"))
	    		  target.imageView.setImageBitmap(pdf);
	    	  else if(type.equalsIgnoreCase("MP4")||type.equalsIgnoreCase("WMV")||type.equalsIgnoreCase("MPEG")||type.equalsIgnoreCase("RMVB"))
	    		  target.imageView.setImageBitmap(movies);
	    	  else if (type.equalsIgnoreCase("PPT")||type.equalsIgnoreCase("PPTX"))
	    		  target.imageView.setImageBitmap(ppt);
	    	  else if(type.equalsIgnoreCase("DOC")||type.equalsIgnoreCase("DOCX"))
	    		  target.imageView.setImageBitmap(word);
	    	  else if(type.equalsIgnoreCase("XLS")||type.equalsIgnoreCase("XLSX"))
	    		  target.imageView.setImageBitmap(excel);
	    	  else if(type.equalsIgnoreCase("JPG")||type.equalsIgnoreCase("PNG")||type.equalsIgnoreCase("GIF"))
	    		  target.imageView.setImageBitmap(image);
	    	  else if(type.equalsIgnoreCase("APK"))
	    		  target.imageView.setImageBitmap(apk);
	    	  else 
	   	        target.imageView.setImageBitmap(mIcon2);
	      }
	    return convertView;
	}
	private class Target
	{
		TextView textView;
		TextView doc_info;
		ImageView imageView;
	    CheckBox checkBox;
	}
	private int tree(File f)
	{
		int counter=0;
		File[] fa=f.listFiles();
		if(fa!=null)
			counter=fa.length;
/*		if(fa!=null)
		{
		for (int i = 0; i < fa.length; i++) {
			counter++;
			if(fa[i].isDirectory())
				counter+=tree(fa[i]);
			}
		}*/
		return counter;
	}
}
