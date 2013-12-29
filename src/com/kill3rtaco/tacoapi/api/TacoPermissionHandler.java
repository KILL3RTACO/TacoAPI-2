package com.kill3rtaco.tacoapi.api;

import org.bukkit.entity.Player;

public interface TacoPermissionHandler {
	
	/**
	 * Get the prefix for this handler.
	 * @return The prefix that will invoke this handler
	 */
	public String getPrefix();
	
	/**
	 * Test whether the given player has permission or not.
	 * @param player The player to test
	 * @param perm The permission to test, with the prefix removed
	 * @return Whether the given player has permission or not.
	 */
	public boolean handlePerm(Player player, String perm);
	
}
