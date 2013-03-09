package taco.tacoapi;

import java.io.File;
import java.sql.SQLException;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;

import taco.tacoapi.api.TacoPlugin;
import taco.tacoapi.database.Database;
import taco.tacoapi.obj.ChatObject;
import taco.tacoapi.obj.PlayerObject;
import taco.tacoapi.obj.WorldEditObject;
import taco.tacoapi.util.ChatUtils;
import taco.tacoapi.util.ItemUtils;

public class TacoAPI extends TacoPlugin {
	
	public static TacoAPI plugin;
	public static TacoAPIConfig config;
	private static Database data;
	private static WorldEditObject we;
	
	@Override
	public void onStart() {
		plugin = this;
		config = new TacoAPIConfig(new File(getDataFolder() + "/config.yml"));
		try {
			data = new Database();
			chat.out("Database online");
		} catch (SQLException e) {
			getChatAPI().outSevere("Could not hook into MySQL. Did you update the config.yml?");
		}
		try {
			WorldEditPlugin wePlugin = (WorldEditPlugin) plugin.getServer().getPluginManager().getPlugin("WorldEdit");
			if(wePlugin == null){
				chat.outWarn("WorldEdit not found");
			}else{
				we = new WorldEditObject(wePlugin);
				chat.out("WorldEdit API online");
			}
		} catch (NoClassDefFoundError e) {
			chat.outWarn("WorlEditAPI not loaded");
		}
	}

	@Override
	public void onStop() {
		
	}
	
	public static ChatObject getChatAPI(){
		return new ChatObject("TacoAPI");
	}
	
	public static ChatUtils getChatUtils(){
		return new ChatUtils();
	}
	
	public static Database getDB(){
		return data;
	}
	
	public static ItemUtils getItemUtils(){
		return new ItemUtils();
	}
	
	public static PlayerObject getPlayerAPI(){
		return new PlayerObject();
	}
	
	public static WorldEditObject getWorldEditAPI(){
		return we;
	}
	
}
