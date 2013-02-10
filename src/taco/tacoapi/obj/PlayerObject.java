package taco.tacoapi.obj;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.bukkit.entity.Wolf;

import taco.tacoapi.TacoAPI;

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
	
	public void ignite(Player player, int duration){
		player.setFireTicks(duration);
	}
	
	public void kill(Player player){
		player.setHealth(0);
	}
	
	public void strike(Player player){
		player.getWorld().strikeLightning(player.getLocation());
	}
	
	public void strikeEffect(Player player){
		player.getWorld().strikeLightningEffect(player.getLocation());
	}
	
//	public void sendItemMail(){
//		//used for sending ItemMail to a player
//	}
	
	public void teleport(Player player, Entity entity){
		player.teleport(entity);
	}
	
	public void teleport(Player player, Location location){
		player.teleport(location);
	}

}
