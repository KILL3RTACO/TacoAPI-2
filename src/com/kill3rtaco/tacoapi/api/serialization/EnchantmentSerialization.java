package com.kill3rtaco.tacoapi.api.serialization;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.enchantments.Enchantment;

import com.kill3rtaco.tacoapi.TacoAPI;

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

}
