package taco.tacoapi.obj;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import taco.tacoapi.TacoAPI;
import taco.tacoapi.api.TacoMessage;
import taco.tacoapi.api.messages.InvalidPermissionsMessage;
import taco.tacoapi.api.messages.InvalidSubCommandMessage;

/**
 * Class to help with chatting
 * @author KILL3RTACO
 *
 */
public class ChatObject {

	private String name;
	
	public ChatObject(String name){
		this.name = name;
	}
	
	public void sendInvalidPermissionsMessage(Player player){
		sendPlayerMessageNoHeader(player, new InvalidPermissionsMessage());
	}
	
	public void sendInvalidSubCommandMessage(Player player, String attempt){
		sendPlayerMessageNoHeader(player, new InvalidSubCommandMessage(attempt));
	}
	
	public void sendInvalidSubCommandMessage(CommandSender sender, String attempt){
		if(sender instanceof Player){
			sendInvalidSubCommandMessage((Player) sender, attempt);
			return;
		}
		String message = TacoAPI.getChatUtils().removeColorCodes(new InvalidSubCommandMessage(attempt).getMessage());
		TacoAPI.getChatAPI().out(message);
	}
	
	public void sendPlayerMessage(Player player, String message){
		message = TacoAPI.getChatUtils().formatMessage("&7[&9" + name + "&7]&f" + message);
		player.sendMessage(message);
	}
	
	public void sendPlayerMessageNoHeader(Player player, String message){
		message = TacoAPI.getChatUtils().formatMessage(message);
		player.sendMessage(message);
	}
	
	public void sendPlayerMessage(Player player, TacoMessage message){
		String msg = TacoAPI.getChatUtils().formatMessage("&7[&9" + name + "&7]&f" + message.getMessage());
		player.sendMessage(msg);
	}
	
	public void sendPlayerMessageNoHeader(Player player, TacoMessage message){
		String msg = TacoAPI.getChatUtils().formatMessage(message.getMessage());
		player.sendMessage(msg);
	}
	
	public void sendGlobalMessage(String message){
		message = TacoAPI.getChatUtils().formatMessage("&7[&9" + name + "&7]&f" + message);
		TacoAPI.plugin.getServer().broadcastMessage(message);
	}
	
	public void sendGlobalMessageNoHeader(String message){
		TacoAPI.plugin.getServer().broadcastMessage(message);
	}
	
	public void sendGlobalMessage(TacoMessage message){
		String msg = TacoAPI.getChatUtils().formatMessage("&7[&9" + name + "&7]&f" + message.getMessage());
		TacoAPI.plugin.getServer().broadcastMessage(msg);
	}
	
	public void sendGlobalMessageNoHeader(TacoMessage message){
		TacoAPI.plugin.getServer().broadcastMessage(message.getMessage());
	}
	
	public void out(String message){
		Logger logger = Logger.getLogger("Minecraft");
		logger.log(Level.INFO, message);
	}
	
	public void outSevere(String message){
		Logger logger = Logger.getLogger("Minecraft");
		logger.log(Level.SEVERE, message);
	}
	
	public void outWarn(String message){
		Logger logger = Logger.getLogger("Minecraft");
		logger.log(Level.WARNING, message);
	}

}
