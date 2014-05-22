package com.kill3rtaco.tacoapi.commands;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.kill3rtaco.tacoapi.TacoAPI;
import com.kill3rtaco.tacoapi.api.ncommands.Command;
import com.kill3rtaco.tacoapi.api.ncommands.CommandContext;
import com.kill3rtaco.tacoapi.api.ncommands.CommandPermission;
import com.kill3rtaco.tacoapi.api.ncommands.ParentCommand;
import com.kill3rtaco.tacoapi.api.serialization.SingleItemSerialization;

public class DevCommands {
	
	@ParentCommand("dev")
	@Command(name = "item-as-string", args = "[printToConsole]", aliases = {"ias"}, desc = "Convert your held item to a TacoSerialization string", onlyPlayer = true)
	@CommandPermission("TacoAPI.dev.itemAsString")
	public static void itemAsString(CommandContext context) {
		ItemStack hand = context.getPlayer().getItemInHand();
		if(hand == null) {
			context.sendMessageToSender("&cYou are not holding anything");
			return;
		}
		boolean console = context.gt(0) ? Boolean.valueOf(context.getString(0)) : false;
		String str = SingleItemSerialization.serializeItemAsString(hand);
		if(console) {
			TacoAPI.plugin.chat.out(str);
		} else {
			context.sendMessageToSender(str);
		}
	}
	
	@ParentCommand("dev")
	@Command(name = "gpy", desc = "Get your pitch/yaw angles", onlyPlayer = true)
	@CommandPermission("TacoAPI.dev.getPitchYaw")
	public static void getPitchYaw(CommandContext context) {
		Player p = context.getPlayer();
		double pitch = p.getLocation().getPitch();
		double yaw = p.getLocation().getYaw();
		context.sendMessageToSender("&aPitch&7: &2" + pitch + " &aYaw&7: &2" + yaw);
	}
	
}
