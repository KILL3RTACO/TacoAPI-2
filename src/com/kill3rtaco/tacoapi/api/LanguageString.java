package com.kill3rtaco.tacoapi.api;

import java.util.HashMap;
import java.util.Map;

public class LanguageString {

	private String string;
	private HashMap<String, String> replacements;
	
	public LanguageString(String string) {
		this(string, new HashMap<String, String>());
	}
	
	public LanguageString(String string, HashMap<String, String> replacements){
		this.string = string;
		this.replacements = replacements;
	}
	
	public void addReplacement(String replace, String replacement){
		replacements.put(replace, replacement);
	}
	
	public void addAllReplacements(Map<String, String> replacements){
		this.replacements.putAll(replacements);
	}
	
	public String format(){
		String newString = string;
		for(String s : replacements.keySet()){
			newString = newString.replaceAll(s, replacements.get(s));
		}
		return newString;
	}

}
