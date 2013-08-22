package com.kill3rtaco.tacoapi.api.serialization;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import com.kill3rtaco.tacoapi.json.JSONException;
import com.kill3rtaco.tacoapi.json.JSONObject;

/**
 * A class to help with the serialization of dyed leather armor. The Red, Green, and Blue values are saved
 * appropriately.
 * @author KILL3RTACO
 *
 */
public class LeatherArmorSerialization {

	protected LeatherArmorSerialization() {}
	
	public static JSONObject serializeArmor(LeatherArmorMeta meta){
		try {
			JSONObject root = new JSONObject();
			JSONObject color = new JSONObject();
			Color c = meta.getColor();
			color.put("red", c.getRed());
			color.put("green", c.getGreen());
			color.put("blue", c.getBlue());
			root.put("color", color);
			return root;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String serializeArmorAsString(LeatherArmorMeta meta){
		return serializeArmorAsString(meta, false);
	}

	public static String serializeArmorAsString(LeatherArmorMeta meta, boolean pretty){
		return serializeArmorAsString(meta, pretty, 5);
	}

	public static String serializeArmorAsString(LeatherArmorMeta meta, boolean pretty, int indentFactor){
		try {
			if(pretty){
				return serializeArmor(meta).toString(indentFactor);
			}else{
				return serializeArmor(meta).toString();
			}
		} catch (JSONException e){
			e.printStackTrace();
			return null;
		}
	}
	
	public static LeatherArmorMeta getLeatherArmorMeta(String json){
		try {
			return getLeatherArmorMeta(new JSONObject(json));
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static LeatherArmorMeta getLeatherArmorMeta(JSONObject json){
		try {
			ItemStack dummyItems = new ItemStack(Material.LEATHER_HELMET, 1);
			LeatherArmorMeta meta = (LeatherArmorMeta) dummyItems.getItemMeta();
			if(json.has("color")){
				JSONObject color = json.getJSONObject("color");
				int r, g, b;
				
				if(color.has("red")) r = color.getInt("red");
				else r = 0;
				if(color.has("green")) g = color.getInt("green");
				else g = 0;
				if(color.has("blue")) b = color.getInt("blue");
				else b = 0;
				
				meta.setColor(Color.fromRGB(r, g, b));
			}
			return meta;
		} catch(JSONException e){
			e.printStackTrace();
			return null;
		}
	}

}
