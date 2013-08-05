package com.kill3rtaco.tacoapi.api.serialization;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import com.kill3rtaco.tacoapi.TacoAPI;
import com.kill3rtaco.tacoapi.json.JSONArray;
import com.kill3rtaco.tacoapi.json.JSONException;
import com.kill3rtaco.tacoapi.json.JSONObject;

public class InventorySerialization {

	protected InventorySerialization() {}
	
	public static JSONObject serializeInventory(Inventory inv){
		JSONObject root = new JSONObject();
		JSONArray inventory = new JSONArray();
		for(int i=0; i<inv.getSize(); i++){
			Map<String, Object> values = serializeItem(inv.getItem(i), i);
			if(values != null) inventory.put(values);
		}
		try {
			return root.put("inventory", inventory);
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	//road
	public static String serializeInventoryAsString(Inventory inventory){
		return serializeInventoryAsString(inventory.getContents(), false);
	}

	//road
	public static String serializeInventoryAsString(Inventory inventory, boolean pretty){
		return serializeInventoryAsString(inventory.getContents(), pretty, 5);
	}

	//road
	public static String serializeInventoryAsString(Inventory inventory, boolean pretty, int indentFactor){
		return serializeInventoryAsString(inventory.getContents(), pretty, indentFactor);
	}

	//road
	public static String serializeInventoryAsString(ItemStack[] contents){
		return serializeInventoryAsString(contents, false);
	}

	//road
	public static String serializeInventoryAsString(ItemStack[] contents, boolean pretty){
		return serializeInventoryAsString(contents, pretty, 5);
	}

	//road
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
	
	//rome (all roads lead here)
	public static JSONObject serializeInventory(ItemStack[] contents){
		JSONObject root = new JSONObject();
		JSONArray inventory = new JSONArray();
		for(int i=0; i<contents.length; i++){
			Map<String, Object> values = serializeItem(contents[i], i);
			if(values != null) inventory.put(values);
		}
		try {
			return root.put("inventory", inventory);
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static ItemStack[] getInventory(String json, int size){
		try {
			JSONObject root = new JSONObject(json);
			JSONArray items = root.getJSONArray("inventory");
			if(size < items.length())
				throw new IllegalArgumentException("size cannot be smaller than the inventory length (" + size + " < " + items.length() + ")");
			ItemStack[] contents = new ItemStack[size];
			for(int i=0; i<items.length(); i++){
				JSONObject item = items.getJSONObject(i);
				int id = item.getInt("id");
				int amount = item.getInt("amount");
				int data = item.getInt("data");
				int index = item.getInt("index");
				String name = null;
				Map<Enchantment, Integer> enchants = null;
				ArrayList<String> lore = null;
				if(item.has("name"))
					name = item.getString("name");
				if(item.has("enchantments"))
					enchants = EnchantmentSerialization.getEnchantments(item.getString("enchantments"));
				if(item.has("lore")){
					JSONArray l = item.getJSONArray("lore");
					lore = new ArrayList<String>();
					for(int j=0; j<l.length(); j++){
						lore.add(l.getString(j));
					}
				}
				
				if(Material.getMaterial(id) == null)
					throw new IllegalArgumentException("Item "+ i + " - No Material found with id of " + id);
				Material mat = Material.getMaterial(id);
				if(index > contents.length || index < 0)
					throw new IllegalArgumentException("Item "+ i + " - Slot " + index + " does not exist in this inventory");
				ItemStack stuff = new ItemStack(mat, amount, (short) data);
				if(mat == Material.BOOK_AND_QUILL || mat == Material.WRITTEN_BOOK){
					BookMeta meta = BookSerialization.getBookMeta(item.getJSONObject("book-meta"));
					stuff.setItemMeta(meta);
				}else if(mat == Material.ENCHANTED_BOOK){
					EnchantmentStorageMeta meta = BookSerialization.getEnchantedBookMeta(item.getJSONObject("book-meta"));
					stuff.setItemMeta(meta);
				}else if(TacoAPI.getItemUtils().isLeatherArmor(mat)){
					LeatherArmorMeta meta = LeatherArmorSerialization.getLeatherArmorMeta(item.getJSONObject("armor-meta"));
					stuff.setItemMeta(meta);
				}
				ItemMeta meta = stuff.getItemMeta();
				if(name != null) meta.setDisplayName(name);
				if(lore != null) meta.setLore(lore);
				stuff.setItemMeta(meta);
				if(enchants != null) stuff.addUnsafeEnchantments(enchants);
				contents[index] = stuff;
			}
			return contents;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
	
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
	
	private static Map<String, Object> serializeItem(ItemStack items, int index){
		HashMap<String, Object> values = new HashMap<String, Object>();
		if(items == null) return null;
		int id = items.getTypeId();
		int amount = items.getAmount();
		int data = items.getDurability();
		boolean hasMeta = items.hasItemMeta();
		String name = null, enchants = null;
		String[] lore = null;
		Material mat = items.getType();
		JSONObject bookMeta = null, armorMeta = null;
		if(mat == Material.BOOK_AND_QUILL || mat == Material.WRITTEN_BOOK){
			bookMeta = BookSerialization.serializeBookMeta((BookMeta) items.getItemMeta());
		}else if(mat == Material.ENCHANTED_BOOK){
			bookMeta = BookSerialization.serializeEnchantedBookMeta((EnchantmentStorageMeta) items.getItemMeta());
		}else if(TacoAPI.getItemUtils().isLeatherArmor(mat)){
			armorMeta = LeatherArmorSerialization.serializeArmor((LeatherArmorMeta) items.getItemMeta());
		}
		if(hasMeta){
			ItemMeta meta = items.getItemMeta();
			if(meta.hasDisplayName())
				name = meta.getDisplayName();
			if(meta.hasLore()){
				lore = meta.getLore().toArray(new String[]{});
			}
			if(meta.hasEnchants())
				enchants = EnchantmentSerialization.serializeEnchantments(meta.getEnchants());
		}
		
		values.put("id", id);
		values.put("amount", amount);
		values.put("data", data);
		values.put("index", index);
		if(name != null) values.put("name", name);
		if(enchants != null) values.put("enchantments", enchants);
		if(lore != null) values.put("lore", lore);
		if(bookMeta != null && bookMeta.length() > 0) values.put("book-meta", bookMeta);
		if(armorMeta != null && armorMeta.length() > 0) values.put("armor-meta", armorMeta);
		return values;
	}

}
