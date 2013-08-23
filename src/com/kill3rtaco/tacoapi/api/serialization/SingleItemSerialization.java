package com.kill3rtaco.tacoapi.api.serialization;

import java.util.ArrayList;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import com.kill3rtaco.tacoapi.TacoAPI;
import com.kill3rtaco.tacoapi.json.JSONArray;
import com.kill3rtaco.tacoapi.json.JSONException;
import com.kill3rtaco.tacoapi.json.JSONObject;

public class SingleItemSerialization {

	protected SingleItemSerialization() {}
	
	public static JSONObject serializeItemInInventory(ItemStack items, int index){
		return serializeItems(items, true, index);
	}
	
	public static JSONObject serializeItem(ItemStack items){
		return serializeItems(items, false, 0);
	}
	
	private static JSONObject serializeItems(ItemStack items, boolean useIndex, int index){
		try {
			JSONObject values = new JSONObject();
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
			if(useIndex) values.put("index", index);
			if(name != null) values.put("name", name);
			if(enchants != null) values.put("enchantments", enchants);
			if(lore != null) values.put("lore", lore);
			if(bookMeta != null && bookMeta.length() > 0) values.put("book-meta", bookMeta);
			if(armorMeta != null && armorMeta.length() > 0) values.put("armor-meta", armorMeta);
			return values;
		} catch (JSONException e){
			e.printStackTrace();
			return null;
		}
	}
	
	public static ItemStack getItem(String item){
		return getItem(item, 0);
	}
	
	public static ItemStack getItem(String item, int index){
		try {
			return getItem(new JSONObject(item), index);
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static ItemStack getItem(JSONObject item){
		return getItem(item, 0);
	}
	
	public static ItemStack getItem(JSONObject item, int index){
		try {
			int id = item.getInt("id");
			int amount = item.getInt("amount");
			int data = item.getInt("data");
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
				throw new IllegalArgumentException("Item "+ index + " - No Material found with id of " + id);
			Material mat = Material.getMaterial(id);
			ItemStack stuff = new ItemStack(mat, amount, (short) data);
			if((mat == Material.BOOK_AND_QUILL || mat == Material.WRITTEN_BOOK) && item.has("book-meta")){
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
			return stuff;
		} catch (JSONException e){
			e.printStackTrace();
			return null;
		}
	}
	
	public static String serializeItemInInventoryAsString(ItemStack items, int index){
		return serializeItemInInventoryAsString(items, index, false);
	}
	
	public static String serializeItemInInventoryAsString(ItemStack items, int index, boolean pretty){
		return serializeItemInInventoryAsString(items, index, pretty, 5);
	}
	
	public static String serializeItemInInventoryAsString(ItemStack items, int index, boolean pretty, int indentFactor){
		try {
			if(pretty){
				return serializeItemInInventory(items, index).toString(indentFactor);
			}else{
				return serializeItemInInventory(items, index).toString();
			}
		} catch (JSONException e){
			e.printStackTrace();
			return null;
		}
	}
	
	public static String serializeItemAsString(ItemStack items){
		return serializeItemAsString(items, false);
	}
	
	public static String serializeItemAsString(ItemStack items, boolean pretty){
		return serializeItemAsString(items, pretty, 5);
	}
	
	public static String serializeItemAsString(ItemStack items, boolean pretty, int indentFactor){
		try {
			if(pretty){
				return serializeItem(items).toString(indentFactor);
			}else{
				return serializeItem(items).toString();
			}
		} catch (JSONException e){
			e.printStackTrace();
			return null;
		}
	}

}
