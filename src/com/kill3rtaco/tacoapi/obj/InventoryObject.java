package com.kill3rtaco.tacoapi.obj;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class InventoryObject {
	
	/**
	 * Test if a player has room in their inventory to hold the given items
	 * @param player the player to test
	 * @param items the items to test
	 * @return true if the items will fit in their inventory
	 */
	public boolean canHold(Player player, ItemStack items){
		int space = 0, needed = items.getAmount();
		for(ItemStack i : player.getInventory()){
			if(i == null){
				space += items.getMaxStackSize();
			}else if(i.getType() == items.getType() && i.getDurability() == items.getDurability() && 
					(i.getItemMeta().hasDisplayName() ? i.getItemMeta().getDisplayName()
							.equalsIgnoreCase(items.getItemMeta().getDisplayName()) : true)){
				space += items.getMaxStackSize() - i.getAmount();
			}
		}
		if(space >= needed){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * Gives the player items
	 * @param player the player to give items to
	 * @param items the items to give
	 * @return false if the player doesn't have enough room to hold the items in their inventory
	 */
	public boolean giveItems(Player player, ItemStack items){
		if(canHold(player, items)){
			player.getInventory().addItem(items);
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * Tests if the player has the given items in their inventory
	 * @param player the player to test
	 * @param items the items to test
	 * @return
	 */
	public boolean hasItems(Player player, ItemStack items){
		int needed = items.getAmount();
		int amount = 0;
		for(ItemStack i : player.getInventory()){
			if(i != null){
				if(i.getType() == items.getType() && i.getDurability() == items.getDurability()){
					boolean hasEnchants = true;
					for(Enchantment e : i.getEnchantments().keySet()){
						if(items.getItemMeta().hasEnchant(e)){
							if(i.getEnchantmentLevel(e) != items.getEnchantmentLevel(e)){
								hasEnchants = false;
								break;
							}
						}else{
							hasEnchants = false;
							break;
						}
					}
					if(hasEnchants) amount += i.getAmount();
				}
			}
		}
		if(amount >= needed){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * Takes the specified items out of the given player's invetory. This will return false if the player does not have the items
	 * @param player the player to test
	 * @param items the items to test
	 * @return false if the player does not have the specified items, true otherwise.
	 */
	public boolean takeItems(Player player, ItemStack items){
		if(hasItems(player, items)){
			player.getInventory().removeItem(items);
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * Removes any ItemStacks whose .getType() == material.
	 * @param player the player to remove the items
	 * @param material then material to remove
	 * @return the amount removed
	 */
	public int removeAll(Player player, Material material){
		int removed = getAmountOfMaterial(player, material);
		if(removed > 0) player.getInventory().remove(material);
		return removed;
	}
	
	/**
	 * Gets the amount of the given material that the player has in their inventory
	 * @param player the player whose inventory is to be searched
	 * @param material the material to look for
	 * @return the amount found, or 0 if material == null
	 */
	public int getAmountOfMaterial(Player player, Material material){
		if(material == null) return 0;
		else return getAmountOfMaterial(player, material.getId(), 0);
	}

	/**
	 * Gets the amount of the material with the given id that the player has in their inventory
	 * @param player the player whose inventory is to be searched
	 * @param id the id of the material to look for
	 * @return the amount found, or 0 if Material.getMaterial(id) == null
	 */
	public int getAmountOfMaterial(Player player, int id){
		return getAmountOfMaterial(player, Material.getMaterial(id));
	}

	/**
	 * Gets the amount of the material with the given id and damage that the player has in their inventory
	 * @param player the player whose inventory is to be searched
	 * @param id the id of the material to look for
	 * @param damage the damage of the material to look for
	 * @return the amount found, or 0 if Material.getMaterial(id) == null
	 */
	public int getAmountOfMaterial(Player player, int id, int damage){
		int amount = 0;
		if(id == 0 || Material.getMaterial(id) == null) return 0;
		for(ItemStack i : player.getInventory()){
			if(i.getTypeId() == id && i.getDurability() == damage){
				amount += i.getAmount();
			}
		}
		return amount;
	}

}
