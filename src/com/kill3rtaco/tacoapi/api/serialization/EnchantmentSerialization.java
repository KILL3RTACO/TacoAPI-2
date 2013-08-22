package com.kill3rtaco.tacoapi.api.serialization;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.enchantments.Enchantment;

import com.kill3rtaco.tacoapi.TacoAPI;

/**
 * A class to help with the serialization of Enchantments. Because of the ability to add unsafe enchantments
 * (item and enchantment level are ignored), enchantments are serialized in a different way than like ChestShop.
 * ChestShop takes the enchantment id, converts it into a String, and appends the level. It then iterates this
 * process for each enchantment. Afterwards, it has a very long number, which it then conveters to a base-36 String.
 * 
 * This class serializes it much differently, and it is in fact more human readable. The process can be explained
 * as such:
 * <pre>
 * String serializationString = "";
 * for(Enchantment e : ItemStack){
 *      serializationString += e.getId() + ":" + e.getLevel() + ";";
 * }
 * </pre>
 * 
 * So that the result would be:<br/>
 * <pre>id:level;...</pre>
 * 
 * This allows for much easier readability as well as the possibility to unsafely add enchantments
 * @author KILL3RTACO
 *
 */
public class EnchantmentSerialization {

	protected EnchantmentSerialization() {}
	
	public static String serializeEnchantments(Map<Enchantment, Integer> enchantments){
		String serialized = "";
		for(Enchantment e : enchantments.keySet()){
			serialized += e.getId() + ":" + enchantments.get(e) + ";";
		}
		return serialized;
	}
	
	public static Map<Enchantment, Integer> getEnchantments(String serializedEnchants){
		HashMap<Enchantment, Integer> enchantments = new HashMap<Enchantment, Integer>();
		String[] enchants = serializedEnchants.split(";");
		for(int i=0; i<enchants.length; i++){
			String[] ench = enchants[i].split(":");
			if(ench.length < 2)
				throw new IllegalArgumentException(serializedEnchants + " - Enchantment " + i + "(" + enchants[i] + "): split be length of 2");
			if(!TacoAPI.getChatUtils().isNum(ench[0]))
				throw new IllegalArgumentException(serializedEnchants + " - Enchantment " + i + "(" + enchants[i] + "): id is not a number");
			if(!TacoAPI.getChatUtils().isNum(ench[1]))
				throw new IllegalArgumentException(serializedEnchants + " - Enchantment " + i + "(" + enchants[i] + "): level is not a number");
			int id = Integer.parseInt(ench[0]);
			int level = Integer.parseInt(ench[1]);
			Enchantment e = Enchantment.getById(id);
			if(e == null)
				throw new IllegalArgumentException(serializedEnchants + " - Enchantment " + i + "(" + enchants[i] + "): no Enchantment with id of " + id);
			enchantments.put(e, level);
		}
		return enchantments;
	}
	
	public static Map<Enchantment, Integer> getEnchantsFromOldFormat(String oldFormat){
		HashMap<Enchantment, Integer> enchants = new HashMap<Enchantment, Integer>();
		if(oldFormat.length() == 0){
			return enchants;
		}
		String nums = Long.parseLong(oldFormat, 32) + "";
		System.out.println(nums);
		for(int i=0; i<nums.length(); i+=3){
			int enchantId = Integer.parseInt(nums.substring(i, i+2));
			int enchantLevel = Integer.parseInt(nums.charAt(i+2) + "");
			Enchantment ench = Enchantment.getById(enchantId);
			enchants.put(ench, enchantLevel);
		}
		return enchants;
	}
	
	public static String convert(String oldFormat){
		Map<Enchantment, Integer> enchants = getEnchantsFromOldFormat(oldFormat);
		return serializeEnchantments(enchants);
	}
	
	public static Map<Enchantment, Integer> convertAndGetEnchantments(String oldFormat){
		String newFormat = convert(oldFormat);
		return getEnchantments(newFormat);
	}

}
