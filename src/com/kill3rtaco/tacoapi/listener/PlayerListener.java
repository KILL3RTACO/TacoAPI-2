package com.kill3rtaco.tacoapi.listener;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import com.kill3rtaco.tacoapi.TacoAPI;

public class PlayerListener implements Listener {

	@EventHandler(ignoreCancelled=true)
	public void onPlayerTeleport(PlayerTeleportEvent event){
		TacoAPI.getPlayerAPI().saveLocation(event.getPlayer().getName(), event.getFrom());
	}
	
	@EventHandler(ignoreCancelled=true)
	public void onGameModeChange(PlayerGameModeChangeEvent event){
		Player player = event.getPlayer();
		TacoAPI.getPlayerAPI().saveGameMode(player.getName(), player.getGameMode());
	}
	
//	@EventHandler
//	public void onPlayerInteract(PlayerInteractEvent event){
//		if(event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getClickedBlock().getType() == Material.SIGN_POST){
//			TacoAPI.getPlayerAPI().savePlayerInventory(event.getPlayer());
//		}else{
//			TacoAPI.getPlayerAPI().restoreInventory(event.getPlayer());
//		}
//	}
	
}
