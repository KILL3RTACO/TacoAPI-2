package com.kill3rtaco.tacoapi.obj;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

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
		ArrayList<String> aliases = new ArrayList<String>();
		if(pluginOwnsCommand(plugin, name)){
			aliases.add(name);
		}
		//test if the aliases, when used, belong to the given JavaPlugin
		for(String s : plugin.getCommand(name).getAliases()){
			if(pluginOwnsCommand(plugin, s)){
				aliases.add(s);
			}
		}
		if(aliases.size() == 0){
			return null;
		}else if(aliases.size() > 1){
			Collections.sort(aliases, new Comparator<String>(){

				@Override
				public int compare(String s1, String s2) {
					Integer l1 = s1.length();
					Integer l2 = s2.length();
					return l1.compareTo(l2);
				}
				
			});
		}
		return aliases.get(0);
	}
	
	public boolean pluginOwnsCommand(Plugin plugin, String alias){
		return getPluginFromCommand(alias).getName().equalsIgnoreCase(plugin.getName());
	}

}
