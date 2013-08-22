package com.kill3rtaco.tacoapi.obj;

import org.bukkit.entity.Player;

public class PermObject {

	public boolean hasPermission(Player player, String perm){
		if(perm == null || perm.isEmpty() || player.hasPermission(perm)) return true;
		if(perm.contains(".")){
			return hasOverridePerm(player, perm);
		}
		return false;
	}
	
	private String getOverridePerm(String perm){
		if(perm.matches(".*\\.\\*")){ //Plugin.perm.*
			perm = perm.substring(0, perm.lastIndexOf('.')); //Plugin.perm -> Plugin.*
		}
		if(!perm.contains(".")) return perm;
		return perm.substring(0, perm.lastIndexOf('.')) + ".*"; //Mineopoly.general.anfirs
	}
	
	/*
	 * Recursively checks parent permissions
	 * 
	 * i.e. when testing the permission 'ExamplePlugin.example.parent.child', the method
	 * checks to see if the player has any of the following permissions:
	 * 
	 * ExamplePlugin.example.parent.child
	 * ExamplePlugin.example.parent.*
	 * ExamplePlugin.example.*
	 * ExamplePlugin.*
	 * ExamplePlugin
	 * 
	 * If the player has any of these permissions, the method returns true
	 */
	private boolean hasOverridePerm(Player player, String perm){
		String overridePerm = getOverridePerm(perm);
		if(player.hasPermission(overridePerm)) return true;
		if(overridePerm.contains(".")) return hasOverridePerm(player, overridePerm);
		return false;
	}

}
