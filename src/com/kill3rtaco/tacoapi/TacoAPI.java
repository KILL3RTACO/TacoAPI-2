package com.kill3rtaco.tacoapi;

import java.io.File;
import java.sql.SQLException;

import com.kill3rtaco.tacoapi.api.TacoPlugin;
import com.kill3rtaco.tacoapi.database.Database;
import com.kill3rtaco.tacoapi.listener.PlayerListener;
import com.kill3rtaco.tacoapi.obj.ChatObject;
import com.kill3rtaco.tacoapi.obj.EconObject;
import com.kill3rtaco.tacoapi.obj.EffectObject;
import com.kill3rtaco.tacoapi.obj.PlayerObject;
import com.kill3rtaco.tacoapi.obj.ServerObject;
import com.kill3rtaco.tacoapi.obj.WorldEditObject;
import com.kill3rtaco.tacoapi.util.ChatUtils;
import com.kill3rtaco.tacoapi.util.ItemUtils;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;

public class TacoAPI extends TacoPlugin {
	
	public static TacoAPI plugin;
	public static TacoAPIConfig config;
	public static File playerData;
	private static Database data;
	private static WorldEditObject we;
	private static EconObject econ;
	
	@Override
	public void onStart() {
		plugin = this;
		config = new TacoAPIConfig(new File(getDataFolder() + "/config.yml"));
		playerData = new File(getDataFolder() + "/playerData");
		if(!playerData.exists()){
			playerData.mkdir();
		}
		try {
			data = new Database();
			chat.out("[MySQL] API online");
		} catch (SQLException e) {
			data = null;
			getChatAPI().outWarn("[MySQL] Could not hook into MySQL. Did you update the config.yml?");
		}
		try {
			WorldEditPlugin wePlugin = (WorldEditPlugin) plugin.getServer().getPluginManager().getPlugin("WorldEdit");
			if(wePlugin == null){
				chat.outWarn("[WorldEdit] API not loaded");
			}else{
				we = new WorldEditObject(wePlugin);
				chat.out("[WorldEdit] API online");
			}
		} catch (NoClassDefFoundError e) {
			chat.outWarn("[WorlEdit] API not loaded");
		}
		try{
			econ = new EconObject();
			chat.out("[Economy] API online. Using " + econ.getEconomyName() + " for EconAPI");
		}catch(NoClassDefFoundError e){
			econ = null;
			chat.outWarn("[Economy] API not loaded");
		}
		getServer().getPluginManager().registerEvents(new PlayerListener(), this);
	}

	@Override
	public void onStop() {
		chat.out("Disabled");
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
	
	public static EconObject getEconAPI(){
		return econ;
	}
	
	public static EffectObject getEffectAPI(){
		return new EffectObject();
	}
	
	public static ItemUtils getItemUtils(){
		return new ItemUtils();
	}
	
	public static ServerObject getServerAPI(){
		return new ServerObject();
	}
	
	public static PlayerObject getPlayerAPI(){
		return new PlayerObject();
	}
	
	public static WorldEditObject getWorldEditAPI(){
		return we;
	}
	
	public static boolean isItemMailInstalled(){
		return plugin.getServer().getPluginManager().getPlugin("ItemMail") != null;
	}
	
	public static boolean isEconAPIOnline(){
		return econ != null;
	}
	
	public static boolean isWorldEditAPIOnline(){
		return we != null;
	}
	
}
