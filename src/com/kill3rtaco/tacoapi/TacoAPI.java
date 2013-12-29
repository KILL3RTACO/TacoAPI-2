package com.kill3rtaco.tacoapi;

import java.io.File;
import java.sql.SQLException;

import com.kill3rtaco.tacoapi.api.serialization.SerializationConfig;
import com.kill3rtaco.tacoapi.api.TacoPlugin;
import com.kill3rtaco.tacoapi.database.Database;
import com.kill3rtaco.tacoapi.listener.PlayerListener;
import com.kill3rtaco.tacoapi.obj.ChatObject;
import com.kill3rtaco.tacoapi.obj.EconObject;
import com.kill3rtaco.tacoapi.obj.EffectObject;
import com.kill3rtaco.tacoapi.obj.PermObject;
import com.kill3rtaco.tacoapi.obj.PlayerObject;
import com.kill3rtaco.tacoapi.obj.ServerObject;
import com.kill3rtaco.tacoapi.obj.WorldEditObject;
import com.kill3rtaco.tacoapi.util.ChatUtils;
import com.kill3rtaco.tacoapi.util.ItemUtils;
import com.kill3rtaco.tacoapi.util.TimeUtils;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;

public class TacoAPI extends TacoPlugin {
	
	public static TacoAPI plugin;
	public static TacoAPIConfig config;
	public static File playerData;
	private static ChatObject chatAPI;
	private static ChatUtils chatUtils;
	private static EconObject econAPI;
	private static EffectObject effectAPI;
	private static ItemUtils itemUtils;
	private static Database mysqlAPI;
	private static PermObject permAPI;
	private static PlayerObject playerAPI;
	private static ServerObject serverAPI;
	private static TimeUtils timeUtils;
	private static WorldEditObject weAPI;
	
	@Override
	public void onStart() {
		plugin = this;
		config = new TacoAPIConfig(new File(getDataFolder() + "/config.yml"));
		playerData = new File(getDataFolder() + "/playerData");
		if(!playerData.exists()){
			playerData.mkdir();
		}
		
		//Utils/Chat
		chatUtils = new ChatUtils();
		chat.out("[Utils] [Chat] Helper online");
		
		//Utils/Item
		itemUtils = new ItemUtils();
		chat.out("[Utils] [Item] Helper online");
		
		//Utils/Time
		timeUtils = new TimeUtils();
		chat.out("[Utils] [Time] Helper online");
		
		//ChatAPI
		chatAPI = chat;
		chat.out("[Chat] API online");
		
		//EconomyAPI
		try{
			econAPI = new EconObject();
			chat.out("[Economy] API online. Using " + econAPI.getEconomyName() + " for EconAPI");
		}catch(NoClassDefFoundError e){
			econAPI = null;
			chat.outWarn("[Economy] API not loaded");
		}
		
		//EffectAPI
		effectAPI = new EffectObject();
		chat.out("[Effect] API Online");
		
		//MySQLAPI
		try {
			mysqlAPI = new Database();
			chat.out("[MySQL] API online");
		} catch (SQLException e) {
			mysqlAPI = null;
			getChatAPI().outWarn("[MySQL] Could not hook into MySQL. Did you update the config.yml?");
		}
		
		//PermAPI
		permAPI = new PermObject();
		chat.out("[Permissions] API Online");
		
		//PlayerAPI
		playerAPI = new PlayerObject();
		chat.out("[Player] API Online");
		
		//ServerAPI
		serverAPI = new ServerObject();
		chat.out("[Server] API Online");
		
		//WorldEditAPI
		try {
			WorldEditPlugin wePlugin = (WorldEditPlugin) plugin.getServer().getPluginManager().getPlugin("WorldEdit");
			if(wePlugin == null){
				chat.outWarn("[WorldEdit] API not loaded");
			}else{
				weAPI = new WorldEditObject(wePlugin);
				chat.out("[WorldEdit] API online");
			}
		} catch (NoClassDefFoundError e) {
			chat.outWarn("[WorlEdit] API not loaded");
		}
		
		//SerializationConfig
		SerializationConfig.reload();
		
		getServer().getPluginManager().registerEvents(new PlayerListener(), this);
		startMetrics();
	}

	@Override
	public void onStop() {
		chat.out("Disabled");
	}
	
	public static ChatObject getChatAPI(){
		return chatAPI;
	}
	
	public static ChatUtils getChatUtils(){
		return chatUtils;
	}
	
	public static Database getDB(){
		return mysqlAPI;
	}
	
	public static EconObject getEconAPI(){
		return econAPI;
	}
	
	public static EffectObject getEffectAPI(){
		return effectAPI;
	}
	
	public static ItemUtils getItemUtils(){
		return itemUtils;
	}
	
	public static PermObject getPermAPI(){
		return permAPI;
	}
	
	public static PlayerObject getPlayerAPI(){
		return playerAPI;
	}
	
	public static TimeUtils getTimeUtils(){
		return timeUtils;
	}
	
	public static ServerObject getServerAPI(){
		return serverAPI;
	}
	
	public static WorldEditObject getWorldEditAPI(){
		return weAPI;
	}
	
	public static boolean isItemMailInstalled(){
		return plugin.getServer().getPluginManager().getPlugin("ItemMail") != null;
	}
	
	public static boolean isEconAPIOnline(){
		return econAPI != null;
	}
	
	public static boolean isWorldEditAPIOnline(){
		return weAPI != null;
	}
	
}
