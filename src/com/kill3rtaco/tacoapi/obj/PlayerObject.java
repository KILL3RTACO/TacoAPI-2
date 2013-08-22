package com.kill3rtaco.tacoapi.obj;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.bukkit.entity.Wolf;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.Plugin;

import com.kill3rtaco.tacoapi.TacoAPI;
import com.kill3rtaco.tacoapi.api.serialization.InventorySerialization;
import com.kill3rtaco.itemmail.mail.ItemMail;

public class PlayerObject {
	
	public InventoryObject getInventoryAPI(){
		return new InventoryObject();
	}
	
	public ArrayList<Tameable> getPets(String playername){
		ArrayList<Tameable> pets = new ArrayList<Tameable>();
		pets.addAll(getTamedWolves(playername));
		pets.addAll(getTamedOcelots(playername));
		return pets;
	}
	
	public ArrayList<Wolf> getTamedWolves(String playername){
		ArrayList<Wolf> wolves = new ArrayList<Wolf>();
		for(World w : TacoAPI.plugin.getServer().getWorlds()){
			for(Wolf wolf : w.getEntitiesByClass(Wolf.class)){
				if(wolf.isTamed() && wolf.getOwner() instanceof Player){
					if(((Player) wolf.getOwner()).getName().equalsIgnoreCase(playername)) wolves.add(wolf);
				}
			}
		}
		return wolves;
	}
	
	public ArrayList<Ocelot> getTamedOcelots(String playername){
		ArrayList<Ocelot> cats = new ArrayList<Ocelot>();
		for(World w : TacoAPI.plugin.getServer().getWorlds()){
			for(Ocelot cat : w.getEntitiesByClass(Ocelot.class)){
				if(cat.isTamed() && cat.getOwner() instanceof Player){
					if(((Player) cat.getOwner()).getName().equalsIgnoreCase(playername)) cats.add(cat);
				}
			}
		}
		return cats;
	}
	
	public void sendItemMail(Plugin plugin, String sender, String receiver, ItemStack items){
		sendItemMail(plugin, sender, receiver, items, "Sent via TacoAPI");
	}
	
	public void sendItemMail(Plugin plugin, String sender, String receiver, ItemStack items, String notes){
		if(TacoAPI.isItemMailInstalled()){
			ItemMail.sendUnsafely(sender, receiver, items, notes);
		}else{
			TacoAPI.getChatAPI().out("The plugin '" + plugin.getName() + "' tried to send ItemMail without ItemMail being installed on server");
		}
	}
	
	public void teleport(Player player, Entity entity){
		teleport(player, entity, true);
	}
	
	public void teleport(Player player, Entity entity, boolean smoke){
		teleport(player, entity.getLocation(), smoke);
	}
	
	public void teleport(Player player, Location location){
		teleport(player, location, true);
	}
	
	public void teleport(Player player, Location location, boolean smoke){
		if(smoke) TacoAPI.getEffectAPI().showSmoke(player.getLocation());
		player.teleport(location);
		if(smoke) TacoAPI.getEffectAPI().showSmoke(player.getLocation());
	}
	
	public void teleportToLastLocation(Player player){
		teleportToLastLocation(player, true);
	}
	
	public void teleportToLastLocation(Player player, boolean smoke){
		teleport(player, getLastLocation(player.getName()), smoke);
	}
	
	public Location getLastLocation(String name){
		File file = new File(TacoAPI.playerData + "/" + name + ".yml");
		YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
		String world = config.getString("last-location.world");
		World w = TacoAPI.plugin.getServer().getWorld(world);
		if(w == null) return null;
		double x = config.getDouble("last-location.x");
		double y = config.getDouble("last-location.y");
		double z = config.getDouble("last-location.z");
		TacoAPI.plugin.chat.out(world + " " + x + " " + y + " " + z);
		return new Location(w, x, y, z);
	}
	
	public void saveLocation(String name, Location location){
		File file = new File(TacoAPI.playerData + "/" + name + ".yml");
		YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
		config.set("last-location.world", location.getWorld().getName());
		config.set("last-location.x", location.getX());
		config.set("last-location.y", location.getY());
		config.set("last-location.z", location.getZ());
		save(file, config);
	}
	
	public void setToLastGameMode(Player player){
		GameMode gm = getLastGameMode(player.getName());
		player.setGameMode(gm);
	}
	
	public GameMode getLastGameMode(String name){
		File file = new File(TacoAPI.playerData + "/" + name + ".yml");
		YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
		return GameMode.getByValue(config.getInt("last-gamemode"));
	}
	
	public void saveGameMode(String name, GameMode gameMode){
		File file = new File(TacoAPI.playerData + "/" + name + ".yml");
		YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
		config.set("last-gamemode", gameMode.getValue());
		save(file, config);
	}
	
	public void savePlayerInventory(Player player){
		savePlayerInventory(player.getInventory(), player.getName());
	}
	
	public void savePlayerInventory(PlayerInventory inventory, String name){
		try {
			File invFile = new File(TacoAPI.playerData + "/inventories/" + name + "/inventory.json");
			File armorFile = new File(TacoAPI.playerData + "/inventories/" + name + "/armor.json");
			if(invFile.exists()) invFile.delete();
			if(armorFile.exists()) armorFile.delete();
			invFile.getParentFile().mkdirs();
			invFile.createNewFile();
			armorFile.createNewFile();
			
			String inv = InventorySerialization.serializeInventoryAsString(inventory, true, 5);
			String armor = InventorySerialization.serializeInventoryAsString(inventory.getArmorContents(), true, 5);
			FileWriter invWriter = new FileWriter(invFile);
			FileWriter armorWriter = new FileWriter(armorFile);
			invWriter.append(inv);
			invWriter.close();
			armorWriter.append(armor);
			armorWriter.close();
		} catch (IOException e){
			e.printStackTrace();
		}
	}
	
	public ItemStack[] getPlayerInventory(Player player){
		return getPlayerInventory(player.getName());
	}
	
	public ItemStack[] getPlayerInventory(String name){
		File invFile = new File(TacoAPI.playerData + "/inventories/" + name + "/inventory.json");
		return getItemsFromFile(invFile, 36);
	}
	
	public ItemStack[] getPlayerArmor(Player player){
		return getPlayerArmor(player.getName());
	}
	
	public ItemStack[] getPlayerArmor(String name){
		File armorFile = new File(TacoAPI.playerData + "/inventories/" + name + "/armor.json");
		return getItemsFromFile(armorFile, 4);
	}
	
	private ItemStack[] getItemsFromFile(File file, int size){
		return InventorySerialization.getInventory(file, size);
	}
	
	public void restoreInventory(Player player){
		String name = player.getName();
		ItemStack[] inv = getPlayerInventory(name);
		ItemStack[] armor = getPlayerArmor(name);
		PlayerInventory inventory = player.getInventory();
		inventory.clear();
		inventory.setArmorContents(armor);
		for(int i=0; i<inv.length; i++){
			ItemStack items = inv[i];
			if(items != null) inventory.setItem(i, items);
		}
	}
	
	private void save(File file, YamlConfiguration config){
		try {
			file.getParentFile().mkdir();
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
