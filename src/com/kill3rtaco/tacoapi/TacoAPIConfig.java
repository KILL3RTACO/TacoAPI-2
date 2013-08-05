package com.kill3rtaco.tacoapi;

import java.io.File;

import com.kill3rtaco.tacoapi.api.TacoConfig;


public class TacoAPIConfig extends TacoConfig {

	public TacoAPIConfig(File file) {
		super(file);
	}

	@Override
	protected void setDefaults() {
		addDefaultValue("mysql.server.address", "localhost");
		addDefaultValue("mysql.server.port", 3306);
		addDefaultValue("mysql.database.name", "minecraft");
		addDefaultValue("mysql.database.username", "root");
		addDefaultValue("mysql.database.password", "root");
	}
	
	public String getMySqlServerAddress(){
		return getString("mysql.server.address");
	}
	
	public int getMySqlServerPort(){
		return getInt("mysql.server.port");
	}
	
	public String getDatabaseName(){
		return getString("mysql.database.name");
	}
	
	public String getDatabaseUsername(){
		return getString("mysql.database.username");
	}
	
	public String getDatabasePassword(){
		return getString("mysql.database.password");
	}

}
