package taco.tacoapi.obj;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class PlayerObject {
	
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
