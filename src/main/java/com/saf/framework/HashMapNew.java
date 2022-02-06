package com.saf.framework;

import java.util.HashMap;

public class HashMapNew extends HashMap<String,String>{
	static final long serialVersionUID = 1L;

	@Override
	public String get(Object key){
		String value = super.get(key);
		if (value==null){return "";}
		return value;
	}
}

