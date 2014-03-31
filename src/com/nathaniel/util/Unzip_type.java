package com.nathaniel.util;

import java.util.ArrayList;
import java.util.List;

public class Unzip_type {
	private List<String> type=new ArrayList<String>();
	public Unzip_type()
	{
		type.add("7z");
		type.add("TAR");
		type.add("ZIP");
		type.add("RAR");
		type.add("WIM");
	}
	public boolean can_unzip(String name)
	{
		if (name.toLowerCase().equalsIgnoreCase("7z")||type.contains(name.toUpperCase())) {
			return true;
		}
		else return false;
	}
}
