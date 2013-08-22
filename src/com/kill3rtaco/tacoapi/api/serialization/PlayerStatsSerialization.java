package com.kill3rtaco.tacoapi.api.serialization;

import org.bukkit.entity.Player;

import com.kill3rtaco.tacoapi.json.JSONException;
import com.kill3rtaco.tacoapi.json.JSONObject;

/**
 * A class to help with the serialization of player stats, like exp level and health.
 * @author KILL3RTACO
 *
 */
public class PlayerStatsSerialization {

	protected PlayerStatsSerialization() {}
	
	public static JSONObject serializePlayerStats(Player player){
		try {
			JSONObject root = new JSONObject();
			root.put("health", player.getHealth());
			root.put("food", player.getFoodLevel());
			root.put("exhaustion", player.getExhaustion());
			root.put("saturation", player.getSaturation());
			root.put("level", player.getLevel());
			root.put("exp", player.getExp());
			return root;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static String serializePlayerStatsAsString(Player player){
		return serializePlayerStatsAsString(player, false);
	}
	
	public static String serializePlayerStatsAsString(Player player, boolean pretty){
		return serializePlayerStatsAsString(player, pretty, 5);
	}
	
	public static String serializePlayerStatsAsString(Player player, boolean pretty, int indentFactor){
		try{
			if(pretty){
				return serializePlayerStats(player).toString(indentFactor);
			}else{
				return serializePlayerStats(player).toString();
			}
		} catch(JSONException e){
			e.printStackTrace();
			return null;
		}
	}
	
	public static void applyPlayerStats(Player player, String stats){
		try {
			applyPlayerStats(player, new JSONObject(stats));
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public static void applyPlayerStats(Player player, JSONObject stats){
		try {
			if(stats.has("health"))
				player.setHealth(stats.getInt("health"));
			if(stats.has("food"))
				player.setFoodLevel(stats.getInt("food"));
			if(stats.has("exhaustion"))
				player.setExhaustion((float) stats.getDouble("exhaustion"));
			if(stats.has("saturation"))
				player.setSaturation((float) stats.getDouble("saturation"));
			if(stats.has("level"))
				player.setLevel(stats.getInt("level"));
			if(stats.has("exp"))
				player.setExp((float) stats.getDouble("exp"));
		} catch (JSONException e){
			e.printStackTrace();
		}
	}

}
