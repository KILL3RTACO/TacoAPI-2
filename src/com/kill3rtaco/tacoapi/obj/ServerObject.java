package com.kill3rtaco.tacoapi.obj;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.kill3rtaco.tacoapi.TacoAPI;

public class ServerObject {

	public Plugin getPluginFromCommand(String name){
		String alias = name.toLowerCase();
		PluginCommand cmd = TacoAPI.plugin.getServer().getPluginCommand(alias);
		if(cmd == null) return null;
		return cmd.getPlugin();
	}
	
	public String getShortestAlias(JavaPlugin plugin, String name){
		PluginCommand cmd = plugin.getCommand(name);
		if(cmd == null) return null;
		List<String> aliases = cmd.getAliases();
		if(aliases == null || aliases.size() == 0) return name;
		aliases.add(name);
		Collections.sort(aliases, new Comparator<String>(){

			@Override
			public int compare(String s1, String s2) {
				Integer s1l = s1.length();
				Integer s2l = s2.length();
				return s1l.compareTo(s2l);
			}
			
		});
		return aliases.get(0);
	}

}
