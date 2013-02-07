package taco.tacoapi;

import java.io.File;
import java.sql.SQLException;

import taco.tacoapi.api.TacoPlugin;
import taco.tacoapi.database.Database;
import taco.tacoapi.obj.ChatObject;
import taco.tacoapi.obj.PlayerObject;
import taco.tacoapi.util.ChatUtils;

public class TacoAPI extends TacoPlugin {
	
	public static TacoAPI plugin;
	public static TacoAPIConfig config;
	public static Database data;
	
	@Override
	public void onStart() {
		plugin = this;
		config = new TacoAPIConfig(new File(getDataFolder() + "/config.yml"));
		try {
			data = new Database();
		} catch (SQLException e) {
			getChatAPI().outSevere("Could not hook into MySQL. Did you update the config.yml?");
		}
	}

	@Override
	public void onStop() {
		
	}
	
	public static ChatUtils getChatUtils(){
		return new ChatUtils();
	}
	
	public static ChatObject getChatAPI(){
		return new ChatObject("TacoAPI");
	}
	
	public static PlayerObject getPlayerAPI(){
		return new PlayerObject();
	}
	
	public static Database getDB(){
		return data;
	}
	
}
