package com.kill3rtaco.tacoapi.database;

public class DatabaseException extends Exception {

	private static final long serialVersionUID = -4155286232672183170L;
	private String message;
	
	public DatabaseException(String message){
		this.message = message;
	}
	
	public String getMessage(){
		return message;
	}

}
