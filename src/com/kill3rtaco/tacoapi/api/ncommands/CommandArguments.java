package com.kill3rtaco.tacoapi.api.ncommands;

public class CommandArguments {
	
	protected String[]	_args;
	
	public CommandArguments(String[] args) {
		_args = args;
	}
	
	public String getString(int index) {
		return _args[index];
	}
	
	public String getString(int index, String def) {
		return index < _args.length ? _args[index] : def;
	}
	
	public String getJoinedArgs() {
		return getJoinedArgs(0);
	}
	
	public String getJoinedArgs(int initialIndex) {
		return getJoinedArgs(initialIndex, " ");
	}
	
	public String getJoinedArgs(int initialIndex, String delimiter) {
		String result = "";
		for(int i = initialIndex; i < _args.length; i++) {
			result += _args[i] + delimiter;
		}
		return result.trim();
	}
	
	public int getInteger(int index) throws NumberFormatException {
		return Integer.parseInt(_args[index]);
	}
	
	public int getInteger(int index, int def) throws NumberFormatException {
		return index < _args.length ? Integer.parseInt(_args[index]) : def;
	}
	
	public double getDouble(int index) throws NumberFormatException {
		return Double.parseDouble(_args[index]);
	}
	
	public double getDouble(int index, double def) throws NumberFormatException {
		return index < _args.length ? Double.parseDouble(_args[index]) : def;
	}
	
	public int length() {
		return _args.length;
	}
	
}
