package com.kill3rtaco.tacoapi.api.serialization;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import com.kill3rtaco.tacoapi.json.JSONArray;
import com.kill3rtaco.tacoapi.json.JSONException;
import com.kill3rtaco.tacoapi.json.JSONObject;

/**
 * A class to help with the serializatiion of inventory. All data is saved in a JSON format.
 * 
 * Inventories are serialized perfectly. Books, whether they are enchanted or written (signed or not)
 * are saved with the enchantments or pages in their ItemMeta. Dyed armor is also perfectly saved, with
 * the Red, Green, and Blue value saved appropriately.
 * 
 * PlayerInventories will have a longer String length, depending on the contents or their armor.
 * @author KILL3RTACO
 *
 */
public class InventorySerialization {

	protected InventorySerialization() {}
	
	/**
	 * Serialization an Inventory. Note that this does not save the armor contents for a PlayerInventory.
	 * @param inv The Inventory to serialize
	 * @return A JSONObject representing the serialized Inventory.
	 */
	public static JSONObject serializeInventory(Inventory inv){
		JSONObject root = new JSONObject();
		JSONArray inventory = new JSONArray();
		for(int i=0; i<inv.getSize(); i++){
			JSONObject values = SingleItemSerialization.serializeItemInInventory(inv.getItem(i), i);
			if(values != null) inventory.put(values);
		}
		try {
			return root.put("inventory", inventory);
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Serialize a PlayerInventory. This will save the armor contents of the inventory as well
	 * @param inv The Inventory to serialize
	 * @return A JSONObject representing the serialized Inventory.
	 */
	public static JSONObject serializePlayerInventory(PlayerInventory inv){
		try {
			JSONObject inventory = serializeInventory(inv);
			JSONObject armor = serializeInventory(inv.getArmorContents());
			return inventory.put("armor", armor);
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Get the string form of the serialized PlayerInventory. This produces the exact same results as
	 * <code>serializePlayerInventory(inv).toString()</code>
	 * @param inv The Inventory to serialize
	 * @return The String form of the serialized PlayerInventory
	 */
	public static String serializePlayerInventoryAsString(PlayerInventory inv){
		return serializePlayerInventoryAsString(inv, false);
	}
	
	/**
	 * Get the string form of the serialized PlayerInventory. If <code>pretty</code> is <code>true</code>
	 * then the resulting String will include whitespace and tabs, with each tab having a size of 5.
	 * @param inv The Inventory to serialize
	 * @param pretty Whether the resulting string should be 'pretty' or not
	 * @return The String form of the serialized PlayerInventory
	 */
	public static String serializePlayerInventoryAsString(PlayerInventory inv, boolean pretty){
		return serializePlayerInventoryAsString(inv, pretty, 5);
	}
	
	/**
	 * Get the string form of the serialized PlayerInventory. If <code>pretty</code> is <code>true</code>
	 * then the resulting String will include whitespace and tabs, with each tab having a size of 
	 * <code>indentFactor</code>.
	 * @param inv The Inventory to serialize
	 * @param pretty Whether the resulting string should be 'pretty' or not
	 * @param indentFactor The size of the tabs
	 * @return The String form of the serialized PlayerInventory
	 */
	public static String serializePlayerInventoryAsString(PlayerInventory inv, boolean pretty, int indentFactor){
		try{
			if(pretty){
				return serializePlayerInventory(inv).toString(indentFactor);
			}else{
				return serializePlayerInventory(inv).toString(); 
			}
		} catch(JSONException e){
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Get the string form of the serialized Inventory. This produces the exact same results as
	 * <code>serializeInventory(inventory).toString()</code>
	 * @param inv The Inventory to serialize
	 * @return The String form of the serialized Inventory
	 */
	public static String serializeInventoryAsString(Inventory inventory){
		return serializeInventoryAsString(inventory, false);
	}

	/**
	 * Get the string form of the serialized Inventory. If <code>pretty</code> is <code>true</code>
	 * then the resulting String will include whitespace and tabs, with each tab having a size of 5.
	 * @param inv The Inventory to serialize
	 * @param pretty Whether the resulting string should be 'pretty' or not
	 * @return The String form of the serialized Inventory
	 */
	public static String serializeInventoryAsString(Inventory inventory, boolean pretty){
		return serializeInventoryAsString(inventory, pretty, 5);
	}

	/**
	 * Get the string form of the serialized Inventory. If <code>pretty</code> is <code>true</code>
	 * then the resulting String will include whitespace and tabs, with each tab having a size of 
	 * <code>indentFactor</code>.
	 * @param inv The Inventory to serialize
	 * @param pretty Whether the resulting string should be 'pretty' or not
	 * @param indentFactor The size of the tabs
	 * @return The String form of the serialized Inventory
	 */
	public static String serializeInventoryAsString(Inventory inventory, boolean pretty, int indentFactor){
		try{
			if(pretty){
				return serializeInventory(inventory).toString(indentFactor);
			}else{
				return serializeInventory(inventory).toString(); 
			}
		} catch(JSONException e){
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Get the String form of the serialiazed ItemStack array. This produces the same result as
	 * <code>serializeInventory(contents).toString()</code>
	 * @param contents
	 * @return
	 */
	public static String serializeInventoryAsString(ItemStack[] contents){
		return serializeInventoryAsString(contents, false);
	}

	/**
	 * Get the String form of the serialiazed ItemStack array. If <code>pretty</code> is <code>true</code>
	 * then the resulting String will include whitespace and tabs, with each tab having a size of 5.
	 * @param inv The Inventory to serialize
	 * @param pretty Whether the resulting string should be 'pretty' or not
	 * @return The String form of the serialized Inventory
	 */
	public static String serializeInventoryAsString(ItemStack[] contents, boolean pretty){
		return serializeInventoryAsString(contents, pretty, 5);
	}

	/**
	 * Get the String form of the serialiazed ItemStack array. If <code>pretty</code> is <code>true</code>
	 * then the resulting String will include whitespace and tabs, with each tab having a size of 
	 * <code>indentFactor</code>.
	 * @param inv The Inventory to serialize
	 * @param pretty Whether the resulting string should be 'pretty' or not
	 * @param indentFactor The size of the tabs
	 * @return The String form of the serialized Inventory
	 */
	public static String serializeInventoryAsString(ItemStack[] contents, boolean pretty, int indentFactor){
		try {
			if(pretty){
				return serializeInventory(contents).toString(indentFactor);
			}else{
				return serializeInventory(contents).toString();
			}
		} catch (JSONException e){
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Serialize an ItemStack array.
	 * @param contents
	 * @return
	 */
	public static JSONObject serializeInventory(ItemStack[] contents){
		JSONObject root = new JSONObject();
		JSONArray inventory = new JSONArray();
		for(int i=0; i<contents.length; i++){
			JSONObject values = SingleItemSerialization.serializeItemInInventory(contents[i], i);
			if(values != null) inventory.put(values);
		}
		try {
			return root.put("inventory", inventory);
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Get an ItemStack array from a JSON String.
	 * @param json The JSON String to use
	 * @param size The expected size of the inventory, can be greater than expected
	 * @return
	 */
	public static ItemStack[] getInventory(String json, int size){
		try {
			return getInventory(new JSONObject(json), size);
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Gets an ItemStack array from a JSONObject.
	 * @param inv The JSONObject to get from
	 * @param size The expected size of the inventory, can be greater than expected
	 * @return
	 */
	public static ItemStack[] getInventory(JSONObject inv, int size){
		try {
			JSONArray items = inv.getJSONArray("inventory");
			ItemStack[] contents = new ItemStack[size];
			for(int i=0; i<items.length(); i++){
				JSONObject item = items.getJSONObject(i);
				int index = item.getInt("index");
				if(index > size)
					throw new IllegalArgumentException("index found is greator than expected size (" + index + ">" + size + ")");
				if(index > contents.length || index < 0)
					throw new IllegalArgumentException("Item "+ i + " - Slot " + index + " does not exist in this inventory");
				ItemStack stuff = SingleItemSerialization.getItem(item);
				contents[index] = stuff;
			}
			return contents;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Get an ItemStack array from a json file
	 * @param jsonFile
	 * @param size The expected size of the inventory, can be greater than expected
	 * @return
	 */
	public static ItemStack[] getInventory(File jsonFile, int size){
		String source = "";
		try {
			Scanner x = new Scanner(jsonFile);
			while(x.hasNextLine()){
				source += x.nextLine() + "\n";
			}
			x.close();
			return getInventory(source, size);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static void setInventory(InventoryHolder holder, String inv){
		try {
			setInventory(holder, new JSONObject(inv));
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public static void setInventory(InventoryHolder holder, JSONObject inv){
		Inventory inventory = holder.getInventory();
		ItemStack[] items = getInventory(inv, inventory.getSize());
		inventory.clear();
		for(int i=0; i<items.length; i++){
			ItemStack item = items[i];
			if(item == null) continue;
			inventory.setItem(i, item);
		}
	}
	
	public static void setPlayerInventory(Player player, String inv){
		try {
			setPlayerInventory(player, new JSONObject(inv));
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public static void setPlayerInventory(Player player, JSONObject inv){
		try {
			PlayerInventory inventory = player.getInventory();
			ItemStack[] armor = getInventory(inv.getJSONObject("armor"), 4);
			inventory.clear();
			inventory.setArmorContents(armor);
			setInventory(player, inv);
		} catch (JSONException e){
			e.printStackTrace();
		}
	}

}
